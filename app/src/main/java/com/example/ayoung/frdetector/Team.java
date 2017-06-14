package com.example.ayoung.frdetector;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by AYoung on 2017-06-09.
 */
public class Team implements Serializable {
    String teamname;
    String teamcode;
    ArrayList<Person> persons = new ArrayList<Person>();

    public Team() {
    }

    public Team(String teamname) {
        this.teamname = teamname;
    }

    public void addPerson(Person person){
        this.persons.add(person);
    }

    public void fromMap(Map<String, Object> map){
        teamname = (String)map.get("teamname");
        teamcode = (String) map.get("teamcode");
        persons = (ArrayList<Person>) map.get("persons");
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("teamname", teamname);
        result.put("teamcode", teamcode);
        result.put("persons", persons);
        return result;
    }
}
