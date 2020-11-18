package com.sticker.kevin.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.ContextCompat;

 import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sticker.kevin.R;
import com.sticker.kevin.StickerPackListActivity;

public class FcmMessagingService extends FirebaseMessagingService {

    public FcmMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    if  (remoteMessage.getData().size()>0 && remoteMessage.getNotification()!=null){
        sendNotificacion(remoteMessage);
    }

    }

    private void sendNotificacion(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, StickerPackListActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSound  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificacionBuilder = new Notification.Builder(this)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
            ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary) ;
        }

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            String chanel = getString(R.string.normal_channel_id);
            String chanelName = getString(R.string.normal_channel_name);
            NotificationChannel channel = new NotificationChannel(chanel,chanelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100,200,200,50});

            if  (notificationManager!= null){
                notificationManager.createNotificationChannel(channel);
            }
            notificacionBuilder.setChannelId(chanel);
        }
        if  (notificationManager!= null){
            notificationManager.notify("",0,notificacionBuilder.build());
        }

    }

}
