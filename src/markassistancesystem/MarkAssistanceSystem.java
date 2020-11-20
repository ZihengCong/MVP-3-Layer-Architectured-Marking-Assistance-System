package markassistancesystem;

import markassistancesystem.presenter.StudentPresenter;
import markassistancesystem.view.StudentView;
import markassistancesystem.model.ConnectionException;
import markassistancesystem.model.MarkAssistanceSystemModule;

/**
 * MarkAssistanceSystem is the application class that refactor to MVP 
 * structure.It can be used to assist allocation of student grades. 
 *
 * @author Ziheng Cong
 */
public class MarkAssistanceSystem {

    public static void main(String args[]) {
        // Create the model. Exit the application if connection be made to the 
        // mark assistance system.
        MarkAssistanceSystemModule msm = new MarkAssistanceSystemModule();
        try {
            msm.connect();
            msm.initialise();
        } catch (ConnectionException e) {
            System.err.println( e.getMessage());
            e.getCause().printStackTrace();
            System.exit(1);
        }
        // Create the presenter and view and inject their dependencies. Note 
        // there is a circular dependency beetween the presenter and the view, so
        // an explicit binding method (bind()) is required.
        StudentPresenter pp = new StudentPresenter(msm, msm);
        StudentView pv = new StudentView(pp);
        pp.bind(pv);
        // Start the application
        pv.setVisible(true);
    }
    
}
