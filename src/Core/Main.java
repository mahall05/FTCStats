package Core;

import Match.Match;
import TeamMember.*;
import UIElements.TesterWindow;
import UIElements.Window;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.ZoneId;
import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    private Window window;
    private TesterWindow testerWindow;
    private Team redTeam;

    private void runSetup(){
        XSSFWorkbook redTeamWorkbook = Utilities.getWorkbookFromFile(Settings.redTeamDataFile);
        //window = new Window(this, redTeam);
        testerWindow = new TesterWindow(this, redTeam);

        redTeam = new Team(redTeamWorkbook,
                            new Driver[] {new Driver("Bredan"), new Driver("Erin"), new Driver("Luca")},
                            new Operator[] {new Operator("Mason"), new Operator("Zoe"), new Operator("Cyrus")},
                            new Coach[] {new Coach("Caleb"), new Coach("Matt"), new Coach("Zach")},
                            new String[][] {{"Bredan","Mason","Caleb"},{"Erin","Zoe","Matt"},{"Luca","Cyrus","Zach"}});
    }

    private void start() {
        runSetup();
        run();
    }
    private void run(){
        while(true){
            //window.update();
            testerWindow.update();
        }
    }

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