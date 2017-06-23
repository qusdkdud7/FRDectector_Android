package com.example.ayoung.frdetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AYoung on 2017-06-22.
 */
public class TaskData {
    String taskname;
    String name;
    String star;
    String date;

    public TaskData() {
    }

    public void fromMap(Map<String, Object> map) {
        taskname = (String) map.get("taskname");
        name = (String) map.get("name");
        star = (String) map.get("star");
        date = (String) map.get("date");

    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("taskname", taskname);
        result.put("name", name);
        result.put("star", star);
        result.put("date", date);
        return result;
    }
}
