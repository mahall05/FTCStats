package UIElements;

import TeamMember.Team;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SpreadsheetButtonListener implements ActionListener {
    private Team team;
    public SpreadsheetButtonListener(Team team){
        this.team = team;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        team.saveAndLaunch();
    }
}
