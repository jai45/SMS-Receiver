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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username, emailField, passwordField;

    Button submit;

    Map<String,String> response;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        submit = (Button) findViewById(R.id.signupbutton);

        username = (EditText) findViewById(R.id.usernameSignup);
        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.passwordSignup);

        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String userid = username.getText().toString();
        String password = passwordField.getText().toString();
        String email = emailField.getText().toString();
        Map<String,String> reqBody = new HashMap<>();
        reqBody.put("userid",userid);
        reqBody.put("password",password);
        reqBody.put("email",email);
        Map<String,String> response = new HashMap<>();
        try {
            HttpUtils apiCall =new HttpUtils("https://kfx26a7zfd.execute-api.us-east-1.amazonaws.com/dev/signup", reqBody, new TaskListener() {
                @Override
                public void onTaskComplete(String responseMessage) {
                    Map<String,String> response = new Gson().fromJson(responseMessage, new TypeToken<HashMap<String, String>>(){}.getType());
                    if(response.get("statusCode").equals("200")){
                        Intent confirmationActivity = new Intent(SignUpActivity.this,ConfirmationActivity.class);
                        confirmationActivity.putExtra("userid",userid);
                        startActivity(confirmationActivity);
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