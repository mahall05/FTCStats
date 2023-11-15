package Core;

public class Settings {
    public static final String redTeamDataFile = "Red Team Data.xlsx";

    // Change weight of competition matches vs practice matches
    // Change weight of new matches with the new robot

    /* Match Settings */

    /* The relative weight of practice matches when compared to real competition matches */
    public static double relativePracticeWeight = 0.8;
    /* The relative weight of matches with the old robot when compared to the new robot */
    public static double relativeOldRobotWeight = 1;

    /* Team Member Settings */
    public static double dateWeight = 0.0294117647;

    /* Drive Team Settings */

    /* The relative weight of the coach's average score compared to the driver and operator (which are equal) */
    public static double relativeCoachWeight = 0.5;
    /* The relative weight of the driver's average score. Recommended to keep this at 1, only change coach */
    public static double relativeDriverWeight = 1.0;
    /* The relative weight of the operator's average score. Recommended to keep this at 1, only change coach */
    public static double relativeOperatorWeight = 1.0;

    public static double relativeSoloWeight = 1.0;
    public static double relativeDuoWeight = 2.0;
    public static double relativeTrioWeight = 3.0;
}
