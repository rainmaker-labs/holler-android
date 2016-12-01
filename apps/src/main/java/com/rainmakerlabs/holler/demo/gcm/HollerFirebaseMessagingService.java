package com.rainmakerlabs.holler.demo.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rainmakerlabs.holler.demo.HollerApplication;
import com.rainmakerlabs.holler.demo.NotificationActivity;
import com.rainmakerlabs.holler.demo.R;

import static java.security.AccessController.getContext;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class HollerFirebaseMessagingService extends FirebaseMessagingService{

    public static final String INTENT_NOTIFICATION = "com.rainmakerlabs.holler.demo.UPDATE_NOTIFICATION";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String message = "";
        try {
            message = remoteMessage.getData().get("body");
        } catch (Exception e) {
            e.printStackTrace();
            message = "Notification Message";
        }
        if(HollerApplication.isAppIsInBackground(getApplicationContext())){
            sendNotification(getApplicationContext(),message);
        }else{
            sendNotificationForceground(getApplicationContext(),message,"key");
        }

        logRemoteMessage(remoteMessage);
    }

    private void logRemoteMessage(RemoteMessage remoteMessage){
        for (String s : remoteMessage.getData().keySet()) {
            Log.w(this.getClass().getName(), s + ":" + remoteMessage.getData().get(s));
        }
    }

    //simple navigation for demo only
    private void sendNotification(Context context, String message){
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

        public static void sendNotificationForceground(Context context, String message, String key) {

            Intent intent = new Intent();
            intent.putExtra("msg", message);
            intent.putExtra("key", key);
            intent.setAction(INTENT_NOTIFICATION);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
}
