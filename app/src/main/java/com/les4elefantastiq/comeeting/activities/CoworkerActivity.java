package com.les4elefantastiq.comeeting.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.les4elefantastiq.comeeting.R;
import com.les4elefantastiq.comeeting.activities.utils.BaseActivity;
import com.les4elefantastiq.comeeting.managers.CoworkerManager;
import com.les4elefantastiq.comeeting.models.Coworker;
import com.linkedin.platform.DeepLinkHelper;
import com.squareup.picasso.Picasso;


public class CoworkerActivity extends BaseActivity implements View.OnClickListener {

    // -------------- Objects, Variables -------------- //

    public static final String EXTRA_COWORKER_ID = "EXTRA_COWORKER_ID";

    public CoworkerAsyncTask mCoworkerAsyncTask;


    // -------------------- Views --------------------- //

    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private NestedScrollView mNestedScrollView;
    private RelativeLayout linkedInView;


    // ------------------ LifeCycle ------------------- //

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coworker_activity);

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

        mCoworkerAsyncTask = new CoworkerAsyncTask();
        mCoworkerAsyncTask.execute();

        linkedInView = (RelativeLayout) findViewById(R.id.layout_linked_in);
        linkedInView.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCoworkerAsyncTask != null)
            mCoworkerAsyncTask.cancel(false);
    }


    // ------------------ Listeners ------------------- //

    @Override
    public void onClick(View view) {
        final Activity thisActivity = this;

        /*
        DeepLinkHelper deepLinkHelper = DeepLinkHelper.getInstance();

        // Open the current user's profile
        deepLinkHelper.openCurrentProfile(thisActivity, new DeeplinkListener() {
            @Override
            public void onDeepLinkSuccess() {
                // Successfully sent user to LinkedIn app
            }

            @Override
            public void onDeepLinkError(LiDeepLinkError error) {
                // Error sending user to LinkedIn app
            }
        });
        */
    }

    // ------------------- Methods -------------------- //

    // ------------------ AsyncTasks ------------------ //

    private class CoworkerAsyncTask extends AsyncTask<Void, Void, Coworker> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mNestedScrollView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Coworker doInBackground(Void... voids) {
            return CoworkerManager.getCoworker(getIntent().getStringExtra(EXTRA_COWORKER_ID));
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        protected void onPostExecute(Coworker coworker) {
            super.onPostExecute(coworker);

            mProgressBar.setVisibility(View.GONE);
            mNestedScrollView.setVisibility(View.VISIBLE);

            if (coworker != null) {
                ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setTitle(coworker.firstName + " " + coworker.lastName);
                ((TextView) findViewById(R.id.textview_summary)).setText(coworker.summary);
                ((TextView) findViewById(R.id.textview_linked_in_title)).setText("Profil LinkedIn de " + coworker.firstName);

                Picasso.with(getBaseContext())
                        .load(coworker.pictureUrl)
                        .placeholder(R.drawable.user)
                        .into(mImageView);

            } else
                Toast.makeText(CoworkerActivity.this, R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
        }

    }


    // ----------------- GUI Adapter ------------------ //

    // --------------------- Menu --------------------- //

}