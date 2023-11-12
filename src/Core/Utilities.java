package Core;

import TeamMember.TeamMember;

public class Utilities {
    public static TeamMember findByName(TeamMember[] members, String name){
        for(TeamMember m : members){
            if(m.getName().equalsIgnoreCase(name)){
                return m;
            }
        }
        return null;
    }
}
