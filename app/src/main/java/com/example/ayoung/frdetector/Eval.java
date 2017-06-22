package com.example.ayoung.frdetector;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AYoung on 2017-06-22.
 */
public class Eval {
    String name;
    String number;
    String task;
    String attend;

    public Eval() {}

    public void fromMap(Map<String, Object> map) {
        name = (String) map.get("name");
        number = (String) map.get("number");
        task = (String) map.get("task");
        attend = (String) map.get("attend");
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("number", number);
        result.put("task", task);
        result.put("attend", attend);
        return result;
    }
}
