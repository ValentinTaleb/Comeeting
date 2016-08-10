package com.les4elefantastiq.les4elefantcowork.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.les4elefantastiq.les4elefantcowork.R;
import com.les4elefantastiq.les4elefantcowork.activities.utils.BaseActivity;

public class CoworkspaceActivity extends BaseActivity {

    // -------------- Objects, Variables -------------- //

    // -------------------- Views --------------------- //

    // ------------------ LifeCycle ------------------- //

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coworkspace_activity);

        manageToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the fragment
        CoworkspaceFragment coworkspaceFragment = new CoworkspaceFragment();

        // Pass the CoworkspaceId to the Fragment
        Bundle bundle = new Bundle();
        bundle.putString(CoworkspaceFragment.EXTRA_COWORKSPACE_ID, getIntent().getStringExtra(CoworkspaceFragment.EXTRA_COWORKSPACE_ID));
        coworkspaceFragment.setArguments(bundle);

        // Display the fragment
        getSupportFragmentManager().beginTransaction().add(R.id.layout_content, coworkspaceFragment).commit();
    }


    // ------------------ Listeners ------------------- //

    // ------------------- Methods -------------------- //

    // ------------------ AsyncTasks ------------------ //

    // ----------------- GUI Adapter ------------------ //

    // --------------------- Menu --------------------- //

}