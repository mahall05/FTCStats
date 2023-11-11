import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

public class Tester {
    public static void main(String[] args){
        LocalDate initialDate = LocalDate.of(2010, 10, 10);
        LocalDate now = LocalDate.now();
        Period diff = Period.between(initialDate, now);
        System.out.println(diff.getYears()+"\n"+diff.getMonths()+"\n"+diff.getDays());
        System.out.println("\n"+ChronoUnit.DAYS.between(initialDate,now));

    }
}
