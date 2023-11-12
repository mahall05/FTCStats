package UIElements;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TypeListener implements ActionListener {
    private static char type;
    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        type = (cb.getSelectedItem().equals("Practice")?'c':'p');
    }

    public static char getType(){
        return type;
    }
}
