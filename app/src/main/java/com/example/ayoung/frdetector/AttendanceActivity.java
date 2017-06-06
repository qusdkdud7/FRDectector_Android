package com.example.ayoung.frdetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AttendanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
    }

    public void onFeedClicked(View v){
        Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
        startActivity(intent);
    }
    public void onTaskClicked(View v){
        Intent intent = new Intent(getApplicationContext(),TaskActivity.class);
        startActivity(intent);
    }
    public void onAttendanceClicked(View v){
        Intent intent = new Intent(getApplicationContext(),AttendanceActivity.class);
        startActivity(intent);
    }
    public void onEvaluationClicked(View v){
        Intent intent = new Intent(getApplicationContext(),EvaluationResultActivity.class);
        startActivity(intent);
    }
    public void onLogoClicked(View v){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

}
