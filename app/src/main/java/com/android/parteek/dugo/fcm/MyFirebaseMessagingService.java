package com.android.parteek.dugo.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.android.parteek.dugo.Home;
import com.android.parteek.dugo.R;
import com.android.parteek.dugo.Register;
import com.android.parteek.dugo.UserNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ishant on 05-Dec-16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */

    Intent intent;

    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage == null){
            Log.i("message", "Msg Not Rcvd");
            return;
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.i("message", remoteMessage.getNotification().getBody());
            Log.i("messageRecevied","Msg Rcvd");
            handleNotification(remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.i( "Data Payload: " ,remoteMessage.getData().toString());
            Log.i("size", String.valueOf(remoteMessage.getData().size()));

            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                Log.i("TRY","inTry");
                handleDataMessage(json);
            } catch (Exception e) {
                Log.i( "Exception: " , e.getMessage());
            }
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.i( "push json: " , json.toString());
        String message = null;
        try {
            message = json.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("message",message);
        if(message.contains("-")){

            try {



                String[] msgs = message.split("-");

                for(String s : msgs){
                    Log.i("TEST",s);
                }

                intent = new Intent(this, UserNotification.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                int seeker= Integer.parseInt(msgs[1]);
                int request= Integer.parseInt(msgs[2]);
                intent.putExtra("Seeker",seeker );
                intent.putExtra("Request",request );


                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Dugo")
                        .setContentText(msgs[0])
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

            } catch (Exception e) {
                Log.e( "Exception: " , e.getMessage());
            }


        }else{

            try {



                //String[] msgs = message.split("-");



                intent = new Intent(this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //int id= Integer.parseInt(msgs[1]);
               // intent.putExtra("message",id );

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Dugo")
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

            } catch (Exception e) {
                Log.e( "Exception: " , e.getMessage());
            }
        }


    }

    private void handleNotification(String message) {

        intent = new Intent(this, Register.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Dugo")
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
}
