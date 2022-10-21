import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm {
    private TrialPanel trialPanel;
    private JPanel panelContainer;
    private JPanel controlPanel;
    private JPanel targetSizePanel;
    private JPanel testRadiusPanel;
    private JPanel StartOrResetPanel;
    private JSlider targetSizeSlider;
    private JSlider testRadiusSlider;
    private JButton startOrResetButton;
    private JLabel targetSizeInfoLabel;
    private JLabel testRadiusInfoLabel;
    private JTextField subjectIDTextField;
    private JPanel subjectIDPanel;
    private JLabel subjectIDInfoLabel;

    private SubjectInfo subject;



    public MainForm() {

        targetSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                trialPanel.setTargetSize(targetSizeSlider.getValue());
            }
        });
        testRadiusSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                trialPanel.setTestRadius(testRadiusSlider.getValue());
            }
        });
        startOrResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (subjectIDTextField.getText().equals("")) {
                    int input = JOptionPane.showConfirmDialog(null, "Enter a Subject ID", "Invalid", JOptionPane.CANCEL_OPTION);
                } else {
                    subject = new SubjectInfo(subjectIDTextField.getText(), targetSizeSlider.getValue(), testRadiusSlider.getValue());
                    trialPanel.addListener(MainForm.this);
                    trialPanel.runTrial(subject);

                }
            }
        });
    }

    public void updateSubjectInfo(SubjectInfo subject){
        this.subject = subject;
        feedback();
    }

    public void feedback(){
        subject.calculateIndexOfDifficulty();
        subject.calulateAverageMotionTime();
        int input = JOptionPane.showConfirmDialog(null,"Index of Difficulty: " + subject.getIndexOfDifficulty() + "\nAverage Motion Time to Acquire Target: " + subject.getAverageMotionTime(), "Trial Complete!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
        subject.saveSubjectInfo();
        trialPanel.reset();
        subjectIDTextField.setText("");
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        trialPanel = new TrialPanel(55, 250);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().panelContainer);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
