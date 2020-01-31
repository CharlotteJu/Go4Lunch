package com.mancel.yann.go4lunch.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.mancel.yann.go4lunch.notifications.AlarmNotification;

/**
 * Created by Yann MANCEL on 30/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.workers
 *
 * A {@link Worker} subclass
 */
public class AlarmWorker extends Worker {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final Context mContext;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor
     * @param context       a {@link Context}
     * @param workerParams  a {@link WorkerParameters}
     */
    public AlarmWorker(@NonNull Context context,
                       @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.mContext = context;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- Worker --

    @NonNull
    @Override
    public Result doWork() {
        // Data (Input)
        final String uidCurrentUser = this.getInputData().getString(WorkerController.DATA_UID_CURRENT_USER);

        // No Uid of current user
        if (uidCurrentUser == null) {
            return Result.retry();
        }

        AlarmNotification.sendVisualNotification(this.mContext,
                                                "doWork (uid) " + uidCurrentUser);

        return Result.success();
    }
}
