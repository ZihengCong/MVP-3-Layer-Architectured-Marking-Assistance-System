package markassistancesystem.model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.EnumMap;

/**
 * The MarkAssistanceSystem class is responsible for the management of student
 * marks.Connection functionality is accessed via the IConnect interface; query
 * functionality via the IQuery interface.
 *
 * @author Ziheng Cong
 */
public class MarkAssistanceSystemModule implements IConnect, IQuery<MarkAssistanceSystemModule.Query, Student> {
    
    /**
     * The Query enum specifies the queries that are supported by this manager
     */
    public static enum Query {
        ALL, LAST_NAME, UPDATE, RANGE, GRADE, TOLERANCE, UPDATE_CURRENT_GREADE, UPDATE_ALL_GRADE, 
    };

    // Database details for the address book being managed
    private static final String URL = "jdbc:derby://localhost:1527/marks";
    private static final String USERNAME = "marks";
    private static final String PASSWORD = "marks";

    /* 
     * We use enummaps to map queries (enum values) to SQL commands and prepared 
     * statements in a typesafe manner. Hashmaps could be used to the same effect,
     * but they would be less eficient, not that it matters. You could use arrays
     * indexed by enum.ordinal() or by int constants, but you then lose type safety 
     * among other things.
     */
    private EnumMap<Query, String> sqlCommands = 
        new EnumMap<>( MarkAssistanceSystemModule.Query.class );
        private EnumMap<Query, PreparedStatement> statements = 
        new EnumMap<>( MarkAssistanceSystemModule.Query.class );

    // The connection to the marks
    private Connection connection = null;

    /**
     * Create an instance of the marks manager. Clients have no access to
     * the implementation details of the address book. Also, clients can create
     * multiple instances of the manager, which is probably a bad idea.
     */
    public MarkAssistanceSystemModule() {
        // Specify the queries that are supported
        sqlCommands.put( Query.ALL, 
            "SELECT * FROM Marks" );
        sqlCommands.put( Query.LAST_NAME, 
            "SELECT * FROM Marks WHERE Assignment1 = ?" );
        sqlCommands.put( Query.UPDATE, 
            "UPDATE Marks SET Assignment1 = ?, Assignment2 = ?, Exam = ?, Total = ?, Grade = ? WHERE StudentID = ?" );
        sqlCommands.put( Query.RANGE, 
            "SELECT * FROM Marks WHERE Total >= ? AND Total <= ?" );
        sqlCommands.put( Query.GRADE, 
            "SELECT * FROM Marks WHERE Grade = ? ORDER BY Total" );
        sqlCommands.put( Query.UPDATE_ALL_GRADE, 
            "UPDATE Marks SET Grade = ? WHERE StudentID = ?" );
        sqlCommands.put( Query.UPDATE_CURRENT_GREADE, 
            "UPDATE Marks SET Grade = ? WHERE StudentID = ?" );
        sqlCommands.put( Query.TOLERANCE, 
            "SELECT * FROM Marks WHERE (Total + ?) = 85 OR (Total + ?) = 75 OR (Total + ?) = 65 OR (Total + ?) = 50" );
    }
    
    
    // IConnct implementation

    /**
     * Connect to the address book
     * @throws ConnectionException 
     */ 
    @Override
    public void connect() throws ConnectionException {
        // Connect to the address book database
        try {
        connection = DriverManager.getConnection( URL, USERNAME, PASSWORD );
        // Set proper schema
        connection.setSchema("APP"); 
        } catch(SQLException e ) {
            throw new ConnectionException("Unable to open data source",e);
        }
    }

    /**
     * Perform any initialization that is needed before queries can be
     * performed.
     *
     * @throws ConnectionException
     */
    @Override
    public void initialise() throws ConnectionException {
        // Create prepared statements for each query
        try {
            for (Query q : Query.values()) {
                statements.put(q, connection.prepareStatement(sqlCommands.get(q)));
            }
        } catch (SQLException e) {
            throw new ConnectionException("Unable to initialise data source",e);
        }
    }

    /**
     * Disconnect from the address book
     *
     * @throws ConnectionException
     */
    @Override
    public void disconnect() throws ConnectionException {
        // Close the connection 
        try (Connection c = connection) {
            // connection is closed automatically with try with resources
            // close prepared statements first
            for (Query q : Query.values()) {
                statements.get(q).close();
            }
        } catch (SQLException e) {
            throw new ConnectionException("Unable to close data source",e);
        }
    }

    // IQuery implementation
    
    /**
     * Perform a selection on the address book.
     * @param q the selection as specified in the Query enum
     * @param p parameters for the query specified as a varags of type Object
     * @return a List of Student objects that match query specification
     * @throws QueryException 
     */
    @Override
    public List<Student> select( Query q, String... p ) throws QueryException {
        switch ( q ) {
            case ALL:
                return getAllStudents();
            case TOLERANCE:
                return getStudentsByTolerance( p[0] );
            case RANGE:
                return getStudentsByRange( p[0], p[1] );
            case UPDATE_ALL_GRADE:
                return updateAllGrades();
            case GRADE:
                return getStudentsByGrade( p[0] );
        }
        // Should never happen
        return null;
    }

    /**
     * Perform a command (insert, delete, update ... ) on the address book
     * @param q the command as specified in the Query enum
     * @param p a Student object containing the data for the command
     * @return the number of records in the address book impacted on by the command
     * @throws QueryException 
     */
    @Override
    public int command( Query q, Student p ) throws QueryException {
        switch ( q ) {
            case UPDATE:
                return updateStudent( p );
            case UPDATE_CURRENT_GREADE:
                return updateCurrentGrade( p );
        }
        // Should never happen
        return -1;
    }

    private Student createStudent(ResultSet rs) throws QueryException {
        Student p = null;
        try {
            p = new Student(
                    rs.getString("studentID"),
                    rs.getInt("assignment1"),
                    rs.getInt("assignment2"),
                    rs.getInt("exam"),
                    rs.getInt("total"),
                    rs.getString("grade")
            );
        } catch (SQLException e) {
            throw (new QueryException("Unable to process the result of selection Query",e));
        }
        return p;
    }
    
    // Helper methods

    /*
     * Select all of the entries in the marks
     */
    private List< Student> getAllStudents() throws QueryException {
        // get prepared statement
        PreparedStatement ps = statements.get(Query.ALL);
        // executeQuery returns ResultSet containing matching entries
        try (ResultSet resultSet = ps.executeQuery()) {
            List<Student> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(createStudent(resultSet));
            }
            return results;
        } catch (SQLException e) {
            throw (new QueryException("Unable to execute selection ruery", e));
        }
    }
    
    /*
     * Select people by tolerance
     */
    private List< Student> getStudentsByTolerance(String tolerance) throws QueryException {
        // Look up prepared statement
        PreparedStatement ps = statements.get(Query.TOLERANCE);
        try {
            // Insert tolerance into prepared statement
            ps.setInt(1, Integer.parseInt(tolerance));
            ps.setInt(2, Integer.parseInt(tolerance));
            ps.setInt(3, Integer.parseInt(tolerance));
            ps.setInt(4, Integer.parseInt(tolerance));
        } catch (SQLException e) {
            throw (new QueryException("Unable to paramaterise selection query", e));
        }
        // executeQuery returns ResultSet containing matching entries
        try (ResultSet resultSet = ps.executeQuery()) {
            List<Student> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(createStudent(resultSet));
            }
            return results;
        } catch (SQLException e) {
            throw (new QueryException("Unable to execute selection query", e));
        }
    }
    
    /*
     * Select people by range
     */
    private List< Student> getStudentsByRange(String rangeFrom, String rangeTo) throws QueryException {
        // Look up prepared statement
        PreparedStatement ps = statements.get(Query.RANGE);
        try {
            // Insert range into prepared statement
            ps.setInt(1, Integer.parseInt(rangeFrom));
            ps.setInt(2, Integer.parseInt(rangeTo));
        } catch (SQLException e) {
            throw (new QueryException("Unable to paramaterise selection query", e));
        }
        // executeQuery returns ResultSet containing matching entries
        try (ResultSet resultSet = ps.executeQuery()) {
            List<Student> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(createStudent(resultSet));
            }
            return results;
        } catch (SQLException e) {
            throw (new QueryException("Unable to execute selection query", e));
        }
    }
    
    /*
     * Select people by grade and present in order of increasing total mark
     */
    private List< Student> getStudentsByGrade(String grade) throws QueryException {
        // Look up prepared statement
        PreparedStatement ps = statements.get(Query.GRADE);
        try {
            // Insert last name into prepared statement
            ps.setString(1, grade);
        } catch (SQLException e) {
            throw (new QueryException("Unable to paramaterise selection query", e));
        }
        // executeQuery returns ResultSet containing matching entries
        try (ResultSet resultSet = ps.executeQuery()) {
            List<Student> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(createStudent(resultSet));
            }
            return results;
        } catch (SQLException e) {
            throw (new QueryException("Unable to execute selection query", e));
        }
    }
    
    /*
     * Update a record to the marks. Record fields are extracted from the method
     * parameter, which is a Student object. 
     */
    private int updateStudent(Student p) throws QueryException {
        // Look up prepared statement
        PreparedStatement ps = statements.get(Query.UPDATE);
        // insert student attributes into prepared statement
        try {
            ps.setString(6, p.getStudentID());
            ps.setInt(1, p.getAssignment1());
            ps.setInt(2, p.getAssignment2());
            ps.setInt(3, p.getExam());
            ps.setInt(4, p.getTotal());
            ps.setString(5, p.getGrade());
        } catch (SQLException e) {
            throw (new QueryException("Unable to paramaterise selection query", e));
        }
        // update the new entry; returns # of rows updated
        try {
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw (new QueryException("Unable to perform update command", e));
        }
    }
    
    /*
     * Update a grade record to the marks. Record fields are extracted from the 
     * method parameter, which is a Student object. 
     */
    private int updateCurrentGrade(Student p) throws QueryException {
        // Look up prepared statement
        PreparedStatement ps = statements.get(Query.UPDATE_CURRENT_GREADE);
        // update student grade attributes into prepared statement
        String grade = gradeDetermination(p.getTotal(),p.getAssignment1(),p.getAssignment2(),p.getExam());
        try {
            ps.setString(2, p.getStudentID());
            ps.setString(1, grade);
        } catch (SQLException e) {
            throw (new QueryException("Unable to paramaterise selection query", e));
        }
        // update the new entry; returns # of rows updated
        try {
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw (new QueryException("Unable to perform update current grade command", e));
        }
    }
    
    /*
     * Update all student grade records to the marks. Record fields are extracted 
     * from the method parameter, which is a Student object. 
     */
    private List< Student> updateAllGrades() throws QueryException{
        // Look up prepared statement
        PreparedStatement ps = statements.get(Query.ALL);
        try (ResultSet resultSet = ps.executeQuery()) {
            List<Student> results = new ArrayList<>();
            // Go through every records in marks
            while (resultSet.next()) {
                String id = resultSet.getString("studentID");
                int asn1 = resultSet.getInt("assignment1");
                int asn2 = resultSet.getInt("assignment2");
                int exam = resultSet.getInt("exam");
                int total = resultSet.getInt("total");
                String grade = gradeDetermination(total, asn1, asn2, exam);
                // update student grade attributes into prepared statement
                PreparedStatement psu = statements.get(Query.UPDATE_CURRENT_GREADE);
                try {
                    psu.setString(2, id);
                    psu.setString(1, grade);
                } catch (SQLException e) {
                    throw (new QueryException("Unable to paramaterise calculate all query", e));
                }
                // Update every enties
                try {
                    psu.executeUpdate();
                } catch (SQLException e) {
                    throw (new QueryException("Unable to perform calculate all grade command", e));
                }
            }
            return getAllStudents();   
        } catch (SQLException e) {
            throw (new QueryException("Unable to execute selection all ruery", e));
        }
    }
    
    /*
     * Input the total mark of a student and output the grade of this student base
     * on the rules of grade determination.
     */
    private String gradeDetermination(Integer total, Integer asn1, Integer asn2, Integer exam){
        String grade = "?";
        if (total>=85) {
            grade = "HD";
        }else if (total>=75 && total<85){
            grade = "D";
        }else if (total>=65 && total<75){
            grade = "C";
        }else if(total>=50 && total<65){
            grade = "P";
        }else if(total<50){
            if (total>=45 && asn1<10 && asn2>=15 && exam>=25) {
                grade = "SA";
            }else if (total>=45 && asn2<15 && asn1>=10 && exam>=25) {
                grade = "SA";
            }else if (total>=45 && asn1>10 && asn2<15 && exam<25) {
                grade = "SE";
            }else if (asn1==0 && asn2==0 && exam==0){
                grade = "AF";
            }else{
                grade = "F";
            }
        }
        return grade;
    }
    
}
