package TeamMember;

import Core.Settings;
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

    //private double[] duoSums = new double[4];
    //private double[] nonZeroDuos = new double[4];

    private double[] duoAverages = new double[4];
    private double[] duoStdDevs = new double[4];
    private double[] duoSampleSizes = new double[4];

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
        for(int i = 0; i < duoAverages.length; i++){
            a.add(duoAverages[i]);
            a.add(duoStdDevs[i]);
            a.add(duoSampleSizes[i]);
        }
        return a;
    }

    public void calcAll(){
        for(int i = 0; i < averages.length; i++){
            averages[i] = calcAvg(TeamMember::getAverages, i);
        }

        if(matchHistory != null)
            for(int i = 0; i < weightedAverages.length; i++) {
                weightedAverages[i] = calcWeightedAverage(TeamMember::getDividends, TeamMember::getDivisors, i);
                calcDuoOnlyData(i);
            }
        else System.out.println("False");

        //System.out.println(toStringWeighted());
    }

    private double calcAvg(AvgGetter ag, int i){
        return (ag.get(driver)[i]+ag.get(operator)[i]+(coach==null?0:ag.get(coach)[i]*Settings.relativeCoachWeight))/(coach==null?2:(2+ Settings.relativeCoachWeight));
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

        double avgOfSolos = (driverSolo*Settings.relativeDriverWeight + operatorSolo*Settings.relativeOperatorWeight + coachSolo*Settings.relativeCoachWeight)
                / (driverSoloMatches*Settings.relativeDriverWeight + operatorSoloMatches*Settings.relativeOperatorWeight + coachSoloMatches*Settings.relativeCoachWeight);
        double avgOfDuos = (duoSum * 2.0) / (validDuos * 2.0);
        double avgOfTrios = (trioSum*3.0) / (validTrios*3.0);

        avgOfSolos = Double.isNaN(avgOfSolos) ? 0 : avgOfSolos;
        avgOfDuos = Double.isNaN(avgOfDuos) ? 0 : avgOfDuos;
        avgOfTrios = Double.isNaN(avgOfTrios) ? 0 : avgOfTrios;

        double totalAverage = (avgOfSolos*Settings.relativeSoloWeight + avgOfDuos*Settings.relativeDuoWeight + avgOfTrios*Settings.relativeTrioWeight) /
                (Settings.relativeSoloWeight+Settings.relativeDuoWeight+Settings.relativeTrioWeight - (avgOfSolos==0 ? Settings.relativeSoloWeight : 0) - (avgOfDuos==0 ? Settings.relativeDuoWeight : 0) - (avgOfTrios==0 ? Settings.relativeTrioWeight : 0));
        totalAverage = Double.isNaN(totalAverage) ? 0 : totalAverage;

        return totalAverage;
    }

    public void calcDuoOnlyData(int i){
        double scoreSum = 0;
        double valid = 0;

        for(Match m : matchHistory){
            double score = m.getScore(i);

            if (score>=0){
                scoreSum += score;
                valid++;
            }
        }
        double meanScore = scoreSum/valid;

        ArrayList<Double> deviations = new ArrayList<Double>();
        double validDeviations = 0;
        for(Match m : matchHistory){
            double score = m.getScore(i);

            if(score>=0){
                validDeviations++;
                deviations.add(Math.abs(meanScore-score));
            }
        }

        double squaredDeviationSum = 0;
        for(Double d : deviations){
            squaredDeviationSum += (d*d);
        }
        double variation = squaredDeviationSum / (validDeviations>30?validDeviations:validDeviations-1);
        double standardDeviation = Math.sqrt(variation);

        duoAverages[i] = meanScore;
        duoStdDevs[i] = standardDeviation;
        duoSampleSizes[i] = validDeviations;
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

        for(int i = 0; i < averages.length; i++){
            averages[i] = -1;
            //weightedAverages[i] = -1; TODO
        }
    }
    public DriveTeam(Driver d, Operator o, Coach c, ArrayList<Match> matches){this(d.getName()+"+"+o.getName(),d,o,c,matches);}

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