package TeamMember;

import Match.Match;

import java.util.ArrayList;

public class DriveTeam {
    private Driver driver;
    private Operator operator;
    private Coach coach;
    private ArrayList<Match> matchHistory;
    private String name;

    /* Basic Averages */
    private double avgTotal;
    private double avgTeleop;
    private double avgAuton;
    private double avgPenalties;

    /* Averages Weighted by Team */
    private double weightedAvgTotal;
    private double weightedAvgTeleop;
    private double weightedAvgAuton;
    private double weightedAvgPenalties;

    public void calcAll(){
        calcBasicAverages();
        if(matchHistory != null) calcWeightedAverages();
    }

    /**
     * Calculate averages using averages from driver, operator, and drive coach, with the drive coach impact having a lower weight
     */
    public void calcBasicAverages(){
        avgTotal = calcAvgTotal();
        avgTeleop = calcAvgTeleop();
        avgAuton = calcAvgAuton();
        avgPenalties = calcAvgPenalties();
    }
    public double calcAvgTotal(){
        return (driver.getAvgTotal()+operator.getAvgTotal()+(coach==null?0:coach.getAvgTotal()*0.5))/(coach==null?2:2.5);
    }
    public double calcAvgTeleop(){
        return (driver.getAvgTeleop()+operator.getAvgTeleop()+(coach==null?0:coach.getAvgTeleop()*0.5))/(coach==null?2:2.5);
    }
    public double calcAvgAuton(){
        return (driver.getAvgAuton()+operator.getAvgAuton()+(coach==null?0:coach.getAvgAuton()*0.5))/(coach==null?2:2.5);
    }
    public double calcAvgPenalties(){
        return (driver.getAvgPenalties()+operator.getAvgPenalties()+(coach==null?0:coach.getAvgPenalties()*0.5))/(coach==null?2:2.5);
    }

    public void calcWeightedAverages(){
        weightedAvgTotal = calcWeightedAvgTotal();
        weightedAvgTeleop = calcWeightedAvgTeleop();
        weightedAvgAuton = calcWeightedAvgAuton();
        weightedAvgPenalties = calcWeightedAvgPenalties();
    }
    public double calcWeightedAvgTotal(){
        // Get raw data
        double[] totalSums = {driver.getRawSums()[0], operator.getRawSums()[0], coach.getRawSums()[0]};
        double[] valids = {driver.getRawValids()[0], operator.getRawValids()[0], coach.getRawValids()[0]};

        double[][] averages = new double[3][3];
        /*SoloAvg   Driver   Operator  Coach
        * DuoAvg    Driver   Operator  Coach
        * TrioAvg   Driver   Operator  Coach */

        // Subtract the overlapping matches
        for(Match m : matchHistory){
            valids[0]--;
            valids[1]--;
            totalSums[0] -= m.getTotalScore();
            totalSums[1] -= m.getTotalScore();

            if(m.getCoach().equals(coach)){
                valids[2]--;
                totalSums[2] -= m.getTotalScore();
            }
        }
        averages[0][0] = (valids[0]==0?0:totalSums[0]/valids[0]);
        averages[0][1] = (valids[1]==0?0:totalSums[1]/valids[1]);
        averages[0][2] = (valids[2]==0?0:totalSums[2]/valids[2]);
        //System.out.println("Erin Solo: "+averages[0][0]+"  Zoe Solo: "+averages[1][0]+"  Matt Solo"+averages[2][0]);

        double duoSum = 0;
        double trioSum = 0;
        double trioMatches = 0;

        for(Match m : matchHistory){
            if(!(m.getCoach()==this.coach)) {
                duoSum += m.getTotalScore();
            }else{
                trioSum += m.getTotalScore();
                trioMatches++;
            }
        }

        return avg(averages, duoSum, trioSum, trioMatches);
    }
    public double calcWeightedAvgTeleop(){
        // Get raw data
        double[] totalSums = {driver.getRawSums()[1], operator.getRawSums()[1], coach.getRawSums()[1]};
        double[] valids = {driver.getRawValids()[1], operator.getRawValids()[1], coach.getRawValids()[1]};

        double[][] averages = new double[3][3];
        /*SoloAvg   Driver   Operator  Coach
         * DuoAvg    Driver   Operator  Coach
         * TrioAvg   Driver   Operator  Coach */

        // Subtract the overlapping matches
        for(Match m : matchHistory){
            valids[0]--;
            valids[1]--;
            totalSums[0] -= m.getTeleopScore();
            totalSums[1] -= m.getTeleopScore();

            if(m.getCoach().equals(coach)){
                valids[2]--;
                totalSums[2] -= m.getTeleopScore();
            }
        }
        averages[0][0] = (valids[0]==0?0:totalSums[0]/valids[0]);
        averages[0][1] = (valids[1]==0?0:totalSums[1]/valids[1]);
        averages[0][2] = (valids[2]==0?0:totalSums[2]/valids[2]);
        //System.out.println("Erin Solo: "+averages[0][0]+"  Zoe Solo: "+averages[1][0]+"  Matt Solo"+averages[2][0]);

        double duoSum = 0;
        double trioSum = 0;
        double trioMatches = 0;

        for(Match m : matchHistory){
            if(!(m.getCoach()==this.coach)) {
                duoSum += m.getTeleopScore();
            }else{
                trioSum += m.getTeleopScore();
                trioMatches++;
            }
        }

        return avg(averages, duoSum, trioSum, trioMatches);
    }
    public double calcWeightedAvgAuton(){
        // Get raw data
        double[] totalSums = {driver.getRawSums()[2], operator.getRawSums()[2], coach.getRawSums()[2]};
        double[] valids = {driver.getRawValids()[2], operator.getRawValids()[2], coach.getRawValids()[2]};

        double[][] averages = new double[3][3];
        /*SoloAvg   Driver   Operator  Coach
         * DuoAvg    Driver   Operator  Coach
         * TrioAvg   Driver   Operator  Coach */

        // Subtract the overlapping matches
        for(Match m : matchHistory){
            valids[0]--;
            valids[1]--;
            totalSums[0] -= m.getAutonScore();
            totalSums[1] -= m.getAutonScore();

            if(m.getCoach().equals(coach)){
                valids[2]--;
                totalSums[2] -= m.getAutonScore();
            }
        }
        averages[0][0] = (valids[0]==0?0:totalSums[0]/valids[0]);
        averages[0][1] = (valids[1]==0?0:totalSums[1]/valids[1]);
        averages[0][2] = (valids[2]==0?0:totalSums[2]/valids[2]);
        //System.out.println("Erin Solo: "+averages[0][0]+"  Zoe Solo: "+averages[1][0]+"  Matt Solo"+averages[2][0]);

        double duoSum = 0;
        double trioSum = 0;
        double trioMatches = 0;

        for(Match m : matchHistory){
            if(!(m.getCoach()==this.coach)) {
                duoSum += m.getAutonScore();
            }else{
                trioSum += m.getAutonScore();
                trioMatches++;
            }
        }

        return avg(averages, duoSum, trioSum, trioMatches);
    }
    public double calcWeightedAvgPenalties(){
        // Get raw data
        double[] totalSums = {driver.getRawSums()[3], operator.getRawSums()[3], coach.getRawSums()[3]};
        double[] valids = {driver.getRawValids()[3], operator.getRawValids()[3], coach.getRawValids()[3]};

        double[][] averages = new double[3][3];
        /*SoloAvg   Driver   Operator  Coach
         * DuoAvg    Driver   Operator  Coach
         * TrioAvg   Driver   Operator  Coach */

        // Subtract the overlapping matches
        for(Match m : matchHistory){
            valids[0]--;
            valids[1]--;
            totalSums[0] -= m.getPenalties();
            totalSums[1] -= m.getPenalties();

            if(m.getCoach().equals(coach)){
                valids[2]--;
                totalSums[2] -= m.getPenalties();
            }
        }
        averages[0][0] = (valids[0]==0?0:totalSums[0]/valids[0]);
        averages[0][1] = (valids[1]==0?0:totalSums[1]/valids[1]);
        averages[0][2] = (valids[2]==0?0:totalSums[2]/valids[2]);
        //System.out.println("Erin Solo: "+averages[0][0]+"  Zoe Solo: "+averages[1][0]+"  Matt Solo"+averages[2][0]);

        double duoSum = 0;
        double trioSum = 0;
        double trioMatches = 0;

        for(Match m : matchHistory){
            if(!(m.getCoach()==this.coach)) {
                duoSum += m.getPenalties();
            }else{
                trioSum += m.getPenalties();
                trioMatches++;
            }
        }

        return avg(averages, duoSum, trioSum, trioMatches);
    }
    public double avg(double[][] a, double duoSum, double trioSum, double trioMatches){
        a[1][0] = duoSum/(matchHistory.size()-trioMatches);
        a[1][1] = duoSum/(matchHistory.size()-trioMatches);
        //a[1][2] = duoSum/(matchHistory.size()-trioMatches);
        a[2][0] = trioSum/trioMatches;
        a[2][1] = trioSum/trioMatches;
        a[2][2] = trioSum/trioMatches;
        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < a[i].length; j++){
                if(a[i][j]<0) a[i][j]=0;
                //System.out.print(a[i][j]+"   ");
            }
            //System.out.println();
        }

        double da = (a[0][0]*1+a[1][0]*2+a[2][0]*3)/(6-(a[0][0]==0?1:0)-(a[1][0]==0?2:0)-(a[2][0]==0?3:0));
        double oa = (a[0][1]*1+a[1][1]*2+a[2][1]*3)/(6-(a[0][1]==0?1:0)-(a[1][1]==0?2:0)-(a[2][1]==0?3:0));
        double ca = (a[0][2]*1+a[1][2]*2+a[2][2]*3)/(6-(a[0][2]==0?1:0)-(a[1][2]==0?2:0)-(a[2][2]==0?3:0));
        da = Double.isNaN(da) ? 0 : da;
        oa = Double.isNaN(oa) ? 0 : oa;
        ca = Double.isNaN(ca) ? 0 : ca;
        return (da*1+oa*1+ca*0.5)/2.5;
    }

    private ArrayList<Match> findCommonMatches(ArrayList<Match> ms){
        ArrayList<Match> commons = new ArrayList<Match>();
        for(Match m : ms){
            if(m.getDriver().equals(driver) && m.getOperator().equals(operator)){
                commons.add(m);
            }
        }

        return commons;
    }

    public DriveTeam(String name, Driver d, Operator o, Coach c, ArrayList<Match> matches){
        this(name, d,o,c);
        matchHistory = findCommonMatches(matches);
    }
    public DriveTeam(String name, Driver d, Operator o, Coach c){
        this.name = name;
        this.driver = d;
        this.operator = o;
        this.coach = c;
    }
    public DriveTeam(String name, Driver d, Operator o){
        this.name = name;
        this.driver = d;
        this.operator = o;
    }
    public DriveTeam(String name, Driver d, Operator o, ArrayList<Match> matches){
        this(name, d, o, null, matches);
    }
    public DriveTeam(Driver d, Operator o, Coach c, ArrayList<Match> matches){this(d.getName()+"+"+o.getName(),d,o,c,matches);}

    public String toStringWeighted(){
        return String.format("Driver: %-10s  Operator: %-10s  WeightedAvgTotal: %-6.2f  WeightedAvgTeleop: %-6.2f  WeightedAvgAuton: %-6.2f  WeightedAvgPenalties: %-6.2f",
                driver.getName(), operator.getName(), weightedAvgTotal, weightedAvgTeleop, weightedAvgAuton, weightedAvgPenalties);
    }
    public String toStringUnweighted(){
        return String.format("Driver: %-10s  Operator: %-10s  UnweightedAvgTotal: %-6.2f  UnweightedAvgTeleop: %-6.2f  UnweightedAvgAuton: %-6.2f  UnweightedAvgPenalties: %-6.2f",
                driver.getName(), operator.getName(), avgTotal, avgTeleop, avgAuton, avgPenalties);
    }
    public String getName(){
        return name;
    }
}
