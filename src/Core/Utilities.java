package Core;

import Match.Match;
import TeamMember.TeamMember;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Utilities {
    /* PROGRAM-SPECIFIC METHODS */
    /* Methods used specifically by FTCStats to perform operations */

    /**
     * Find a TeamMember in a list of members that has a name matching the passed name
     * @param members The array of members to search through - ASSUMED that array contains NO MORE THAN ONE instance of desired name
     * @param name The name of the TeamMember to find
     * @return The team member with name 'name' or @null if a TeamMember cannot be located with that name
     */
    public static TeamMember findByName(TeamMember[] members, String name)
    {
        for(TeamMember m : members){
            if(m.getName().equalsIgnoreCase(name)){
                return m;
            }
        }
        return null;
    }

    /**
     * A method used for entering an entry of match data. Needs the match type, driver names, and scores associated
     * @param wb The workbook to get the data from
     * @param sheetName The name of the sheet to write the entry to
     * @param type Match type. Needs to be either 'p' or 'c' for "practice" or "competition" respectively
     * @param names The names of the drivers of the match. This method technically has no limit to array sizes, but this is intended to be 3
     * @param scores The scores of the match in order of: total, teleop, auton, penalties.
     */
    public static void writeEntry(XSSFWorkbook wb, String sheetName, char type, String[] names, double[] scores, String... comments){
        XSSFSheet sheet = getSheetFromWorkbook(wb, sheetName);
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
    }

    /**
     * Get all the matches from the
     * @param wb The workbook to get the data from
     * @param sheetName The name of the sheet to get the data from
     * @return An ArrayList of Matches containing all the matches found in the file
     */
    public static ArrayList<Match> getMatches(XSSFWorkbook wb, String sheetName){
        XSSFSheet sheet = getSheetFromWorkbook(wb,sheetName);
        ArrayList<Match> matches = new ArrayList<Match>();

        int maxRow = 1;
        while (sheet.getRow(maxRow+1)!=null && sheet.getRow(maxRow+1).getCell(0) != null && sheet.getRow(maxRow+1).getCell(0).getCellType() != CellType.BLANK) {
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

    /* SPREADSHEET UTILITIES - ALL TESTED AND WORK AS INTENDED */
    // TODO better error handling. Maybe make it return a boolean based on if it succeeded? Either way, can't do print error messages
    // TODO don't require passing the extension for the file name, it should be assumed that we're looking for .xlsx files
    /* File Handling */

    /**
     * Get an XSSFWorkbook file (a class that contains all the information from an Excel file) from a provided file name
     * @param fileName The name of the file to look for. Include the extension
     * @return a full XSSFWorkbook with all contained data
     */
    public static XSSFWorkbook getWorkbookFromFile(String fileName)
    {
        try{
            // Get the file input
            FileInputStream file = new FileInputStream(fileName);
            // Create a blank workbook from that file
            XSSFWorkbook wb = new XSSFWorkbook(file);
            file.close();
            return wb;
        }catch (FileNotFoundException e)
        {
            System.out.println("Error loading file: Could not find a file with name\""+fileName+"\" or was open with another program");
            return null;
        }catch(IOException e){
            System.out.println("Error loading file: An I/O error occurred");
            return null;
        }
    }

    /**
     * Write the provided workbook to the Excel file with name 'fileName'
     * @param fileName The name of the Excel file to look for. Include the extension
     * @param wb The workbook to be saved to the
     */
    public static void writeWorkbookToSpreadsheet(String fileName, XSSFWorkbook wb)
    {
        try{
            File file = new File(fileName);
            FileOutputStream out = new FileOutputStream(file);
            wb.write(out);
            out.close();
            System.out.println("Successfully written");
        }catch(FileNotFoundException e){
            System.out.println("Error writing to file: Could not find a file with name \""+fileName+"\" or was open in another program");
            //e.printStackTrace();
        }catch(IOException e){
            System.out.println("Error writing to file: An I/O error occurred");
        }
    }

    /* QOL methods for working with XSSFWorkbooks and XSSFSheets */

    /**
     * Not the preferred method of getting a sheet, requires loading the Excel spreadsheet file
     * @param fileName The name of the Excel spreadsheet to load. Include the extension
     * @param sheetName The name of the sheet to look for. If a sheet by that name cannot be found, one will be created
     * @return The XSSFSheet contained in the workbook
     */
    public static XSSFSheet getSheetFromWorkbook(String fileName, String sheetName)
    {
        XSSFWorkbook wb = getWorkbookFromFile(fileName);
        return getSheetFromWorkbook(wb, sheetName);
    }

    /**
     * The preferred method of getting a sheet, using a workbook that has already been pulled from a file
     * @param wb The workbook to search in
     * @param sheetName The name of the sheet to look for. If a sheet by that name cannot be found, one will be created
     * @return The XSSFSheet contained in the workbook
     */
    public static XSSFSheet getSheetFromWorkbook(XSSFWorkbook wb, String sheetName)
    {
        XSSFSheet sheet = wb.getSheet(sheetName);
        if(sheet == null){
            sheet = wb.createSheet(sheetName);
        }
        return sheet;
    }

    /**
     * Write a Map of double values to a sheet. The Map must have integers as the key, because it uses that to determine the row
     * @param sheet The XSSFSheet to be written to
     * @param sheetMap The Map containing double values to be written into the sheet
     */
    public static void writeDatamapToSheet(XSSFSheet sheet, Map<Integer, ArrayList<Double>> sheetMap){
        for(int i = 1; i < sheetMap.size()+1; i++){
            Row row = (sheet.getRow(i)==null?sheet.createRow(i):sheet.getRow(i));
            writeDataToRow(row, sheetMap.get(i));
        }
    }

    /**
     * A simple method to carry out writing an ArrayList of decimal numbers in order to a single row of a sheet
     * @param row The row of a sheet to be written to
     * @param d The ArrayList of doubles to be written into the row
     */
    public static void writeDataToRow(Row row, ArrayList<Double> d) {
        for (int i = 0; i < d.size(); i++) {
            Cell cell = (row.getCell(i) == null ? row.createCell(i) : row.getCell(i));
            cell.setCellValue(d.get(i));
        }
    }

    /**
     * A simple method to just launch the file with provided filename in a seperate window with the default application
     * @param fileName The name of the file to be launched
     */
    public static void launchSpreadsheet(String fileName){
        Desktop d = Desktop.getDesktop();
        try{
            d.open(new File(fileName));
        }catch(Exception e){
            System.out.println("Error opening file");
        }
    }
}
