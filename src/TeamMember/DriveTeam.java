package TeamMember;

import Match.Match;

import java.util.ArrayList;

public class DriveTeam {
    private Driver driver;
    private Operator operator;
    private Coach coach;
    private ArrayList<Match> matchHistory;

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

    // TODO clearly weighted formula is wrong
    public void calcWeightedAverages(){
        weightedAvgTotal = calcWeightedAvgTotal();
        weightedAvgTeleop = calcWeightedAvgTeleop();
        weightedAvgAuton = calcWeightedAvgAuton();
        weightedAvgPenalties = calcWeightedAvgPenalties();
    }
    public double calcWeightedAvgTotal(){
        // Get raw data
        double driverTotal = driver.getRawSums()[0];
        double operatorTotal = operator.getRawSums()[0];
        double coachTotal = operator.getRawSums()[0];

        double driverValids = driver.getRawValids()[0];
        double operatorValids = operator.getRawValids()[0];
        double coachValids = coach.getRawValids()[0];

        // Subtract the overlapping matches
        for(Match m : matchHistory){
            driverValids--;
            operatorValids--;
            driverTotal -= m.getTotalScore();
            operatorTotal -= m.getTotalScore();

            if(m.getCoach().equals(coach)){
                coachValids--;
                coachTotal -= m.getTotalScore();
            }
        }

        // Calculate average from remaining
        double rdAvg = (driverValids==0?0:driverTotal/driverValids);
        double roAvg = (operatorValids==0?0:operatorTotal/operatorValids);
        double rcAvg = (coachValids==0?0:coachTotal/coachValids);
        double remainingAvg = (1*rdAvg + 1*roAvg + 0.5*rcAvg)/((driverValids==0?0:1)+(operatorValids==0?0:1)+(coachValids==0?0:0.5));

        double ncsum = 0;
        double csum = 0;
        double ncn = 0;
        double cn = 0;
        // Find the average from the overlap
        for(Match m : matchHistory){
            int s = m.getTotalScore();

            if(s>=0){
                if(m.getCoach().equals(coach)){
                    csum += s;
                    cn++;
                }else{
                    ncsum += s;
                    ncn++;
                }
            }
        }
        double ncAvg = ncsum/ncn;
        double cAvg = (cn==0?0:csum/cn);

        return ((1*remainingAvg + 2*ncAvg + 3*cAvg)/((remainingAvg==0?0:1) + (ncAvg==0?0:2) + (cAvg==0?0:3)));
    }
    public double calcWeightedAvgTeleop(){
        // Get raw data
        double driverTotal = driver.getRawSums()[1];
        double operatorTotal = operator.getRawSums()[1];
        double coachTotal = operator.getRawSums()[1];

        double driverValids = driver.getRawValids()[1];
        double operatorValids = operator.getRawValids()[1];
        double coachValids = coach.getRawValids()[1];

        // Subtract the overlapping matches
        for(Match m : matchHistory){
            driverValids--;
            operatorValids--;
            driverTotal -= m.getTeleopScore();
            operatorTotal -= m.getTeleopScore();

            if(m.getCoach().equals(coach)){
                coachValids--;
                coachTotal -= m.getTeleopScore();
            }
        }

        // Calculate average from remaining
        double rdAvg = (driverValids==0?0:driverTotal/driverValids);
        double roAvg = (operatorValids==0?0:operatorTotal/operatorValids);
        double rcAvg = (coachValids==0?0:coachTotal/coachValids);
        double remainingAvg = (1*rdAvg + 1*roAvg + 0.5*rcAvg)/((driverValids==0?0:1)+(operatorValids==0?0:1)+(coachValids==0?0:0.5));

        double ncsum = 0;
        double csum = 0;
        double ncn = 0;
        double cn = 0;
        // Find the average from the overlap
        for(Match m : matchHistory){
            int s = m.getTeleopScore();

            if(s>=0){
                if(m.getCoach().equals(coach)){
                    csum += s;
                    cn++;
                }else{
                    ncsum += s;
                    ncn++;
                }
            }
        }
        double ncAvg = ncsum/ncn;
        double cAvg = (cn==0?0:csum/cn);

        return ((1*remainingAvg + 2*ncAvg + 3*cAvg)/((remainingAvg==0?0:1) + (ncAvg==0?0:2) + (cAvg==0?0:3)));
    }
    public double calcWeightedAvgAuton(){
        // Get raw data
        double driverTotal = driver.getRawSums()[2];
        double operatorTotal = operator.getRawSums()[2];
        double coachTotal = operator.getRawSums()[2];

        double driverValids = driver.getRawValids()[2];
        double operatorValids = operator.getRawValids()[2];
        double coachValids = coach.getRawValids()[2];

        // Subtract the overlapping matches
        for(Match m : matchHistory){
            driverValids--;
            operatorValids--;
            driverTotal -= m.getAutonScore();
            operatorTotal -= m.getAutonScore();

            if(m.getCoach().equals(coach)){
                coachValids--;
                coachTotal -= m.getAutonScore();
            }
        }

        // Calculate average from remaining
        double rdAvg = (driverValids==0?0:driverTotal/driverValids);
        double roAvg = (operatorValids==0?0:operatorTotal/operatorValids);
        double rcAvg = (coachValids==0?0:coachTotal/coachValids);
        double remainingAvg = (1*rdAvg + 1*roAvg + 0.5*rcAvg)/((driverValids==0?0:1)+(operatorValids==0?0:1)+(coachValids==0?0:0.5));

        double ncsum = 0;
        double csum = 0;
        double ncn = 0;
        double cn = 0;
        // Find the average from the overlap
        for(Match m : matchHistory){
            int s = m.getAutonScore();

            if(s>=0){
                if(m.getCoach().equals(coach)){
                    csum += s;
                    cn++;
                }else{
                    ncsum += s;
                    ncn++;
                }
            }
        }
        double ncAvg = ncsum/ncn;
        double cAvg = (cn==0?0:csum/cn);

        return ((1*remainingAvg + 2*ncAvg + 3*cAvg)/((remainingAvg==0?0:1) + (ncAvg==0?0:2) + (cAvg==0?0:3)));
    }
    public double calcWeightedAvgPenalties(){
        // Get raw data
        double driverTotal = driver.getRawSums()[3];
        double operatorTotal = operator.getRawSums()[3];
        double coachTotal = operator.getRawSums()[3];

        double driverValids = driver.getRawValids()[3];
        double operatorValids = operator.getRawValids()[3];
        double coachValids = coach.getRawValids()[3];

        // Subtract the overlapping matches
        for(Match m : matchHistory){
            driverValids--;
            operatorValids--;
            driverTotal -= m.getPenalties();
            operatorTotal -= m.getPenalties();

            if(m.getCoach().equals(coach)){
                coachValids--;
                coachTotal -= m.getPenalties();
            }
        }

        // Calculate average from remaining
        double rdAvg = (driverValids==0?0:driverTotal/driverValids);
        double roAvg = (operatorValids==0?0:operatorTotal/operatorValids);
        double rcAvg = (coachValids==0?0:coachTotal/coachValids);
        double remainingAvg = (1*rdAvg + 1*roAvg + 0.5*rcAvg)/((driverValids==0?0:1)+(operatorValids==0?0:1)+(coachValids==0?0:0.5));

        double ncsum = 0;
        double csum = 0;
        double ncn = 0;
        double cn = 0;
        // Find the average from the overlap
        for(Match m : matchHistory){
            int s = m.getPenalties();

            if(s>=0){
                if(m.getCoach().equals(coach)){
                    csum += s;
                    cn++;
                }else{
                    ncsum += s;
                    ncn++;
                }
            }
        }
        double ncAvg = ncsum/ncn;
        double cAvg = (cn==0?0:csum/cn);

        return ((1*remainingAvg + 2*ncAvg + 3*cAvg)/((remainingAvg==0?0:1) + (ncAvg==0?0:2) + (cAvg==0?0:3)));
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

    public DriveTeam(Driver d, Operator o, Coach c, ArrayList<Match> matches){
        this(d,o,c);
        matchHistory = findCommonMatches(matches);
    }
    public DriveTeam(Driver d, Operator o, Coach c){
        this.driver = d;
        this.operator = o;
        this.coach = c;
    }
    public DriveTeam(Driver d, Operator o){
        this.driver = d;
        this.operator = o;
    }
    public DriveTeam(Driver d, Operator o, ArrayList<Match> matches){
        this(d, o, null, matches);
    }

    public String toStringWeighted(){
        return String.format("Driver: %-10s  Operator: %-10s  WeightedAvgTotal: %-6.2f  WeightedAvgTeleop: %-6.2f  WeightedAvgAuton: %-6.2f  WeightedAvgPenalties: %-6.2f",
                driver.getName(), operator.getName(), weightedAvgTotal, weightedAvgTeleop, weightedAvgAuton, weightedAvgPenalties);
    }
    public String toStringUnweighted(){
        return String.format("Driver: %-10s  Operator: %-10s  UnweightedAvgTotal: %-6.2f  UnweightedAvgTeleop: %-6.2f  UnweightedAvgAuton: %-6.2f  UnweightedAvgPenalties: %-6.2f",
                driver.getName(), operator.getName(), avgTotal, avgTeleop, avgAuton, avgPenalties);
    }
}
