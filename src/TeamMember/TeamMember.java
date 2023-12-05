package TeamMember;

import Core.Settings;
import Match.Match;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Supplier;

public abstract class TeamMember {
    private Type type;
    private String name;
    private ArrayList<Match> matches;

    private double[] averages;

    //private double[] weightedAverages = new double[4];

    private double[] dividends = new double[9];
    private double[] divisors = new double[9];

    private double grade;

    public ArrayList<Double> getGroupedData(){
        //System.out.println(name);
        ArrayList<Double> a = new ArrayList<Double>();
        for(Double d : averages){
            //System.out.println("      " + d);
            a.add(d);
        }
        a.add(grade);
        /*
        for(Double d : weightedAverages){
            a.add(d);
        }

         */
        return a;
    }

    public TeamMember(String name, Type type){
        this.type = type;
        this.name = name;

        matches = new ArrayList<Match>();
    }

    public void calcAll(double[] teamAverages){
        averages = new double[teamAverages.length];

        // First set everything to -1 just in case
        for(int i = 0; i < averages.length; i++){
            averages[i] = -1;
            //weightedAverages[i] = -1;
        }
        for(int i = 0; i < averages.length; i++){
            averages[i] = calcAverage(0, i);
        }

        averages[averages.length-1] *= -1;

        double[] grades = new double[teamAverages.length];
        double sum = 0;
        System.out.println(name);
        for(int i = 0; i < teamAverages.length; i++){
            grades[i] = (double) averages[i] / (double) teamAverages[i] * 100;
            System.out.println("     "+grades[i]);
        }
        for(int i = 0; i < Settings.scoreWeights.length; i++){
            sum += Settings.scoreWeights[i];
            grade += grades[i]*Settings.scoreWeights[i];
        }
        grade /= sum;
    }

    private double calcAverage(double weight, int i){
        double sum = 0;
        double n = 0;

        for(Match m : matches){
            double s = m.getWeightedScore(i);
            long daysAgo = ChronoUnit.DAYS.between(m.getDate(), LocalDate.now());

            if(s>=0){
                double num = s*(1-Settings.dateWeight*daysAgo);
                double w = m.getRelativeWeight()*(1-Settings.dateWeight*daysAgo);
                if(num < 0 || w < 0){
                    num = 0;
                    w = 0;
                }

                sum += num;
                n += w;
            }
        }
        if(weight == 0) {
            divisors[i] = n;
            dividends[i] = sum;
        }

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
    /*
    public double[] getWeightedAverages(){
        return weightedAverages;
    }

     */

    public enum Type{DRIVER, OPERATOR, COACH}

    public double[] getDividends(){
        return dividends;
    }
    public double[] getDivisors(){
        return divisors;
    }

    public void eraseMatches(){
        matches = new ArrayList<Match>();
    }
}
