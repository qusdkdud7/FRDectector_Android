package com.example.ayoung.frdetector;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference myRef;
    final ArrayList<Team> personalteams = new ArrayList<Team>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Person testp1 = new Person("홍길동", "01000000000");
        Person testp2 = new Person("김철수", "01012345678");
        Person testp3 = new Person("박영희", "01087654321");
        Person testp4 = new Person("강민수", "01011111111");
        Person testp5 = new Person("최미영", "01022222222");

        Team testt1 = new Team("테스트팀1");
        Team testt2 = new Team("테스트팀2");
        testt1.teamcode = "ASDFGHJKL";
        testt2.teamcode = "LKJHGFDSA";
        testt1.addPerson(testp1);
        testt1.addPerson(testp2);
        testt1.addPerson(testp3);
        testt1.addPerson(testp4);
        testt2.addPerson(testp5);
        testt2.addPerson(testp3);
        testt2.addPerson(testp2);
        testt2.addPerson(testp1);

        personalteams.add(testt1);
        personalteams.add(testt2);
        showTeam();

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

    public void showTeam() {
        final LinearLayout teamlistview = (LinearLayout) findViewById(R.id.teamlistview);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("teams/person-teams/강땡땡0000");

        myRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Map<String, Object> v = (Map<String, Object>) dataSnapshot.getValue();
                    for (Map.Entry<String, Object> e : v.entrySet()) {
                        Team t = new Team();
                        t.fromMap((Map<String, Object>) e.getValue());
                        personalteams.add(t);
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            }
        });

        for (int i = 0; i < personalteams.size(); i++) {
            final Team t = personalteams.get(i);
            Button button = new Button(this);
            button.setGravity(0);
            button.setText("팀이름: " + t.teamname + "\n팀플코드: " + t.teamcode + "\n조장: " + t.persons.get(0).name);
            teamlistview.addView(button, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), FuncActivity.class);
                    intent.putExtra("team", (Serializable) t);
                    startActivity(intent);
                }
            });
        }

    }
}