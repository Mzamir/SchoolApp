package com.example.mahmoudsamir.schoolappand.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.parent_flow.pick_up.view.ParentPickUpActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.mahmoudsamir.schoolappand.MyApplication.getMyApplicationContext;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.CHANEL_ID;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.NOTIFICATION_ID;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.NOTIFICATION_MESSAGE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.NOTIFICATION_TITLE;

public class LocalNoificationCreator {

    public static NotificationManager notificationManager = (NotificationManager)
            getMyApplicationContext().getSystemService(NOTIFICATION_SERVICE);

    public static void notifyParrentArrived() {
        Intent intent = new Intent(getMyApplicationContext(), ParentPickUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // See https://developer.android.com/training/notify-user/navigation for better navigation
        PendingIntent pendingIntent = PendingIntent.getActivity(getMyApplicationContext(), 0, intent, 0);

        // Setup Ringtone & Vibrate
        Uri alarmSound = Settings.System.DEFAULT_NOTIFICATION_URI;
        long[] vibratePattern = {0, 100, 200, 300};
        // Setup Notification

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getMyApplicationContext(), CHANEL_ID)
                .setContentText(NOTIFICATION_MESSAGE)
                .setContentTitle(NOTIFICATION_TITLE)
                .setSmallIcon(R.mipmap.logo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound, AudioManager.STREAM_NOTIFICATION)
                .setOnlyAlertOnce(true)
                .setVibrate(vibratePattern)
                .setAutoCancel(true);
        // Send Notification
        NotificationManager manager = (NotificationManager) getMyApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
