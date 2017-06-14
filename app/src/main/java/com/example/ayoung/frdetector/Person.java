package com.example.ayoung.frdetector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AYoung on 2017-06-09.
 */
public class Person implements Serializable {
    String name;
    String number;

    public Person() {
    }

    public Person(String name, String number) {
        this.name = name;
        this.number = number;
    }
    public void fromMap(Map<String, Object> map){
        name = (String)map.get("name");
        number = (String) map.get("teamcode");
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("number", number);
        return result;
    }
}
