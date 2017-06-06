package com.example.ayoung.frdetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button)findViewById(R.id.testbutton);
        btn.setText("팀이름: ");
    }


    public void onLogoClicked(View v){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void onCreateTeamClicked(View v){
        Intent intent = new Intent(getApplicationContext(),CreateTeamActivity.class);
        startActivity(intent);
    }

    public void onJoinTeamClicked(View v){
        Intent intent = new Intent(getApplicationContext(),JoinTeamActivity.class);
        startActivity(intent);
    }


}
