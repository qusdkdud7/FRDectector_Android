package com.example.ayoung.frdetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference Ref;
    final ArrayList<Team> personalteams = new ArrayList<Team>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout teamlistview = (LinearLayout) findViewById(R.id.teamlistview);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Ref = database.getReference("teams/teamlist");

        Ref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    Map<String,Object> v = (Map<String,Object>)dataSnapshot.getValue();
                    for(Map.Entry<String,Object> e:v.entrySet()) {
                        Team t = new Team();
                        t.fromMap((Map<String, Object>) e.getValue());
                        Button button = new Button(MainActivity.this);
                        button.setText("팀이름: "+t.teamname+"\n팀플코드: "+t.teamcode+"\n조장: "+t.persons.get(0).name);
                        teamlistview.addView(button, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        button.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), FuncActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }catch(Exception e){
                    Toast.makeText(MainActivity.this,"등록되어 있는 팀이 없습니다.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onLogoClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void onCreateTeamClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), CreateTeamActivity.class);
        startActivity(intent);
    }

    public void onJoinTeamClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), JoinTeamActivity.class);
        startActivity(intent);
    }

    public void showTeam(String s) {

    }

}