package com.les4elefantastiq.comeeting.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.les4elefantastiq.comeeting.R;
import com.les4elefantastiq.comeeting.activities.utils.BaseActivity;
import com.les4elefantastiq.comeeting.managers.CoworkerManager;
import com.les4elefantastiq.comeeting.models.Coworker;
import com.squareup.picasso.Picasso;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Display the information (Name, description, job title, ...) of the specified Coworker
 */
public class CoworkerActivity extends BaseActivity {

    // -------------- Objects, Variables -------------- //

    public static final String EXTRA_COWORKER_ID = "EXTRA_COWORKER_ID";

    private Subscription mLoadCoworkerSubscription;


    // -------------------- Views --------------------- //

    private ImageView mImageViewCoworker;
    private ProgressBar mProgressBar;
    private NestedScrollView mNestedScrollView;


    // ------------------ LifeCycle ------------------- //

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coworker_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FIXME : It's already done in BaseActivity (but with colorPrimaryDark)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#00000000"));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mImageViewCoworker = (ImageView) findViewById(R.id.imageview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mNestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);

        String coworkerId = getIntent().getStringExtra(EXTRA_COWORKER_ID);
        loadCoworker(coworkerId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mLoadCoworkerSubscription != null && !mLoadCoworkerSubscription.isUnsubscribed())
            mLoadCoworkerSubscription.unsubscribe();
    }


    // ------------------- Methods -------------------- //

    /**
     * Load the information about the specified Coworker
     *
     * @param coworkerId The id of the Coworker to display
     */
    private void loadCoworker(String coworkerId) {
        mNestedScrollView.setVisibility(View.GONE);

        mLoadCoworkerSubscription = CoworkerManager.getCoworker((coworkerId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mCoworkerObserver);
    }

    /**
     * Display the information about the emitted Coworker
     */
    private Observer<Coworker> mCoworkerObserver = new Observer<Coworker>() {

        @Override
        public void onCompleted() {
            mProgressBar.setVisibility(View.GONE);
            mNestedScrollView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(CoworkerActivity.this, R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        @Override
        public void onNext(Coworker coworker) {
            ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setTitle(coworker.firstName + " " + coworker.lastName);
            ((TextView) findViewById(R.id.textview_summary)).setText(coworker.summary);
            ((TextView) findViewById(R.id.textview_linked_in_title)).setText("Profil LinkedIn de " + coworker.firstName);

            Picasso.with(getBaseContext())
                    .load(coworker.pictureUrl)
                    .into(mImageViewCoworker);
        }

    };

}