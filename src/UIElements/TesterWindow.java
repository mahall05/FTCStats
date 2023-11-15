package UIElements;

import Core.Main;
import Core.Settings;
import Core.Utilities;
import TeamMember.Team;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
//import java.awt.event.ActionListener;


public class TesterWindow {
    JFrame frame = new JFrame("FTCStats");
    private static final int HEIGHT = 700, WIDTH = HEIGHT*16/9;
    private Team[] teams; // TODO change this
    private Main main;
    private JPanel activeScreen;

    /* MAIN SCREEN */
    private JPanel mainScreen;
    private ArrayList<JComponent> msComponents;
    private ArrayList<Dimension> msDimensions;
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
        for(int i = 0; i < selectionBoxes.length; i++){
            selectionBoxes[i].setSelectedIndex(0);
            //selectionBoxes[i].addActionListener(new DriversListListener()); TODO
            selectionBoxes[i].setSize(typeSelector.getSize());
            selectionBoxes[i].setLocation(i==0 ? (typeSelector.getLocation().x+typeSelector.getWidth()+((int)(25*windowRatio().getWidth()))) :
                            (selectionBoxes[i].getLocation().x+selectionBoxes[i-1].getWidth()+((int)(25*windowRatio().getWidth()))),
                    (int)(100*windowRatio().getHeight()));
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
        for(JComponent c : msComponents){
            msDimensions.add(c.getSize());
        }
        for(JComponent c : msComponents){
            mainScreen.add(c);
        }
        frame.add(mainScreen, BorderLayout.CENTER);

    }
    private void updateMainScreen(){
        for(int i = 0; i < msComponents.size(); i++){
            msComponents.get(i).setSize((int) (msDimensions.get(i).getWidth()*windowRatio().getWidth()), (int) (msDimensions.get(i).getHeight()*windowRatio().getHeight()));
        }
    }

    /* SETTINGS SCREEN */
    private JPanel settingsScreen;
    private ArrayList<JComponent> ssComponents;
    private ArrayList<Dimension> ssDimensions;
    private void settingsScreenSetup(){
        settingsScreen = new JPanel();
        settingsScreen.setLayout(null);

        JLabel dateWeightLabel = new JLabel(String.valueOf(Settings.dateWeight));
        dateWeightLabel.setLocation(200, 200);
        dateWeightLabel.setSize(20, 20);
        JTextField dateWeightInput = new JTextField();
        dateWeightInput.setLocation(200, 300);
        dateWeightLabel.setSize(30, 30);
    }

    public void update(){
        if(activeScreen.equals(mainScreen)){
            updateMainScreen();
        }else if(activeScreen.equals(settingsScreen)){

        }
    }

/*
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
*/

    public TesterWindow(Main main, Team... teams){
        this.main = main;
        this.teams = teams;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        mainScreenSetup();
        setActiveScreen(mainScreen);
        frame.setVisible(true);
    }

    public void setActiveScreen(JPanel panel){
        activeScreen = panel;
        //settingsScreen.setVisible(false);
        mainScreen.setVisible(false);

        panel.setVisible(true);
    }

    public void createEntry(){
        char type = TypeListener.getType();
        String[] names = DriversListListener.getDrivers();
        double[] scores = TextListener.getScores();

        //TODO change this
        XSSFWorkbook wb = teams[0].getWorkbook();
        Utilities.writeEntry(wb, "Match Data", type, names, scores);
    }

    public Dimension windowRatio(){
        return new Dimension(frame.getWidth()/WIDTH, frame.getHeight()/HEIGHT);
    }
}
