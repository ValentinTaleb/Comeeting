package com.les4elefantastiq.comeeting.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.gson.Gson;
import com.les4elefantastiq.comeeting.R;
import com.les4elefantastiq.comeeting.activities.utils.BaseActivity;
import com.les4elefantastiq.comeeting.managers.CoworkerManager;
import com.les4elefantastiq.comeeting.managers.SharedPreferencesManager;
import com.les4elefantastiq.comeeting.models.linkedin.LinkedInCoworker;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Ask the user to Login with LinkedIn then lead him to NavigationActivity.<br />
 * LinkedIn must be installed.
 */
public class SignInActivity extends BaseActivity implements View.OnClickListener, ApiListener, AuthListener {

    // -------------- Objects, Variables -------------- //

    private Subscription mLoginSubscription;


    // -------------------- Views --------------------- //

    private ProgressDialog mProgressDialog;


    // ------------------ LifeCycle ------------------- //

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        // FIXME : It's already done in BaseActivity O_o
        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        findViewById(R.id.button_linked_in).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Let LinkedIn manage the result of onActivityResult
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mLoginSubscription != null && !mLoginSubscription.isUnsubscribed())
            mLoginSubscription.unsubscribe();
    }


    // ------------------ Listeners ------------------- //

    @Override
    public void onClick(View view) {
        mProgressDialog = ProgressDialog.show(SignInActivity.this, null, getString(R.string.Please_wait), true, false);

        launchLinkedInLogInActivity();
    }

    @Override
    public void onApiSuccess(ApiResponse apiResponse) {
        login(apiResponse);
    }

    @Override
    public void onApiError(LIApiError LIApiError) {
        displayConnectionErrorAlertDialog();
    }

    @Override
    public void onAuthSuccess() {
        // Authentication was successful.
        // Now, get profile data
        String url = "https://api.linkedin.com/v1/people/~:(first-name,last-name,id,picture-urls::(original),positions,summary,headline)?format=json";

        APIHelper.getInstance(this).getRequest(getApplicationContext(), url, this);
    }

    @Override
    public void onAuthError(LIAuthError error) {
        displayConnectionErrorAlertDialog();
    }


    // ------------------- Methods -------------------- //

    /**
     * Launch the LinkedIn Activity to log in
     */
    private void launchLinkedInLogInActivity() {
        Scope linkedInScope = Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
        LISessionManager.getInstance(SignInActivity.this).init(this, linkedInScope, this, true);
    }

    /**
     * Display a error message if the log in failed
     */
    public void displayConnectionErrorAlertDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        Toast.makeText(this, R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
    }

    /**
     * Based on the response of LinkedIn, log the user in the App server
     *
     * @param apiResponse The response given by LinkedIn LogIn
     */
    private void login(ApiResponse apiResponse) {
        LinkedInCoworker linkedInCoworker = new Gson().fromJson(apiResponse.getResponseDataAsString(), LinkedInCoworker.class);
        SharedPreferencesManager.setLinkedInId(SignInActivity.this, linkedInCoworker.linkedInId);
        SharedPreferencesManager.saveProfile(SignInActivity.this, linkedInCoworker.getCoworker());

        mLoginSubscription = CoworkerManager.login(linkedInCoworker.getCoworker())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginObserver);
    }

    /**
     * Lead the user to NavigationActivity after the Login succeeded
     */
    private Observer<Void> loginObserver = new Observer<Void>() {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(SignInActivity.this, R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        @Override
        public void onNext(Void aVoid) {
            startActivity(new Intent(SignInActivity.this, NavigationActivity.class));
            finish();
        }

    };

}