package TeamMember;

import Core.Main;
import Core.Settings;
import Core.Utilities;
import TeamMember.*;
import Match.Match;
import jdk.jshell.execution.Util;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
        this.matches = Utilities.getMatches(workbook, "Match Data");
        this.drivers = drivers;
        this.operators = operators;
        this.coaches = coaches;
        for(Match m : matches){
            m.assign(drivers, operators, coaches);
        }

        this.driveTeams = driveTeams;
        runCalculations();
    }
    public Team(XSSFWorkbook wb, Driver[] drivers, Operator[] operators, Coach[] coaches, String[][] dtNames){
        this.workbook = wb;
        this.matches = Utilities.getMatches(workbook, "Match Data");
        this.drivers = drivers;
        this.operators = operators;
        this.coaches = coaches;
        for(Match m : matches){
            m.assign(drivers, operators, coaches);
        }

        DriveTeam[] driveTeams = new DriveTeam[dtNames.length];
        for(int i = 0; i < driveTeams.length; i++){
            driveTeams[i] = new DriveTeam((Driver) Utilities.findByName(drivers,dtNames[i][0]), (Operator) Utilities.findByName(operators,dtNames[i][1]), coaches, matches);
        }
        this.driveTeams = driveTeams;
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
        writePerMemberData();
        writeDriveTeamData();
    }

    /**
     * Writes the data to the workbook
     */
    public void writePerMemberData(){
        Map<Integer, ArrayList<Double>> dataMap = new HashMap<Integer, ArrayList<Double>>();

        int row = 0;
        for(Driver d : drivers){
            dataMap.put(row++, d.getGroupedData());
        }
        for(Operator o : operators){
            dataMap.put(row++, o.getGroupedData());
        }
        for(Coach c : coaches){
            dataMap.put(row++, c.getGroupedData());
        }
        Utilities.writeDatamapToSheet(2, Utilities.getSheetFromWorkbook(workbook, "Per Member Data"), dataMap);
    }

    /**
     * Writes the drive teams' data to a sheet
     */
    public void writeDriveTeamData(){
        Map<Integer, ArrayList<Double>> dataMap = new HashMap<Integer, ArrayList<Double>>();

        int row = 0;
        for(DriveTeam dt : driveTeams){
            dataMap.put(row++, dt.getGroupedTheoreticalData());
        }
        row+=3;
        for(DriveTeam dt : driveTeams){
            dataMap.put(row++, dt.getGroupedExperimentalData());
        }
        Utilities.writeDatamapToSheet(3, Utilities.getSheetFromWorkbook(workbook, "Drive Team Data"), dataMap);
    }

    public void saveWorkbook(){
        Utilities.writeWorkbookToSpreadsheet("Red Team Data.xlsx", workbook);
    }

    public XSSFWorkbook getWorkbook(){
        return workbook;
    }

    public void saveAndLaunch(){
        saveWorkbook();
        Utilities.launchSpreadsheet(Settings.redTeamDataFile);
    }

    public void writeMatchEntry(char type, LocalDate date, String[] names, double[] scores){
        Utilities.writeEntry(workbook, "Match Data", type, date, names, scores);
        new Team(this.workbook, this.drivers, this.operators, this.coaches, this.driveTeams);
        //runCalculations();
    }
}
