package TeamMember;

import Core.Main;
import Core.Utilities;
import TeamMember.*;
import Match.Match;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Team {
    Driver[] drivers;
    Operator[] operators;
    Coach[] coaches;
    DriveTeam[] driveTeams;
    ArrayList<Match> matches = new ArrayList<Match>();
    XSSFWorkbook workbook; // The team's workbook

    /* Basic Averages */
    private double matchAvg;
    private double teleopAvg;
    private double autonAvg;
    private double penaltyAvg;

    /* Date Weighted Averages */
    private double weightedMatchAvg;
    private double weightedTeleopAvg;
    private double weightedAutonAvg;
    private double weightedPenaltyAvg;

    public Team(XSSFWorkbook wb, Driver[] drivers, Operator[] operators, Coach[] coaches, DriveTeam[] driveTeams){
        this.workbook = wb;
        this.drivers = drivers;
        this.operators = operators;
        this.coaches = coaches;
        this.driveTeams = driveTeams;
        this.matches = Utilities.getMatches(workbook, "Match Data");
        runSetup();
    }
    public Team(XSSFWorkbook wb, Driver[] drivers, Operator[] operators, Coach[] coaches, String[][] dtNames){
        this.workbook = wb;
        DriveTeam[] driveTeams = new DriveTeam[dtNames.length];
        for(Match m : matches){
            m.assign(drivers, operators, coaches);
        }
        this.drivers = drivers;
        this.operators = operators;
        this.coaches = coaches;
        this.driveTeams = driveTeams;
        this.matches = Utilities.getMatches(workbook, "Match Data");
        runSetup();
    }

    /**
     * Begins the setup of the team's data. This is automatically called from the constructor
     * Assigns the matches to their associated team members, and runs the calculations
     */
    private void runSetup(){
        for(Match m : matches){
            m.assign(drivers, operators, coaches);
        }
        runCalculations();
    }

    /**
     * Runs the calculations of the drive teams. Calculates all the individual data, and then the drive team data
     */
    private void runCalculations(){
        for(Driver d : drivers){
            d.calcAll();
        }
        for(Operator o : operators){
            o.calcAll();
        }
        for(Coach c : coaches){
            c.calcAll();
        }
        for(DriveTeam dt : driveTeams){
            dt.calcAll();
        }
        writeDataToSheet();
    }

    /**
     * Writes the data to the workbook
     */
    public void writeDataToSheet(){
        Map<Integer, ArrayList<Double>> dataMap = new HashMap<Integer, ArrayList<Double>>();

        int row = 1;
        for(Driver d : drivers){
            dataMap.put(row++, d.getGroupedData());
        }
        for(Operator o : operators){
            dataMap.put(row++, o.getGroupedData());
        }
        for(Coach c : coaches){
            dataMap.put(row++, c.getGroupedData());
        }
        Utilities.writeDatamapToSheet(Utilities.getSheetFromWorkbook(workbook, "Per Member Data"), dataMap);
    }

    public XSSFWorkbook getWorkbook(){
        return workbook;
    }
}
