import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.time.LocalDate;
//import java.awt.event.ActionListener;


public class Window {
    private static final int HEIGHT = 700, WIDTH = HEIGHT*16/9;
    public Window(){
        JFrame frame = new JFrame("FTCStats");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        //JLabel label = new JLabel("Some info");
        JButton spreadsheetButton = new JButton("Launch Spreadsheet");
        JPanel panel = new JPanel();
        panel.setLayout(null);

        spreadsheetButton.addActionListener(new DriversListListener() {
            public void actionPerformed(ActionEvent e) {
                Main.launchSpreadsheet();
            }
        });
        spreadsheetButton.setSize(200, 50);
        spreadsheetButton.setLocation(frame.getSize().width/2 - spreadsheetButton.getWidth()/2, 500);

        JComboBox[] selectionBoxes = {
                new JComboBox(new String[] {"--select--","Bredan", "Erin", "Luca"}),
                new JComboBox(new String[] {"--select--", "Mason", "Zoe", "Cyrus"}),
                new JComboBox(new String[] {"--select--", "Caleb", "Matt", "Zach"})
        };
        int listX = 125, listY = 100;

        JComboBox typeSelector = new JComboBox(new String[] {"--select--", "Practice", "Competition"});
        typeSelector.setSelectedIndex(0);
        typeSelector.addActionListener(new TypeListener());
        typeSelector.setSize(100, 30);
        typeSelector.setLocation(listX,listY);
        typeSelector.setName("Type");
        panel.add(typeSelector);

        for(JComboBox b : selectionBoxes){
            b.setSelectedIndex(0);
            b.addActionListener(new DriversListListener());
            b.setSize(100, 30);
            b.setLocation(listX+=150, listY);
            panel.add(b);
        }
        selectionBoxes[0].setName("Drivers");
        selectionBoxes[1].setName("Operators");
        selectionBoxes[2].setName("Coaches");

        listX+=25;
        JTextField[] scoreFields = {
                new JTextField(),
                new JTextField(),
                new JTextField(),
                new JTextField()
        };
        for(JTextField f : scoreFields){
            f.setColumns(6);
            f.setSize(80, 30);
            f.setLocation(listX+=100, listY);
            f.addFocusListener(new TextListener());
            f.addActionListener(new TextListener());
            panel.add(f);
        }
        scoreFields[0].setName("Total");
        scoreFields[1].setName("Teleop");
        scoreFields[2].setName("Auto");
        scoreFields[3].setName("Penalties");

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createEntry();
            }
        });
        submitButton.setSize(80, 40);
        submitButton.setLocation(frame.getSize().width/2 - submitButton.getWidth()/2, 200);

        panel.add(spreadsheetButton);
        panel.add(submitButton);

        //frame.add(label, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void createEntry(){
        char type = TypeListener.getType();
        String[] names = DriversListListener.getDrivers();
        double[] scores = TextListener.getScores();
        Main.writeEntry(type, names, scores);
    }
}
