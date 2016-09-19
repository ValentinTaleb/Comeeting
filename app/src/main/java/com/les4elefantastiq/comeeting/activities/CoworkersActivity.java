package com.les4elefantastiq.comeeting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.les4elefantastiq.comeeting.R;
import com.les4elefantastiq.comeeting.activities.utils.BaseActivity;
import com.les4elefantastiq.comeeting.managers.CoworkspaceManager;
import com.les4elefantastiq.comeeting.models.Coworker;
import com.squareup.picasso.Picasso;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CoworkersActivity extends BaseActivity {

    // -------------- Objects, Variables -------------- //

    private Subscription mLoadCoworkersSubscription;


    // -------------------- Views --------------------- //

    private ListView mListView;
    private ProgressBar mProgressBar;


    // ------------------ LifeCycle ------------------- //

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coworkers_activity);

        manageToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Coworkers");

        mListView = (ListView) findViewById(R.id.listview);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        String coworkspaceId = getIntent().getStringExtra(CoworkspaceFragment.EXTRA_COWORKSPACE_ID);

        loadCoworkers(coworkspaceId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mLoadCoworkersSubscription != null && !mLoadCoworkersSubscription.isUnsubscribed())
            mLoadCoworkersSubscription.unsubscribe();
    }


    // ------------------- Methods -------------------- //

    private void loadCoworkers(String coworkspaceId) {
        mLoadCoworkersSubscription = CoworkspaceManager.getCoworkspace(coworkspaceId)
                .flatMap(CoworkspaceManager::getCoworkers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mCoworkersObserver);
    }

    private Observer<List<Coworker>> mCoworkersObserver = new Observer<List<Coworker>>() {

        @Override
        public void onCompleted() {
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(CoworkersActivity.this, R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        @Override
        public void onNext(List<Coworker> coworkers) {

            if (coworkers != null)
                mListView.setAdapter(new Adapter(coworkers));
            else
                Toast.makeText(CoworkersActivity.this, R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
        }

    };


    // ----------------- GUI Adapter ------------------ //

    private class Adapter extends BaseAdapter {

        private List<Coworker> coworkers;

        private class ObjectsHolder {
            Coworker coworker;
            ImageView imageView;
            TextView textView_Name;
            TextView textView_Description;
        }

        public Adapter(List<Coworker> coworkers) {
            this.coworkers = coworkers;
        }

        @Override
        public int getCount() {
            return coworkers.size();
        }

        @Override
        public Coworker getItem(int position) {
            return coworkers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Coworker coworker = getItem(position);
            ObjectsHolder objectsHolder;

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.coworkers_activity_item, parent, false);
                objectsHolder = new ObjectsHolder();
                convertView.setTag(objectsHolder);

                objectsHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
                objectsHolder.textView_Name = (TextView) convertView.findViewById(R.id.textview_name);
                objectsHolder.textView_Description = (TextView) convertView.findViewById(R.id.textview_description);
            } else
                objectsHolder = (ObjectsHolder) convertView.getTag();

            Picasso.with(getBaseContext())
                    .load(coworker.pictureUrl)
                    .placeholder(R.drawable.user)
                    .into(objectsHolder.imageView);

            objectsHolder.textView_Name.setText(coworker.firstName + " " + coworker.lastName);
            objectsHolder.textView_Description.setText(coworker.headline);

            objectsHolder.coworker = coworker;

            convertView.setOnClickListener(onCoworkerClickListener);

            return convertView;
        }

        private View.OnClickListener onCoworkerClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Coworker coworker = ((ObjectsHolder) view.getTag()).coworker;
                startActivity(new Intent(CoworkersActivity.this, CoworkerActivity.class).putExtra(CoworkerActivity.EXTRA_COWORKER_ID, coworker.linkedInId));
            }

        };

    }


}