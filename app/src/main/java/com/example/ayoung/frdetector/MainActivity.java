package com.example.ayoung.frdetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
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

        Person testp1 = new Person("박지훈", "01000000000");
        Person testp2 = new Person("라이관린", "01012345678");
        Person testp3 = new Person("유선호", "01087654321");
        Person testp4 = new Person("배진영", "01011111111");

        Team testt1 = new Team("프로듀스101");
        Team testt2 = new Team("프로듀스101 시즌2");
        testt1.teamcode = "ASDFGHJKL";
        testt2.teamcode = "LKJHGFDSA";
        testt1.addPerson(testp1);
        testt1.addPerson(testp2);
        testt1.addPerson(testp3);
        testt1.addPerson(testp4);
        testt2.addPerson(testp4);
        testt2.addPerson(testp3);
        testt2.addPerson(testp2);
        testt2.addPerson(testp1);

        personalteams.add(testt1);personalteams.add(testt2);

        ArrayList<Button> btns = new ArrayList<Button>();

        final LinearLayout teamlistview = (LinearLayout) findViewById(R.id.teamlistview);

        for(int i=0;i<personalteams.size();i++){
            final Team t = personalteams.get(i);
            final int teamnum = i;
            Button button = new Button(this);
            button.setGravity(0);
            button.setText("팀이름: " + t.teamname + "\n팀플코드: " + t.teamcode + "\n조장: " + t.persons.get(0).name);
            teamlistview.addView(button, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), FuncActivity.class);
                    intent.putExtra("team", (Serializable) t);
                    intent.putExtra("teamNum",teamnum);
                    startActivity(intent);
                }
            });
        }


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

}