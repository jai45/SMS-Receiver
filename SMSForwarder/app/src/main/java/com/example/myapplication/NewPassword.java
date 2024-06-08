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

public class NewPassword extends AppCompatActivity implements View.OnClickListener {

    Button submit;

    String userId;
    EditText codeField, passwordField, reEnterPasswordField;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userid");

        codeField = (EditText) findViewById(R.id.code);
        passwordField = (EditText) findViewById(R.id.newpassword);
        reEnterPasswordField = (EditText) findViewById(R.id.reenterPassword);

        submit = (Button) findViewById(R.id.passwordSubmit);

        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String code = codeField.getText().toString();
        String password = passwordField.getText().toString();
        String reEnterPassword = reEnterPasswordField.getText().toString();
        if (password.equals(reEnterPassword)) {
            Map<String, String> reqBody = new HashMap<>();
            reqBody.put("userid", userId);
            reqBody.put("code", code);
            reqBody.put("password", password);
            Map<String, String> response = new HashMap<>();
            try {
                HttpUtils apiCall = new HttpUtils("https://kfx26a7zfd.execute-api.us-east-1.amazonaws.com/dev/confirmforgotpassword", reqBody, new TaskListener() {
                    @Override
                    public void onTaskComplete(String responseMessage) {
                        Map<String,String> response = new Gson().fromJson(responseMessage, new TypeToken<HashMap<String, String>>(){}.getType());
                        if (response.get("statusCode").equals("200")) {
                            Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_SHORT).show();
                            Intent mainActivity = new Intent(NewPassword.this, MainActivity.class);
                            startActivity(mainActivity);
                        } else {
                            Toast.makeText(getApplicationContext(), "Password Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                apiCall.execute();
            } catch (Exception e) {
                Log.d("d", e.getMessage());
            }
        }
    }
}