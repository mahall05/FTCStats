package UIElements;

import TeamMember.Team;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.ZoneId;

public class SubmitListener implements ActionListener {
    private JComboBox typeSelect;
    private JDateChooser dateChooser;
    private JComboBox[] selections;
    private JTextField[] scoreSelectors;
    private Team team;
    public SubmitListener(Team team, JDateChooser dateChooser, JComboBox typeSelect, JComboBox[] selections, JTextField[] scores){
        this.team = team;
        this.dateChooser = dateChooser;
        this.typeSelect = typeSelect;
        this.selections = selections;
        this.scoreSelectors = scores;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean validEntry = true;
        char type = ' ';
        String[] names = new String[3];
        double[] scores = new double[4];
        String typeString = (String) typeSelect.getSelectedItem();
        if(typeString.equals("Practice")){
            type = 'p';
        }else if(typeString.equals("Competition")){
            type = 'c';
        }else{
            validEntry = false;
        }

        for(int i = 0; i < selections.length; i++){
            if(((String) selections[i].getSelectedItem()).equals("--select--")){
                validEntry = false;
                break;
            }else{
                names[i] = (String) selections[i].getSelectedItem();
            }
        }

        for(int i = 0; i < scoreSelectors.length; i++){
            String text = scoreSelectors[i].getText();
            if(text.isEmpty() || Double.parseDouble(text) < -1){
                validEntry = false;
                break;
            }else{
                scores[i] = Double.parseDouble(text);
            }
        }

        if(validEntry){
            team.writeMatchEntry(type, dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), names, scores);
        }
    }
}
