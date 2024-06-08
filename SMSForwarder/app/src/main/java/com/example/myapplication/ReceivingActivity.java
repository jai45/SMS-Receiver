package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceivingActivity extends AppCompatActivity {
    private String API = "https://j8yxuilf6d.execute-api.us-east-1.amazonaws.com/prod/getsms";
    private Handler handler = new Handler(Looper.getMainLooper());
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    String userId;
    Gson gson = new Gson();
    Button reloadButton,logoutButton;
    List<Message> messages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        reloadButton = (Button) findViewById(R.id.reload);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadData();
            }
        });

        logoutButton = (Button) findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        //Log.i("type",type);
        if("sender".equals(type)){

        }
        ReceivingActivity obj = this;
        userId = getSharedPreferences("smsReceiver",MODE_PRIVATE).getString("user", "jai");
        String messagesString = getSharedPreferences("smsReceiver",MODE_PRIVATE).getString("messages", null);
        if(messagesString!=null){
            Type typeToken = new TypeToken<List<Message>>(){}.getType();
            messages = gson.fromJson(messagesString, typeToken);
        }
        adapter = new ListAdapter(ReceivingActivity.this, messages, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("click", "clicked position:" + position);
                onListItemClick(v,position);
            }
        });
        recyclerView.setAdapter(adapter);
        if(messagesString==null){
            reloadData();
        }

    }


    protected void onListItemClick(View v, int position) {
        Message message = (Message) messages.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(ReceivingActivity.this);
        builder.setMessage(message.getText());
        builder.setTitle(message.getSender()+"      "+ message.getTimestamp());
        builder.setCancelable(true);
        builder.setNegativeButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void logout(){
        messages = null;
        getSharedPreferences("smsReceiver",MODE_PRIVATE).edit().clear().commit();
        Intent mainActivityIntent = new Intent(ReceivingActivity.this,MainActivity.class);
        startActivity(mainActivityIntent);
    }

    public void reloadData(){
        try {
            Map<String,String> data = new HashMap<>();
            data.put("username",userId);
            HttpUtils apiCall = new HttpUtils(API, data, new TaskListener() {
                @Override
                public void onTaskComplete(String result) {
                    Map<String,String> response = new Gson().fromJson(result, new TypeToken<HashMap<String, Object>>(){}.getType());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("res",result.toString());
                            if(response.get("statusCode").equals("200")){
                                String jsonArray = response.get("body");
                                Type listType = new TypeToken<ArrayList<Message>>(){}.getType();
                                messages = new Gson().fromJson(jsonArray, listType);
                                Collections.sort(messages, new Comparator<Message>() {
                                    @Override
                                    public int compare(Message o1, Message o2) {
                                        long timestamp1 = Long.parseLong(o1.getTimestamp());
                                        long timestamp2 = Long.parseLong(o2.getTimestamp());
                                        if(timestamp2>timestamp1)
                                            return 1;
                                        else if(timestamp2 == timestamp1)
                                            return 0;
                                        return -1;
                                    }
                                });
                                SimpleDateFormat sf = new SimpleDateFormat("YY-MM-dd HH:mm:ss");
                                messages.stream().forEach(message -> message.setTimestamp(String.valueOf(sf.format(new Date(Long.parseLong(message.getTimestamp()))))));
                                SharedPreferences.Editor editor = getSharedPreferences("smsReceiver",MODE_PRIVATE).edit();
                                String json = gson.toJson(messages);
                                editor.putString("messages", json);
                                editor.commit();
                                ((ListAdapter) adapter).setMessages(messages);
                                adapter.notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"SMS Retrieval Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            apiCall.execute();
        } catch (Exception e) {
            Log.i("resp",e.getMessage());
        }
    }

}