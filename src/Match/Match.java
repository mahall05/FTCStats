package Match;

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

    private int totalScore;
    private int teleopScore;
    private int autonScore;
    private int penalties;

    public Match(String type, LocalDate date, String driver, String operator, String driveCoach, int totalScore, int penalties){
        this(type, date, driver, operator, driveCoach, totalScore, -1, -1, penalties);
    }
    public Match(String type, LocalDate date, String driver, String operator, String driveCoach, int totalScore, int teleopScore, int autonScore, int penalties){
        this.type = (type.equalsIgnoreCase("p") ? Type.PRACTICE : Type.COMP);
        this.date = date;
        this.driverString = driver;
        this.operatorString = operator;
        this.driveCoachString = driveCoach;
        this.teleopScore = teleopScore;
        this.autonScore = autonScore;
        this.totalScore = totalScore;
        this.penalties = penalties;
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

    public int getTotalScore(){
        return totalScore;
    }
    public int getTeleopScore(){
        return teleopScore;
    }
    public int getAutonScore(){
        return autonScore;
    }
    public int getPenalties(){
        return penalties;
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

    public String toString(){
        return String.format(type.toString()+" "+dateString()+" "+driver.getName()+" "+operator.getName()+" "+driveCoach.getName()+" "+totalScore+" "+teleopScore+" "+autonScore+" "+penalties);
    }

    private String dateString(){
        String ds = date.toString();
        return (date.getMonth()+" "+date.getDayOfMonth());
    }

    public enum Type{ PRACTICE, COMP}
}
