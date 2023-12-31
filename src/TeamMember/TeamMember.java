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
    private double[] zscores;

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

    public void calcAll(double[] teamAverages, double[] teamStdDevs){
        averages = new double[teamAverages.length];
        zscores = new double[teamStdDevs.length];

        // First set everything to -1 just in case
        for(int i = 0; i < averages.length; i++){
            averages[i] = -1;
            //weightedAverages[i] = -1;
        }
        for(int i = 0; i < averages.length; i++){
            averages[i] = calcAverage(0, i);
        }
        averages[averages.length-1] *= -1;
        calcGrade(teamAverages, teamStdDevs);
    }

    private void calcGrade(double[] teamAverages, double[] teamStdDevs){
        grade = 0;
        double weightSum = 0;
        for(int i = 0; i < zscores.length; i++){
            zscores[i] = (averages[i]-teamAverages[i])/teamStdDevs[i];
            grade += zscores[i]*Settings.scoreWeights[i];
            weightSum += Settings.scoreWeights[i];
        }
        grade = grade / weightSum * 100;
    }

    private double calcAverage(double weight, int i){
        double sum = 0;
        double n = 0;

        for(Match m : matches){
            double s = m.getWeightedScore(i);

            if(s>=0){
                sum += s;
                n += m.getRelativeWeight();
            }
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

    public void eraseMatches(){
        matches = new ArrayList<Match>();
    }
}
