package com.mancel.yann.go4lunch.views.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mancel.yann.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 21/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 *
 * A {@link RecyclerView.ViewHolder} subclass.
 */
class WorkmateViewHolder extends RecyclerView.ViewHolder {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.item_workmate_image)
    ImageView mImage;
    @BindView(R.id.item_workmate_text)
    TextView mText;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor
     * @param itemView a {@link View}
     */
    WorkmateViewHolder(@NonNull View itemView) {
        super(itemView);

        // Using the ButterKnife library
        ButterKnife.bind(this, itemView);
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- Layout --

    /**
     * Returns the layout value
     * @return an integer that contains the layout value
     */
    static int getLayout() {
        return R.layout.item_workmate;
    }

    // -- Update UI --

    /**
     * Updates the item
     */
    void updateWorkmate() {

    }
}
