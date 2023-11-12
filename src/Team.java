import TeamMember.*;
import Match.Match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Team {
    Driver[] drivers;
    Operator[] operators;
    Coach[] coaches;
    DriveTeam[] driveTeams;
    ArrayList<Match> matches = new ArrayList<Match>();

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

    public Team(Driver[] drivers, Operator[] operators, Coach[] coaches, DriveTeam[] driveTeams, ArrayList<Match> matches){
        this.drivers = drivers;
        this.operators = operators;
        this.coaches = coaches;
        this.driveTeams = driveTeams;
        this.matches = matches;
        for(Match m : matches){
            m.assign(drivers, operators, coaches);
        }
        runSetup();
    }
    public Team(Driver[] drivers, Operator[] operators, Coach[] coaches, String[][] dtNames, ArrayList<Match> matches){
        DriveTeam[] driveTeams = new DriveTeam[dtNames.length];
        for(Match m : matches){
            m.assign(drivers, operators, coaches);
        }
        this.drivers = drivers;
        this.operators = operators;
        this.coaches = coaches;
        this.driveTeams = driveTeams;
        this.matches = matches;
        for(int i = 0; i < dtNames.length; i++){
            driveTeams[i] = new DriveTeam(dtNames[i][0]+"+"+dtNames[i][1],(Driver) Util.findByName(drivers, dtNames[i][0]), (Operator) Util.findByName(operators, dtNames[i][1]), (Coach) Util.findByName(coaches, dtNames[i][2]),matches);
        }
        runSetup();
    }

    private void runSetup(){
        for(Match m : matches){
            m.assign(drivers, operators, coaches);
        }
        runCalculations();
    }
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
        writeData();
    }
    public void writeData(){
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
        Main.writeData("Per Member Data", dataMap);
    }
}
