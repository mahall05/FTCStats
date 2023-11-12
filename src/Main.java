import Match.Match;
import TeamMember.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    Team redTeam;
    private static final String statsFileName = "Drive Teams.xlsx";

    private void runSetup(){
        ArrayList<Match> matches = getMatches();
        redTeam = new Team(new Driver[] {new Driver("Bredan"), new Driver("Erin"), new Driver("Luca")},
                            new Operator[] {new Operator("Mason"), new Operator("Zoe"), new Operator("Cyrus")},
                            new Coach[] {new Coach("Caleb"), new Coach("Matt"), new Coach("Zach")},
                            new String[][] {{"Bredan","Mason","Caleb"},{"Erin","Zoe","Matt"},{"Luca","Cyrus","Zach"}},
                            matches);
    }

    private void run() {
        //runSetup();
        Window window = new Window();
    }

    private ArrayList<Match> getMatches(){
        XSSFSheet sheet = getSheet("Match Data");
        ArrayList<Match> matches = new ArrayList<Match>();

        int maxRow = 1;
        while (sheet.getRow(maxRow+1)!=null && sheet.getRow(maxRow+1).getCell(0).getCellType() != CellType.BLANK) {
            maxRow++;
        }

        for(int i = 1; i <= maxRow; i++)
        {
            Row row = sheet.getRow(i);

            matches.add(new Match(
                    row.getCell(0)==null||row.getCell(0).getCellType()==CellType.BLANK?"b":row.getCell(0).getStringCellValue(),        // Type
                    (row.getCell(1)==null||row.getCell(1).getCellType()==CellType.BLANK?new Date():row.getCell(1).getDateCellValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),   // Date
                    row.getCell(2)==null||row.getCell(2).getCellType()==CellType.BLANK?"":row.getCell(2).getStringCellValue(),         // Driver
                    row.getCell(3)==null||row.getCell(3).getCellType()==CellType.BLANK?"":row.getCell(3).getStringCellValue(),         // Operator
                    row.getCell(4)==null||row.getCell(4).getCellType()==CellType.BLANK?"":row.getCell(4).getStringCellValue(),         // Drive Coach
                    row.getCell(5)==null||row.getCell(5).getCellType()==CellType.BLANK?-1:(int) row.getCell(5).getNumericCellValue(),  // Total Score
                    row.getCell(6)==null||row.getCell(6).getCellType()==CellType.BLANK?-1:(int) row.getCell(6).getNumericCellValue(),  // Teleop Score
                    row.getCell(7)==null||row.getCell(7).getCellType()==CellType.BLANK?-1:(int) row.getCell(7).getNumericCellValue(),  // Auton Score
                    row.getCell(8)==null||row.getCell(8).getCellType()==CellType.BLANK?-1:(int) row.getCell(8).getNumericCellValue()   // Penalties
            ));
            if(row.getCell(9)!=null && row.getCell(9).getStringCellValue().equals("**")){
                matches.removeLast();
            }
        }
        return matches;
    }
    private static XSSFSheet getSheet(String sheetName)
    {
        XSSFWorkbook wb = getWorkbook(statsFileName);
        XSSFSheet sheet = wb.getSheet(sheetName);
        if(sheet == null){
            sheet = wb.createSheet(sheetName);
        }
        return sheet;
    }

    private static XSSFWorkbook getWorkbook(String fileName)
    {
        try{
            // Get the file input
            FileInputStream file = new FileInputStream(fileName);
            // Create a blank workbook from that file
            XSSFWorkbook wb = new XSSFWorkbook(file);
            file.close();
            return wb;
        }catch (Exception e)
        {
            System.out.println("Error loading file");
            return null;
        }
    }
    private static void writeToWorkbook(XSSFWorkbook wb){
        try{
            File file = new File(statsFileName);
            FileOutputStream out = new FileOutputStream(file);
            wb.write(out);
            out.close();
            System.out.println("Successfully written");
        }catch(Exception e){
            System.out.println("FAILED TO SAVE: FILE OPENED IN ANOTHER PROGRAM");
        }
    }
    public static void writeEntry(char type, String[] names, double[] scores){
        XSSFSheet sheet = getSheet("Match Data");
        int i = 0;
        while(sheet.getRow(i)!=null) i++;
        Row row = sheet.createRow(i);
        row.createCell(0).setCellValue(String.valueOf(type));
        row.createCell(1);

        for(int j = 0; j < names.length; j++){
            row.createCell(j+2).setCellValue(names[j]);
        }
        for(int j = 0; j < scores.length; j++){
            System.out.println(scores[j]);
            row.createCell(j+2+names.length).setCellValue(scores[j]);
        }
        writeToWorkbook(sheet.getWorkbook());
    }

    public Main(){
        run();
    }
    public static void main(String[] args) {
        new Main();
    }

    public static void writeData(String sheetName, Map<Integer, ArrayList<Double>> sheetMap){
        XSSFSheet sheet = getSheet(sheetName);

        for(int i = 1; i < sheetMap.size()+1; i++){
            Row row = (sheet.getRow(i)==null?sheet.createRow(i):sheet.getRow(i));
            writeRow(row, sheetMap.get(i));
        }

        writeToWorkbook(sheet.getWorkbook());
    }

    private static void writeRow(Row row, ArrayList<Double> d) {
        for (int i = 0; i < d.size(); i++) {
            Cell cell = (row.getCell(i) == null ? row.createCell(i) : row.getCell(i));
            cell.setCellValue(d.get(i));
        }
    }

    public static void launchSpreadsheet(){
        Desktop d = Desktop.getDesktop();
        try{
            d.open(new File(statsFileName));
        }catch(Exception e){
            System.out.println("Error opening file");
        }
    }


}