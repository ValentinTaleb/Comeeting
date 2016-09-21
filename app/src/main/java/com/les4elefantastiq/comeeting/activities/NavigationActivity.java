package com.les4elefantastiq.comeeting.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.les4elefantastiq.comeeting.R;
import com.les4elefantastiq.comeeting.activities.utils.BaseActivity;
import com.les4elefantastiq.comeeting.managers.ProfileManager;
import com.les4elefantastiq.comeeting.managers.SharedPreferencesManager;
import com.les4elefantastiq.comeeting.models.Coworker;
import com.les4elefantastiq.comeeting.models.Coworkspace;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    // -------------- Objects, Variables -------------- //

    private static final int MENU_CURRENT_COWORKSPACE = 1;
    private static final int MENU_MORE_COWORKSPACE = 2;
    private static final int MENU_SPECIFIC_COWORKSPACE = 3;
    private static final int MENU_NO_ACTION = 4;

    private Subscription mCurrentCoworkspacesSubscription;

    private HashMap<MenuItem, Coworkspace> mMenuItemCoworkspaceMap = new HashMap<>();


    // -------------------- Views --------------------- //

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;

    private ProgressDialog progressDialog;


    // ------------------ LifeCycle ------------------- //

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        mNavigationView = ((NavigationView) findViewById(R.id.navigationView));

        if (ProfileManager.isLogged(this)) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);

            // Manage Toolbar/ActionBar
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(R.string.app_name);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            progressDialog = ProgressDialog.show(NavigationActivity.this, null, getString(R.string.Please_wait), true, false);

            initializeNavigationDrawer();

            loadCurrentCoworkspace();
        } else {
            startActivity(new Intent(NavigationActivity.this, SignInActivity.class));
            finish();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCurrentCoworkspacesSubscription != null && !mCurrentCoworkspacesSubscription.isUnsubscribed())
            mCurrentCoworkspacesSubscription.unsubscribe();
    }


    // ------------------ Listeners ------------------- //


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);

        getSupportActionBar().setTitle(menuItem.getTitle());

        Fragment fragment = null;
        Bundle bundle = new Bundle();

        switch (menuItem.getItemId()) {
            case MENU_CURRENT_COWORKSPACE:
                fragment = new CoworkspaceFragment();

                // Pass the CoworkspaceId to the Fragment
                bundle.putString(CoworkspaceFragment.EXTRA_COWORKSPACE_ID, mMenuItemCoworkspaceMap.get(menuItem).id);
                fragment.setArguments(bundle);
                break;

            case MENU_MORE_COWORKSPACE:
                fragment = new CoworkspacesFragment();
                break;

            case MENU_SPECIFIC_COWORKSPACE:
                fragment = new CoworkspaceFragment();

                // Pass the CoworkspaceId to the Fragment
                Coworkspace selectedCoworkspace = mMenuItemCoworkspaceMap.get(menuItem);
                if (selectedCoworkspace != null) {
                    bundle.putString(CoworkspaceFragment.EXTRA_COWORKSPACE_ID, selectedCoworkspace.id);
                    fragment.setArguments(bundle);
                    break;
                }
                return true;
            case MENU_NO_ACTION:
                return true;
        }

        showFragment(fragment);

        return true;
    }


    // ------------------- Methods -------------------- //

    private void loadCurrentCoworkspace() {
        mCurrentCoworkspacesSubscription = ProfileManager.getCurrentCowerkspace(getBaseContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentCoworkspaceObserver);
    }

    private Observer<Coworkspace> currentCoworkspaceObserver = new Observer<Coworkspace>() {

        @Override
        public void onCompleted() {
            progressDialog.dismiss();
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getBaseContext(), R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        @Override
        public void onNext(Coworkspace coworkspace) {

            addCurrentCoworkspaceInNavigationDrawer(coworkspace);

            // The user has a CurrentCoworkspace -> display it in the Fragment
            if (coworkspace != null) {
                CoworkspaceFragment coworkspaceFragment = new CoworkspaceFragment();

                // Pass the CoworkspaceId to the Fragment
                Bundle bundle = new Bundle();
                bundle.putString(CoworkspaceFragment.EXTRA_COWORKSPACE_ID, coworkspace.id);
                coworkspaceFragment.setArguments(bundle);

                showFragment(coworkspaceFragment);
            }

            // The user doesn't have a CurrentCoworkspace -> display the list of all Coworkspaces
            else {
                showFragment(new CoworkspacesFragment());
            }
        }

    };

    private void loadFavoriteCoworkspaces() {

    }

    private Observer<List<Coworkspace>> favoriteCoworkspacesObserver = new Observer<List<Coworkspace>>() {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<Coworkspace> coworkspaces) {
            addFavoritesCoworkspacesInNavigationDrawer(coworkspaces);
        }

    };

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_Content, fragment)
                .commit();

        mDrawerLayout.closeDrawers();
    }


    // -------------- Navigation Drawer --------------- //

    private void initializeNavigationDrawer() {
        // Manage DrawerLayout and his toggle
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(NavigationActivity.this, mDrawerLayout, mToolbar, R.string.Open, R.string.Close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mNavigationView.setNavigationItemSelectedListener(this);

        showUserProfileInNavigationDrawer();

        // Add "See all coworkspaces" menu at the end of the drawer
        Menu menu = mNavigationView.getMenu();
        menu.add(Integer.MAX_VALUE, MENU_MORE_COWORKSPACE, 0, "Voir les autres coworkspaces");
    }

    private void showUserProfileInNavigationDrawer() {
        Coworker coworkerProfile = SharedPreferencesManager.getProfile(this);
        if (coworkerProfile == null) {
            return;
        }

        View navigationViewHeader = mNavigationView.getHeaderView(0);

        CircleImageView userPictureImageView = (CircleImageView) navigationViewHeader.findViewById(R.id.imageView_UserPicture);
        Picasso.with(this).load(coworkerProfile.pictureUrl).into(userPictureImageView);

        TextView userNameTextView = (TextView) navigationViewHeader.findViewById(R.id.textView_UserName);
        userNameTextView.setText(coworkerProfile.firstName + " " + coworkerProfile.lastName);

        TextView emailTextView = (TextView) navigationViewHeader.findViewById(R.id.textView_Email);
        emailTextView.setText(coworkerProfile.headline);
    }

    private void addCurrentCoworkspaceInNavigationDrawer(@Nullable Coworkspace currentCoworkspace) {
        Menu menu = mNavigationView.getMenu();

        SubMenu currentSubMenu = menu.addSubMenu("Coworkspace actuel");
        if (currentCoworkspace != null) {
            // If currently in a coworkspace
            MenuItem menuCurrentCoworkspace = currentSubMenu.add(0, MENU_CURRENT_COWORKSPACE, 0, currentCoworkspace.name);
            mMenuItemCoworkspaceMap.put(menuCurrentCoworkspace, currentCoworkspace);
        } else {
            // If not in a coworkspace
            currentSubMenu.add(0, MENU_NO_ACTION, 0, "Pas dans un coworkspace");
        }
    }

    private void addFavoritesCoworkspacesInNavigationDrawer(List<Coworkspace> favoriteCoworkspace) {
        Menu menu = mNavigationView.getMenu();

        SubMenu favSubMenu = menu.addSubMenu("Coworkspaces Favoris");

        if (favoriteCoworkspace != null && favoriteCoworkspace.size() > 0) {
            // If have some favorite(s) coworkspace
            for (Coworkspace coworkspace : favoriteCoworkspace) {
                MenuItem menuCoworkspace = favSubMenu.add(0, MENU_SPECIFIC_COWORKSPACE, 0, coworkspace.name);
                mMenuItemCoworkspaceMap.put(menuCoworkspace, coworkspace);
            }
        } else {
            // If don't have favorite(s) coworkspace
            favSubMenu.add(0, MENU_NO_ACTION, 0, "Pas de favoris");
        }

        // More
        menu.add(1, MENU_MORE_COWORKSPACE, 0, "Voir les autres coworkspaces");
    }

}