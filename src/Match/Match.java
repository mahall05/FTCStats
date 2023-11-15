package Match;

import Core.Settings;
import TeamMember.Coach;
import TeamMember.Driver;
import TeamMember.Operator;

import java.time.LocalDate;

public class Match {
    private Type type;
    private LocalDate date;
    private String driverString;
    private Driver driver;

    private String operatorString;
    private Operator operator;

    private String driveCoachString;
    private Coach driveCoach;

    private double relativeWeight;

    // Total, Teleop, Auton, Penalties
    private int[] scores;

    private void calcWeight(){
        double weightFromOldRobot = date.isBefore(LocalDate.of(2023, 11, 14)) ? Settings.relativeOldRobotWeight : 1;
        double weightFromType = type == Type.PRACTICE ? Settings.relativePracticeWeight : 1;
        relativeWeight = 1.0 * weightFromType * weightFromOldRobot;
    }

    public Match(String type, LocalDate date, String driver, String operator, String driveCoach, int[] scores){
        this.type = (type.equalsIgnoreCase("p") ? Type.PRACTICE : Type.COMP);
        this.date = date;
        this.driverString = driver;
        this.operatorString = operator;
        this.driveCoachString = driveCoach;
        this.scores = scores;
        calcWeight();
    }

    public void assign(Driver[] ds, Operator[] os, Coach[] cs){
        for(Driver d : ds){
            if(d.getName().equals(driverString)){
                driver = d;
                d.addMatch(this);
            }
        }
        for(Operator o : os){
            if(o.getName().equals(operatorString)){
                operator = o;
                o.addMatch(this);
            }
        }
        for(Coach c : cs){
            if(c.getName().equals(driveCoachString)){
                driveCoach = c;
                c.addMatch(this);
            }
        }

    }

    public int[] getScores(){
        return scores;
    }
    public int getScore(int i){
        return scores[i];
    }

    public double getWeightedScore(int i){
        return scores[i]*relativeWeight;
    }
    public double getRelativeWeight(){
        return relativeWeight;
    }

    public LocalDate getDate(){
        return date;
    }
    public Driver getDriver(){
        return driver;
    }
    public Operator getOperator(){
        return operator;
    }
    public Coach getCoach(){
        return driveCoach;
    }

    private String dateString(){
        String ds = date.toString();
        return (date.getMonth()+" "+date.getDayOfMonth());
    }

    public enum Type{ PRACTICE, COMP}
}
