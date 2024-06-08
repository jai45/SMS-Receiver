package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class SMSReceiver extends BroadcastReceiver {
    private final String API = "https://j8yxuilf6d.execute-api.us-east-1.amazonaws.com/prod/postsms";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("sms", "Intent recieved: " + intent.getAction());

        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                String type = context.getSharedPreferences("smsReceiver",MODE_PRIVATE).getString("type", null);
                if (type!=null && type.equals("sender") && messages.length > -1) {
                    Map<String,String> data = new HashMap<>();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String userId = context.getSharedPreferences("smsReceiver",MODE_PRIVATE).getString("user", null);
                    data.put("username",userId);
                    data.put("timestamp", String.valueOf(timestamp.getTime()));
                    data.put("sender",messages[0].getOriginatingAddress());
                    data.put("text",messages[0].getMessageBody());
                    try {
                    HttpUtils apiCall = new HttpUtils(API, data, new TaskListener() {
                        @Override
                        public void onTaskComplete(String result) {
                            Log.i("resp2",result.toString());
                        }
                    });
                    apiCall.execute();
                    } catch (Exception e) {
                        Log.i("resp",e.getMessage());
                    }

                }
            }
        }
    }
}
