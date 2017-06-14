package com.example.ayoung.frdetector;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FuncActivity extends AppCompatActivity{

    private DatabaseReference myRef;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func);

        Intent intent = getIntent();

        final Team team = (Team)intent.getSerializableExtra("team");

        TextView projectname = (TextView)findViewById(R.id.projectname);
        projectname.setText(team.teamname);

        ImageButton info = (ImageButton)findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo(team);
            }
        });

        Button feed = (Button)findViewById(R.id.feed);
        feed.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                changeView("feed");
            }
        });

        Button task = (Button)findViewById(R.id.task);
        task.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                changeView("task");
            }
        });

        Button evaluation = (Button)findViewById(R.id.evaluation);
        evaluation.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                changeView("evaluation");
            }
        });

        Button attendance = (Button)findViewById(R.id.attendance);
        attendance.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                changeView("attendance");
            }
        });
        changeView("feed");

///////////Feed Layout/////////////
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Feed");

        ListView listView = (ListView) findViewById(R.id.feedview);
        final EditText editText = (EditText) findViewById(R.id.txtMsg);
        Button sendButton = (Button) findViewById(R.id.msgSendBtn);

        final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("a HH:mm:ss");
                String formatDate = sdf.format(date);

                FeedData feedData = new FeedData("testuser",editText.getText().toString(),formatDate);
                myRef.child("feed").push().setValue(feedData);
                editText.setText("");
            }
        });
        myRef.child("feed").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FeedData feedData = dataSnapshot.getValue(FeedData.class);
                adapter.add(feedData.pName+"\n"+feedData.message+"\n"+feedData.time);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void changeView(String view){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        FrameLayout frame = (FrameLayout)findViewById(R.id.frame);
        if(frame.getChildCount() > 0){
            frame.removeViewAt(0);
        }

        View v = null;
        switch(view){
            case "feed":
                v = inflater.inflate(R.layout.feedlayout, frame, false);
                break;
            case "attendance":
                v = inflater.inflate(R.layout.attendancelayout, frame, false);
                break;
            case "evaluation":
                v = inflater.inflate(R.layout.evaluationlayout, frame, false);
                break;
            case "task":
                v = inflater.inflate(R.layout.tasklayout, frame, false);
                break;
        }
        if(v !=null){
            frame.addView(v);
        }
    }

    public void onLogoClicked(View v){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void showInfo(Team team){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String s = "";
        for(int i=1;i<team.persons.size();i++){
            s += team.persons.get(i).name+" ";
        }
        builder.setMessage("팀이름: "+team.teamname+"\n팀플코드: "+team.teamcode+"\n조장: "+team.persons.get(0).name+"\n조원: "+s);
        builder.setPositiveButton("확인",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int whichButton){
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }




}
