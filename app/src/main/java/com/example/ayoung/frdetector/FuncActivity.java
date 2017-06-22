package com.example.ayoung.frdetector;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import static com.example.ayoung.frdetector.R.id.center;

public class FuncActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private Team team;


    private ArrayList<ArrayList<String>> attendDates = new ArrayList<ArrayList<String>>();
    private ArrayList<String> attendPersons;
    private ArrayList<String> NotattendPersons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func);

        Intent intent = getIntent();

        team = (Team) intent.getSerializableExtra("team");

        TextView projectname = (TextView) findViewById(R.id.projectname);
        projectname.setText(team.teamname);

        final ImageButton info = (ImageButton) findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo(team);
            }
        });

        final Button feed = (Button) findViewById(R.id.feed);
        feed.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                changeView("feed");
                showFeed();
            }
        });

        final Button task = (Button) findViewById(R.id.task);
        task.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                changeView("task");

                final LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final LinearLayout tasklist = (LinearLayout)findViewById(R.id.tasklist);

                View view = inflater.inflate(R.layout.task,null);
                EditText taskname = (EditText)view.findViewById(R.id.taskname);
                Button TaskUpdateBtn = (Button)view.findViewById(R.id.TaskUpdateBtn);
                TaskUpdateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = inflater.inflate(R.layout.task,null);
                        final EditText taskname = (EditText)view.findViewById(R.id.taskname);
                        final Button TaskUpdateBtn = (Button)view.findViewById(R.id.TaskUpdateBtn);
                        tasklist.addView(view);

                        LayoutInflater pinflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        LinearLayout ptask = (LinearLayout) findViewById(R.id.taskpersonview);

                        if (TaskUpdateBtn.getText().equals("+")) {
                            TaskUpdateBtn.setText("-");
                            myRef.child(team.teamcode).push().child(taskname.getText().toString());
                            ptask.addView(pinflater.inflate(R.layout.taskperson, ptask, false));
                            addpersonview(taskname.getText().toString());

                            //TODO: TaskPersonView Update


                        } else {
                            TaskUpdateBtn.setText("+");

                            ptask.removeViewAt(0);
                            //TODO: Task Delete
                        }

                    }
                });

                tasklist.addView(view);



                //taskUpdate();




            }
        });

        final Button attendance = (Button) findViewById(R.id.attendance);
        attendance.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                changeView("attendance");
                changeAttendView("result");
                showAttendResult();
                attendButton();
            }
        });

        final Button evaluation = (Button) findViewById(R.id.evaluation);
        evaluation.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                changeView("evaluation");
                showEvaluation();
            }
        });

        changeView("feed");
        showFeed();


    }

    //////////////Feed Function///////////////////
    public void showFeed() {
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

                FeedData feedData = new FeedData("testuser", editText.getText().toString(), formatDate);
                myRef.child(team.teamcode).push().setValue(feedData);
                editText.setText("");
            }
        });
        myRef.child(team.teamcode).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FeedData feedData = dataSnapshot.getValue(FeedData.class);
                adapter.add(feedData.pName + "\n" + feedData.message + "\n" + feedData.time);
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


    //////////////Task Function///////////////////



    private void addpersonview(String taskname){

        final ImageButton taskdateBtn = (ImageButton) findViewById(R.id.taskdateBtn);
        taskdateBtn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: calendar popup & add date

            }
        });

        final Button ptaskBtn = (Button) findViewById(R.id.taskpersonUpdate);
        ptaskBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ptaskBtn.getText().equals("+")) {
                    ptaskBtn.setText("-");
                    //TODO: add Taskperson
                } else {
                    ptaskBtn.setText("+");
                    //TODO: delete Taskperson
                }
            }
        });
    }


    //////////////Attend Function///////////////////
    private void attendButton() {
        final Button attendBtn = (Button) findViewById(R.id.attendCheckBtn);
        attendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                if (attendBtn.getText().equals("출석체크")) {
                    attendBtn.setText("완료");
                    changeAttendView("check");

                    addAttendCheckView();

                    //TODO: Save team attendance result
                } else {
                    attendBtn.setText("출석체크");
                    saveattendance();
                    changeAttendView("result");
                    showAttendResult();
                    //TODO: Show team attendance result
                }
            }catch(Exception e){
                Toast.makeText(FuncActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
            }
            }
        });
    }

    private void showAttendResult(){
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.attendresultlayout);
        TableRow namerow = new TableRow(this);
        TextView t = new TextView(this);
        t.setText("날짜");
        namerow.addView(t);
        for(int i=0;i<team.persons.size();i++){
            TextView name = new TextView(this);
            namerow.addView(name);
        }


    }

    private void saveattendance(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Attendance");

        EditText attendanceDate = (EditText)findViewById(R.id.attendanceDate);

        myRef.child("attend" + team.teamcode+"/"+attendanceDate.getText().toString()+"/yes").push().setValue(attendPersons);
        myRef.child("attend" + team.teamcode+"/"+attendanceDate.getText().toString()+"/no").push().setValue(NotattendPersons);
    }

    private void addAttendCheckView(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        LinearLayout attendcheck = (LinearLayout) findViewById(R.id.attendcheck);

        attendcheck.addView(inflater.inflate(R.layout.attendcheckview, attendcheck, false));

        addAttendPersonBtn();
    }

    private void addAttendPersonBtn(){
        final EditText attendanceDate = (EditText)findViewById(R.id.attendanceDate);
        final TextView attendInfo = (TextView)findViewById(R.id.attendInfo);

        attendPersons = new ArrayList<String>();
        NotattendPersons = new ArrayList<String>();

        for(int i=0;i<team.persons.size();i++){
            Person p = team.persons.get(i);
            NotattendPersons.add(p.name);
        }

        final LinearLayout attendBtnlist = (LinearLayout) findViewById(R.id.attendPersonBtns);
        for(int i=0;i<team.persons.size();i++){
            final Person p = team.persons.get(i);
            final ToggleButton pBtn = new ToggleButton(this);
            pBtn.setText(p.name);
            pBtn.setTextOn(p.name);
            pBtn.setTextOff(p.name);
            attendBtnlist.addView(pBtn, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            pBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pBtn.isChecked()) {
                        attendPersons.add(p.name);
                        NotattendPersons.remove(p.name);
                        attendInfoText(attendInfo, attendanceDate);

                    }
                    else{
                        attendPersons.remove(p.name);
                        NotattendPersons.add(p.name);
                        attendInfoText(attendInfo, attendanceDate);
                    }

                    //TODO: 새로운 날짜레이아웃 추가
                }
            });
        }
    }

   private void attendInfoText(TextView attendInfo, EditText attendanceDate){
        String s1 = "", s2 = "";
        for (int j = 0; j < attendPersons.size(); j++) {
            s1 += attendPersons.get(j) + " ";
        }
        for (int j = 0; j < NotattendPersons.size(); j++) {
            s2 += NotattendPersons.get(j) + " ";
        }
        attendInfo.setText("날짜: " + attendanceDate.getText() + "\n출석: " + s1 + "\n불참: " + s2);

    }

    //////////////Evaluation Function///////////////////
    private void showEvaluation() {
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.attendTL);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Evaluation/" + team.teamcode);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Map<String, Object> v = (Map<String, Object>) dataSnapshot.getValue();
                    for (Map.Entry<String, Object> e : v.entrySet()) {
                        Eval eval = new Eval();
                        eval.fromMap((Map<String, Object>) e.getValue());

                        TableRow tbr = new TableRow(FuncActivity.this);
                        tableLayout.addView(tbr, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        TextView name = new TextView(FuncActivity.this);
                        TextView task = new TextView(FuncActivity.this);
                        TextView attend = new TextView(FuncActivity.this);
                        TextView res = new TextView(FuncActivity.this);

                        name.setText(eval.name);
                        name.setGravity(Gravity.CENTER);
                        name.setPadding(10,10,10,10);
                        name.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
                        task.setText(eval.task);
                        task.setGravity(Gravity.RIGHT);
                        task.setPadding(10,10,10,10);
                        attend.setText(Double.parseDouble(eval.attend) * 100 + "%");
                        attend.setGravity(Gravity.RIGHT);
                        attend.setPadding(10,10,10,10);
                        res.setText((Double.parseDouble(eval.task) + Double.parseDouble(eval.attend) * 5) + "/10");
                        res.setGravity(Gravity.RIGHT);
                        res.setPadding(10,10,10,10);

                        tbr.addView(name, new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
                        tbr.addView(task, new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
                        tbr.addView(attend, new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
                        tbr.addView(res, new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
                    }
                } catch (Exception e) {
                    Toast.makeText(FuncActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }






    private void changeAttendView(String s) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        FrameLayout frame = (FrameLayout) findViewById(R.id.attendFrameview);
        if (frame.getChildCount() > 0) {
            frame.removeViewAt(0);
        }

        View v = null;
        switch (s) {
            case "check":
                v = inflater.inflate(R.layout.attendcheck, frame, false);
                break;
            case "result":
                v = inflater.inflate(R.layout.attendresult, frame, false);
                break;
        }
        if (v != null) {
            frame.addView(v);
        }
    }

    private void changeView(String view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
        if (frame.getChildCount() > 0) {
            frame.removeViewAt(0);
        }

        View v = null;
        switch (view) {
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
        if (v != null) {
            frame.addView(v);
        }
    }

    public void onLogoClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void showInfo(Team team) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String s = "";
        for (int i = 1; i < team.persons.size(); i++) {
            s += team.persons.get(i).name + " ";
        }
        builder.setMessage("팀이름: " + team.teamname + "\n팀플코드: " + team.teamcode + "\n조장: " + team.persons.get(0).name + "\n조원: " + s);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
