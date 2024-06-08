package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class TaskChooser extends AppCompatActivity implements View.OnClickListener {

    Button submitButton;

    RadioGroup radioGroup;

    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_chooser);
        submitButton = (Button) findViewById(R.id.chooseButton);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if(selectedId==-1){
            Toast.makeText(getApplicationContext(),"Please select any option",Toast.LENGTH_SHORT).show();
            return;
        }
        radioButton = (RadioButton) findViewById(selectedId);
        String type = radioButton.getId()==R.id.senderButton?"sender":"receiver";
        SharedPreferences.Editor editor = getSharedPreferences("smsReceiver",MODE_PRIVATE).edit();
        editor.putString("type",type);
        editor.commit();
        Intent receivingIntent = new Intent(TaskChooser.this,ReceivingActivity.class);
        receivingIntent.putExtra("type",type);
        startActivity(receivingIntent);
    }

}