package com.les4elefantastiq.comeeting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
import com.les4elefantastiq.comeeting.models.Coworkspace;
import com.squareup.picasso.Picasso;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Display all the Coworkspaces
 */
public class CoworkspacesFragment extends Fragment {

    // -------------- Objects, Variables -------------- //

    private Subscription mCoworkspacesSubscription;


    // -------------------- Views --------------------- //

    private ListView mListView;
    private ProgressBar mProgressBar;


    // ------------------ LifeCycle ------------------- //

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coworkspaces_fragment, container, false);

        ((BaseActivity) getActivity()).getSupportActionBar().setTitle(R.string.All_coworkspaces);

        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.addHeaderView(getActivity().getLayoutInflater().inflate(R.layout.coworkspaces_activity_header, null));

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        loadCoworkspaces();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mCoworkspacesSubscription != null && !mCoworkspacesSubscription.isUnsubscribed())
            mCoworkspacesSubscription.unsubscribe();
    }


    // ------------------- Methods -------------------- //

    /**
     * Load all the Coworkspaces
     */
    private void loadCoworkspaces() {
        mCoworkspacesSubscription = CoworkspaceManager.getCoworkspaces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mCoworkspacesObserver);
    }

    /**
     * Display the emitted list of Coworkspaces
     */
    private Observer<List<Coworkspace>> mCoworkspacesObserver = new Observer<List<Coworkspace>>() {

        @Override
        public void onCompleted() {
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getActivity(), R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        @Override
        public void onNext(List<Coworkspace> coworkspaces) {
            if (coworkspaces != null)
                mListView.setAdapter(new Adapter(coworkspaces));
            else
                Toast.makeText(getActivity(), R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
        }

    };


    // ----------------- GUI Adapter ------------------ //

    /**
     * Display a list of Coworkspaces into mListView
     */
    private class Adapter extends BaseAdapter {

        private List<Coworkspace> coworkspaces;

        private class ObjectsHolder {
            Coworkspace coworkspace;
            ImageView imageView;
            TextView textView_Name, textView_Distance, textView_CowerkersCount, textView_Address;
            TextView textView_Opening;
        }

        public Adapter(List<Coworkspace> coworkspaces) {
            this.coworkspaces = coworkspaces;
        }

        @Override
        public int getCount() {
            return coworkspaces.size();
        }

        @Override
        public Coworkspace getItem(int position) {
            return coworkspaces.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Coworkspace coworkspace = getItem(position);
            ObjectsHolder objectsHolder;

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.coworkspaces_fragment_item, parent, false);
                objectsHolder = new ObjectsHolder();
                objectsHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
                objectsHolder.textView_Name = (TextView) convertView.findViewById(R.id.textview_name);
                objectsHolder.textView_CowerkersCount = (TextView) convertView.findViewById(R.id.textview_coworkers_count);
                objectsHolder.textView_Distance = (TextView) convertView.findViewById(R.id.textview_distance);
                objectsHolder.textView_Address = (TextView) convertView.findViewById(R.id.textview_address);
                objectsHolder.textView_Opening = (TextView) convertView.findViewById(R.id.textview_opening);

                convertView.setTag(objectsHolder);

            } else
                objectsHolder = (ObjectsHolder) convertView.getTag();

            Picasso.with(getActivity())
                    .load(coworkspace.pictureUrl)
                    .into(objectsHolder.imageView);

            objectsHolder.textView_Name.setText(coworkspace.name);
            objectsHolder.textView_CowerkersCount.setText(coworkspace.coworkers.size() + " coworkers actuellement");
            // objectsHolder.textView_Distance.setText(coworkspace.);
            objectsHolder.textView_Opening.setText("Ouvert de 8h à 17h");
            objectsHolder.textView_CowerkersCount.setText(coworkspace.coworkers.size() + " Coworkers");
            objectsHolder.textView_Distance.setText("3 km");
            objectsHolder.textView_Address.setText(coworkspace.address + "\n" + coworkspace.zipCode + " " + coworkspace.city);

            objectsHolder.coworkspace = coworkspace;

            convertView.setOnClickListener(onCoworkspaceClickListener);

            return convertView;
        }

        private View.OnClickListener onCoworkspaceClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CoworkspaceActivity.class)
                        .putExtra(CoworkspaceFragment.EXTRA_COWORKSPACE_ID, ((ObjectsHolder) view.getTag()).coworkspace.id);
                startActivity(intent);
            }

        };

    }

}