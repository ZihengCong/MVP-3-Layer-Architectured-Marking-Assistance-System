package markassistancesystem.model;

/**
 * A data container for student grade records. No setters are provided, as
 * attributes are read-only. Getters for all attributes are provided.
 *
 * @author Ziheng Cong
 */
public class Student {

    private final String studentID;
    private final int assignment1;
    private final int assignment2;
    private final int exam;
    private final int total;
    private final String grade;

    /**
     * Create a data transfer object for an student grade record. 
     * 
     * @param studentID record identifier
     * @param assignment1 assignment1 grade
     * @param assignment2 assignment2 grade
     * @param exam exam grade
     * @param total total grade
     * @param grade student grade
     */
    public Student(String studentID, int assignment1, int assignment2, int exam, int total, String grade) {
        this.studentID = studentID;
        this.assignment1 = assignment1;
        this.assignment2 = assignment2;
        this.exam = exam;
        this.total = total;
        this.grade = grade;
    }

    // No setters are provided - use the constructor.
    // Getters.
    /**
    * @return record identifier
    */
    public String getStudentID() {
        return studentID;
    }
    
    /**
     * @return assignment1 grade
     */
    public int getAssignment1() {
        return assignment1;
    }
    
    /**
     * @return assignment2 grade
     */
    public int getAssignment2() {
        return assignment2;
    }

    /**
     * @return exam grade
     */
    public int getExam() {
        return exam;
    }

    /**
     * @return total grade
     */
    public int getTotal() {
        return total;
    }

    /**
     * @return student grade
     */
    public String getGrade() {
        return grade;
    }
    
}
