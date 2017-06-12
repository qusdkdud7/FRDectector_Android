package com.example.ayoung.frdetector;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JoinTeamActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    final ArrayList<String> teamcodes = new ArrayList<String>();
    final ArrayList<Team> teams = new ArrayList<Team>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("teams/teamlist");

        myRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    Map<String,Object> v = (Map<String,Object>)dataSnapshot.getValue();
                    for(Map.Entry<String,Object> e:v.entrySet()) {
                        Team t = new Team();
                        t.fromMap((Map<String, Object>) e.getValue());
                        teamcodes.add(t.teamcode);
                        teams.add(t);
                    }
                }catch(Exception e){
                    Toast.makeText(JoinTeamActivity.this,"등록되어 있는 팀이 없습니다.",Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(JoinTeamActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onLogoClicked(View v){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void onJoinDoneClicked(View v){
        EditText teamcode = (EditText) findViewById(R.id.jointeamcode);
        EditText name = (EditText) findViewById(R.id.joinname);
        EditText number = (EditText) findViewById(R.id.joinnumber);

        if(teamcode.getText().toString().equals("") || name.getText().toString().equals("") || number.getText().toString().equals("")){
            Toast.makeText(this,"모두 입력한 후 완료해주세요.",Toast.LENGTH_LONG).show();
        }
        else{
            int tmp=0;
            for(int i=0;i<teamcodes.size();i++){
                if(teamcodes.get(i).equals(teamcode.getText().toString())) {
                    showJoinDialog(i);
                    tmp = 1;
                    break;
                }
            }
            if(tmp==0) showCancelDialog();
        }
    }

    public void showJoinDialog(int index){
        final Team team = teams.get(index);
        Person p = new Person();
        p.fromMap((Map<String,Object>)team.persons.get(0));

        EditText name = (EditText) findViewById(R.id.joinname);
        EditText number = (EditText) findViewById(R.id.joinnumber);

        final Person person = new Person(name.getText().toString(),number.getText().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //TODO: 조장 이름만 나오게 수정 => HashMap cast Person못함 .name하니 오류
        builder.setMessage("팀플이름: "+team.teamname+"\n팀플코드: "+team.teamcode+"\n조장: "+p.name+"\n참여하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int whichButton){
                try {
                    team.addPerson(person);
                    saveNewPerson(team,person);
                }catch(Exception e){
                    Toast.makeText(JoinTeamActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("취소",new DialogInterface.OnClickListener(){
           public void onClick(DialogInterface dialog, int whichButton){
               resetText();
           }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveNewPerson(Team team, Person person){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("teams");
        Map<String,Object> teamValues = team.toMap();
        Map<String, Object> personValues = person.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/teamlist/"+team.teamcode+"/persons/"+(team.persons.size()-1)+"/",personValues);
        String p = team.persons.get(team.persons.size()-1).name+team.persons.get(team.persons.size()-1).number;
        childUpdates.put("/person-teams/"+ p +"/"+team.teamcode+"/",teamValues);

        myRef.updateChildren(childUpdates);

        finish();
    }

    public void showCancelDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("존재하지 않는 코드입니다.");
        builder.setPositiveButton("확인",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int whichButton){
                resetText();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void resetText(){
        EditText teamcode = (EditText) findViewById(R.id.jointeamcode);
        EditText name = (EditText) findViewById(R.id.joinname);
        EditText number = (EditText) findViewById(R.id.joinnumber);
        teamcode.setText("");
        name.setText("");
        number.setText("");
    }
}
