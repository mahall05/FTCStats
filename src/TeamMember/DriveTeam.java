package TeamMember;

import Core.Settings;
import Match.Match;

import java.util.ArrayList;

public class DriveTeam {
    private Driver driver;
    private Operator operator;
    private Coach[] coaches;
    private ArrayList<Match> matchHistory;
    private String name;

    /* THEORETICAL AVERAGES */
    private double[] tDuoAverages = new double[9];
    private double[] tDuoStdDevs = new double[9];
    private double[] tDuoSampleSizes = new double[9];
    private double[] getDuoUnweightedSampleSizes = new double[9];

    private double[][] tCoachIncludedAverages = new double[3][9];
    private double[][] tCoachIncludedStdDevs = new double[3][9];
    private double[][] tCoachIncludedSampleSizes = new double[3][9];
    private double[][] getCoachIncludedUnweightedSampleSizes = new double[3][9];

    /* EXPERIMENTAL AVERAGES */
    private double[] duoAverages = new double[9];
    private double[] duoStdDevs = new double[9];
    private double[] duoSampleSizes = new double[9];
    private double[] duoUnweightedSampleSizes = new double[9];

    private double[][] coachIncludedAverages = new double[3][9];
    private double[][] coachIncludedStdDevs = new double[3][9];
    private double[][] coachIncludedSampleSizes = new double[3][9];
    private double[][] coachIncludedUnweightedSampleSizes = new double[3][9];

    private double[] theoreticalGrades = new double[4];
    private double[] experimentalGrades = new double[4];

    public ArrayList<Double> getGroupedData(int i){
        ArrayList<Double> a = new ArrayList<Double>();

        for(int j = 0; j < tDuoAverages.length; j++){
            if(i == 0){
                a.add(tDuoAverages[j]);
            }else{
                a.add(tCoachIncludedAverages[i-1][j]);
            }
        }
        a.add(theoreticalGrades[i]);
        for(int j = 0; j < duoAverages.length; j++){
            if(i == 0){
                a.add(duoAverages[j]);
            }else{
                a.add(coachIncludedAverages[i-1][j]);
            }
        }
        a.add(experimentalGrades[i]);

        return a;
    }

    public void calcAll(){

        if(matchHistory != null) {
            for (int i = 0; i < duoAverages.length; i++) {
                //weightedAverages[i] = calcWeightedAverage(TeamMember::getDividends, TeamMember::getDivisors, i);
                calcDuoOnlyData(i);
                calcDuoOnlyTheoreticalData(i);
            }
            for(int i = 0; i < coachIncludedAverages[0].length; i++){
                calcPerCoachData(i);
                calcPerCoachTheoreticalData(i);
            }
        }
        else System.out.println("False");

        duoAverages[duoAverages.length-1] *= -1;
        for(int i = 0; i < coachIncludedAverages.length; i++){
            coachIncludedAverages[i][coachIncludedAverages[i].length-1] *= -1;
        }

        double sum = 0;
        for(int i = 0; i < Settings.scoreWeights.length; i++){
            sum += Settings.scoreWeights[i];
        }
        for(int i = 0; i < coaches.length+1; i++){
            for(int j = 0; j < duoAverages.length; j++){
                if(i == 0){
                    theoreticalGrades[i] += tDuoAverages[j]*Settings.scoreWeights[j];
                    experimentalGrades[i] += duoAverages[j]*Settings.scoreWeights[j];
                }else{
                    theoreticalGrades[i] += tCoachIncludedAverages[i-1][j];
                    experimentalGrades[i] += coachIncludedAverages[i-1][j];
                }
            }

            theoreticalGrades[i] /= sum;
            experimentalGrades[i] /= sum;
        }

        //System.out.println(toStringWeighted());
    }

    public void calcDuoOnlyData(int i){
        double scoreSum = 0;
        double valid = 0;
        double sampleSize = 0;

        for(Match m : matchHistory){
            double score = m.getWeightedScore(i);

            if (score>=0){
                scoreSum += score;
                valid += m.getRelativeWeight();
                sampleSize++;
            }
        }
        double meanScore = scoreSum/valid;

        ArrayList<Double> deviations = new ArrayList<Double>();
        ArrayList<Double> weights = new ArrayList<Double>();
        double validDeviations = 0;
        for(Match m : matchHistory){
            double score = m.getWeightedScore(i);

            if(score>=0){
                validDeviations += m.getRelativeWeight();
                deviations.add(Math.abs(meanScore-score));
                weights.add(m.getRelativeWeight());
            }
        }

        double squaredDeviationSum = 0;
        for(int j = 0; j < deviations.size(); j++){
            squaredDeviationSum += (weights.get(j) * deviations.get(j));
        }
        double variation = squaredDeviationSum / (validDeviations>30?validDeviations:validDeviations-1);
        double standardDeviation = Math.sqrt(variation);

        duoAverages[i] = meanScore;
        duoStdDevs[i] = standardDeviation;
        duoSampleSizes[i] = validDeviations;
        duoUnweightedSampleSizes[i] = sampleSize;
    }
    public void calcPerCoachData(int i){
        double[] coachSums = new double[3];
        double[] coachValids = new double[3];

        for(int j = 0; j < coaches.length; j++){
            for(Match m : matchHistory){
                if(m.getCoach().equals(coaches[j])){
                    double s = m.getWeightedScore(i);

                    if(s >= 0){
                        coachSums[j] += s;
                        coachValids[j] += m.getRelativeWeight();
                    }
                }
            }
        }

        for(int j = 0; j < coaches.length; j++){
            coachIncludedAverages[j][i] = coachSums[j]/coachValids[j];
        }
    }

    public void calcDuoOnlyTheoreticalData(int i){
        double sums = (driver.getAverages()[i]*1 + operator.getAverages()[i]*1);
        tDuoAverages[i] = sums/2;
    }
    public void calcPerCoachTheoreticalData(int i){
        for(int j = 0; j < tCoachIncludedAverages.length; j++){
            double sums = (driver.getAverages()[i]*1 + operator.getAverages()[i]*1 + coaches[j].getAverages()[i]*Settings.relativeCoachWeight);
            tCoachIncludedAverages[j][i] = sums/(2+Settings.relativeCoachWeight);
        }
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

    public DriveTeam(String name, Driver d, Operator o, Coach[] coaches, ArrayList<Match> matches){
        this(name, d,o, coaches);
        matchHistory = findCommonMatches(matches);
    }
    private DriveTeam(String name, Driver d, Operator o, Coach[] coaches){
        this.name = name;
        this.driver = d;
        this.operator = o;
        this.coaches = coaches;

        for(int i = 0; i < duoAverages.length; i++){
            duoAverages[i] = -1;
            duoStdDevs[i] = -1;
            duoSampleSizes[i] = -1;
            duoUnweightedSampleSizes[i] = -1;

            tDuoAverages[i] = -1;
            tDuoStdDevs[i] = -1;
            tDuoSampleSizes[i] = -1;
            getDuoUnweightedSampleSizes[i] = -1;

            for(int j = 0; j < coachIncludedAverages.length; j++){
                coachIncludedAverages[j][i] = -1;
                coachIncludedStdDevs[j][i] = -1;
                coachIncludedSampleSizes[j][i] = -1;
                coachIncludedUnweightedSampleSizes[j][i] = -1;

                tCoachIncludedAverages[j][i] = -1;
                tCoachIncludedStdDevs[j][i] = -1;
                tCoachIncludedSampleSizes[j][i] = -1;
                getCoachIncludedUnweightedSampleSizes[j][i] = -1;
            }
        }
    }
    public DriveTeam(Driver d, Operator o, Coach[] coaches, ArrayList<Match> matches){this(d.getName()+"+"+o.getName(),d,o,coaches,matches);}

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