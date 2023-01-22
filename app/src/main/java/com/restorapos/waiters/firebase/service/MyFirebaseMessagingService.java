package com.restorapos.waiters.firebase.service;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;

import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.firebase.utils.NotificationUtils;
import com.restorapos.waiters.firebase.vo.NotificationVO;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgingService";
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String IMAGE = "image";
    private static final String ACTION = "subtitle";
    private static final String ACTION_DESTINATION = "action_destination";

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private static final String SHARED_PREF_NAME = "com.restorapos.waiters";

    @Override
    public void onNewToken(String Token) {
        super.onNewToken(Token);
        sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = sp.edit();
        editor.putString("TOKEN", Token);
        editor.apply();
        Log.wtf("NEW TOKEN", sp.getString("TOKEN", ""));
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Intent intent = new Intent("REALDATA");
        intent.putExtra("ACTION", remoteMessage.getData().get(ACTION));
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            handleData(data);

        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification());
        }// Check if message contains a notification payload.

    }

    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {
        String message = RemoteMsgNotification.getBody();
        String title = RemoteMsgNotification.getTitle();
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);

        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationSound();
    }

    private void handleData(Map<String, String> data) {

        String title = data.get(TITLE);
        String message = data.get(MESSAGE);
        String iconUrl = data.get(IMAGE);
        String action = data.get(ACTION);
        String actionDestination = data.get(ACTION_DESTINATION);
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);
        notificationVO.setIconUrl(iconUrl);
        notificationVO.setAction(action);
        notificationVO.setActionDestination(actionDestination);

        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

        resultIntent.putExtra("OVI", action);
        try {
            sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
            editor = sp.edit();
            editor.putString("OVI", action);
            editor.apply();
        } catch (Exception e) {
            Log.d(TAG, "handleData: " + e.getLocalizedMessage());
        }
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationSound();

    }
}
