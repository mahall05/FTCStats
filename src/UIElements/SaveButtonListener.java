package UIElements;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Core.Settings;
import TeamMember.Team;

public class SaveButtonListener implements ActionListener {
    private Team team;
    private JTextField dateWeightIn;
    private JTextField coachWeightIn;
    private JTextField practiceWeightIn;
    private JTextField oldRobotIn;

    public SaveButtonListener(Team team, JTextField dateWeightIn, JTextField coachWeightIn, JTextField practiceWeightIn, JTextField oldRobotIn){
        this.team = team;
        this.dateWeightIn = dateWeightIn;
        this.coachWeightIn = coachWeightIn;
        this.practiceWeightIn = practiceWeightIn;
        this.oldRobotIn = oldRobotIn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Settings.dateWeight = Double.parseDouble(dateWeightIn.getText());
        Settings.relativeCoachWeight = Double.parseDouble(coachWeightIn.getText());
        Settings.relativePracticeWeight = Double.parseDouble(practiceWeightIn.getText());
        Settings.relativeOldRobotWeight = Double.parseDouble(oldRobotIn.getText());

        team.loadMatches();
        team.assignMatches();
        team.runCalculations();
        team.saveWorkbook();
    }
}
