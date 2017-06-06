package com.example.ayoung.frdetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;

class Team implements Serializable{
    String teamname;
    String teamcode;
    ArrayList<Person> persons;

    public Team(){
        super();
    }
    public Team(String teamname, String teamcode,Person p){
        this.teamname = teamname;
        this.teamcode = teamcode;
        this.persons.add(p);
    }
}

class Person implements Serializable{
    boolean teamleader;
    String name;
    String number;

    public Person(){
        super();
    }
    public Person(boolean teamleader, String name, String number){
        this.teamleader = teamleader;
        this.name = name;
        this.number = number;
    }
}

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Team> teams = new ArrayList<Team>();
    public static String teamcode = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button)findViewById(R.id.testbutton);
        btn.setText("팀이름: ");
    }


    public void onLogoClicked(View v){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void onCreateTeamClicked(View v){
        Intent intent = new Intent(getApplicationContext(),CreateTeamActivity.class);
        startActivity(intent);
    }

    public void onJoinTeamClicked(View v){
        Intent intent = new Intent(getApplicationContext(),JoinTeamActivity.class);
        startActivity(intent);
    }


}
