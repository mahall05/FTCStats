import TeamMember.TeamMember;

import java.util.ArrayList;

public class Util {
    public static TeamMember findByName(TeamMember[] members, String name){
        for(TeamMember m : members){
            if(m.getName().equalsIgnoreCase(name)){
                return m;
            }
        }
        return null;
    }
}
