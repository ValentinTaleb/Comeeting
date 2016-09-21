package com.les4elefantastiq.comeeting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.les4elefantastiq.comeeting.managers.LivefeedManager;
import com.les4elefantastiq.comeeting.models.Coworker;
import com.les4elefantastiq.comeeting.models.Coworkspace;
import com.les4elefantastiq.comeeting.models.LiveFeedMessage;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Display some Coworkers and the liveFeed of the specified Coworkspace
 * <p>
 * This Fragment is used in CoworkspaceActivity and NavigationActivity
 */
public class CoworkspaceFragment extends Fragment {

    // -------------- Objects, Variables -------------- //

    public static final String EXTRA_COWORKSPACE_ID = "EXTRA_COWORKSPACE_ID";

    private Subscription mCoworkspaceSubscription;
    private Subscription mLiveFeedMessagesSubscription;

    private Coworkspace mCoworkspace;


    // -------------------- Views --------------------- //

    private ListView mListView;
    private ProgressBar mProgressBar;


    // ------------------ LifeCycle ------------------- //

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coworkspace_fragment, container, false);

        ((BaseActivity) getActivity()).getSupportActionBar().setTitle("Coworkspace");

        setHasOptionsMenu(true);

        mListView = (ListView) view.findViewById(R.id.listview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        String coworkspaceId = getArguments().getString(EXTRA_COWORKSPACE_ID);
        loadCoworkspace(coworkspaceId);
        loadLiveFeedMessages(coworkspaceId);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mCoworkspaceSubscription != null && !mCoworkspaceSubscription.isUnsubscribed())
            mCoworkspaceSubscription.unsubscribe();

        if (mLiveFeedMessagesSubscription != null && !mLiveFeedMessagesSubscription.isUnsubscribed())
            mLiveFeedMessagesSubscription.unsubscribe();
    }


    // ------------------- Methods -------------------- //

    /**
     * Load a Coworkspace based of the specified id
     *
     * @param coworkspaceId The id of the Coworkspace
     */
    private void loadCoworkspace(String coworkspaceId) {
        mCoworkspaceSubscription = CoworkspaceManager.getCoworkspace(coworkspaceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mCoworkspaceObserver);
    }

    /**
     * Display some of the Coworkers of the emitted Coworkspace as a header of mListView,
     * set the title and the menu of the ActionBar
     */
    private Observer<Coworkspace> mCoworkspaceObserver = new Observer<Coworkspace>() {

        @Override
        public void onCompleted() {
            if (mLiveFeedMessagesSubscription.isUnsubscribed())
                mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getActivity(), R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        @Override
        public void onNext(Coworkspace coworkspace) {

            if (coworkspace != null) {
                // Set the ActionBar's title
                ((BaseActivity) getActivity()).getSupportActionBar().setTitle(coworkspace.name);

                // Display the coworkers of this coworkspace in header of the ListView
                mListView.addHeaderView(getCoworkersView(coworkspace));

                // Now that the Coworkspace has been loaded, refresh the menu to add 'Info' and 'Favorite'
                mCoworkspace = coworkspace;
                getActivity().invalidateOptionsMenu();
            }

        }

    };

    /**
     * @return a view that display some of the Coworkers of the specified Coworkspace
     */
    private View getCoworkersView(Coworkspace coworkspace) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.coworkspace_fragment_coworkers, null, false);

        if (coworkspace != null) {
            ArrayList<ImageView> imageViews = new ArrayList<>();
            imageViews.add((ImageView) view.findViewById(R.id.imageview_coworker_1));
            imageViews.add((ImageView) view.findViewById(R.id.imageview_coworker_2));
            imageViews.add((ImageView) view.findViewById(R.id.imageview_coworker_3));
            imageViews.add((ImageView) view.findViewById(R.id.imageview_coworker_4));

            for (int i = 0; i < 4; i++) {
                if (coworkspace.coworkers.size() > i) {
                    ImageView imageView = imageViews.get(i);
                    Coworker coworker = coworkspace.coworkers.get(i);

                    Picasso.with(getContext())
                            .load(coworker.pictureUrl)
                            .placeholder(R.drawable.user)
                            .into(imageView);

                    imageView.setTag(coworker);

                    // Click on the picture of a Coworker -> Open Activity with info of this Coworker
                    imageView.setOnClickListener(v -> {
                        Coworker coworker1 = (Coworker) v.getTag();
                        startActivity(new Intent(getActivity(), CoworkerActivity.class)
                                .putExtra(CoworkerActivity.EXTRA_COWORKER_ID, coworker1.linkedInId));
                    });

                }
            }

            // Click on "See more" -> Open Activity with all Coworkers
            view.findViewById(R.id.textview_see_more)
                    .setOnClickListener(v -> startActivity(new Intent(getActivity(), CoworkersActivity.class)
                            .putExtra(CoworkersActivity.EXTRA_COWORKSPACE_ID, getArguments().getString(EXTRA_COWORKSPACE_ID))));
        }

        return view;
    }

    /**
     * Load the LiveFeedMessages of the specified Coworkspace
     *
     * @param coworkspaceId The id of the Coworkspace
     */
    private void loadLiveFeedMessages(String coworkspaceId) {
        mLiveFeedMessagesSubscription = CoworkspaceManager.getCoworkspace(coworkspaceId)
                .flatMap(LivefeedManager::getLiveFeedMessages)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mLiveFeedMessageObserver);
    }

    /**
     * Display the emitted list of LiveFeedMessages
     */
    private Observer<List<LiveFeedMessage>> mLiveFeedMessageObserver = new Observer<List<LiveFeedMessage>>() {

        @Override
        public void onCompleted() {
            if (mCoworkspaceSubscription.isUnsubscribed())
                mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getActivity(), R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        @Override
        public void onNext(List<LiveFeedMessage> liveFeedMessages) {

            if (liveFeedMessages != null)
                mListView.setAdapter(new Adapter(liveFeedMessages));
            else
                Toast.makeText(getActivity(), R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
        }

    };


    // ----------------- GUI Adapter ------------------ //

    /**
     * Display a list of LiveFeedMessages into mListView
     */
    private class Adapter extends BaseAdapter {

        private List<LiveFeedMessage> mLiveFeedMessages = new ArrayList<>();

        private class ObjectsHolder {
            ImageView imageView;
            TextView textView_Name, textView_Description, textView_Date;
            TextView textView_Subdescription, textView_Badge;
        }

        public Adapter(List<LiveFeedMessage> liveFeedMessages) {
            mLiveFeedMessages = liveFeedMessages;
        }

        @Override
        public int getCount() {
            return mLiveFeedMessages.size();
        }

        @Override
        public LiveFeedMessage getItem(int position) {
            return mLiveFeedMessages.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LiveFeedMessage liveFeedMessage = getItem(position);

            ObjectsHolder objectsHolder;

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.coworkspace_fragment_item, parent, false);
                objectsHolder = new ObjectsHolder();
                convertView.setTag(objectsHolder);

                objectsHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
                objectsHolder.textView_Name = (TextView) convertView.findViewById(R.id.textview_name);
                objectsHolder.textView_Description = (TextView) convertView.findViewById(R.id.textview_description);
                objectsHolder.textView_Subdescription = (TextView) convertView.findViewById(R.id.textview_subdescription);
                objectsHolder.textView_Date = (TextView) convertView.findViewById(R.id.textview_date);
                objectsHolder.textView_Badge = (TextView) convertView.findViewById(R.id.textview_badge);
            } else
                objectsHolder = (ObjectsHolder) convertView.getTag();

            objectsHolder.textView_Name.setText(liveFeedMessage.title);
            objectsHolder.textView_Description.setText(liveFeedMessage.text);
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(liveFeedMessage.dateTime);
                String dateString = DateUtils.getRelativeTimeSpanString(date.getTime()).toString();
                objectsHolder.textView_Date.setText(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
                objectsHolder.textView_Date.setText("-");
            }

            // Arrival of a coworker
            if (liveFeedMessage.type == LiveFeedMessage.TYPE_ARRIVAL) {
                objectsHolder.textView_Badge.setText("Arrivée");

                Picasso.with(getActivity())
                        .load(liveFeedMessage.pictureUrl)
                        .placeholder(R.drawable.user)
                        .into(objectsHolder.imageView);

                if (liveFeedMessage.isBirthday) {
                    objectsHolder.textView_Subdescription.setVisibility(View.VISIBLE);
                    objectsHolder.textView_Subdescription.setText("C'est son anniversaire !");
                } else
                    objectsHolder.textView_Subdescription.setVisibility(View.GONE);
            }

            // Admin message
            else if (liveFeedMessage.type == LiveFeedMessage.TYPE_COWORKSPACE_ADMIN) {
                objectsHolder.textView_Badge.setText("Info");

                objectsHolder.imageView.setImageResource(R.drawable.ic_live_feed_admin);

                objectsHolder.textView_Subdescription.setVisibility(View.GONE);
            }

            // Twitter message
            else if (liveFeedMessage.type == LiveFeedMessage.TYPE_TWITTER) {
                objectsHolder.textView_Badge.setText("Retweet");

                Picasso.with(getActivity())
                        .load(liveFeedMessage.pictureUrl)
                        .placeholder(R.drawable.ic_live_feed_tweeter)
                        .into(objectsHolder.imageView);

                objectsHolder.textView_Subdescription.setVisibility(View.GONE);

            } else if (liveFeedMessage.type == LiveFeedMessage.TYPE_COWORKSPACE_OPENING) {
                objectsHolder.textView_Badge.setText("Info");

                objectsHolder.imageView.setImageResource(R.drawable.ic_live_feed_clock);
                objectsHolder.textView_Subdescription.setVisibility(View.VISIBLE);
                objectsHolder.textView_Subdescription.setText("Fermeture du coworkspace");
            }

            return convertView;
        }

    }


    // --------------------- Menu --------------------- //

    private final int MENU_FAVORITE = 1;
    private final int MENU_INFORMATION = 2;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (mCoworkspace != null) {
            MenuItem menuItemInformation = menu.add(0, MENU_INFORMATION, 0, "Information");
            menuItemInformation.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItemInformation.setIcon(R.drawable.ic_info_outline);

            MenuItem menuItemFavorite = menu.add(0, MENU_FAVORITE, 0, "Favoris");
            menuItemFavorite.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItemFavorite.setIcon(R.drawable.ic_favorite);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case MENU_FAVORITE: // Set this Coworkspace as Favorite
                // Not implemented yet on server side
                break;

            case MENU_INFORMATION: // Open Activity that display the informations about this Coworkspace
                Intent intent = new Intent(getContext(), CoworkspaceDetailsActivity.class);
                intent.putExtra(CoworkspaceDetailsActivity.EXTRA_COWORKSPACE_ID, mCoworkspace.id);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}