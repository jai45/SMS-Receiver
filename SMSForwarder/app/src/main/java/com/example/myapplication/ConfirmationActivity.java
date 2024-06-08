package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class ConfirmationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText confirmationcode;

    Button submit;

    String userId;

    Map<String,String> response;

    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        confirmationcode = (EditText) findViewById(R.id.confirmation);
        submit = (Button) findViewById(R.id.reload);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userid");
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String code = confirmationcode.getText().toString();
        Map<String,String> reqBody = new HashMap<>();
        reqBody.put("userid",userId);
        reqBody.put("code",code);
        Map<String,String> response = new HashMap<>();
        try {
            HttpUtils apiCall =new HttpUtils("https://kfx26a7zfd.execute-api.us-east-1.amazonaws.com/dev/confirmation", reqBody, new TaskListener() {
                @Override
                public void onTaskComplete(String responseMessage) {
                    Map<String,String> response = new Gson().fromJson(responseMessage, new TypeToken<HashMap<String, String>>(){}.getType());
                    if(response.get("statusCode").equals("200")) {
                        Toast.makeText(getApplicationContext(), "Signup Success", Toast.LENGTH_SHORT).show();
                        Intent mainActivity = new Intent(ConfirmationActivity.this, MainActivity.class);
                        startActivity(mainActivity);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Signup Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
           apiCall.execute();

        } catch (Exception e) {
            Log.d("d",e.getMessage());
        }
    }
}