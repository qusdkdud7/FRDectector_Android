package com.example.ayoung.frdetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.ayoung.frdetector.MainActivity.teamcode;
import static com.example.ayoung.frdetector.MainActivity.teams;


public class CreateTeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

    }

    public void onLogoClicked(View v){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void onCreateCheckClicked(View v){
        Toast.makeText(getApplicationContext(),"팀플코드는 " + teamcode + "입니다.", Toast.LENGTH_LONG).show();
    }

    public void onCreateDoneClicked(View v){


    }
}
