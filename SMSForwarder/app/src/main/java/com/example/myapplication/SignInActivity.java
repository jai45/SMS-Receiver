package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    Button signin, signup, forgot;

    EditText username, passwordField;

    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signin = (Button) findViewById(R.id.signinbutton);
        signup = (Button) findViewById(R.id.signupbutton);
        forgot = (Button) findViewById(R.id.forgotbutton);


        signin.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgot.setOnClickListener(this);

        username = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signinbutton:
                String userid = username.getText().toString();
                String password = passwordField.getText().toString();
                Map<String,String> reqBody = new HashMap<>();
                reqBody.put("userid",userid);
                reqBody.put("password",password);
                Map<String,String> response = new HashMap<>();
                try {
                    HttpUtils apiCall =new HttpUtils("https://kfx26a7zfd.execute-api.us-east-1.amazonaws.com/dev/signin", reqBody, new TaskListener() {
                        @Override
                        public void onTaskComplete(String responseMessage) {
                            Map<String,String> response = new Gson().fromJson(responseMessage, new TypeToken<HashMap<String, String>>(){}.getType());
                            if(response!=null && response.get("statusCode").equals("200")){
                                SharedPreferences.Editor editor = getSharedPreferences("smsReceiver",MODE_PRIVATE).edit();
                                editor.putString("user",userid);
                                editor.commit();
                                Intent taskChooserIntent = new Intent(SignInActivity.this, TaskChooser.class);
                                startActivity(taskChooserIntent);
                            }
                            else{
                                //Toast.makeText(getApplicationContext(),"Login Failed: ",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    apiCall.execute();
                } catch (Exception e) {
                    Log.d("d",e.getMessage());
                }
                break;
            case  R.id.signupbutton:
                Intent signupActivity = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(signupActivity);
                break;
            case R.id.forgotbutton:
                Intent forgotPasswordActivity = new Intent(SignInActivity.this,ForgotPasswordActivity.class);
                startActivity(forgotPasswordActivity);
                break;
        }
    }
}