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


public class UIHandler {
    Window window = new Window();
    private Team[] teams; // TODO change this
    private Main main;
    private JPanel activeScreen;

    /* MAIN SCREEN */
    private JPanel mainScreen;
    private ArrayList<JComponent> msComponents;
    private ArrayList<Dimension> msDimensions;
    private ArrayList<Point> msLocations;


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
