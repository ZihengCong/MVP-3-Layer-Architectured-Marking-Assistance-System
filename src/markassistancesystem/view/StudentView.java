package markassistancesystem.view;

import markassistancesystem.presenter.StudentPresenter;
import markassistancesystem.presenter.IndexedStudent;
import markassistancesystem.model.Student;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * 
 * @author Ziheng Cong
 */

public class StudentView extends JFrame implements IView<IndexedStudent> {
    // the presenter for this view
    private StudentPresenter presenter;

    // GUI components
    private JPanel navigatePanel;
    private JPanel displayPanel;
    private JButton previousButton;
    private JTextField indexTextField;
    private JLabel ofLabel;
    private JTextField maxTextField;
    private JButton nextButton;
    private JLabel studentIDLabel;
    private JTextField studentIDTextField;
    private JLabel assignment1Label;
    private JTextField assignment1TextField;
    private JLabel assignment2Label;
    private JTextField assignment2TextField;
    private JLabel examLabel;
    private JTextField examTextField;
    private JLabel totalLabel;
    private JTextField totalTextField;
    private JLabel gradeLabel;
    private JTextField gradeTextField;
    private JButton calculateAllGradesButton;
    private JButton calculateCurrentGradeButton;
    private JPanel toleranceQueryPanel;
    private JLabel toleranceLabel;
    private JTextField toleranceTextField;
    private JButton toleranceFindButton;
    private JPanel rangeQueryPanel;
    private JLabel rangeLabel;
    private JTextField rangeFromTextField;
    private JLabel rangeToLabel;
    private JTextField rangeToTextField;
    private JButton rangeFindButton;
    private JButton browseAllEntiesButton;
    private JButton updateCurrentEntryButton;
    private JPanel gradeQueryPanel;
    private JLabel gradeFindLabel;
    private JTextField gradeFindTextField;
    private JButton gradeFindButton;
    

    public StudentView( StudentPresenter pp ) {
        super("Mark Assistance System");
        
        presenter = pp;

        // create GUI
        navigatePanel = new JPanel();
        displayPanel = new JPanel();
        previousButton = new JButton();
        indexTextField = new JTextField(2);
        ofLabel = new JLabel();
        maxTextField = new JTextField(2);
        nextButton = new JButton();
        studentIDLabel = new JLabel();
        studentIDTextField = new JTextField(10);
        assignment1Label = new JLabel();
        assignment1TextField = new JTextField(10);
        assignment2Label = new JLabel();
        assignment2TextField = new JTextField(10);
        examLabel = new JLabel();
        examTextField = new JTextField(10);
        totalLabel = new JLabel();
        totalTextField = new JTextField(10);
        gradeLabel = new JLabel();
        gradeTextField = new JTextField(10);
        calculateAllGradesButton = new JButton();
        calculateCurrentGradeButton = new JButton();
        toleranceQueryPanel = new JPanel();
        toleranceLabel = new JLabel();
        toleranceTextField = new JTextField(10);
        toleranceFindButton = new JButton();
        rangeQueryPanel = new JPanel();
        rangeLabel = new JLabel();
        rangeFromTextField = new JTextField(3);
        rangeToLabel = new JLabel();
        rangeToTextField = new JTextField(3);
        rangeFindButton = new JButton();
        browseAllEntiesButton = new JButton();
        updateCurrentEntryButton = new JButton();
        gradeQueryPanel = new JPanel();
        gradeFindLabel = new JLabel();
        gradeFindTextField = new JTextField(10);
        gradeFindButton = new JButton();
        

        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setSize(400, 495);
        setResizable(false);
        
        // Construct a panel for browsing of records - previous and next buttons,
        // "position" of current record, ie x of y.
        navigatePanel.setLayout(
                new BoxLayout(navigatePanel, BoxLayout.X_AXIS));

        previousButton.setText("Previous");
        previousButton.setEnabled(false);
        previousButton.addActionListener( (ActionEvent evt) -> {
            previousButtonActionPerformed( evt );
        });
        navigatePanel.add(previousButton);
        
        navigatePanel.add(Box.createHorizontalStrut(10));

        indexTextField.setHorizontalAlignment(
                JTextField.CENTER);
        navigatePanel.add(indexTextField);
        
        navigatePanel.add(Box.createHorizontalStrut(10));

        ofLabel.setText("of");
        navigatePanel.add(ofLabel);
        
        navigatePanel.add(Box.createHorizontalStrut(10));

        maxTextField.setHorizontalAlignment(
                JTextField.CENTER);
        maxTextField.setEditable(false);
        navigatePanel.add(maxTextField);
        
        navigatePanel.add(Box.createHorizontalStrut(10));

        nextButton.setText("Next");
        nextButton.setEnabled(false);
        nextButton.addActionListener( (ActionEvent evt) -> {
            nextButtonActionPerformed( evt );
        });
        navigatePanel.add(nextButton);
        add(navigatePanel);

        // Construct a panel to display / enter fields of a record
        displayPanel.setLayout(new GridLayout(6, 2, 4, 4));

        studentIDLabel.setText("Student ID:");
        displayPanel.add(studentIDLabel);
        displayPanel.add(studentIDTextField);

        assignment1Label.setText("Assignment 1:");
        displayPanel.add(assignment1Label);
        displayPanel.add(assignment1TextField);

        assignment2Label.setText("Assignment 2:");
        displayPanel.add(assignment2Label);
        displayPanel.add(assignment2TextField);

        examLabel.setText("Exam:");
        displayPanel.add(examLabel);
        displayPanel.add(examTextField);

        totalLabel.setText("Total:");
        displayPanel.add(totalLabel);
        displayPanel.add(totalTextField);
        
        gradeLabel.setText("Grade:");
        displayPanel.add(gradeLabel);
        displayPanel.add(gradeTextField);
        add(displayPanel);
        
        
        calculateAllGradesButton.setText("Calcualte All Grades");
        calculateAllGradesButton.addActionListener( (ActionEvent evt) -> {
            calculateAllGradesButtonPerformed(evt);
        } );
        add(calculateAllGradesButton);
          
        calculateCurrentGradeButton.setText("Calculate Current Grade");
        calculateCurrentGradeButton.addActionListener( (ActionEvent evt) -> {
            calculateCurrentGradeButtonPerformed(evt);
        } );
        add(calculateCurrentGradeButton);
        

        // construct a panel for the tolerance query
        toleranceQueryPanel.setLayout(
                new BoxLayout(toleranceQueryPanel, BoxLayout.X_AXIS));
        toleranceQueryPanel.setBorder(BorderFactory.createTitledBorder(
                "Find an entry by tolerance"));
        toleranceLabel.setText("Tolerance:");
        toleranceQueryPanel.add(Box.createHorizontalStrut(15));
        toleranceQueryPanel.add(toleranceLabel);
        toleranceQueryPanel.add(Box.createHorizontalStrut(30));
        toleranceQueryPanel.add(toleranceTextField);
        toleranceQueryPanel.add(Box.createHorizontalStrut(30));
        toleranceFindButton.setText("Find");
        toleranceFindButton.addActionListener( (ActionEvent evt) -> {
            toleranceFindButtonActionPerformed(evt);
        } ); 
        toleranceQueryPanel.add(toleranceFindButton);
        toleranceQueryPanel.add(Box.createHorizontalStrut(15));
        add(toleranceQueryPanel);

        // construct a panel for the range queries
        rangeQueryPanel.setLayout(
                new BoxLayout(rangeQueryPanel, BoxLayout.X_AXIS));
        rangeQueryPanel.setBorder(BorderFactory.createTitledBorder(
                "Find an entry by total mark range"));
        rangeLabel.setText("Range from:");
        rangeQueryPanel.add(Box.createHorizontalStrut(15));
        rangeQueryPanel.add(rangeLabel);
        rangeQueryPanel.add(Box.createHorizontalStrut(22));
        rangeQueryPanel.add(rangeFromTextField);
        rangeQueryPanel.add(Box.createHorizontalStrut(14));
        rangeToLabel.setText("to");
        rangeQueryPanel.add(rangeToLabel);
        rangeQueryPanel.add(Box.createHorizontalStrut(16));
        rangeQueryPanel.add(rangeToTextField);
        rangeQueryPanel.add(Box.createHorizontalStrut(28));
        rangeFindButton.setText("Find");
        rangeFindButton.addActionListener( (ActionEvent evt) -> {
            rangeFindButtonActionPerformed(evt);
        } ); 
        rangeQueryPanel.add(rangeFindButton);
        rangeQueryPanel.add(Box.createHorizontalStrut(15));
        add(rangeQueryPanel);
        
        // construct a panel for the grade queries
        gradeQueryPanel.setLayout(
                new BoxLayout(gradeQueryPanel, BoxLayout.X_AXIS));
        gradeQueryPanel.setBorder(BorderFactory.createTitledBorder(
                "Browse all entries by grade"));
        gradeFindLabel.setText("Grade:");
        gradeQueryPanel.add(Box.createHorizontalStrut(16));
        gradeQueryPanel.add(gradeFindLabel);
        gradeQueryPanel.add(Box.createHorizontalStrut(52));
        gradeQueryPanel.add(gradeFindTextField);
        gradeQueryPanel.add(Box.createHorizontalStrut(32));
        gradeFindButton.setText("Find");
        gradeFindButton.addActionListener( (ActionEvent evt) -> {
            gradeFindButtonActionPerformed(evt);
        } ); 
        gradeQueryPanel.add(gradeFindButton);
        gradeQueryPanel.add(Box.createHorizontalStrut(15));
        add(gradeQueryPanel);
        
        
        browseAllEntiesButton.setText("Browse All Entries");
        browseAllEntiesButton.addActionListener( (ActionEvent evt) -> {
            browseButtonActionPerformed(evt);
        } );
        add(browseAllEntiesButton);

        updateCurrentEntryButton.setText("Update Current Entry");
        updateCurrentEntryButton.addActionListener( (ActionEvent evt) -> {
            updateButtonActionPerformed(evt);
        } );
        add(updateCurrentEntryButton);
        
        
        addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    presenter.close();
                    System.exit(0);
                }
            }
        );
    }

    // Event handlers
    // handles call when previousButton is clicked
    private void previousButtonActionPerformed(ActionEvent evt) {
        presenter.showPrevious();
    }

    // handles call when nextButton is clicked
    private void nextButtonActionPerformed(ActionEvent evt) {
        presenter.showNext();
    }

    // handles call when calculateAllGradesButton is clicked
    private void calculateAllGradesButtonPerformed(ActionEvent evt){
        presenter.calculateAllGrades();
        presenter.selectAll();
    }
    
    // handles call when calculateCurrentGradeButton is clicked
    private void calculateCurrentGradeButtonPerformed(ActionEvent evt){
        presenter.calculateCurrentGrade(
            studentIDTextField.getText(),
            Integer.parseInt(assignment1TextField.getText()),
            Integer.parseInt(assignment2TextField.getText()),
            Integer.parseInt(examTextField.getText()),
            Integer.parseInt(totalTextField.getText()),
            gradeTextField.getText()
        );
        presenter.selectAll();
    }
    
    // handles call when toleranceFindButton is clicked
    private void toleranceFindButtonActionPerformed(ActionEvent evt){
        presenter.selectByTolerance(Integer.parseInt(toleranceTextField.getText()));
    }
    
    // handles call when rangeFindButton is clicked
    private void rangeFindButtonActionPerformed(ActionEvent evt){
        presenter.selectByRange(Integer.parseInt(rangeFromTextField.getText()), Integer.parseInt(rangeToTextField.getText()));
    }
    
    // handles call when gradeFindButton is clicked
    private void gradeFindButtonActionPerformed(ActionEvent evt){
        presenter.selectByGrade(gradeFindTextField.getText());
    }

    // handles call when browseButton is clicked
    private void browseButtonActionPerformed(ActionEvent evt) {
        presenter.selectAll();
    }
    
    // handles call when updateButton is clicked
    private void updateButtonActionPerformed(ActionEvent evt){
        presenter.update(
            studentIDTextField.getText(),
            Integer.parseInt(assignment1TextField.getText()),
            Integer.parseInt(assignment2TextField.getText()),
            Integer.parseInt(examTextField.getText()),
            Integer.parseInt(totalTextField.getText()),
            gradeTextField.getText()
        );
        presenter.selectAll();
    }

    
    // IView interface implementation

    @Override
    public void displayRecord( IndexedStudent ip ) {
        Student p = ip.getStudent();
        studentIDTextField.setText(p.getStudentID());
        assignment1TextField.setText(Integer.toString(p.getAssignment1()));
        assignment2TextField.setText(Integer.toString(p.getAssignment2()));
        examTextField.setText(Integer.toString(p.getExam()));
        totalTextField.setText(Integer.toString(p.getTotal()));
        gradeTextField.setText(p.getGrade());

        maxTextField.setText( Integer.toString( ip.getSize() ) );
        indexTextField.setText(Integer.toString( ip.getIndex() ) );
    }

    @Override
    public void setBrowsing( boolean flag ) {
        nextButton.setEnabled( flag );
        previousButton.setEnabled( flag );
    }

    @Override
    public void displayMessage(String s) {
        JOptionPane.showMessageDialog(this, s, "Message", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void displayError(String s) {
        System.err.println(s);
    }

}
