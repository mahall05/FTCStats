package UIElements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.time.LocalDate;
//import java.awt.event.ActionListener;


public class Window {
    JFrame frame = new JFrame("FTCStats");
    private static final int HEIGHT = 700, WIDTH = HEIGHT*16/9;
    JButton spreadsheetButton;
    Dimension spButtonSize = new Dimension(200, 50);
    JButton submitButton;
    Dimension subButtonSize = new Dimension(80, 40);
    JComboBox typeSelector;
    Dimension textBoxSize = new Dimension(80, 30);
    JComboBox[] selectionBoxes = {
            new JComboBox(new String[] {"--select--","Bredan", "Erin", "Luca"}),
            new JComboBox(new String[] {"--select--", "Mason", "Zoe", "Cyrus"}),
            new JComboBox(new String[] {"--select--", "Caleb", "Matt", "Zach"})
    };
    Dimension selBoxSize = new Dimension(100, 30);
    JTextField[] scoreFields = {
            new JTextField(),
            new JTextField(),
            new JTextField(),
            new JTextField()
    };

    public void update(){
        spreadsheetButton.setSize((int) (spButtonSize.getWidth()/WIDTH*frame.getWidth()), (int) (spButtonSize.getHeight()/HEIGHT*frame.getHeight()));
        spreadsheetButton.setLocation(frame.getSize().width/2 - spreadsheetButton.getWidth()/2, 500*frame.getHeight()/HEIGHT);

        submitButton.setSize((int) (subButtonSize.getWidth()/WIDTH*frame.getWidth()), (int) (subButtonSize.getHeight()/HEIGHT*frame.getHeight()));
        submitButton.setLocation(frame.getSize().width/2 - submitButton.getWidth()/2, 200*frame.getHeight()/HEIGHT);

        typeSelector.setSize((int) (selBoxSize.getWidth()/WIDTH*frame.getWidth()), (int) (selBoxSize.getHeight()/HEIGHT*frame.getHeight()));
        typeSelector.setLocation(125*frame.getWidth()/WIDTH,100*frame.getHeight()/HEIGHT);

        selectionBoxes[0].setSize(typeSelector.getSize());
        selectionBoxes[0].setLocation(typeSelector.getX()+125*frame.getWidth()/WIDTH, typeSelector.getY());
        for(int i = 1; i < selectionBoxes.length; i++){
            selectionBoxes[i].setSize(typeSelector.getSize());
            selectionBoxes[i].setLocation(selectionBoxes[i-1].getX()+125*frame.getWidth()/WIDTH, selectionBoxes[i-1].getY());
        }

        scoreFields[0].setSize((int) (textBoxSize.getWidth()/WIDTH*frame.getWidth()), (int) (textBoxSize.getHeight()/HEIGHT*frame.getHeight()));
        scoreFields[0].setLocation(selectionBoxes[selectionBoxes.length-1].getX()+125*frame.getWidth()/WIDTH, typeSelector.getY());
        for(int i = 1; i < scoreFields.length; i++){
            scoreFields[i].setSize(scoreFields[0].getSize());
            scoreFields[i].setLocation(scoreFields[i-1].getX()+125*frame.getWidth()/WIDTH, typeSelector.getY());
        }
    }


    public Window(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        //JLabel label = new JLabel("Some info");
        spreadsheetButton = new JButton("Launch Spreadsheet");
        JPanel panel = new JPanel();
        panel.setLayout(null);

        spreadsheetButton.addActionListener(new DriversListListener() {
            public void actionPerformed(ActionEvent e) {
                Main.launchSpreadsheet();
            }
        });
        spreadsheetButton.setSize(spButtonSize);
        spreadsheetButton.setLocation(frame.getSize().width/2 - spreadsheetButton.getWidth()/2, 500);

        int listX = 125, listY = 100;

        typeSelector = new JComboBox(new String[] {"--select--", "Practice", "Competition"});
        typeSelector.setSelectedIndex(0);
        typeSelector.addActionListener(new TypeListener());
        typeSelector.setSize(selBoxSize);
        typeSelector.setLocation(listX,listY);
        typeSelector.setName("Type");
        panel.add(typeSelector);

        for(JComboBox b : selectionBoxes){
            b.setSelectedIndex(0);
            b.addActionListener(new DriversListListener());
            b.setSize(selBoxSize);
            b.setLocation(listX+=150, listY);
            panel.add(b);
        }
        selectionBoxes[0].setName("Drivers");
        selectionBoxes[1].setName("Operators");
        selectionBoxes[2].setName("Coaches");

        listX+=25;

        for(JTextField f : scoreFields){
            f.setColumns(6);
            f.setSize(textBoxSize);
            f.setLocation(listX+=100, listY);
            f.addFocusListener(new TextListener());
            f.addActionListener(new TextListener());
            panel.add(f);
        }
        scoreFields[0].setName("Total");
        scoreFields[1].setName("Teleop");
        scoreFields[2].setName("Auto");
        scoreFields[3].setName("Penalties");

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createEntry();
            }
        });
        submitButton.setSize(subButtonSize);
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
