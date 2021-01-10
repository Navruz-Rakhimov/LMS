package staticTools;

import java.util.ArrayList;

public class UserTracker {
    public static ArrayList<String> list = new ArrayList<String>();
    public static void trackLogIn(String email){
        list.add(email);
    }
    public static String getLastTrackedUser(){
        if (list != null) {
            return list.get(list.size() - 1);
        }
        else {
            return null;
        }
    }
}

