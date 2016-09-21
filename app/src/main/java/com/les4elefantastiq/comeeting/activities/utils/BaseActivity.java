package com.les4elefantastiq.comeeting.activities.utils;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.les4elefantastiq.comeeting.R;

/**
 * Every Activity of this project should extends BaseActivity
 */
public class BaseActivity extends AppCompatActivity {

    // ------------------- Methods -------------------- //

    /**
     * Initialize the ToolBar and set the color of the StatusBar
     */
    protected void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }


    // --------------------- Menu --------------------- //

    /**
     * The only purpose of this method is to finish() the Activity when the user click the 'Up' button of the ActionBar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

}