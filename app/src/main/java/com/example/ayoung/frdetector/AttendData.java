package com.example.ayoung.frdetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AYoung on 2017-06-22.
 */
public class AttendData {
    String attendDate;
    ArrayList<String> yes;
    ArrayList<String> no;

    public AttendData() {
    }

    public void fromMap(Map<String, Object> map) {
        attendDate = (String) map.get("attendDate");
        yes = ((ArrayList<String>) map.get("yes"));
        no = ((ArrayList<String>) map.get("no"));
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("attendDate", attendDate);
        result.put("yes", yes);
        result.put("no", no);
        return result;
    }
}
