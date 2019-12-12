package com.mancel.yann.go4lunch.viewModels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

/**
 * Created by Yann MANCEL on 10/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.viewModels
 *
 * A {@link ViewModel} subclass.
 */
public class MapViewModel extends ViewModel {

    // FIELDS --------------------------------------------------------------------------------------

    private static final String TAG = MapViewModel.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor by default
     */
    public MapViewModel() {
        Log.d(TAG, "MapViewModel: Init");
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- ViewModel --

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: Finish");
    }
}
