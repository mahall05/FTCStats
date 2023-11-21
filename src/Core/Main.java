package Core;

import TeamMember.*;
import UIElements.Window;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    private Window window;
    private Team redTeam;

    private void runSetup(){
        XSSFWorkbook redTeamWorkbook = Utilities.getWorkbookFromFile(Settings.redTeamDataFile);
        window = new Window(/*this, redTeam*/);

        redTeam = new Team(redTeamWorkbook,
                            new Driver[] {new Driver("Bredan"), new Driver("Erin"), new Driver("Luca")},
                            new Operator[] {new Operator("Mason"), new Operator("Zoe"), new Operator("Cyrus")},
                            new Coach[] {new Coach("Caleb"), new Coach("Matt"), new Coach("Zach")},
                            new String[][] {{"Bredan","Mason"},{"Erin","Zoe"},{"Luca","Cyrus"}});
    }

    private void start() {
        runSetup();
        //run();
    }
    /*
    private void run(){
        while(true){
            window.update();
        }
    }

     */

    public void saveAndLaunch(){
        redTeam.saveWorkbook();
        Utilities.launchSpreadsheet(Settings.redTeamDataFile);
    }

    private Main(){
        start();
    }
    public static void main(String[] args) {
        new Main();
    }


}