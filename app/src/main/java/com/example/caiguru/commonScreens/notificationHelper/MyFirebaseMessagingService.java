package com.example.caiguru.commonScreens.notificationHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService

{

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("Message: ", remoteMessage.getData().toString());
        NotificationHelper helper  = new NotificationHelper(this);
        helper.createNotification(remoteMessage.getData());
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        sendRegistrationToServer(token);
        Log.d(TAG, "Refreshed token: "+token);
    }

    private void sendRegistrationToServer(String token) {
        SharedPreferences.Editor editor = this.getApplicationContext().getSharedPreferences("yogeshData", Context.MODE_PRIVATE).edit();
        editor.putString("deviceId", token);
        editor.apply();
    }
}