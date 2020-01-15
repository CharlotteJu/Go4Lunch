package com.mancel.yann.go4lunch.liveDatas;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.mancel.yann.go4lunch.models.NearbySearch;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Yann MANCEL on 15/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.liveDatas
 *
 * A {@link LiveData<NearbySearch>} subclass.
 */
public class NearbySearchLiveData extends LiveData<NearbySearch> {

    // FIELDS --------------------------------------------------------------------------------------

    @Nullable
    private Disposable mDisposable = null;

    private static final String TAG = NearbySearchLiveData.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor by default
     */
    public NearbySearchLiveData() {}

    // METHODS -------------------------------------------------------------------------------------

    // -- LiveData --

    @Override
    protected void onActive() {
        super.onActive();
        Log.d(TAG, "onActive");
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d(TAG, "onInactive");

        // Disposes the Disposable
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) {
            this.mDisposable.dispose();
        }
    }

    // -- NearbySearch --

    /**
     * Gets the nearby search with {@link Observable<NearbySearch>}
     * @param observable an {@link Observable<NearbySearch>}
     */
    public void getNearbySearchWithObservable(@NonNull final Observable<NearbySearch> observable) {
        // Creates stream
        this.mDisposable = observable.subscribeWith(new DisposableObserver<NearbySearch>() {
            @Override
            public void onNext(NearbySearch nearbySearch) {
                Log.d(TAG, "onNext");

                // Notify
                setValue(nearbySearch);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });
    }
}
