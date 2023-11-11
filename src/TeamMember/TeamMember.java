package TeamMember;

import Match.Match;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Supplier;

public abstract class TeamMember {
    private final double DATE_WEIGHT = 0.0294117647;
    private Type type;
    private String name;
    private ArrayList<Match> matches;

    private double totalSum;
    private double teleopSum;
    private double autonSum;
    private double penaltySum;
    private double validTotals;
    private double validTeleops;
    private double validAutons;
    private double validPenalties;

    /* Basic Averages */
    private double avgTotal;
    private double avgTeleop;
    private double avgAuton;
    private double avgPenalties;

    /* Weighted Averages */
    private double weightedAvgTotal;
    private double weightedAvgTeleop;
    private double weightedAvgAuton;
    private double weightedAvgPenalties;

    public TeamMember(String name, Type type){
        this.type = type;
        this.name = name;

        matches = new ArrayList<Match>();
    }


    public String toString(){
        return String.format("%-10s AvgTotal: %-10.2f AvgTeleop: %-10.2f AvgAuton: %-10.2f AvgPenalties: %-10.2f\n           WeightedAvgTotal: %-10.2f WeightedAvgTeleop: %-10.2f WeightedAvgAuton: %-10.2f WeightedAvgPenalties: %-10.2f",name,avgTotal,avgTeleop,avgAuton,avgPenalties, weightedAvgTotal,weightedAvgTeleop,weightedAvgAuton,weightedAvgPenalties);
    }


    public void calcAll(){
        calcBasicAverages();
        calcDateWeightedAverages();
    }

    public void calcDateWeightedAverages(){
        weightedAvgTotal = calcAvgTotal(DATE_WEIGHT);
        weightedAvgTeleop = calcAvgTeleop(DATE_WEIGHT);
        weightedAvgAuton = calcAvgAuton(DATE_WEIGHT);
        weightedAvgPenalties = calcAvgPenalties(DATE_WEIGHT);
    }
    public void calcBasicAverages(){
        avgTotal = calcAvgTotal(0);
        avgTeleop = calcAvgTeleop(0);
        avgAuton = calcAvgAuton(0);
        avgPenalties = calcAvgPenalties(0);
    }
    private double calcAvgTotal(double weight){
        double sum = 0;
        double n = 0;

        for(Match m : matches){
            int s = m.getTotalScore();
            long daysAgo = ChronoUnit.DAYS.between(m.getDate(), LocalDate.now());

            if(s>=0){
                sum += (s*(1-weight*daysAgo));
                n += (1-weight*daysAgo);
            }
        }
        if(weight==0) {
            totalSum = sum;
            validTotals = n;
        }
        return (n==0?0:sum/n);
    }
    private double calcAvgTeleop(double weight){
        double sum = 0;
        double n = 0;

        for(Match m : matches){
            int s = m.getTeleopScore();
            long daysAgo = ChronoUnit.DAYS.between(m.getDate(), LocalDate.now());

            if(s>=0){
                sum += (s*(1-weight*daysAgo));
                n += (1-weight*daysAgo);
            }
        }

        if(weight==0) {
            teleopSum = sum;
            validTeleops = n;
        }
        return (n==0?0:sum/n);
    }
    private double calcAvgAuton(double weight){
        double sum = 0;
        double n = 0;

        for(Match m : matches){
            int s = m.getAutonScore();
            long daysAgo = ChronoUnit.DAYS.between(m.getDate(), LocalDate.now());

            if(s>=0){
                sum += (s*(1-weight*daysAgo));
                n += (1-weight*daysAgo);
            }
        }

        if(weight==0) {
            autonSum = sum;
            validAutons = n;
        }
        return (n==0?0:sum/n);
    }
    private double calcAvgPenalties(double weight){
        double sum = 0;
        double n = 0;

        for(Match m : matches){
            int s = m.getPenalties();
            long daysAgo = ChronoUnit.DAYS.between(m.getDate(), LocalDate.now());

            if(s>=0){
                sum += (s*(1-weight*daysAgo));
                n += (1-weight*daysAgo);
            }
        }

        if(weight==0) {
            penaltySum = sum;
            validPenalties = n;
        }
        return (n==0?0:sum/n);
    }

    public String getName(){
        return name;
    }
    public void addMatch(Match m){
        matches.add(m);
    }

    public double getAvgTotal(){
        return avgTotal;
    }
    public double getAvgTeleop(){
        return avgTeleop;
    }
    public double getAvgAuton(){
        return avgAuton;
    }
    public double getAvgPenalties(){
        return avgPenalties;
    }
    public double getWeightedAvgTotal(){
        return weightedAvgTotal;
    }
    public double getWeightedAvgTeleop(){
        return weightedAvgTeleop;
    }
    public double getWeightedAvgAuton(){
        return weightedAvgAuton;
    }
    public double getWeightedAvgPenalties(){
        return weightedAvgPenalties;
    }
    public ArrayList<Match> getMatches(){
        return matches;
    }

    public double[] getRawSums(){
        return new double[] {totalSum, teleopSum, autonSum, penaltySum};
    }
    public double[] getRawValids(){
        return new double[] {validTotals, validTeleops, validAutons, validPenalties};
    }

    public enum Type{DRIVER, OPERATOR, COACH}
}
