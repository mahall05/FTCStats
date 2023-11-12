package UIElements;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TextListener implements FocusListener, ActionListener{
    private static double[] scores = new double[4];

    public static double[] getScores(){
        return scores;
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        parseInput((JTextField)e.getSource());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        parseInput((JTextField) e.getSource());
    }

    private void parseInput(JTextField cb){
        if((cb.getName().equals("Total"))){
            System.out.println("Total assessed");
            scores[0] = Double.parseDouble(cb.getText().isEmpty()?"0":cb.getText());
        }else if((cb.getName().equals("Teleop"))){
            System.out.println("Teleop assessed");
            scores[1] = Double.parseDouble(cb.getText().isEmpty()?"0":cb.getText());
        }else if((cb.getName().equals("Auto"))){
            System.out.println("Autos assessed");
            scores[2] = Double.parseDouble(cb.getText().isEmpty()?"0":cb.getText());
        }else if((cb.getName().equals("Penalties"))) {
            System.out.println("Penalties assessed");
            scores[3] = Double.parseDouble(cb.getText().isEmpty() ? "0" : cb.getText());
        }
    }
}
