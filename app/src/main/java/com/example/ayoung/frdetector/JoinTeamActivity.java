package com.example.ayoung.frdetector;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.ayoung.frdetector.MainActivity.teams;

public class JoinTeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);

    }

    public void onLogoClicked(View v){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    //TODO: 팀플정보 입력 실패 시 다시 돌아가기, 정보 입력 완료 시 팀플 참여하도록
    public void onJoinDoneClicked(View v){
        EditText teamcode = (EditText)findViewById(R.id.create_teamname);
        EditText name = (EditText)findViewById(R.id.create_name);
        EditText number = (EditText)findViewById(R.id.create_number);

        String code = teamcode.getText().toString();
        Person p = new Person(false, name.getText().toString(), number.getText().toString());
//TODO 대화상자
        int tmp=0; int index=0;
        for(int i=0;i<teams.size();i++){
            if(teams.get(i).teamcode == code){
                tmp = 1;
                index = i;
                break;
            }
        }

        if(tmp == 1){
            //팀플고유번호가 존재할 때

        }

        else{
            //팀플고유번호 존재하지 않을 때

        }


    }
}
