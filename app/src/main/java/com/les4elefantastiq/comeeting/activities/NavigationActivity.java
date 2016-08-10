package com.les4elefantastiq.comeeting.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.les4elefantastiq.comeeting.R;
import com.les4elefantastiq.comeeting.activities.utils.BaseActivity;
import com.les4elefantastiq.comeeting.managers.CoworkspacesManager;
import com.les4elefantastiq.comeeting.managers.ProfileManager;
import com.les4elefantastiq.comeeting.managers.SharedPreferencesManager;
import com.les4elefantastiq.comeeting.models.Coworker;
import com.les4elefantastiq.comeeting.models.Coworkspace;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    // -------------- Objects, Variables -------------- //

    private CurrentCoworkspaceAsynctask mCurrentCoworkspaceAsynctask;


    // -------------------- Views --------------------- //

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;


    // ------------------ LifeCycle ------------------- //

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        mNavigationView = ((NavigationView) findViewById(R.id.navigationView));

        if (ProfileManager.isLogged(this)) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            loadProfile();

            // Manage Toolbar/ActionBar
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(R.string.app_name);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            manageNavigationDrawer();

            mCurrentCoworkspaceAsynctask = new CurrentCoworkspaceAsynctask();
            mCurrentCoworkspaceAsynctask.execute();
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

        if (mCurrentCoworkspaceAsynctask != null)
            mCurrentCoworkspaceAsynctask.cancel(false);
    }


    // ------------------ Listeners ------------------- //

    private HashMap<Integer, String> mMenuIdCoworkspaceIdMap;

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
                bundle.putString(CoworkspaceFragment.EXTRA_COWORKSPACE_ID, mMenuIdCoworkspaceIdMap.get(MENU_CURRENT_COWORKSPACE));
                fragment.setArguments(bundle);
                break;

            case MENU_MORE_COWORKSPACE:
                fragment = new CoworkspacesFragment();
                break;

            case MENU_SPECIFIC_COWORKSPACE:
                fragment = new CoworkspaceFragment();

                // Pass the CoworkspaceId to the Fragment
                Coworkspace selectedCoworkspace = CoworkspacesManager.getCoworkspaceWithName(menuItem.getTitle().toString());
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

    private static final int MENU_CURRENT_COWORKSPACE = 1;
    private static final int MENU_MORE_COWORKSPACE = 2;
    private static final int MENU_SPECIFIC_COWORKSPACE = 3;
    private static final int MENU_NO_ACTION = 4;

    private void manageNavigationDrawer() {
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
    }

    private void manageNavigationDrawerMenu(Coworkspace currentCoworkspace, Coworkspace[] favoriteCoworkspace) {
        mMenuIdCoworkspaceIdMap = new HashMap<>();

        Menu menu = mNavigationView.getMenu();
        
        SubMenu currentSubMenu = menu.addSubMenu("Coworking actuel");
        if (currentCoworkspace != null) {
            // If currently in a coworkspace
            currentSubMenu.add(0, MENU_CURRENT_COWORKSPACE, 0, currentCoworkspace.name);
            mMenuIdCoworkspaceIdMap.put(MENU_CURRENT_COWORKSPACE, currentCoworkspace.id);
        } else {
            // If not in a coworkspace
            currentSubMenu.add(0, MENU_NO_ACTION, 0, "Pas dans un coworking");
        }

        // Fav
        SubMenu favSubMenu = menu.addSubMenu("Coworking Favoris");

        if (favoriteCoworkspace != null && favoriteCoworkspace.length > 0) {
            // If have some favorite(s) coworkspace
            for (Coworkspace coworkspace : favoriteCoworkspace) {
                favSubMenu.add(0, MENU_SPECIFIC_COWORKSPACE, 0, coworkspace.name);
            }
        } else {
            // If don't have favorite(s) coworkspace
            favSubMenu.add(0, MENU_NO_ACTION, 0, "Pas de favoris");
        }

        // More
        menu.add(1, MENU_MORE_COWORKSPACE, 0, "Voir les autres coworkspaces");
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_Content, fragment)
                .commit();

        mDrawerLayout.closeDrawers();
    }

    private void loadProfile() {
        Coworker coworkerProfile = SharedPreferencesManager.getProfile(this);
        if (coworkerProfile == null) { return; }

        View navigationViewHeader = mNavigationView.getHeaderView(0);

        CircleImageView userPictureImageView = (CircleImageView) navigationViewHeader.findViewById(R.id.imageView_UserPicture);
        Picasso.with(this).load(coworkerProfile.pictureUrl).into(userPictureImageView);

        TextView userNameTextView = (TextView) navigationViewHeader.findViewById(R.id.textView_UserName);
        userNameTextView.setText(coworkerProfile.firstName + " " + coworkerProfile.lastName);

        TextView emailTextView = (TextView) navigationViewHeader.findViewById(R.id.textView_Email);
        emailTextView.setText(coworkerProfile.headline);
    }

    // ------------------ AsyncTasks ------------------ //

    private class CurrentCoworkspaceAsynctask extends AsyncTask<Void, Void, Coworkspace[][]> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(NavigationActivity.this, null, getString(R.string.Please_wait), true, false);
        }

        @Override
        protected Coworkspace[][] doInBackground(Void... voids) {
            Coworkspace[][] coworkspaces =
                    {{ProfileManager.getCurrentCowerkspace(NavigationActivity.this)},
                        ProfileManager.getFavoriteCowerkspaces(NavigationActivity.this)};

            return coworkspaces;
        }

        @Override
        protected void onPostExecute(Coworkspace[][] coworkspaces) {
            super.onPostExecute(coworkspaces);

            Coworkspace currentCoworkspace = coworkspaces[0][0]; // On est d'accord, c'est plutôt horrible, mais à 3h07 j'accepte de vivre avec.
            Coworkspace[] favoriteCoworkspace = coworkspaces[1];

            progressDialog.dismiss();

            manageNavigationDrawerMenu(currentCoworkspace, favoriteCoworkspace);

            if (currentCoworkspace != null) {
                CoworkspaceFragment coworkspaceFragment = new CoworkspaceFragment();

                // Pass the CoworkspaceId to the Fragment
                Bundle bundle = new Bundle();
                bundle.putString(CoworkspaceFragment.EXTRA_COWORKSPACE_ID, currentCoworkspace.id);
                coworkspaceFragment.setArguments(bundle);

                showFragment(coworkspaceFragment);
            } else {
                showFragment(new CoworkspacesFragment());
            }
        }
    }


    // ----------------- GUI Adapter ------------------ //

    // --------------------- Menu --------------------- //

}