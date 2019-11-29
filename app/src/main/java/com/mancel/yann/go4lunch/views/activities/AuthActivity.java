package com.mancel.yann.go4lunch.views.activities;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.utils.BlurTransformation;
import com.mancel.yann.go4lunch.utils.ShowMessage;
import com.mancel.yann.go4lunch.views.bases.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Yann MANCEL on 27/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.activities
 *
 * A {@link BaseActivity} subclass.
 */
public class AuthActivity extends BaseActivity {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.activity_auth_CoordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_auth_image_background)
    ImageView mBackgroundImage;

    //private FirebaseAuth mFirebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;

    public static final int RC_GOOGLE_SIGN_IN = 100;

    private static final String TAG = AuthActivity.class.getSimpleName();

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseActivity --

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_auth;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return null;
    }

    @Override
    protected void configureDesign() {
        this.configureBackgroundImage();
        this.configureGoogleSignIn();
    }

    // -- Activity --

    @Override
    protected void onStart() {
        super.onStart();

        // Checks if user is signed in (non-null)
        this.manageFirebaseUser(this.mFirebaseAuth);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            this.handleSignInResultWithGoogle(data);
        }
    }

    // -- Actions --

    @OnClick({R.id.activity_auth_Google_Button,
              R.id.activity_auth_Facebook_Button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // Google
            case R.id.activity_auth_Google_Button:
                this.signInWithGoogle();
                break;

            // Facebook
            case R.id.activity_auth_Facebook_Button:
                this.signOutCurrentUser();
                break;
        }
    }

    // -- UI --

    /**
     * Configures the {@link ImageView} in background
     */
    private void configureBackgroundImage() {
        // Using to Glide library
        Glide.with(this)
             .load(R.drawable.background_image)
             .transform(new MultiTransformation<>(new CenterCrop(),
                                                  new BlurTransformation(this)))
             .error(R.drawable.ic_close)
             .into(this.mBackgroundImage);
    }

    // -- Firebase --

    /**
     * Manages users of Firebase
     * @param firebaseAuth a {@link FirebaseAuth}
     */
    private void manageFirebaseUser(final FirebaseAuth firebaseAuth) {
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            // User is signed in
            ShowMessage.showMessageWithSnackbar(this.mCoordinatorLayout,
                    "User is signed in");
        }
        else {
            // No user is signed in
            ShowMessage.showMessageWithSnackbar(this.mCoordinatorLayout,
                    "No user is signed in");
        }
    }

    /**
     * Signs out the current user of Firebase
     */
    private void signOutCurrentUser() {
        ShowMessage.showMessageWithSnackbar(this.mCoordinatorLayout,
                "sign out");

        this.mFirebaseAuth.signOut();
    }

    /**
     * Deletes the current user account
     */
    private void deleteCurrentUserAccount() {
        Log.d(TAG, "delete account");
        this.mFirebaseAuth.getCurrentUser()
                          .delete()
                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if (task.isSuccessful()) {
                                      Log.d(TAG, "User account deleted.");
                                  }
                              }
                          });
    }

    // -- Google Sign In --

    /**
     * Configures the Google {@link SignInButton}
     */
    private void configureGoogleSignIn() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                                         .requestIdToken(getString(R.string.default_web_client_id))
                                                         .requestEmail()
                                                         .build();

        this.mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    /**
     * Signs in with Google
     */
    private void signInWithGoogle() {
        final Intent signInIntent = this.mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    /**
     * Handles the Google Sign In result
     * @param data an {@link Intent}
     */
    private void handleSignInResultWithGoogle(@Nullable Intent data) {
        final Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

        try {
            final GoogleSignInAccount account = task.getResult(ApiException.class);
            this.retrieveFirebaseAuthWithGoogle(account);
        }
        catch (ApiException e) {
            // Google Sign In failed
            ShowMessage.showMessageWithSnackbar(this.mCoordinatorLayout,
                                                getString(R.string.google_sign_in_failed));
        }
    }

    /**
     * Retrieves the credential og Google authentication
     * @param account a {@link GoogleSignInAccount}
     */
    private void retrieveFirebaseAuthWithGoogle(GoogleSignInAccount account) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        this.mFirebaseAuth.signInWithCredential(credential)
                          .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                              @Override
                              public void onComplete(@NonNull Task<AuthResult> task) {
                                  if (task.isSuccessful()) {

                                      // Google Sign In was successful
                                      ShowMessage.showMessageWithSnackbar(mCoordinatorLayout,
                                              getString(R.string.google_sign_in_success));

                                      // Sign in success, update UI with the signed-in user's information
                                      Log.d(TAG, "signInWithCredential:success");
                                      //FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                      //updateUI(user);
                                  } else {
                                      // If sign in fails, display a message to the user.
                                      Log.w(TAG, "signInWithCredential:failure", task.getException());
                                      //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                                      //updateUI(null);
                                  }
                              }
                          });
    }
}
