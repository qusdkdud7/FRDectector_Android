package com.example.ayoung.frdetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class CreateTeamActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("teams");

    }

    public void onLogoClicked(View v){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void onCreateCheckBtnClicked(View v){
        EditText teamname = (EditText) findViewById(R.id.createteamname);
        EditText name = (EditText) findViewById(R.id.createname);
        EditText number = (EditText) findViewById(R.id.createnumber);

        if(teamname.getText().toString().equals("") || name.getText().toString().equals("") || number.getText().toString().equals(""))
            Toast.makeText(this,"모두 입력한 후 확인을 눌러주세요.",Toast.LENGTH_LONG).show();
        else{
            key = myRef.child("teams").push().getKey();
            Toast.makeText(CreateTeamActivity.this,"팀플코드는 "+ key +"입니다.",Toast.LENGTH_LONG).show();
        }
    }

    public void onCreateDoneClicked(View v){

        EditText teamname = (EditText) findViewById(R.id.createteamname);
        EditText name = (EditText) findViewById(R.id.createname);
        EditText number = (EditText) findViewById(R.id.createnumber);

        Person leader = new Person(name.getText().toString(), number.getText().toString());
        Team team = new Team(teamname.getText().toString());
        team.addPerson(leader);

        if(teamname.getText().toString().equals("") || name.getText().toString().equals("") || number.getText().toString().equals(""))
            Toast.makeText(this,"모두 입력한 후 완료해주세요.",Toast.LENGTH_LONG).show();
        else saveNewTeam(team);
    }

    private void saveNewTeam(Team team){
        team.teamcode = key;
        Map<String,Object> teamValues = team.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/teamlist/"+key,teamValues);
        String p = team.persons.get(0).name+team.persons.get(0).number;
        childUpdates.put("/person-teams/"+ p +"/"+key,teamValues);

        myRef.updateChildren(childUpdates);

        finish();
    }
}
