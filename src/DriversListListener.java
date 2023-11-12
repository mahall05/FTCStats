import javax.swing.*;
import java.awt.event.ActionEvent;

public class DriversListListener implements java.awt.event.ActionListener {
    private static String[] drivers = new String[3];

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();

        if((cb.getName().equals("Drivers"))){
            drivers[0] = (String) cb.getSelectedItem();
        }else if((cb.getName().equals("Operators"))){
            drivers[1] = (String) cb.getSelectedItem();
        }else if((cb.getName().equals("Coaches"))){
            drivers[2] = (String) cb.getSelectedItem();
        }
    }

    public static String[] getDrivers(){
        return drivers;
    }
}
