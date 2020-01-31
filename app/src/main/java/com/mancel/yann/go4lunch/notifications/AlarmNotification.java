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

import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.views.activities.AuthActivity;

/**
 * Created by Yann MANCEL on 31/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.notifications
 */
public abstract class AlarmNotification {

    // FIELDS --------------------------------------------------------------------------------------

    private static final int NOTIFICATION_ID = 2020;
    private static final String NOTIFICATION_CHANNEL = "com.mancel.yann.go4lunch.workers.AlarmWorker";

    // METHODS -------------------------------------------------------------------------------------

    // -- Notification --

    /**
     * Sends a notification with the message in argument
     * @param context       a {@link Context}
     * @param messageBody   a {@link String} that contains the message
     */
    public static void sendVisualNotification(@NonNull final Context context,
                                              @NonNull final String messageBody) {
        // Intent & PendingIntent
        final Intent intent = new Intent(context,
                                         AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        final PendingIntent pendingIntent = PendingIntent.getActivity(context,
                                                                     0,
                                                                      intent,
                                                                      PendingIntent.FLAG_ONE_SHOT);

        // Style for the Notification
        final NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                                                                               .setBigContentTitle(context.getString(R.string.notification_content_text))
                                                                               .addLine(messageBody);

        // Notification Compat
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                                                                                  NOTIFICATION_CHANNEL)
                                                                         .setSmallIcon(R.drawable.ic_go4lunch_white)
                                                                         .setContentTitle(context.getString(R.string.app_name))
                                                                         .setContentText(context.getString(R.string.notification_content_text))
                                                                         .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                                                         .setContentIntent(pendingIntent)
                                                                         .setAutoCancel(true)
                                                                         .setStyle(inboxStyle);

        // API level >= API 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL,
                                                                                    context.getString(R.string.notification_channel_name),
                                                                                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription(context.getString(R.string.notification_channel_description));

            final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Notification Manager
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Notification Manager Compat
        final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        // Shows notification
        notificationManagerCompat.notify(NOTIFICATION_ID,
                                         builder.build());
    }
}
