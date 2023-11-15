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
    // Total, teleop, auton, penalties
    private double[] averages = new double[4];

    /* Averages Weighted by Team */
    // Total, teleop, auton, penalties
    private double[] weightedAverages = new double[4];

    private double[] duoSums = new double[4];
    private double[] nonZeroDuos = new double[4];

    /**
     * Get the Averages of Total, Teleop, Auton, Penalties, and weighted averages in that order
     * Used to write the information to the workbook
     * @return An ArrayList containing data associated with the drive team.
     */
    public ArrayList<Double> getGroupedData(){
        ArrayList<Double> a = new ArrayList<Double>();
        for(Double d : averages){
            a.add(d);
        }
        for(Double d : weightedAverages){
            a.add(d);
        }
        return a;
    }

    public void calcAll(){
        calcBasicAverages();

        if(matchHistory != null) calcWeightedAverages();
        else System.out.println("False");

        //System.out.println(toStringWeighted());
    }
    private void getDuoScores(AvgMatchGetter ag, int i){
        double scores = 0;
        double nonZeros = 0;

        for(Match m : matchHistory){
            double s = ag.get(m)[i];
            scores += (s>=0?s:0);
            nonZeros += (s>=0?1:0);
        }
        duoSums[i] = scores;
        nonZeroDuos[i] = nonZeros;
    }

    public void calcWeightedAverages(){
        for(int i = 0; i < weightedAverages.length; i++){
            weightedAverages[i] = calcWeightedAverage(TeamMember::getDividends, TeamMember::getDivisors, i);
        }
    }
    public double calcWeightedAverage(AvgGetter dividendGetter, AvgGetter divisorGetter, int i){
        double duoSum = 0;
        double validDuos = 0;
        double trioSum = 0;
        double validTrios = 0;

        for(Match m : matchHistory){
            double s = m.getScores()[i];

            if(s >= 0){
                if(m.getCoach().equals(this.coach)){
                    trioSum += s;
                    validTrios++;
                }
                else{
                    duoSum += s;
                    validDuos++;
                }
            }
        }

        double driverSolo = driver.getDividends()[i] - duoSum - trioSum;
        double driverSoloMatches = driver.getDivisors()[i] - validDuos - validTrios;

        double operatorSolo = operator.getDividends()[i] - duoSum - trioSum;
        double operatorSoloMatches = operator.getDivisors()[i] - validDuos - validTrios;

        double coachSolo = coach.getDividends()[i] - trioSum;
        double coachSoloMatches = coach.getDivisors()[i] - validTrios;

        /*
        System.out.println("Driver Solo: " + driverSolo + "  " + driverSoloMatches);
        System.out.println("Operator Solo: " + operatorSolo + "  " + operatorSoloMatches);
        System.out.println("Coach Solo: " + coachSolo + "  " + coachSoloMatches);
        System.out.println("Duo: " + duoSum + "  " + validDuos);
        System.out.println("Trio: " + trioSum + "  " + validTrios);
        System.out.println("\n");

         */

        double avgOfSolos = (driverSolo*1.0 + operatorSolo*1.0 + coachSolo*0.5) / (driverSoloMatches*1.0 + operatorSoloMatches*1.0 + coachSoloMatches*0.5);
        double avgOfDuos = (duoSum * 2.0) / (validDuos * 2.0);
        double avgOfTrios = (trioSum*3.0) / (validTrios*3.0);
        avgOfSolos = Double.isNaN(avgOfSolos) ? 0 : avgOfSolos;
        avgOfDuos = Double.isNaN(avgOfDuos) ? 0 : avgOfDuos;
        avgOfTrios = Double.isNaN(avgOfTrios) ? 0 : avgOfTrios;

        //System.out.println("Avg of Solos: " + avgOfSolos + "  Avg of Duos: " + avgOfDuos + "  Avg of Trios: " + avgOfTrios + "\n\n");

        double totalAverage = (avgOfSolos*1.0 + avgOfDuos*2.0 + avgOfTrios*3.0) / (6.0 - (avgOfSolos==0 ? 1 : 0) - (avgOfDuos==0 ? 2 : 0) - (avgOfTrios==0 ? 3 : 0));
        totalAverage = Double.isNaN(totalAverage) ? 0 : totalAverage;

        //System.out.println(duoSum + "  " + validDuos + "  " + trioSum + "  " + validTrios);
        return totalAverage;
    }

    /**
     * Calculate averages using averages from driver, operator, and drive coach, with the drive coach impact having a lower weight
     */
    public void calcBasicAverages(){
        for(int i = 0; i < averages.length; i++){
            averages[i] = calcAvg(TeamMember::getAverages, i);
        }
    }
    private double calcAvg(AvgGetter ag, int i){
        return (ag.get(driver)[i]+ag.get(operator)[i]+(coach==null?0:ag.get(coach)[i]*0.5))/(coach==null?2:2.5);
    }

    /*
    public void calcWeightedAverages(){
        weightedAvgTotal = calcWeightedAvgTotal();
        weightedAvgTeleop = calcWeightedAvgTeleop();
        weightedAvgAuton = calcWeightedAvgAuton();
        weightedAvgPenalties = calcWeightedAvgPenalties();
    }

    public double calcWeightedAvgTotal(){
        double duoSum = 0;
        double trioSum = 0;
        double duoMatches = 0;
        double trioMatches = 0;

        for(int i = 0; i < matchHistory.size(); i++){
            if(matchHistory.get(i).getCoach().equals(this.coach)){ // If the match has the correct coach, this is a trio
                trioSum += matchHistory.get(i).getTotalScore();
                trioMatches++;
            }else{   // Otherwise, this is just a duo
                duoSum += matchHistory.get(i).getTotalScore();
                duoMatches++;
            }
        }
        double duoAvg = duoSum/(duoMatches==0?1:duoMatches);
        double trioAvg = trioSum/(trioMatches==0?1:trioMatches);
        double[] soloAverages = {
                (driver.getRawSums()[0]-duoSum-trioSum)/((driver.getRawSums()[0]+1==duoSum+trioSum && driver.getRawValids()[0]==duoMatches+trioMatches?1:driver.getRawValids()[0]-duoMatches-trioMatches+1)),
                (operator.getRawSums()[0]-duoSum-trioSum)/((operator.getRawSums()[0]+1==duoSum+trioSum && operator.getRawValids()[0]==duoMatches+trioMatches?1:operator.getRawValids()[0]-duoMatches-trioMatches+1)),
                (coach.getRawSums()[0]-duoSum-trioSum)/((coach.getRawSums()[0]+1==duoSum+trioSum && coach.getRawValids()[0]==duoMatches+trioMatches?1:coach.getRawValids()[0]-duoMatches-trioMatches+1))
        };

        // Right now the coach is weighted just as heavy as the drivers
        double combinedSoloAverages = (soloAverages[0]+soloAverages[1]+soloAverages[2]) /
                                (3-(soloAverages[0]==0?1:0)-(soloAverages[1]==0?1:0)-(soloAverages[2]==0?1:0));

        // Duo Avg is the average for both the driver and operator
        // duoAvg = duoAvg * 2 / 2; two people with duoAvg, then divide by 2 for average

        // Right now the coach is just as heavy as the drivers
        // trioAvg = trioAvg * 3 / 3; three people with trioAvg, then divide by 3 for average

        int[] weights = {1, 2, 3};
        double overallAvg = (combinedSoloAverages*weights[0] + duoAvg*weights[1] + trioAvg*weights[2]) /
                (weights[0]*(combinedSoloAverages==0?0:1) + weights[1]*(duoAvg==0?0:1) + weights[2]*(trioAvg==0?0:1));

        return overallAvg;
    }

    public double calcWeightedAvgTeleop(){
        double duoSum = 0;
        double trioSum = 0;
        double duoMatches = 0;
        double trioMatches = 0;

        for(int i = 0; i < matchHistory.size(); i++){
            if(matchHistory.get(i).getCoach().equals(this.coach)){ // If the match has the correct coach, this is a trio
                trioSum += matchHistory.get(i).getTeleopScore();
                trioMatches++;
            }else{   // Otherwise, this is just a duo
                duoSum += matchHistory.get(i).getTeleopScore();
                duoMatches++;
            }
        }
        double duoAvg = duoSum/(duoMatches==0?1:duoMatches);
        double trioAvg = trioSum/(trioMatches==0?1:trioMatches);


        double[] soloAverages = {
                (driver.getRawSums()[1]-duoSum-trioSum)/((driver.getRawValids()[1]+1==duoMatches+trioMatches?1:driver.getRawValids()[1]-duoMatches-trioMatches+1)),
                (operator.getRawSums()[1]-duoSum-trioSum)/((operator.getRawValids()[1]+1==duoMatches+trioMatches?1:operator.getRawValids()[1]-duoMatches-trioMatches+1)),
                (coach.getRawSums()[1]-duoSum-trioSum)/((coach.getRawValids()[1]+1==duoMatches+trioMatches?1:coach.getRawValids()[1]-duoMatches-trioMatches+1))
        };

        // Right now the coach is weighted just as heavy as the drivers
        double combinedSoloAverages = (soloAverages[0]+soloAverages[1]+soloAverages[2]) /
                (3-(soloAverages[0]==0?1:0)-(soloAverages[1]==0?1:0)-(soloAverages[2]==0?1:0));

        // Duo Avg is the average for both the driver and operator
        // duoAvg = duoAvg * 2 / 2; two people with duoAvg, then divide by 2 for average

        // Right now the coach is just as heavy as the drivers
        // trioAvg = trioAvg * 3 / 3; three people with trioAvg, then divide by 3 for average

        int[] weights = {1, 2, 3};
        double overallAvg = (combinedSoloAverages*weights[0] + duoAvg*weights[1] + trioAvg*weights[2]) /
                (weights[0]*(combinedSoloAverages==0?0:1) + weights[1]*(duoAvg==0?0:1) + weights[2]*(trioAvg==0?0:1));

        return overallAvg;
    }

    public double calcWeightedAvgAuton(){
        double duoSum = 0;
        double trioSum = 0;
        double duoMatches = 0;
        double trioMatches = 0;

        for(int i = 0; i < matchHistory.size(); i++){
            if(matchHistory.get(i).getCoach().equals(this.coach)){ // If the match has the correct coach, this is a trio
                trioSum += matchHistory.get(i).getAutonScore();
                trioMatches++;
            }else{   // Otherwise, this is just a duo
                duoSum += matchHistory.get(i).getAutonScore();
                duoMatches++;
            }
        }
        double duoAvg = duoSum/(duoMatches==0?1:duoMatches);
        double trioAvg = trioSum/(trioMatches==0?1:trioMatches);
        double[] soloAverages = {
                (driver.getRawSums()[2]-duoSum-trioSum)/((driver.getRawSums()[2]+1==duoSum+trioSum && driver.getRawValids()[2]==duoMatches+trioMatches?1:driver.getRawValids()[2]-duoMatches-trioMatches+1)),
                (operator.getRawSums()[2]-duoSum-trioSum)/((operator.getRawSums()[2]+1==duoSum+trioSum && operator.getRawValids()[2]==duoMatches+trioMatches?1:operator.getRawValids()[2]-duoMatches-trioMatches+1)),
                (coach.getRawSums()[2]-duoSum-trioSum)/((coach.getRawSums()[2]+1==duoSum+trioSum && coach.getRawValids()[2]==duoMatches+trioMatches?1:coach.getRawValids()[2]-duoMatches-trioMatches+1))
        };

        // Right now the coach is weighted just as heavy as the drivers
        double combinedSoloAverages = (soloAverages[0]+soloAverages[1]+soloAverages[2]) /
                (3-(soloAverages[0]==0?1:0)-(soloAverages[1]==0?1:0)-(soloAverages[2]==0?1:0));

        // Duo Avg is the average for both the driver and operator
        // duoAvg = duoAvg * 2 / 2; two people with duoAvg, then divide by 2 for average

        // Right now the coach is just as heavy as the drivers
        // trioAvg = trioAvg * 3 / 3; three people with trioAvg, then divide by 3 for average

        int[] weights = {1, 2, 3};
        double overallAvg = (combinedSoloAverages*weights[0] + duoAvg*weights[1] + trioAvg*weights[2]) /
                (weights[0]*(combinedSoloAverages==0?0:1) + weights[1]*(duoAvg==0?0:1) + weights[2]*(trioAvg==0?0:1));

        return overallAvg;
    }

    public double calcWeightedAvgPenalties(){
        double duoSum = 0;
        double trioSum = 0;
        double duoMatches = 0;
        double trioMatches = 0;

        for(int i = 0; i < matchHistory.size(); i++){
            if(matchHistory.get(i).getCoach().equals(this.coach)){ // If the match has the correct coach, this is a trio
                trioSum += matchHistory.get(i).getPenalties();
                trioMatches++;
            }else{   // Otherwise, this is just a duo
                duoSum += matchHistory.get(i).getPenalties();
                duoMatches++;
            }
        }
        double duoAvg = duoSum/(duoMatches==0?1:duoMatches);
        double trioAvg = trioSum/(trioMatches==0?1:trioMatches);
        double[] soloAverages = {
                (driver.getRawSums()[3]-duoSum-trioSum)/((driver.getRawSums()[3]+1==duoSum+trioSum && driver.getRawValids()[3]==duoMatches+trioMatches?1:driver.getRawValids()[3]-duoMatches-trioMatches+1)),
                (operator.getRawSums()[3]-duoSum-trioSum)/((operator.getRawSums()[3]+1==duoSum+trioSum && operator.getRawValids()[3]==duoMatches+trioMatches?1:operator.getRawValids()[3]-duoMatches-trioMatches+1)),
                (coach.getRawSums()[3]-duoSum-trioSum)/((coach.getRawSums()[3]+1==duoSum+trioSum && coach.getRawValids()[3]==duoMatches+trioMatches?1:coach.getRawValids()[3]-duoMatches-trioMatches+1))
        };

        // Right now the coach is weighted just as heavy as the drivers
        double combinedSoloAverages = (soloAverages[0]+soloAverages[1]+soloAverages[2]) /
                (3-(soloAverages[0]==0?1:0)-(soloAverages[1]==0?1:0)-(soloAverages[2]==0?1:0));

        // Duo Avg is the average for both the driver and operator
        // duoAvg = duoAvg * 2 / 2; two people with duoAvg, then divide by 2 for average

        // Right now the coach is just as heavy as the drivers
        // trioAvg = trioAvg * 3 / 3; three people with trioAvg, then divide by 3 for average

        int[] weights = {1, 2, 3};
        double overallAvg = (combinedSoloAverages*weights[0] + duoAvg*weights[1] + trioAvg*weights[2]) /
                (weights[0]*(combinedSoloAverages==0?0:1) + weights[1]*(duoAvg==0?0:1) + weights[2]*(trioAvg==0?0:1));

        return overallAvg;
    }

 */


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

        for(int i = 0; i < averages.length; i++){
            averages[i] = -1;
            //weightedAverages[i] = -1; TODO
        }
    }
    public DriveTeam(Driver d, Operator o, Coach c, ArrayList<Match> matches){this(d.getName()+"+"+o.getName(),d,o,c,matches);}

    /*
    public String toStringWeighted(){
        return String.format("Driver: %-10s  Operator: %-10s  WeightedAvgTotal: %-6.2f  WeightedAvgTeleop: %-6.2f  WeightedAvgAuton: %-6.2f  WeightedAvgPenalties: %-6.2f",
                driver.getName(), operator.getName(), weightedAvgTotal, weightedAvgTeleop, weightedAvgAuton, weightedAvgPenalties);
    }
    public String toStringUnweighted(){
        return String.format("Driver: %-10s  Operator: %-10s  UnweightedAvgTotal: %-6.2f  UnweightedAvgTeleop: %-6.2f  UnweightedAvgAuton: %-6.2f  UnweightedAvgPenalties: %-6.2f",
                driver.getName(), operator.getName(), avgTotal, avgTeleop, avgAuton, avgPenalties);
    }

     */
    public String getName(){
        return name;
    }

    private interface AvgGetter{
        double[] get(TeamMember t);
    }
    private interface AvgMatchGetter{
        int[] get(Match m);
    }
}
