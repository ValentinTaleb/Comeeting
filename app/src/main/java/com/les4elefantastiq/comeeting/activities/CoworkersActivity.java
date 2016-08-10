package com.les4elefantastiq.comeeting.activities;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.les4elefantastiq.comeeting.managers.CoworkspacesManager;
import com.les4elefantastiq.comeeting.models.Coworker;
import com.les4elefantastiq.comeeting.models.Coworkspace;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CoworkersActivity extends BaseActivity {

    // -------------- Objects, Variables -------------- //

    private CoworkersAsyncTask mCoworkersAsyncTask;


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

        mCoworkersAsyncTask = new CoworkersAsyncTask();
        mCoworkersAsyncTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCoworkersAsyncTask != null)
            mCoworkersAsyncTask.cancel(false);
    }


    // ------------------ Listeners ------------------- //

    // ------------------- Methods -------------------- //

    // ------------------ AsyncTasks ------------------ //

    private class CoworkersAsyncTask extends AsyncTask<Void, Void, List<Coworker>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Coworker> doInBackground(Void... voids) {
            Coworkspace coworkspace = CoworkspacesManager.getCoworkspace(getIntent().getStringExtra(CoworkspaceFragment.EXTRA_COWORKSPACE_ID));

            if (coworkspace != null)
                return CoworkspacesManager.getCoworkers(coworkspace);
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<Coworker> coworkers) {
            super.onPostExecute(coworkers);

            mProgressBar.setVisibility(View.GONE);

            if (coworkers != null)
                mListView.setAdapter(new Adapter(coworkers));
            else
                Toast.makeText(CoworkersActivity.this, R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
        }

    }


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


    // --------------------- Menu --------------------- //

}