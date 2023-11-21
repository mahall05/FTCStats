package UIElements;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

class Window extends JFrame {
    private static final int HEIGHT = 700, WIDTH = HEIGHT*16/9;

    ArrayList<Panel> panels = new ArrayList<Panel>();
    public Window(){
        super("FTCStats");
    }

    public void addPanel(Panel p){
        add(p);
        panels.add(p);
    }

    public void update(){
        FloatingDimension fd = ratio();
        for(Panel p : panels){
            p.update(fd);
        }
    }

    public FloatingDimension ratio(){
        return new FloatingDimension((double) this.getWidth()/WIDTH, (double) this.getHeight()/HEIGHT);
    }

    private void mainScreenSetup(){
        Panel ms = new Panel();
        ms.setLayout(null);

        /* SPREADSHEET BUTTON */
        JButton spreadsheetButton = new JButton("Launch Spreadsheet");
        //spreadsheetButton.addActionListener(new DriversListListener() {public void actionPerformed(ActionEvent e) {main.saveAndLaunch();}}); TODO
        spreadsheetButton.setSize(new Dimension((int) (200*ratio().getWidth()), (int) (50*ratio().getHeight())));
        spreadsheetButton.setLocation(this.getSize().width/2 - spreadsheetButton.getWidth()/2, (int) (500*ratio().getHeight()));

        /* SUBMIT BUTTON */
        JButton submitButton = new JButton("Submit");
        //submitButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {createEntry();}}); TODO
        submitButton.setSize(new Dimension((int) (80*ratio().getWidth()), (int) (40*ratio().getHeight())));
        submitButton.setLocation(this.getSize().width/2 - submitButton.getWidth()/2, (int) (200*ratio().getHeight()));

        /* TYPE SELECTOR */
        JComboBox typeSelector = new JComboBox(new String[] {"--select--", "Practice", "Competition"});
        typeSelector.setSelectedIndex(0);
        //typeSelector.addActionListener(new TypeListener()); TODO
        typeSelector.setSize((int) (100*ratio().getWidth()), (int) (30*ratio().getHeight()));
        typeSelector.setLocation((int) (125*ratio().getWidth()),(int) (100*ratio().getHeight()));
        typeSelector.setName("Type");

        /* DRIVER SELECTORS */
        JComboBox[] selectionBoxes = {
                new JComboBox(new String[] {"--select--","Bredan", "Erin", "Luca"}),
                new JComboBox(new String[] {"--select--", "Mason", "Zoe", "Cyrus"}),
                new JComboBox(new String[] {"--select--", "Caleb", "Matt", "Zach"})
        };

        selectionBoxes[0].setLocation(typeSelector.getLocation().x+typeSelector.getWidth()+((int)(25*ratio().getWidth())),
                typeSelector.getLocation().y);

        for(int i = 0; i < selectionBoxes.length; i++){
            selectionBoxes[i].setSelectedIndex(0);
            //selectionBoxes[i].addActionListener(new DriversListListener()); TODO
            selectionBoxes[i].setSize(typeSelector.getSize());
            if(i>0) selectionBoxes[i].setLocation((selectionBoxes[i-1].getLocation().x+selectionBoxes[i-1].getWidth()+((int)(25*ratio().getWidth()))), typeSelector.getLocation().y);
        }
        selectionBoxes[0].setName("Drivers");
        selectionBoxes[1].setName("Operators");
        selectionBoxes[2].setName("Coaches");

        /* SCORE FIELDS */
        JTextField[] scoreFields = {
                new JTextField(),
                new JTextField(),
                new JTextField(),
                new JTextField()
        };
        scoreFields[0].setSize((int) (80*ratio().getWidth()), (int) (30*ratio().getWidth()));
        scoreFields[0].setLocation(selectionBoxes[selectionBoxes.length-1].getLocation().x+selectionBoxes[selectionBoxes.length-1].getWidth()+((int) (25*ratio().getWidth())), (int)(100*ratio().getHeight()));
        for(int i = 0; i < scoreFields.length; i++){
            scoreFields[i].setColumns(6);
            //f.addFocusListener(); TODO
            //f.addActionListener(); TODO
            if(i>0) {
                scoreFields[i].setSize(scoreFields[0].getSize());
                scoreFields[i].setLocation(scoreFields[i - 1].getLocation().x + scoreFields[i - 1].getWidth() + ((int) (25 * ratio().getWidth())), scoreFields[0].getY());
            }
        }
        scoreFields[0].setName("Total");
        scoreFields[1].setName("Teleop");
        scoreFields[2].setName("Auton");
        scoreFields[3].setName("Penalties");

        ms.addComponents(spreadsheetButton, submitButton, typeSelector);
        ms.addComponents(selectionBoxes);
        ms.addComponents(scoreFields);

        frame.add(mainScreen, BorderLayout.CENTER);
    }
}