package com.les4elefantastiq.les4elefantcowork.activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.les4elefantastiq.les4elefantcowork.R;
import com.les4elefantastiq.les4elefantcowork.activities.utils.BaseActivity;
import com.les4elefantastiq.les4elefantcowork.managers.CoworkspacesManager;
import com.les4elefantastiq.les4elefantcowork.models.Coworkspace;
import com.squareup.picasso.Picasso;

public class CoworkspaceDetailsActivity extends BaseActivity {

    // -------------- Objects, Variables -------------- //

    public static final String EXTRA_COWORKSPACE_ID = "EXTRA_COWORKSPACE_ID";
    public CoworkspaceAsyncTask mCoworkspaceAsyncTask;

    // -------------------- Views --------------------- //

    private ImageView imageView;

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
        getSupportActionBar().setTitle("Coworkspace");

        imageView = (ImageView) findViewById(R.id.imageview);

        mCoworkspaceAsyncTask = new CoworkspaceAsyncTask();
        mCoworkspaceAsyncTask.execute();
    }

    // ------------------ Listeners ------------------- //

    // ------------------- Methods -------------------- //

    // ------------------ AsyncTasks ------------------ //

    private class CoworkspaceAsyncTask extends AsyncTask<Void, Void, Coworkspace> {

        @Override
        protected Coworkspace doInBackground(Void... voids) {
            return CoworkspacesManager.getCoworkspace(getIntent().getStringExtra(EXTRA_COWORKSPACE_ID));
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        protected void onPostExecute(Coworkspace coworkspace) {
            super.onPostExecute(coworkspace);

            if (coworkspace != null) {
                ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setTitle(coworkspace.name);
                ((TextView) findViewById(R.id.textview_address)).setText(coworkspace.address + ", " + coworkspace.zipCode + "\n" + coworkspace.city + ", Belgique");

                Picasso.with(getBaseContext())
                        .load(coworkspace.pictureUrl)
                        .placeholder(R.drawable.user)
                        .into(imageView);

            } else
                Toast.makeText(CoworkspaceDetailsActivity.this, R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
        }

    }

    // ----------------- GUI Adapter ------------------ //

    // --------------------- Menu --------------------- //

}