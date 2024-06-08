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

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    Button submit;

    EditText username;
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        submit = (Button) findViewById(R.id.submitButton);

        username = (EditText) findViewById(R.id.usernameText);

        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String userid = username.getText().toString();
        Map<String,String> reqBody = new HashMap<>();
        reqBody.put("userid",userid);
        Map<String,String> response = new HashMap<>();
        try {
            HttpUtils apiCall =new HttpUtils("https://kfx26a7zfd.execute-api.us-east-1.amazonaws.com/dev/forgotpassword", reqBody, new TaskListener() {
                @Override
                public void onTaskComplete(String responseMessage) {
                    Map<String,String> response = new Gson().fromJson(responseMessage, new TypeToken<HashMap<String, String>>(){}.getType());
                    if(response.get("statusCode").equals("200")){
                        Intent newPasswordActivity = new Intent(ForgotPasswordActivity.this,NewPassword.class);
                        newPasswordActivity.putExtra("userid",userid);
                        startActivity(newPasswordActivity);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Invalid Username",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            apiCall.execute();
        } catch (Exception e) {
            Log.d("d",e.getMessage());
        }
    }
}