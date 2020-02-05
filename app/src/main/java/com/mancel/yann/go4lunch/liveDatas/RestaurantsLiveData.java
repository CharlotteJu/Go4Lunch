package com.mancel.yann.go4lunch.liveDatas;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.mancel.yann.go4lunch.models.Restaurant;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Yann MANCEL on 09/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.liveDatas
 *
 * A {@link LiveData} of {@link List<Restaurant>} subclass.
 */
public class RestaurantsLiveData extends LiveData<List<Restaurant>> {

    // FIELDS --------------------------------------------------------------------------------------

    @Nullable
    private Disposable mDisposable = null;

    private static final String TAG = RestaurantsLiveData.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor by default
      */
    public RestaurantsLiveData() {}

    // METHODS -------------------------------------------------------------------------------------

    // -- LiveData --

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();

        // Disposes the Disposable
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) {
            this.mDisposable.dispose();
        }
    }

    // -- Restaurants --

    /**
     * Gets the restaurants with {@link Observable} of {@link List<Restaurant>>}
     * @param observable an {@link Observable} of {@link List<Restaurant>>}
     */
    public void getRestaurantsWithObservable(@NonNull final Observable<List<Restaurant>> observable) {
        // Creates stream
        this.mDisposable = observable.subscribeWith(new DisposableObserver<List<Restaurant>>() {
            @Override
            public void onNext(List<Restaurant> restaurants) {
                // Notify
                setValue(restaurants);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                // Do nothing
            }
        });
    }
}
