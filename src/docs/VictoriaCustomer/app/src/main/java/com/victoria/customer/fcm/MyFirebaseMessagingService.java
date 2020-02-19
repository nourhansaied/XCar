package com.victoria.customer.fcm;


import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.victoria.customer.R;
import com.victoria.customer.core.Common;
import com.victoria.customer.data.pojo.Notification;
import com.victoria.customer.ui.home.activity.HomeActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {

        }
        if (remoteMessage.getData() != null) {
            Notification notification = new Notification();
            notification.setMessage(remoteMessage.getData().get(Common.BODY));
            notification.setTag(remoteMessage.getData().get(Common.TAG));
            notification.setTitle(remoteMessage.getData().get(Common.TITLE));
            if (remoteMessage.getData() != null && remoteMessage.getData().get("channel_sid") != null) {
                if (isAppIsInBackground(getApplicationContext())) {
                    notification = new Notification();
                    notification.setTag(Common.PUSH_TAG_TWILLIO);
                    notification.setMessage(remoteMessage.getData().get("twi_body"));
                    sendNotification(notification, Common.PUSH_TAG_TWILLIO);
                } else {
                    notification = new Notification();
                    notification.setTag(Common.PUSH_TAG_TWILLIO);
                    notification.setMessage(remoteMessage.getData().get("twi_body"));
                    EventBus.getDefault().post(notification);
                }
            } else if (notification.getTag().equals(Common.PUSH_TAG_DECLINE_ORDER)) {
                if (isAppIsInBackground(getApplicationContext())) {
                    sendNotification(notification, Common.PUSH_TAG_DECLINE_ORDER);
                } else {
                    EventBus.getDefault().post(notification);
                }
            } else if (notification.getTag().equals(Common.PUSH_TAG_ACCEPT_ORDER)) {
                if (isAppIsInBackground(getApplicationContext())) {
                    sendNotification(notification, Common.PUSH_TAG_ACCEPT_ORDER);
                } else {
                    EventBus.getDefault().post(notification);
                }
            } else if (notification.getTag().equals(Common.PUSH_TAG_REFER_USER)) {
                sendNotification(notification, Common.PUSH_TAG_REFER_USER);
            } else if (notification.getTag().equals(Common.PUSH_TAG_ARRIVED_ORDER)) {
                if (isAppIsInBackground(getApplicationContext())) {
                    sendNotification(notification, Common.PUSH_TAG_ARRIVED_ORDER);
                } else {
                    //EventBus.getDefault().post(notification);
                }
            } else if (notification.getTag().equals(Common.PUSH_TAG_START_ORDER)) {
                if (isAppIsInBackground(getApplicationContext())) {
                    sendNotification(notification, Common.PUSH_TAG_START_ORDER);
                } else {
                    //EventBus.getDefault().post(notification);
                }
            } else if (notification.getTag().equals(Common.PUSH_TAG_CHANGE_DROP_OFF_ORDER)) {
                if (isAppIsInBackground(getApplicationContext())) {
                    sendNotification(notification, Common.PUSH_TAG_CHANGE_DROP_OFF_ORDER);
                } else {
                    //EventBus.getDefault().post(notification);
                }
            } else if (notification.getTag().equals(Common.PUSH_TAG_COMPLETE_ORDER)) {
                if (isAppIsInBackground(getApplicationContext())) {
                    sendNotification(notification, Common.PUSH_TAG_COMPLETE_ORDER);
                } else {
                    EventBus.getDefault().post(notification);
                }
            } else if (notification.getTag().equals(Common.PUSH_TAG_CANCEL_LATE_TRIP)) {
                if (isAppIsInBackground(getApplicationContext())) {
                    sendNotification(notification, Common.PUSH_TAG_COMPLETE_ORDER);
                } else {
                    EventBus.getDefault().post(notification);
                }
            } else if (notification.getTag().equals(Common.PUSH_TAG_CANCEL_TRIP)) {
                if (isAppIsInBackground(getApplicationContext())) {
                    sendNotification(notification, Common.PUSH_TAG_CANCEL_TRIP);
                } else {
                    EventBus.getDefault().post(notification);
                }
            }

            /*if (notification.getTag().equals(Common.MORNING_START_TRIP)) {
                sendNotification(notification, Common.MY_ROUTE);
            } else if (notification.getTag().equals(Common.EVENING_BUS_LEFT_SCHOOL)) {
                sendNotification(notification, Common.MY_ROUTE);
            } else if (notification.getTag().equals(Common.ACCEPT_REQ_PUSH)) {
                notification.setMessage(remoteMessage.getData().get(Common.MESSAGE));
                sendNotification(notification, Common.ACCEPT_REQ_PUSH);
            } else {
                sendNotification(notification, "");
            }*/
            //sendNotification(notification, "");
        }
    }

    @Override
    public void onNewToken(String token) {
        // sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {

        // TODO: Implement this method to send token to your app server.
    }


    private void sendNotification(Notification notification, String flag) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /*intent.putExtra(Common.ACTIVITY_FIRST_PAGE, notification.getTag());

        if (!flag.equals("")) {
            intent.putExtra(Common.ACTIVITY_FIRST_PAGE, flag);
        }*/
        if (flag.equals(Common.PUSH_TAG_TWILLIO)) {
            intent.putExtra(Common.ACTIVITY_FIRST_PAGE, flag);
        } else if (flag.equals(Common.PUSH_TAG_REFER_USER)) {
            intent.putExtra(Common.ACTIVITY_FIRST_PAGE, flag);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        /*Uri defaultSoundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() +
                "/" + R.raw.notification_sound);*/

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_app_logo)
                        .setContentTitle(notification.getTitle())
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notification.getMessage()))
                        .setContentText(notification.getMessage())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(defaultSoundUri, attributes);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(0, notificationBuilder.build());
        }
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}