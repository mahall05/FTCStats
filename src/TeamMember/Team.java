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

    public Team(Driver[] drivers, Operator[] operators, Coach[] coaches){
        this.workbook = Utilities.getWorkbookFromFile(Settings.redTeamDataFile);
        loadMatches();
        this.drivers = drivers;
        this.operators = operators;
        this.coaches = coaches;
        assignMatches();

        DriveTeam[] driveTeams = new DriveTeam[drivers.length*operators.length];
        for(int i = 0; i < drivers.length*operators.length; i+=operators.length){
            for(int j = 0; j < operators.length; j++){
                driveTeams[i+j] = new DriveTeam(drivers[i/operators.length], operators[j], coaches, matches);
            }
        }

        this.driveTeams = driveTeams;
        runCalculations();
    }

    public void loadMatches(){
        matches = Utilities.getMatches(Utilities.getWorkbookFromFile(Settings.redTeamDataFile), "Match Data");
    }
    public void assignMatches(){
        for(Driver d : drivers){
            d.eraseMatches();
        }
        for(Operator o : operators){
            o.eraseMatches();
        }
        for(Coach c : coaches){
            c.eraseMatches();
        }
        for(Match m : matches){
            m.assign(drivers, operators, coaches);
        }
    }

    /**
     * Runs the calculations of the drive teams. Calculates all the individual data, and then the drive team data
     */
    public void runCalculations(){
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

        int row = 1;
        for(int i = 0; i < coaches.length+1; i++){
            for(DriveTeam dt : driveTeams){
                dataMap.put(row++, dt.getGroupedData(i));
            }
            row+=2;
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
        workbook = Utilities.getWorkbookFromFile(Settings.redTeamDataFile);
        loadCalcSave();
        Utilities.launchSpreadsheet(Settings.redTeamDataFile);
    }

    public void writeMatchEntry(char type, LocalDate date, String[] names, double[] scores){
        workbook = Utilities.getWorkbookFromFile(Settings.redTeamDataFile);
        Utilities.writeEntry(workbook, "Match Data", type, date, names, scores);
        //new Team(this.workbook, this.drivers, this.operators, this.coaches, this.driveTeams);
        loadCalcSave();
        //runCalculations();
    }

    public void loadCalcSave(){
        loadMatches();
        assignMatches();
        runCalculations();
        saveWorkbook();
    }
}
