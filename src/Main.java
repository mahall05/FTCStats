import Match.Match;
import TeamMember.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.time.ZoneId;
import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public Driver[] drivers = {new Driver("Bredan"), new Driver("Erin"), new Driver("Luca")};
    public Operator[] operators = {new Operator("Mason"), new Operator("Zoe"), new Operator("Cyrus")};
    public Coach[] coaches = {new Coach("Caleb"), new Coach("Matt"), new Coach("Zach")};
    public DriveTeam BM;
    public DriveTeam EZ;
    public DriveTeam LC;

    private void runSetup(){
        ArrayList<Match> matches = getMatches();
        for(Match m : matches){
            m.assign(drivers, operators, coaches);
        }

        BM = new DriveTeam(drivers[0], operators[0], coaches[0], matches);
        EZ = new DriveTeam(drivers[1], operators[1], coaches[1], matches);
        LC = new DriveTeam(drivers[2], operators[2], coaches[2], matches);
    }
    private void runAnalysis(){
        for(int i = 0; i < drivers.length; i++){
            drivers[i].calcAll();
            operators[i].calcAll();
            coaches[i].calcAll();
        }

        BM.calcAll();
        EZ.calcAll();
        LC.calcAll();
    }
    private void printData(){
        System.out.println(BM.toStringUnweighted());
        System.out.println(BM.toStringWeighted());
        System.out.println(EZ.toStringUnweighted());
        System.out.println(EZ.toStringWeighted());
        System.out.println(LC.toStringUnweighted());
        System.out.println(LC.toStringWeighted());
    }

    private void run(){
        runSetup();
        runAnalysis();
        printData();
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
        }
        return matches;
    }
    private XSSFSheet getSheet(String sheetName)
    {
        String statsFileName = "Drive Teams.xlsx";
        XSSFWorkbook wb = getWorkbook(statsFileName);
        try{
            XSSFSheet sheet = wb.getSheet(sheetName);
            return sheet;
        }catch (NullPointerException e)
        {
            System.out.println("Error loading sheet");
            return null;
        }
    }

    private XSSFWorkbook getWorkbook(String fileName)
    {
        try{
            // Get the file input
            FileInputStream file = new FileInputStream(fileName);
            // Create a blank workbook from that file
            XSSFWorkbook wb = new XSSFWorkbook(file);
            return wb;
        }catch (Exception e)
        {
            System.out.println("Error loading file");
            return null;
        }
    }

    public Main(){
        run();
    }
    public static void main(String[] args) {
        new Main();
    }



        /*
        // Create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Create blank sheet
        XSSFSheet sheet = workbook.createSheet("Test Data");

        // Prepare data to be written as an Object[]
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[] {"ID", "NAME", "LASTNAME"});
        data.put("2", new Object[] {1, "Amit", "Shukla"});
        data.put("3", new Object[] {2, "Lokesh", "Gupta"});
        data.put("4", new Object[] {3, "John", "Adwards"});
        data.put("5", new Object[] {4, "Brian", "Schultz"});

        // Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset)
        {
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
                Cell cell = row.createCell(cellnum++);
                if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer)obj);
            }
        }

        // Write the workbook in file system
        try{
            FileOutputStream out = new FileOutputStream(new File("testsheet.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("Successfully written");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        */
}