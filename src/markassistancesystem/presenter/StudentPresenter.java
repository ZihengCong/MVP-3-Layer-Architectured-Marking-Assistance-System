package markassistancesystem.presenter;

import markassistancesystem.model.IQuery;
import markassistancesystem.model.QueryException;
import markassistancesystem.model.IConnect;
import markassistancesystem.model.ConnectionException;
import markassistancesystem.model.Student;
import markassistancesystem.view.IView;

import java.util.List;

// The queries that are available for the marks
import static markassistancesystem.model.MarkAssistanceSystemModule.Query.*;

/**
 * StudentPresenter provides a presenter implementation of the MVP pattern for
 the mark assistance application. As such, the class provides methods that manage
 * the connection to the mark assistance model (via the IConnect interface), query
 * the model (via the IQuery interface) and update the view (via the IView
 * interface). In this implementation, the presenter only interacts with the
 * view for display updating, and not for retrieval of user input. Furthermore,
 * user input is validated in the presenter and not in the view.
 *
 * @author Ziheng Cong
 */
public class StudentPresenter {
    
    // This ViewModel class contains methods to go through record in marks
    private static class ViewModel {
        List<Student> model;
        Student current;
        int index;
        int n;
        
        ViewModel() {
        }
        
        void set( List<Student> m ) {
            model = m;
            index = 0;
            n = model.size();
            current = model.get(index);
        }
        
        IndexedStudent previous() {
            if (--index < 0 )
                index = n-1;
            return new IndexedStudent( model.get(index), index+1, n );              
        }
        
        IndexedStudent next() {
            if (++index > n-1 )
                index = 0;
            return new IndexedStudent( model.get(index), index+1, n );            
        }
        
        IndexedStudent current() {
            return new IndexedStudent( model.get(index), index+1, n );  
        }
    }

    // The context for model and view interaction
    IView view;
    IQuery queries;
    IConnect connector;
    ViewModel viewModel;

    /**
     * Create a presenter instance. As there is a circular dependency between the
     * view and the presenter, only the presenters model dependencies are injected 
     * via the constructor - the view dependency is explictly injected via the
     * bind() method.
     * @param iq 
     * @param ic 
     */
    public StudentPresenter(IQuery iq, IConnect ic) {
        // intialise model access
        queries = iq;
        connector = ic;
        // initialise the browsing context
        viewModel = new ViewModel();
    }

    /**
     * Set the view dependency for the presenter  
     * @param iv the view
     */
    public void bind(IView iv) {
        view = iv;
    }

    /**
     * For the records being browsed, make the previous record the current
     * record and display it, together with its position in the browsing
     * context. If the current record is the first record, the last record will
     * become the current record.
     */
    public void showPrevious() {
        view.displayRecord( viewModel.previous() );
    }

    /**
     * For the records being browsed, make the next record the current record 
     * and display it, together with its position in the browsing
     * context. If the current record is the last record, the first record will 
     * become the current record.
     */
    public void showNext() {
        view.displayRecord( viewModel.next() );
    }
    
    private void displayCurrentRecord(List results) {
        if (results.isEmpty()) {
            view.displayMessage("No records found");
            view.setBrowsing(false);
            return;
        }
        viewModel.set(results);
        view.displayRecord(viewModel.current());
        view.setBrowsing(true);
    }
    
    /**
     * Set the browsing context to contain all records in the marks with 
     * a specified tolerance. Display he first record or an error message if no 
     * records are found.
     * @param tolerancce the tolerance of total marks being searched for.
     * @throws IllegalArgumentException if tolerance is an empty string.
     */
    public void selectByTolerance(Integer tolerance) throws IllegalArgumentException {
        if ("".equals(tolerance + "")) {
            throw new IllegalArgumentException("Argument must not be an empty string");
        }
        try {
            List results = queries.select(TOLERANCE, tolerance.toString());
            displayCurrentRecord(results);
        } catch (QueryException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Set the browsing context to contain all records in the marks with 
     * a specified range. Display he first record or an error message if no 
     * records are found.
     * @param rangeFrom the start point of range in the total marks being searched for.
     * @param rangeTo the end point of range in total marks being searched for.
     * @throws IllegalArgumentException if range is an empty string.
     */
    public void selectByRange(Integer rangeFrom, Integer rangeTo) throws IllegalArgumentException {
        if ("".equals(rangeFrom + "") || "".equals(rangeTo + "")) {
            throw new IllegalArgumentException("Argument must not be an empty string");
        }
        try {
            List results = queries.select(RANGE, rangeFrom.toString(), rangeTo.toString());
            displayCurrentRecord(results);
        } catch (QueryException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Set the browsing context to contain all records in the marks with 
     * a specified grade. Display he first record or an error message if no 
     * records are found.
     * @param grade the grade in the total marks being searched for.
     * @throws IllegalArgumentException if grade is an empty string.
     */
    public void selectByGrade(String grade) throws IllegalArgumentException {
        if (grade.equals("")) {
            throw new IllegalArgumentException("Argument must not be an empty string");
        }
        try {
            List results = queries.select(GRADE, grade);
            displayCurrentRecord(results);
        } catch (QueryException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Set the browsing context to all records in the marks and display 
     * the first record.
     */
    public void selectAll() {
        try {
            List results = queries.select(ALL);
            displayCurrentRecord(results);
        } catch (QueryException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Update a new entry into the marks.
     * @param id student id
     * @param asg1 assignment1 mark
     * @param asg2 assignment2 mark
     * @param exam exam mark
     * @param total total mark
     * @param grade grade of student
     * @throws IllegalArgumentException if any of the parameters are empty strings
     */
    public void update(String id, Integer asg1, Integer asg2, Integer exam, Integer total, String grade) throws IllegalArgumentException {
        if (id.equals("") || "".equals(asg1 + "") || "".equals(asg2 + "") || "".equals(exam + "") || "".equals(total + "") || grade.equals("")) {
            throw new IllegalArgumentException("Arguments must not contain an empty string");
        }
        try {
            //The grade field will be calculated by calculate button
            Student p = new Student(id, asg1, asg2, exam, total, grade);
            int result = queries.command(UPDATE, p);
            if (result == 1) {
                view.displayMessage("Student updated");
            } else {
                view.displayMessage("Student not updated");
            }
            view.setBrowsing(false);
        } catch (QueryException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Calculate current grade of the student
     * @param id the id of the student
     * @param asg1 the assignment1 mark
     * @param asg2 the assignment2 mark
     * @param exam the mark of the exam
     * @param total the total mark of this student
     * @param grade the grade of this student
     */
    public void calculateCurrentGrade(String id, Integer asg1, Integer asg2, Integer exam, Integer total, String grade){
        if (id.equals("") || "".equals(asg1 + "") || "".equals(asg2 + "") || "".equals(exam + "") || "".equals(total + "") || grade.equals("")) {
            throw new IllegalArgumentException("Arguments must not contain an empty string");
        }
        try {
            //The grade field will be calculated by calculate button
            Student p = new Student(id, asg1, asg2, exam, total, grade);
            int result = queries.command(UPDATE_CURRENT_GREADE, p);
            if (result == 1) {
                view.displayMessage("Current grade calculated");
            } else {
                view.displayMessage("Current grade not calculated");
            }
            view.setBrowsing(false);
        } catch (QueryException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Calculate all the grades of the student mark records store in the 
     * table marks.
     */
    public void calculateAllGrades(){
        try {
            List results = queries.select(UPDATE_ALL_GRADE);
            view.displayMessage("All grades calculated");
            displayCurrentRecord(results);
        } catch (QueryException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     *  Close the address book.
     */
    public void close() {
        try {
            connector.disconnect();
        } catch (ConnectionException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }
    }
}
