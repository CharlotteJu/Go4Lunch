package com.mancel.yann.go4lunch.views.activities;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.repositories.MessageRepositoryImpl;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;
import com.mancel.yann.go4lunch.repositories.UserRepositoryImpl;
import com.mancel.yann.go4lunch.viewModels.GoogleMapsAndFirestoreViewModel;
import com.mancel.yann.go4lunch.viewModels.GoogleMapsAndFirestoreViewModelFactory;
import com.mancel.yann.go4lunch.views.adapters.AdapterListener;
import com.mancel.yann.go4lunch.views.adapters.MessageAdapter;
import com.mancel.yann.go4lunch.views.bases.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Yann MANCEL on 27/21/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.activities
 *
 * A {@link BaseActivity} subclass.
 */
public class ChatActivity extends BaseActivity implements AdapterListener {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.activity_chat_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_chat_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.activity_chat_no_message)
    TextView mNoMessage;
    @BindView(R.id.activity_chat_TextInputLayout)
    TextInputLayout mTextInputLayout;
    @BindView(R.id.activity_chat_TextInputEditText)
    TextInputEditText mTextInputEditText;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private MessageAdapter mAdapter;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private GoogleMapsAndFirestoreViewModel mViewModel;

    private static final String TAG = ChatActivity.class.getSimpleName();

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseActivity --

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_chat;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return this.mToolbar;
    }

    @Override
    protected void configureDesign() {
        // UI
        this.configureToolBar();
        this.configureUpButtonOfToolBar();
        this.configureRecyclerView();

        // ViewModel
        this.configureViewModel();

        // LiveData
        this.configureMessagesLiveData();
    }

    // -- AdapterListener --

    @Override
    public void onDataChanged() {
        this.mNoMessage.setVisibility( (this.mAdapter.getItemCount() == 0) ? View.VISIBLE :
                                                                             View.GONE);
    }

    // -- Actions --

    @OnClick({R.id.activity_chat_send_button,
              R.id.activity_chat_add_button})
    public void onFABClicked(@NonNull final View view) {
        // According to the View's Id
        switch (view.getId()) {

            // Send message
            case R.id.activity_chat_send_button:
                this.sendMessage();
                break;

            // Other
            case R.id.activity_chat_add_button:
                // Do nothing
        }
    }

    // -- RecyclerView --

    /**
     * Configures the {@link RecyclerView}
     */
    private void configureRecyclerView() {
        // Adapter
        this.mAdapter = new MessageAdapter(this,
                                            Glide.with(this));

        // RecyclerView
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
    }

    // -- GoogleMapsAndFirestoreViewModel --

    /**
     * Configures the {@link GoogleMapsAndFirestoreViewModel}
     */
    private void configureViewModel() {
        // TODO: 27/01/2020 Repositories must be removed thanks to Dagger 2
        final GoogleMapsAndFirestoreViewModelFactory factory = new GoogleMapsAndFirestoreViewModelFactory(new UserRepositoryImpl(),
                                                                                                          new MessageRepositoryImpl(),
                                                                                                          new PlaceRepositoryImpl());

        this.mViewModel = ViewModelProviders.of(this, factory)
                                            .get(GoogleMapsAndFirestoreViewModel.class);
    }

    /**
     * Configures the {@link LiveData} of {@link List<com.mancel.yann.go4lunch.models.Message>}
     */
    private void configureMessagesLiveData() {
        // Bind between liveData of ViewModel and the Adapter of RecyclerView
        this.mViewModel.getMessages()
                       .observe(this,
                                 messages -> this.mAdapter.updateData(messages));
    }

    // -- Message --

    /**
     * Sends the {@link com.mancel.yann.go4lunch.models.Message} into Firebase Firestore
     */
    private void sendMessage() {
        // Fetches the data of current user
        try {
            this.mViewModel.getUser(this.getCurrentUser())
                           .addOnSuccessListener( documentSnapshot -> {
                               final User user = documentSnapshot.toObject(User.class);

                               if (user != null) {
                                   // TODO: 27/01/2020 create message
                                   //this.mViewModel.createMessage();
                               }
                           });
        }
        catch (Exception e) {
            Log.e(TAG, "getUser: " + e.getMessage());
        }
    }
}
