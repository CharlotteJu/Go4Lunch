package com.mancel.yann.go4lunch.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Yann MANCEL on 21/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 *
 * A {@link RecyclerView.Adapter} subclass.
 */
public class LunchAdapter extends RecyclerView.Adapter<LunchViewHolder> {

    // METHODS -------------------------------------------------------------------------------------

    // -- Adapter --

    @NonNull
    @Override
    public LunchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Creates a Context to the LayoutInflater
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Creates the View thanks to the inflater
        View view = layoutInflater.inflate(LunchViewHolder.getLayout(), viewGroup, false);

        return new LunchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LunchViewHolder lunchViewHolder, int i) {
        lunchViewHolder.updateLunch();
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
