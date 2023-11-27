package UIElements;

import Core.Settings;
import TeamMember.Team;
import com.toedter.calendar.JDateChooser;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;

public class Window {
    final static String INPUTPANEL = "Match Input";
    final static String SETTINGSPANEL = "Settings";
    final static int extraWindowWidth = 100;
    final static int extraWindowHeight = 300;
    private Team team;

    public void addComponentToPane(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();

        //Create the "cards".
        JPanel card1 = new JPanel() {
            //Make the panel wider than it really needs, so
            //the window's wide enough for the tabs to stay
            //in one row.
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                size.height += extraWindowHeight;
                return size;
            }
        };
        JComboBox ts = new JComboBox(new String[] {"--select--", "Practice", "Competition"});
        JComboBox d = new JComboBox(new String[] {"--select--", "Bredan", "Erin", "Luca"});
        JComboBox o = new JComboBox(new String[] {"--select--", "Mason", "Zoe", "Cyrus"});
        JComboBox c = new JComboBox(new String[] {"--select--", "Caleb", "Matt", "Zach"});
        JTextField to = new JTextField();
        JTextField te = new JTextField();
        JTextField au = new JTextField();
        JTextField pe = new JTextField();
        JDateChooser date = new JDateChooser();
        to.setColumns(6);
        te.setColumns(6);
        au.setColumns(6);
        pe.setColumns(6);
        card1.add(ts);
        card1.add(date);
        card1.add(d);
        card1.add(o);
        card1.add(c);
        card1.add(to);
        card1.add(te);
        card1.add(au);
        card1.add(pe);
        JButton su = new JButton("Submit");
        su.addActionListener(new SubmitListener(team, date, ts, new JComboBox[] {d, o, c}, new JTextField[] {to, te, au, pe}));
        JButton sb = new JButton("Spreadsheet");
        sb.addActionListener(new SpreadsheetButtonListener(team));
        card1.add(su);
        card1.add(sb);

        JTextField da = new JTextField(Double.toString(Settings.dateWeight), 15);
        JTextField cw = new JTextField(Double.toString(Settings.relativeCoachWeight), 5);
        JTextField pw = new JTextField(Double.toString(Settings.relativePracticeWeight), 5);
        JTextField ow = new JTextField(Double.toString(Settings.relativeOldRobotWeight), 5);

        JPanel card2 = new JPanel();
        card2.add(new JLabel("Date Weight"));
        card2.add(da);
        card2.add(new JLabel("Coach Weight"));
        card2.add(cw);
        card2.add(new JLabel("Practice Weight"));
        card2.add(pw);
        card2.add(new JLabel("Old Robot Weight"));
        card2.add(ow);
        JButton save = new JButton("Save");
        save.addActionListener(new SaveButtonListener(team, da, cw, pw, ow));
        card2.add(save);


        tabbedPane.addTab(INPUTPANEL, card1);
        tabbedPane.addTab(SETTINGSPANEL, card2);

        pane.add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("FTCStats");
        frame.setDefaultCloseOperation(/*closeOperation()*/ JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        //TabDemo demo = new TabDemo();
        addComponentToPane(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private int closeOperation(){
        team.loadCalcSave();
        return JFrame.EXIT_ON_CLOSE;
    }

    public Window(Team team){
        this.team = team;
        createAndShowGUI();
    }
}
