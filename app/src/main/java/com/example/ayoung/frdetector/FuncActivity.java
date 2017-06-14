package com.example.ayoung.frdetector;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class FuncActivity extends AppCompatActivity {

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Feed");

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
/*
        final Button attendCheck = (Button)findViewById(R.id.attendCheckBtn);
        attendCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attendCheck.getText().equals("출석체크")){
                    //TODO: 조장이 출석체크 할 수 있는 view로 바꿔주기, 출석현황 저장
                    attendCheck.setText("완료");
                }
                else{
                    //TODO: 저장된 출석현황 보여주기
                    attendCheck.setText("출석체크");
                }
            }
        });*/
        //showFeed();

    }
    private void showFeed() {
        LinearLayout feedview = (LinearLayout)findViewById(R.id.feed);
        final EditText txtMsg = (EditText) findViewById(R.id.txtMsg);
        Button msgSendBtn = (Button) findViewById(R.id.msgSendBtn);

        final String userName = "user" + new Random().nextInt(10000);  // 유저이름 설정 바꾸기

        msgSendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String message = txtMsg.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    txtMsg.setText("");
                    FeedData feedData = new FeedData();
                    feedData.pName = userName;
                    feedData.message = message;
                    feedData.time = System.currentTimeMillis();
                    myRef.push().child("feed").setValue(feedData);
                }
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
