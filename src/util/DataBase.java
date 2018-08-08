package util;

import entity.Staff;

import java.util.HashMap;

public class DataBase {
    public static HashMap<String,String> USER=new HashMap<>();

    public static HashMap<String, Staff> STAFF=new HashMap<>();

    static {
        USER.put("1","1");

        STAFF.put("1",new Staff("Qin","man",22));
        STAFF.put("2",new Staff("Peter","man",30));
        STAFF.put("3",new Staff("Ben","man",23));
        STAFF.put("4",new Staff("Lucy","woman",20));
    }
}
