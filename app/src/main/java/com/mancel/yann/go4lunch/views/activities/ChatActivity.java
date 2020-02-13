package com.mancel.yann.go4lunch.views.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.android.material.textfield.TextInputLayout;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.Message;
import com.mancel.yann.go4lunch.models.User;
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

    @BindView(R.id.activity_chat_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.activity_chat_no_message)
    TextView mNoMessage;
    @BindView(R.id.activity_chat_TextInputLayout)
    TextInputLayout mTextInputLayout;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private MessageAdapter mAdapter;

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseActivity --

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_chat;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return null;
    }

    @Override
    protected void configureDesign(@Nullable Bundle savedInstanceState) {
        // UI
        this.configureRecyclerView();
        this.configureTextInput();

        // LiveData
        this.configureMessagesLiveData();
    }

    // -- AdapterListener interface --

    @Override
    public void onDataChanged() {
        this.mNoMessage.setVisibility( (this.mAdapter.getItemCount() == 0) ? View.VISIBLE :
                                                                             View.GONE);
    }

    @Override
    public void onCardViewClicked(@NonNull final View view) {
        // TODO: 07/02/2020 Implement this action
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
                                            Glide.with(this),
                                            this.getCurrentUser().getUid());

        // RecyclerView
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
    }

    // -- TextInput --

    private void configureTextInput() {
        this.mTextInputLayout.getEditText()
                             .addTextChangedListener(new TextWatcher() {
                                 @Override
                                 public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                                 @Override
                                 public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    mTextInputLayout.setError(null);
                                 }

                                 @Override
                                 public void afterTextChanged(Editable s) {}
                             });
    }

    // -- LiveData --

    /**
     * Configures the {@link LiveData} of {@link List<com.mancel.yann.go4lunch.models.Message>}
     */
    private void configureMessagesLiveData() {
        // Bind between liveData of ViewModel and the Adapter of RecyclerView
        this.mViewModel.getMessages()
                       .observe(this,
                                 messages -> {
                                     // To avoid the value equal to null by Firebase Firestore
                                     // during the update in real-time
                                     for (Message message : messages) {
                                         if (message.getDateCreated() == null) {
                                             return;
                                         }
                                     }

                                     this.mAdapter.updateData(messages);
                                 });
    }

    // -- Message --

    /**
     * Sends the {@link com.mancel.yann.go4lunch.models.Message} into Firebase Firestore
     */
    private void sendMessage() {
        // No message
        if (this.mTextInputLayout.getEditText().getText().toString().isEmpty()) {
            this.mTextInputLayout.setError(this.getString(R.string.error_message));
            return;
        }

        // Fetches the data of current user
        try {
            this.mViewModel.getUser(this.getCurrentUser())
                           .addOnSuccessListener( documentSnapshot -> {
                               final User user = documentSnapshot.toObject(User.class);

                               if (user != null) {
                                   this.mViewModel.createMessage(this.mTextInputLayout.getEditText().getText().toString(),
                                                                 user);
                               }

                               // Clears the message area
                               this.mTextInputLayout.getEditText().setText("");
                           });
        }
        catch (Exception e) {
            Crashlytics.log(Log.ERROR,
                            ChatActivity.class.getSimpleName(),
                           "sendMessage: " + e.getMessage());
        }
    }
}
