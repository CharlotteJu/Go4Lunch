package com.mancel.yann.go4lunch.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.utils.SaveUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 12/02/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.dialogs
 *
 * A class which implements {@link DialogFragment}.
 */
public class SettingsDialogFragment extends DialogFragment {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.dialog_settings_switch)
    Switch mSwitch;

    @Nullable
    private DialogListener mCallback;

    public static final String BUNDLE_SWITCH_NOTIFICATION = "BUNDLE_SWITCH_NOTIFICATION";

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
                              .inflate(R.layout.dialog_settings, null);

        // Using the ButterKnife library
        ButterKnife.bind(this, view);

        // SharedPreferences
        this.fetchDataFromSharedPreferences();

        return new AlertDialog.Builder(this.getActivity())
                              .setView(view)
                              .setTitle(this.getString(R.string.title_alertdialog_settings))
                              .setPositiveButton(this.getString(R.string.positive_button_alertdialog), (dialog, which) -> {
                                  // SharedPreferences
                                  SaveUtils.saveBooleanIntoSharedPreferences(this.getContext(),
                                                                             BUNDLE_SWITCH_NOTIFICATION,
                                                                             this.mSwitch.isChecked());

                                  if (this.mCallback != null) {
                                      this.mCallback.onClickOnPositiveButton(this.mSwitch.isChecked());
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
     * Gets a new instance of {@link SettingsDialogFragment}
     * @return a {@link SettingsDialogFragment}
     */
    @NonNull
    public static SettingsDialogFragment newInstance() {
        return new SettingsDialogFragment();
    }

    // -- SharedPreferences --

    /**
     * Fetches data from the SharedPreferences
     */
    private void fetchDataFromSharedPreferences() {
        this.mSwitch.setChecked(SaveUtils.loadBooleanFromSharedPreferences(this.getContext(),
                                                                           BUNDLE_SWITCH_NOTIFICATION));
    }
}
