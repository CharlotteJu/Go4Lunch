package com.mancel.yann.go4lunch.views.fragments;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.TextView;

import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.Follower;
import com.mancel.yann.go4lunch.models.UserInfos;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;
import com.mancel.yann.go4lunch.repositories.Repository;
import com.mancel.yann.go4lunch.views.adapters.LunchAdapter;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Yann MANCEL on 19/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.fragments
 *
 * A {@link BaseFragment} subclass.
 */
public class LunchListFragment extends BaseFragment {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.fragment_lunch_list_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_lunch_list_no_restaurant)
    TextView mNoRestaurant;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private LunchAdapter mAdapter;

    private Disposable mDisposable;

    private static final String TAG = LunchListFragment.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    public LunchListFragment() {}

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_lunch_list;
    }

    @Override
    protected void configureDesign() {
        this.configureRecyclerView();
        this.testJavaRX();
    }

    // -- Fragment --

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    // -- Observable --

    private Observable<String> getObservable() {
        return Observable.just("Reactive X is cool!");
    }

    // -- Observer --

    private DisposableObserver<String> getObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String text) {
                Log.e(TAG, "OnNext: " + text);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete");
            }
        };
    }

    private void disposeWhenDestroy() {
        Log.e(TAG, "disposeWhenDestroy");

        if (this.mDisposable != null && !this.mDisposable.isDisposed()) {
            this.mDisposable.dispose();
        }
    }

    // -- Stream --

    private void testJavaRX() {
        Log.e(TAG, "testJavaRX");
        //this.mDisposable = this.getObservable().subscribeWith(this.getObserver());

        Repository.PlaceRepository placeRepository = new PlaceRepositoryImpl();

//        this.mDisposable = placeRepository.getStreamToFetchUserFollowing("JakeWharton")
//                                          .subscribeWith(new DisposableObserver<List<Follower>>() {
//                                              @Override
//                                              public void onNext(List<Follower> followers) {
//
//                                                  StringBuilder builder = new StringBuilder();
//
//                                                  for (Follower follower : followers) {
//                                                      builder.append(follower.getLogin() + " ");
//                                                  }
//
//                                                  Log.e(TAG, "OnNext: " + builder.toString());
//                                              }
//
//                                              @Override
//                                              public void onError(Throwable e) {
//                                                  Log.e(TAG, "onError: " + e.getMessage());
//                                              }
//
//                                              @Override
//                                              public void onComplete() {
//                                                  Log.e(TAG, "onComplete");
//                                              }
//                                          });

        this.mDisposable = placeRepository.getStreamToFetchUserInfosFromFirstFollowing("JakeWharton")
                                          .subscribeWith(new DisposableObserver<UserInfos>() {
                                              @Override
                                              public void onNext(UserInfos userInfos) {
                                                  Log.e(TAG, "OnNext: " + userInfos.getLogin());
                                              }

                                              @Override
                                              public void onError(Throwable e) {
                                                  Log.e(TAG, "onError: " + e.getMessage());
                                              }

                                              @Override
                                              public void onComplete() {
                                                  Log.e(TAG, "onComplete");
                                              }
                                          });
    }

    // -- Instances --

    /**
     * Gets a new instance of {@link LunchListFragment}
     * @return a {@link LunchListFragment}
     */
    @NonNull
    public static LunchListFragment newInstance() {
        return new LunchListFragment();
    }

    // -- RecyclerView --

    /**
     * Configures the {@link RecyclerView}
     */
    private void configureRecyclerView() {
        // Adapter
        this.mAdapter = new LunchAdapter();

        // RecyclerView
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                                                                       DividerItemDecoration.VERTICAL));
    }
}
