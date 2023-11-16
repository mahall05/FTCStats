package UIElements;

import Core.Main;
import Core.Settings;
import Core.Utilities;
import TeamMember.Team;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
//import java.awt.event.ActionListener;


public class Window {
    JFrame frame = new JFrame("FTCStats");
    private static final int HEIGHT = 700, WIDTH = HEIGHT*16/9;
    private Team[] teams; // TODO change this
    private Main main;
    private JPanel activeScreen;

    /* MAIN SCREEN */
    private JPanel mainScreen;
    private ArrayList<JComponent> msComponents;
    private ArrayList<Dimension> msDimensions;
    private ArrayList<Point> msLocations;
    private void mainScreenSetup(){
        mainScreen = new JPanel();
        mainScreen.setLayout(null);

        /* SPREADSHEET BUTTON */
        JButton spreadsheetButton = new JButton("Launch Spreadsheet");
        //spreadsheetButton.addActionListener(new DriversListListener() {public void actionPerformed(ActionEvent e) {main.saveAndLaunch();}}); TODO
        spreadsheetButton.setSize(new Dimension((int) (200*windowRatio().getWidth()), (int) (50*windowRatio().getHeight())));
        spreadsheetButton.setLocation(frame.getSize().width/2 - spreadsheetButton.getWidth()/2, (int) (500*windowRatio().getHeight()));

        /* SUBMIT BUTTON */
        JButton submitButton = new JButton("Submit");
        //submitButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {createEntry();}}); TODO
        submitButton.setSize(new Dimension((int) (80*windowRatio().getWidth()), (int) (40*windowRatio().getHeight())));
        submitButton.setLocation(frame.getSize().width/2 - submitButton.getWidth()/2, (int) (200*windowRatio().getHeight()));

        /* TYPE SELECTOR */
        JComboBox typeSelector = new JComboBox(new String[] {"--select--", "Practice", "Competition"});
        typeSelector.setSelectedIndex(0);
        //typeSelector.addActionListener(new TypeListener()); TODO
        typeSelector.setSize((int) (100*windowRatio().getWidth()), (int) (30*windowRatio().getHeight()));
        typeSelector.setLocation((int) (125*windowRatio().getWidth()),(int) (100*windowRatio().getHeight()));
        typeSelector.setName("Type");

        /* DRIVER SELECTORS */
        JComboBox[] selectionBoxes = {
                new JComboBox(new String[] {"--select--","Bredan", "Erin", "Luca"}),
                new JComboBox(new String[] {"--select--", "Mason", "Zoe", "Cyrus"}),
                new JComboBox(new String[] {"--select--", "Caleb", "Matt", "Zach"})
        };

        selectionBoxes[0].setLocation(typeSelector.getLocation().x+typeSelector.getWidth()+((int)(25*windowRatio().getWidth())),
                typeSelector.getLocation().y);

        for(int i = 0; i < selectionBoxes.length; i++){
            selectionBoxes[i].setSelectedIndex(0);
            //selectionBoxes[i].addActionListener(new DriversListListener()); TODO
            selectionBoxes[i].setSize(typeSelector.getSize());
            if(i>0) selectionBoxes[i].setLocation((selectionBoxes[i-1].getLocation().x+selectionBoxes[i-1].getWidth()+((int)(25*windowRatio().getWidth()))), typeSelector.getLocation().y);
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
        scoreFields[0].setSize((int) (80*windowRatio().getWidth()), (int) (30*windowRatio().getWidth()));
        scoreFields[0].setLocation(selectionBoxes[selectionBoxes.length-1].getLocation().x+selectionBoxes[selectionBoxes.length-1].getWidth()+((int) (25*windowRatio().getWidth())), (int)(100*windowRatio().getHeight()));
        for(int i = 0; i < scoreFields.length; i++){
            scoreFields[i].setColumns(6);
            //f.addFocusListener(); TODO
            //f.addActionListener(); TODO
            if(i>0) {
                scoreFields[i].setSize(scoreFields[0].getSize());
                scoreFields[i].setLocation(scoreFields[i - 1].getLocation().x + scoreFields[i - 1].getWidth() + ((int) (25 * windowRatio().getWidth())), scoreFields[0].getY());
            }
        }
        scoreFields[0].setName("Total");
        scoreFields[1].setName("Teleop");
        scoreFields[2].setName("Auton");
        scoreFields[3].setName("Penalties");

        msComponents = new ArrayList<JComponent>();
        msComponents.add(spreadsheetButton);
        msComponents.add(submitButton);
        msComponents.add(typeSelector);
        Collections.addAll(msComponents, selectionBoxes);
        Collections.addAll(msComponents, scoreFields);

        msDimensions = new ArrayList<Dimension>();
        msLocations = new ArrayList<Point>();
        for(JComponent c : msComponents){
            msDimensions.add(c.getSize());
            msLocations.add(c.getLocation());
            mainScreen.add(c);
        }
        frame.add(mainScreen, BorderLayout.CENTER);
    }

    /* SETTINGS SCREEN */
    private JPanel settingsScreen;
    private ArrayList<JComponent> ssComponents;
    private ArrayList<Dimension> ssDimensions;
    private ArrayList<Point> ssLocations;
    private void settingsScreenSetup(){
        settingsScreen = new JPanel();
        settingsScreen.setLayout(null);
        ssComponents = new ArrayList<JComponent>();
        ssDimensions = new ArrayList<Dimension>();
        ssLocations = new ArrayList<Point>();

        JLabel dateWeightLabel = new JLabel(String.valueOf(Settings.dateWeight));
        dateWeightLabel.setLocation(200, 200);
        dateWeightLabel.setSize(100, 20);

        JTextField dateWeightInput = new JTextField();
        dateWeightInput.setLocation(frame.getWidth()/2, frame.getHeight()/2);
        dateWeightLabel.setSize(100, 100);
        dateWeightInput.setColumns(12);
        dateWeightInput.setVisible(true);

        ssComponents.add(dateWeightLabel);
        ssComponents.add(dateWeightInput);

        for(JComponent c : ssComponents){
            ssDimensions.add(c.getSize());
            ssLocations.add(c.getLocation());
            settingsScreen.add(c);
        }
        frame.add(settingsScreen, BorderLayout.CENTER);
    }

    public void update(){
        //if(activeScreen.equals(mainScreen)){
            updateComponents(msComponents, msDimensions, msLocations);
        //}else if(activeScreen.equals(settingsScreen)){
            //updateComponents(ssComponents, ssDimensions, ssLocations);
        //}
    }

    public Window(Main main, Team... teams){
        this.main = main;
        this.teams = teams;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        mainScreenSetup();
        settingsScreenSetup();
        setActiveScreen(mainScreen);
        frame.setVisible(true);
        mainScreen.setVisible(true);
        settingsScreen.setVisible(false);
    }


    public void setActiveScreen(JPanel panel){
        activeScreen = panel;
        //settingsScreen.setVisible(false);
        //mainScreen.setVisible(false);

        //panel.setVisible(true);
    }



    public void createEntry(){
        char type = TypeListener.getType();
        String[] names = DriversListListener.getDrivers();
        double[] scores = TextListener.getScores();

        //TODO change this
        XSSFWorkbook wb = teams[0].getWorkbook();
        Utilities.writeEntry(wb, "Match Data", type, names, scores);
    }

    public FloatingDimension windowRatio(){
        return new FloatingDimension((double) frame.getWidth()/WIDTH, (double) frame.getHeight()/HEIGHT);
    }

    private void updateComponents(ArrayList<JComponent> components, ArrayList<Dimension> dimensions, ArrayList<Point> locations){
        for(int i = 0; i < components.size(); i++){
            components.get(i).setSize((int) (dimensions.get(i).getWidth()*windowRatio().getWidth()), (int) (dimensions.get(i).getHeight()*windowRatio().getHeight()));
            components.get(i).setLocation((int) (locations.get(i).x*windowRatio().getWidth()), (int) (locations.get(i).y*windowRatio().getHeight()));
        }
    }
}
