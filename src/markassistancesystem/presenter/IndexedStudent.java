package markassistancesystem.presenter;

import markassistancesystem.model.Student;

/**
 * IndexedStudent provides a wrapper for a Student record that provides
 information relating to its position in the browsing context maintained by
 StudentPresenter.
 *
 * @author Ziheng Cong
 */
public class IndexedStudent {

    private final Student p;
    private final int i;
    private final int n;

    /**
     * Create a wrapper for a Student object
     *
     * @param p the object to be wrapped
     * @param i the position of the object in the browsing context
     * @param n the number of objects in the browsing context
     */
    public IndexedStudent(Student p, int i, int n) {
        this.p = p;
        this.i = i;
        this.n = n;
    }

    /**
     * @return the person object being wrapped
     */
    public Student getStudent() {
        return p;
    }

    /**
     * @return the position of the wrapped person object in the browsing context
     */
    public int getIndex() {
        return i;
    }

    /**
     * @return the number of objects in the browsing context
     */
    public int getSize() {
        return n;
    }
    
}
