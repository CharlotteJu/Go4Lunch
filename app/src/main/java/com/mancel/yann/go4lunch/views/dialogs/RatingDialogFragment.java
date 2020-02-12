package com.mancel.yann.go4lunch.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.mancel.yann.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 12/02/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.dialogs
 *
 * A class which implements {@link DialogFragment}.
 */
public class RatingDialogFragment extends DialogFragment {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.dialog_rating_bar)
    RatingBar mRatingBar;

    @Nullable
    private DialogListener mCallback;

    // METHODS -------------------------------------------------------------------------------------

    // -- DialogFragment --

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Configures the callback to the parent activity
        try {
            this.mCallback = (DialogListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Creates the View thanks to the inflater
        final View view = this.getActivity()
                              .getLayoutInflater()
                              .inflate(R.layout.dialog_rating, null);

        // Using the ButterKnife library
        ButterKnife.bind(this, view);

        return new AlertDialog.Builder(this.getActivity())
                              .setView(view)
                              .setTitle(this.getString(R.string.title_alertdialog_details))
                              .setPositiveButton(this.getString(R.string.positive_button_alertdialog), (dialog, which) -> {
                                  if (this.mCallback != null) {
                                      this.mCallback.onClickOnPositiveButton(this.mRatingBar.getRating());
                                  }

                                  this.getDialog().cancel();
                              })
                              .setNegativeButton(this.getString(R.string.negative_button_alertdialog), (dialog, which) ->
                                  this.getDialog().cancel()
                              )
                              .create();
    }

    // -- Instances --

    /**
     * Gets a new instance of {@link RatingDialogFragment}
     * @return a {@link RatingDialogFragment}
     */
    @NonNull
    public static RatingDialogFragment newInstance() {
        return new RatingDialogFragment();
    }
}
