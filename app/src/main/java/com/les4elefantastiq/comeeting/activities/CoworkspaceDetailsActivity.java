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
import com.les4elefantastiq.comeeting.managers.CoworkspaceManager;
import com.les4elefantastiq.comeeting.models.Coworkspace;
import com.squareup.picasso.Picasso;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CoworkspaceDetailsActivity extends BaseActivity {

    // -------------- Objects, Variables -------------- //

    public static final String EXTRA_COWORKSPACE_ID = "EXTRA_COWORKSPACE_ID";

    private Subscription mCoworkspaceSubscription;


    // -------------------- Views --------------------- //

    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private NestedScrollView mNestedScrollView;


    // ------------------ LifeCycle ------------------- //

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coworkspace_details_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#00000000"));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mImageView = (ImageView) findViewById(R.id.imageview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mNestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);

        String coworkspaceId = getIntent().getStringExtra(EXTRA_COWORKSPACE_ID);
        loadCoworkspace(coworkspaceId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCoworkspaceSubscription != null && !mCoworkspaceSubscription.isUnsubscribed())
            mCoworkspaceSubscription.unsubscribe();
    }


    // ------------------- Methods -------------------- //

    private void loadCoworkspace(String coworkspaceId) {
        mCoworkspaceSubscription = CoworkspaceManager.getCoworkspace(coworkspaceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mCoworkspaceOserver);
    }

    private Observer<Coworkspace> mCoworkspaceOserver = new Observer<Coworkspace>() {

        @Override
        public void onCompleted() {
            mProgressBar.setVisibility(View.GONE);
            mNestedScrollView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(CoworkspaceDetailsActivity.this, R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        @Override
        public void onNext(Coworkspace coworkspace) {

            if (coworkspace != null) {
                ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setTitle(coworkspace.name);
                ((TextView) findViewById(R.id.textview_address)).setText(coworkspace.address + "\n" + coworkspace.zipCode + " " + coworkspace.city);

                Picasso.with(getBaseContext())
                        .load(coworkspace.pictureUrl)
                        .into(mImageView);

            } else
                Toast.makeText(CoworkspaceDetailsActivity.this, R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
        }

    };

}