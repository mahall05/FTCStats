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

    private double[] averages = new double[4];

    private double[] weightedAverages = new double[4];

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

    public TeamMember(String name, Type type){
        this.type = type;
        this.name = name;

        matches = new ArrayList<Match>();
    }

    /*
    public String toString(){
        return String.format("%-10s AvgTotal: %-10.2f AvgTeleop: %-10.2f AvgAuton: %-10.2f AvgPenalties: %-10.2f\n           WeightedAvgTotal: %-10.2f WeightedAvgTeleop: %-10.2f WeightedAvgAuton: %-10.2f WeightedAvgPenalties: %-10.2f",name,avgTotal,avgTeleop,avgAuton,avgPenalties, weightedAvgTotal,weightedAvgTeleop,weightedAvgAuton,weightedAvgPenalties);
    }
    */


    public void calcAll(){
        for(int i = 0; i < averages.length; i++){
            averages[i] = calcAverage(0, Match::getScores, i);
            weightedAverages[i] = calcAverage(DATE_WEIGHT, Match::getScores, i);
        }
    }

    private double calcAverage(double weight, AvgGetter ag, int i){
        double sum = 0;
        double n = 0;

        for(Match m : matches){
            double s = ag.get(m)[i];
            long daysAgo = ChronoUnit.DAYS.between(m.getDate(), LocalDate.now());

            if(s>=0){
                sum += (s*(1-weight*daysAgo));
                n += (1-weight*daysAgo);
            }
        }
        /*
        if(weight==0){
            totalSum = sum;
            validTotals = n;
        }
         */
        return (n==0?0:sum/n);
    }

    public String getName(){
        return name;
    }
    public void addMatch(Match m){
        matches.add(m);
    }

    public ArrayList<Match> getMatches(){
        return matches;
    }

    public double[] getAverages(){
        return averages;
    }
    public double[] getWeightedAverages(){
        return weightedAverages;
    }
    public double[] getRawSums(){
        return new double[] {totalSum, teleopSum, autonSum, penaltySum};
    }
    public double[] getRawValids(){
        return new double[] {validTotals, validTeleops, validAutons, validPenalties};
    }

    public enum Type{DRIVER, OPERATOR, COACH}

    private interface NumGetter{
        int get(Match m);
    }
    private interface AvgGetter{
        int[] get(Match m);
    }
}
