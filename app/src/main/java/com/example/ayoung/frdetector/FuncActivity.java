package com.example.ayoung.frdetector;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
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

import java.security.spec.ECField;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FuncActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private Team team;
    private String dateselected;
    private int personindex;
    private float rating;
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
                changeTaskView("result");
                showTaskResult();
                taskButton();
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

    private void DialogDatePicker() {
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateselected = String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(dayOfMonth);

                        TextView taskdaterange = (TextView)findViewById(R.id.taskdaterange);
                        taskdaterange.setText(dateselected);
                    }
                };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, mDateSetListener, cyear, cmonth, cday);
        datePickerDialog.show();
    }


    private void taskButton() {
        final Button taskBtn = (Button) findViewById(R.id.taskBtn);
        taskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (taskBtn.getText().equals("할일추가")) {
                        taskBtn.setText("완료");
                        changeTaskView("check");
                        checkBtns();
                    }
                    else {
                        EditText taskname = (EditText) findViewById(R.id.taskname);
                        if (taskname.getText().toString().equals("")) {
                            Toast.makeText(FuncActivity.this, "업무를 입력하세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            taskBtn.setText("할일추가");
                            savetask();
                            changeTaskView("result");
                            showTaskResult();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(FuncActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showTaskResult() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Task/" + team.teamcode);

        myRef.addValueEventListener(new ValueEventListener() {
            final LinearLayout taskPersonsss = (LinearLayout)findViewById(R.id.taskPersonsss);
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Map<String, Object> v = (Map<String, Object>) dataSnapshot.getValue();
                    for (Map.Entry<String, Object> e : v.entrySet()) {
                        TaskData taskdata = new TaskData();
                        taskdata.fromMap((Map<String, Object>) e.getValue());

                        LinearLayout l1 = new LinearLayout(FuncActivity.this);
                        l1.setOrientation(LinearLayout.HORIZONTAL);

                        TextView taskname = new TextView(FuncActivity.this);
                        taskname.setText(taskdata.taskname);
                        taskname.setTextSize(20);
                        Button delete = new Button(FuncActivity.this);
                        delete.setText("-");
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO: 삭제
                            }
                        });

                        l1.addView(taskname, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
                        l1.addView(delete, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,10));



                        LinearLayout l2 = new LinearLayout(FuncActivity.this);
                        l2.setOrientation(LinearLayout.HORIZONTAL);

                        TextView name = new TextView(FuncActivity.this);
                        name.setText(taskdata.name);
                        name.setTextSize(15);
                        final RatingBar rate = new RatingBar(FuncActivity.this);
                        rate.setMax(5);
                        rate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                rating = rate.getRating();
                            }
                        });
                        final Button star = new Button(FuncActivity.this);
                        star.setText("별점주기");
                        star.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    saveRate();
                                }catch(Exception e){
                                    Toast.makeText(FuncActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                        TextView info = new TextView(FuncActivity.this);

                        String str = "";

                        info.setText("기한: ~"+taskdata.date+"\n별점평가기간: "+str+"\n별점: "+rating);


                        l2.addView(name, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        l2.addView(rate, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        l2.addView(star, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));


                        taskPersonsss.addView(l1);
                        taskPersonsss.addView(l2);
                        taskPersonsss.addView(info);

                    }
                } catch (Exception e) {
                    Toast.makeText(FuncActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FuncActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveRate(){
        TaskData td = new TaskData();

        TextView taskname = (TextView)findViewById(R.id.taskname);

        td.taskname = taskname.getText().toString();
        td.date = dateselected;
        td.name = team.persons.get(personindex).name;
        td.star = Float.toString(rating);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Task/" + team.teamcode);

        Map<String, Object> attendValues = td.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(td.taskname+td.name, attendValues);

        myRef.updateChildren(childUpdates);
    }


    private void changeTaskView(String s) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout taskLL = (LinearLayout) findViewById(R.id.taskLL);
        if (taskLL.getChildCount() > 0) {
            taskLL.removeViewAt(0);
        }

        View v = null;
        switch (s) {
            case "check":
                v = inflater.inflate(R.layout.task, taskLL, false);
                break;
            case "result":
                v = inflater.inflate(R.layout.taskresult, taskLL, false);

                break;
        }
        if (v != null) {
            taskLL.addView(v);
        }
    }

    private void checkBtns(){
        ImageButton taskdateBtn = (ImageButton)findViewById(R.id.taskdateBtn);
        taskdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDatePicker();
            }
        });

        final LinearLayout personRB = (LinearLayout)findViewById(R.id.personsRadio);

        for(int i=0;i<team.persons.size();i++){
            final int index = i;

            Button pb = new Button(FuncActivity.this);
            pb.setText(team.persons.get(i).name);
            pb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView task_name = (TextView)findViewById(R.id.task_name);
                    task_name.setText(team.persons.get(index).name);
                    personindex = index;
                }
            });

            personRB.addView(pb, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }

    }

    private void savetask(){
        TaskData td = new TaskData();

        TextView taskname = (TextView)findViewById(R.id.taskname);

        td.taskname = taskname.getText().toString();
        td.date = dateselected;
        td.name = team.persons.get(personindex).name;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Task/" + team.teamcode);

        Map<String, Object> attendValues = td.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(td.taskname+td.name, attendValues);

        myRef.updateChildren(childUpdates);
    }


    //////////////Attend Function///////////////////
    private void attendButton() {
        final Button attendBtn = (Button) findViewById(R.id.attendCheckBtn);
        attendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (attendBtn.getText().equals("출석체크")) {
                        attendBtn.setText("완료");
                        changeAttendView("check");
                        addAttendCheckView();
                    } else {
                        EditText attendDate = (EditText) findViewById(R.id.attendanceDate);
                        if (attendDate.getText().toString().equals("")) {
                            Toast.makeText(FuncActivity.this, "날짜를 입력하세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            attendBtn.setText("출석체크");
                            saveattendance();
                            changeAttendView("result");
                            showAttendResult();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(FuncActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showAttendResult() {
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.attendantRes);

        final TableRow personnames = (TableRow) findViewById(R.id.personnames);
        TextView tvblank = new TextView(FuncActivity.this);
        tvblank.setText(" ");
        tvblank.setGravity(Gravity.CENTER);
        tvblank.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        personnames.addView(tvblank, new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));

        for (int i = 0; i < team.persons.size(); i++) {
            TextView nametv = new TextView(FuncActivity.this);
            nametv.setText(team.persons.get(i).name);
            nametv.setGravity(Gravity.CENTER);
            nametv.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
            personnames.addView(nametv, new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Attendance/" + team.teamcode);

        final int[] totalAttend = new int[team.persons.size() + 1];
        for (int i = 0; i < totalAttend.length; i++) {
            totalAttend[i] = 0;
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Map<String, Object> v = (Map<String, Object>) dataSnapshot.getValue();
                    for (Map.Entry<String, Object> e : v.entrySet()) {
                        totalAttend[totalAttend.length - 1] += 1;
                        AttendData attend = new AttendData();
                        attend.fromMap((Map<String, Object>) e.getValue());

                        TableRow tbr = new TableRow(FuncActivity.this);
                        tableLayout.addView(tbr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                        TextView date = new TextView(FuncActivity.this);
                        date.setText(attend.attendDate);
                        date.setGravity(Gravity.CENTER);
                        date.setPadding(10, 10, 10, 10);
                        date.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
                        tbr.addView(date, new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));

                        for (int i = 0; i < team.persons.size(); i++) {
                            TextView tv = new TextView(FuncActivity.this);
                            if (attend.yes.contains(team.persons.get(i).name)) {
                                tv.setText("출석");
                                totalAttend[i] += 1;
                            } else tv.setText("불참");
                            tv.setGravity(Gravity.CENTER);
                            tv.setPadding(10, 10, 10, 10);
                            tbr.addView(tv, new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));
                        }
                    }

                    TableRow totaltbr = new TableRow(FuncActivity.this);
                    tableLayout.addView(totaltbr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    TextView tvtotal = new TextView(FuncActivity.this);
                    tvtotal.setText("총합");
                    tvtotal.setGravity(Gravity.CENTER);
                    tvtotal.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
                    totaltbr.addView(tvtotal, new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));

                    for (int i = 0; i < totalAttend.length - 1; i++) {
                        TextView total = new TextView(FuncActivity.this);
                        total.setText(Integer.toString(totalAttend[i]));
                        total.setGravity(Gravity.CENTER);
                        total.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
                        totaltbr.addView(total, new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));
                    }

                    saveAttendToEval(totalAttend);

                } catch (Exception e) {
                    Toast.makeText(FuncActivity.this, "출석체크 내역이 없습니다.", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FuncActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void saveattendance() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Attendance/" + team.teamcode);

        AttendData attend = new AttendData();

        EditText attendanceDate = (EditText) findViewById(R.id.attendanceDate);

        attend.attendDate = attendanceDate.getText().toString();
        attend.yes = attendPersons;
        attend.no = NotattendPersons;

        Map<String, Object> attendValues = attend.toMap();
        String key = myRef.child("Attendance/" + team.teamcode).push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, attendValues);

        myRef.updateChildren(childUpdates);
    }

    private void saveAttendToEval(int[] totalAttend) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Evaluation/" + team.teamcode);

        for (int i = 0; i < team.persons.size(); i++) {
            Eval eval = new Eval();
            eval.name = team.persons.get(i).name;
            eval.number = team.persons.get(i).number;
            eval.attend = Double.toString(Math.round((double) totalAttend[i] / totalAttend[totalAttend.length - 1] * 100d) / 100d);

            Map<String, Object> evalValues = eval.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(team.persons.get(i).name + team.persons.get(i).number, evalValues);

            myRef.updateChildren(childUpdates);
        }
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

    private void addAttendCheckView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        LinearLayout attendcheck = (LinearLayout) findViewById(R.id.attendcheck);

        attendcheck.addView(inflater.inflate(R.layout.attendcheckview, attendcheck, false));

        addAttendPersonBtn();
    }

    private void addAttendPersonBtn() {
        final EditText attendanceDate = (EditText) findViewById(R.id.attendanceDate);
        final TextView attendInfo = (TextView) findViewById(R.id.attendInfo);

        attendPersons = new ArrayList<String>();
        NotattendPersons = new ArrayList<String>();

        for (int i = 0; i < team.persons.size(); i++) {
            Person p = team.persons.get(i);
            NotattendPersons.add(p.name);
        }

        final LinearLayout attendBtnlist = (LinearLayout) findViewById(R.id.attendPersonBtns);
        for (int i = 0; i < team.persons.size(); i++) {
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

                    } else {
                        attendPersons.remove(p.name);
                        NotattendPersons.add(p.name);
                        attendInfoText(attendInfo, attendanceDate);
                    }

                    //TODO: 새로운 날짜레이아웃 추가
                }
            });
        }
    }

    private void attendInfoText(TextView attendInfo, EditText attendanceDate) {
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
                        tableLayout.addView(tbr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                        TextView name = new TextView(FuncActivity.this);
                        TextView task = new TextView(FuncActivity.this);
                        TextView attend = new TextView(FuncActivity.this);
                        TextView res = new TextView(FuncActivity.this);

                        name.setText(eval.name);
                        name.setGravity(Gravity.CENTER);
                        name.setPadding(10, 10, 10, 10);
                        name.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
                        task.setText(eval.task);
                        task.setGravity(Gravity.RIGHT);
                        task.setPadding(10, 10, 10, 10);
                        attend.setText(Double.parseDouble(eval.attend) * 100 + "%");
                        attend.setGravity(Gravity.RIGHT);
                        attend.setPadding(10, 10, 10, 10);
                        res.setText(Math.round((Double.parseDouble(eval.task) + Double.parseDouble(eval.attend) * 5) * 100d) / 100d + "/10");
                        res.setGravity(Gravity.RIGHT);
                        res.setPadding(10, 10, 10, 10);

                        tbr.addView(name, new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        tbr.addView(task, new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        tbr.addView(attend, new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        tbr.addView(res, new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    }
                } catch (Exception e) {
                    Toast.makeText(FuncActivity.this, "평가 내역이 없습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FuncActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            }

        });
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
