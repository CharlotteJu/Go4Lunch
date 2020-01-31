package com.mancel.yann.go4lunch.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Yann MANCEL on 31/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.workers
 */
public abstract class WorkerController {

    // FIELDS --------------------------------------------------------------------------------------

    private static final String WORK_REQUEST_NAME = "WORK_REQUEST_NAME_Go4Lunch";
    private static final String WORK_REQUEST_TAG = "WORK_REQUEST_TAG_Go4Lunch";

    private static final int NOTIFICATION_HOUR = 19;
    private static final int NOTIFICATION_MINUTE = 25;
    private static final int NOTIFICATION_FREQUENCY_DAY = 1;

    public static final String DATA_UID_CURRENT_USER = "UID_CURRENT_USER";

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Starts the {@link WorkRequest} into {@link WorkManager}
     * @param context   a {@link Context}
     * @param uidOfUser a {@link String} that contains the Uid of user from Firebase Authentication
     */
    public static void startWorkRequestIntoWorkManager(@NonNull final Context context,
                                                       @Nullable final String uidOfUser) {
        // WorkRequest
        final PeriodicWorkRequest workRequest = configureAsyncWorker(uidOfUser);

        // Adds WorkRequest into WorkManager (Unique)
        WorkManager.getInstance(context)
                   .enqueueUniquePeriodicWork(WORK_REQUEST_NAME,
                                              ExistingPeriodicWorkPolicy.REPLACE,
                                              workRequest);
    }

    /**
     * Configures the {@link AlarmWorker}
     * @param uidOfUser a {@link String} that contains the Uid of user from Firebase Authentication
     * @return a {@link PeriodicWorkRequest}
     */
    private static PeriodicWorkRequest configureAlarmWorker(@Nullable final String uidOfUser) {
        // InitialDelay
        final Calendar calendar = Calendar.getInstance();

        final long nowTimeInMillis = calendar.getTimeInMillis();

        //  NOTIFICATION_HOUR = 12 & NOTIFICATION_MINUTE = 0
        //  if Now 12h01 or 13h -> + 1 day
        if (calendar.get(Calendar.HOUR_OF_DAY) > NOTIFICATION_HOUR ||
            (calendar.get(Calendar.HOUR_OF_DAY) == NOTIFICATION_HOUR && calendar.get(Calendar.MINUTE) + 1 >= NOTIFICATION_MINUTE)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendar.set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR);
        calendar.set(Calendar.MINUTE, NOTIFICATION_MINUTE);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        final long initialDelay = calendar.getTimeInMillis() - nowTimeInMillis;

        // Constraints
        final Constraints constraints = new Constraints.Builder()
                                                       .setRequiredNetworkType(NetworkType.CONNECTED)
                                                       .build();

        // Data
        final Data data = new Data.Builder()
                                  .putString(DATA_UID_CURRENT_USER, uidOfUser)
                                  .build();

        // PeriodicWorkRequest
        return new PeriodicWorkRequest.Builder(AlarmWorker.class,
                                               NOTIFICATION_FREQUENCY_DAY, TimeUnit.DAYS)
                                      .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                                      .setConstraints(constraints)
                                      .setInputData(data)
                                      .addTag(WORK_REQUEST_TAG)
                                      .setBackoffCriteria(BackoffPolicy.EXPONENTIAL,
                                                          PeriodicWorkRequest.MIN_BACKOFF_MILLIS,TimeUnit.MILLISECONDS)
                                      .build();
    }

    /**
     * Configures the {@link AsyncWorker}
     * @param uidOfUser a {@link String} that contains the Uid of user from Firebase Authentication
     * @return a {@link PeriodicWorkRequest}
     */
    private static PeriodicWorkRequest configureAsyncWorker(@Nullable final String uidOfUser) {
        // InitialDelay
        final Calendar calendar = Calendar.getInstance();

        final long nowTimeInMillis = calendar.getTimeInMillis();

        //  NOTIFICATION_HOUR = 12 & NOTIFICATION_MINUTE = 0
        //  if Now 12h01 or 13h -> + 1 day
        if (calendar.get(Calendar.HOUR_OF_DAY) > NOTIFICATION_HOUR ||
            (calendar.get(Calendar.HOUR_OF_DAY) == NOTIFICATION_HOUR && calendar.get(Calendar.MINUTE) + 1 >= NOTIFICATION_MINUTE)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendar.set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR);
        calendar.set(Calendar.MINUTE, NOTIFICATION_MINUTE);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        final long initialDelay = calendar.getTimeInMillis() - nowTimeInMillis;

        // Constraints
        final Constraints constraints = new Constraints.Builder()
                                                       .setRequiredNetworkType(NetworkType.CONNECTED)
                                                       .build();

        // Data
        final Data data = new Data.Builder()
                                  .putString(DATA_UID_CURRENT_USER, uidOfUser)
                                  .build();

        // PeriodicWorkRequest
        return new PeriodicWorkRequest.Builder(AsyncWorker.class,
                                               NOTIFICATION_FREQUENCY_DAY, TimeUnit.DAYS)
                                      .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                                      .setConstraints(constraints)
                                      .setInputData(data)
                                      .addTag(WORK_REQUEST_TAG)
                                      .setBackoffCriteria(BackoffPolicy.EXPONENTIAL,
                                                          PeriodicWorkRequest.MIN_BACKOFF_MILLIS,TimeUnit.MILLISECONDS)
                                      .build();
    }

    /**
     * Stops the {@link WorkRequest} into {@link WorkManager}
     * @param context a {@link Context}
     */
    public static void stopWorkRequestIntoWorkManager(@NonNull final Context context) {
        // Stops WorkRequest into WorkManager thanks to its Tag
        WorkManager.getInstance(context)
                   .cancelAllWorkByTag(WORK_REQUEST_TAG);
    }
}
