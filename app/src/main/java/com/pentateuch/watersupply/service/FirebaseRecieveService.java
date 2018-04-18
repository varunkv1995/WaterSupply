package com.pentateuch.watersupply.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pentateuch.watersupply.activity.MainActivity;
import com.pentateuch.watersupply.utils.NotificationUtils;
import com.pentateuch.watersupply.utils.NotifyConfig;

public class FirebaseRecieveService extends FirebaseMessagingService {
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage == null)
            return;
        if (remoteMessage.getNotification() != null) {
            handleNotification(remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0) {
            handleData(remoteMessage.getData().toString());
        }
    }

    private void handleData(String message) {
        String imageUrl = "";
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(NotifyConfig.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext(), "message");
            notificationUtils.playNotificationSound();
        } else {
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("message", message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), "", message, "", resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), "", message, "", resultIntent, "");
            }
        }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent resultIntent) {
        notificationUtils = new NotificationUtils(context, "message");
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, resultIntent);
    }

    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent resultIntent, String imageUrl) {
        notificationUtils = new NotificationUtils(context, "message");
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, resultIntent, imageUrl);
    }

    private void handleNotification(String body) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(NotifyConfig.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", body);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext(), "message");
            notificationUtils.playNotificationSound();
        }
    }
}
