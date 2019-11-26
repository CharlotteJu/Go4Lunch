package com.mancel.yann.go4lunch.views.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateViewHolder> {

    // METHODS -------------------------------------------------------------------------------------

    // -- Adapter --

    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Creates a Context to the LayoutInflater
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Creates the View thanks to the inflater
        View view = layoutInflater.inflate(WorkmateViewHolder.getLayout(), viewGroup, false);

        return new WorkmateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateViewHolder workmateViewHolder, int i) {
        workmateViewHolder.updateWorkmate();
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
