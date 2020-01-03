package com.mancel.yann.go4lunch.views.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.mancel.yann.go4lunch.models.Details;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yann MANCEL on 21/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 *
 * A {@link RecyclerView.Adapter} subclass.
 */
public class LunchAdapter extends RecyclerView.Adapter<LunchViewHolder> {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final AdapterListener mCallback;

    @NonNull
    private final RequestManager mGlide;

    @NonNull
    private List<Details> mDetailsList;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor by default
     */
    public LunchAdapter(@NonNull final AdapterListener callback,
                        @NonNull final RequestManager glide) {
        this.mCallback = callback;
        this.mGlide = glide;
        this.mDetailsList = new ArrayList<>();
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- RecyclerView.Adapter --

    @NonNull
    @Override
    public LunchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Creates a Context to the LayoutInflater
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Creates the View thanks to the inflater
        View view = layoutInflater.inflate(LunchViewHolder.getLayout(), viewGroup, false);

        return new LunchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LunchViewHolder lunchViewHolder, int position) {
        lunchViewHolder.updateLunch(this.mDetailsList.get(position), this.mGlide);
    }

    @Override
    public int getItemCount() {
        return this.mDetailsList.size();
    }

    // -- Details --

    /**
     * Updates the data thanks to the {@link List<Details>} in argument
     * @param newDetailsList a {@link List<Details>} that contains the new data
     */
    public void updateData(@NonNull final List<Details> newDetailsList) {
        this.mDetailsList = newDetailsList;
        this.notifyDataSetChanged();

        // Callback to update UI
        this.mCallback.onDataChanged();
    }
}
