package com.mancel.yann.go4lunch.views.bases;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.dagger.components.DaggerGo4LunchComponent;
import com.mancel.yann.go4lunch.dagger.components.Go4LunchComponent;
import com.mancel.yann.go4lunch.viewModels.Go4LunchViewModel;
import com.mancel.yann.go4lunch.viewModels.Go4LunchViewModelFactory;

import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 18/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.bases
 *
 * A {@link AppCompatActivity} subclass.
 */
public abstract class BaseActivity extends AppCompatActivity {

    // FIELDS --------------------------------------------------------------------------------------

    @SuppressWarnings("NullableProblems")
    @NonNull
    protected Go4LunchViewModel mViewModel;

    @SuppressWarnings("NullableProblems")
    @NonNull
    protected FirebaseAuth mFirebaseAuth;

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Gets the integer value of the activity layout
     * @return an integer that corresponds to the activity layout
     */
    protected abstract int getActivityLayout();

    /**
     * Gets the {@link Toolbar}
     * @return a {@link Toolbar}
     */
    @Nullable
    protected abstract Toolbar getToolbar();

    /**
     * Configures the design of each daughter class
     */
    protected abstract void configureDesign(@Nullable Bundle savedInstanceState);

    // -- Activity --

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associates the layout file to this class
        this.setContentView(this.getActivityLayout());

        // Using the ButterKnife library
        ButterKnife.bind(this);

        // Configure the Firebase authentication
        this.configureFirebaseAuth();

        // ViewModel
        this.configureViewModel();

        // Configures the design of the activity
        this.configureDesign(savedInstanceState);
    }

    // -- ViewModel --

    /**
     * Configures the {@link Go4LunchViewModel}
     */
    private void configureViewModel() {
        // Component
        final Go4LunchComponent component = DaggerGo4LunchComponent.create();

        // ViewModelFactory
        final Go4LunchViewModelFactory factory = component.getViewModelFactory();

        this.mViewModel = new ViewModelProvider(this, factory).get(Go4LunchViewModel.class);
    }

    // -- Toolbar --

    /**
     * Configures the {@link Toolbar} field
     */
    protected void configureToolBar() {
        // If ToolBar exists
        if (this.getToolbar() != null) {
            this.setSupportActionBar(this.getToolbar());
        }
    }

    /**
     * Configures the Up button of the {@link Toolbar}
     */
    protected void configureUpButtonOfToolBar() {
        // Gets a Support ActionBar corresponding to this ToolBar
        final ActionBar actionBar = this.getSupportActionBar();

        // Enables the Up Button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // -- Firebase --

    /**
     * Configures the {@link FirebaseAuth}
     */
    private void configureFirebaseAuth() {
        this.mFirebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Gets the current user of Firebase
     * @return a {@link FirebaseUser}
     */
    @Nullable
    protected FirebaseUser getCurrentUser() {
        return this.mFirebaseAuth.getCurrentUser();
    }

    /**
     * Signs out the current user of Firebase
     */
    protected void signOutCurrentUser() {
        final FirebaseUser user = this.getCurrentUser();

        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com or facebook.com)
                final String providerId = profile.getProviderId();

                if (providerId.equals("google.com")) {
                    // Google sign out
                    final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                                                           .requestIdToken(getString(R.string.default_web_client_id))
                                                                           .requestEmail()
                                                                           .build();

                    GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

                    googleSignInClient.signOut()
                                      .addOnCompleteListener(this,
                                                             (task) -> {
                                                                // Do nothing
                                                             });
                }

                if (providerId.equals("facebook.com")) {
                    // Facebook sign out
                    LoginManager.getInstance().logOut();
                }
            }

            // Firebase Sign Out
            this.mFirebaseAuth.signOut();
        }
    }

    /**
     * Deletes the current user account
     */
    protected void deleteCurrentUserAccount() {
        final FirebaseUser user = this.getCurrentUser();

        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com or facebook.com)
                String providerId = profile.getProviderId();

                if (providerId.equals("google.com")) {
                    // Disconnect Google account
                    final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                                                           .requestIdToken(getString(R.string.default_web_client_id))
                                                                           .requestEmail()
                                                                           .build();

                    GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

                    googleSignInClient.revokeAccess()
                                      .addOnCompleteListener(this,
                                                             (task) -> {
                                                                // Do nothing
                                                             });
                }

                if (providerId.equals("facebook.com")) {
                    LoginManager.getInstance().logOut();
                }
            }
        }

        this.getCurrentUser().delete()
                             .addOnCompleteListener((task) -> {
                                 if (task.isSuccessful()) {
                                     // Do nothing
                                 }
                             });
    }

    // -- Another Activities --

    /**
     * Starts another activity in generic way
     * @param classActivity a {@link Class}
     * @param <T> a parameter type
     */
    protected <T extends Activity> void startAnotherActivity(@NonNull final Class<T> classActivity) {
        final Intent intent = new Intent(this, classActivity);
        this.startActivity(intent);
    }

    // -- Phone --

    /**
     * Starts a phone call with phone number in argument
     * @param phoneNumber a {@link String} that contains the phone number to call
     */
    protected void startPhoneCall(@NonNull final String phoneNumber) {
        final Intent intent = new Intent(Intent.ACTION_DIAL);

        if (phoneNumber.contains(" ")) {
            final String newPhoneNumber = phoneNumber.replace(" ", "");

            intent.setData(Uri.parse("tel:" + newPhoneNumber));
        }
        else {
            intent.setData(Uri.parse("tel:" + phoneNumber));
        }

        this.startActivity(intent);
    }

    // -- Website --

    /**
     * Starts the opening of a website thanks to the URL in argument
     * @param url a {@link String} that contains the website URL
     */
    protected void startOpenWebsite(@NonNull final String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);

        // Starts just by www...
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            intent.setData(Uri.parse("https://" + url));
        }
        else {
            intent.setData(Uri.parse(url));
        }

        this.startActivity(intent);
    }
}
