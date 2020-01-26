package com.mancel.yann.go4lunch.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.views.activities.AuthActivity;

/**
 * Created by Yann MANCEL on 26/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.notifications
 *
 * A {@link FirebaseMessagingService} subclass.
 */
public class NotificationsService extends FirebaseMessagingService {

    // FIELDS --------------------------------------------------------------------------------------

    public static final int NOTIFICATION_ID = 2020;
    public static final String NOTIFICATION_CHANNEL = "com.mancel.yann.go4lunch.notifications.NotificationsService";

    // METHODS -------------------------------------------------------------------------------------

    // -- FirebaseMessagingService --

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            this.sendVisualNotification(remoteMessage.getNotification().getBody());
        }
    }

    // -- Notification --

    private void sendVisualNotification(final String messageBody) {
        // Intent & PendingIntent
        final Intent intent = new Intent(this.getApplicationContext(),
                                         AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        final PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(),
                                                                     0,
                                                                      intent,
                                                                      PendingIntent.FLAG_ONE_SHOT);

        // Style for the Notification
        final NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                                                                               .setBigContentTitle(this.getString(R.string.notification_content_text))
                                                                               .addLine(messageBody);

        // Notification Compat
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(),
                                                                                  NOTIFICATION_CHANNEL)
                                                                         .setSmallIcon(R.drawable.ic_go4lunch_white)
                                                                         .setContentTitle(this.getString(R.string.app_name))
                                                                         .setContentText(this.getString(R.string.notification_content_text))
                                                                         .setAutoCancel(true)
                                                                         .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                                                         .setContentIntent(pendingIntent)
                                                                         .setStyle(inboxStyle);

        // API level >= API 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL,
                                                                                    this.getString(R.string.notification_channel_name),
                                                                                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription(this.getString(R.string.notification_channel_description));

            final NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            // Notification Manager
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Notification Manager
        final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this.getApplicationContext());

        // Show notification
        notificationManagerCompat.notify(NOTIFICATION_ID,
                                         builder.build());
    }
}