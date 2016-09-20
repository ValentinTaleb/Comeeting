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
import com.les4elefantastiq.comeeting.managers.ProfileManager;
import com.les4elefantastiq.comeeting.managers.SharedPreferencesManager;
import com.les4elefantastiq.comeeting.models.linkedinmodels.LinkedInCoworker;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignInActivity extends BaseActivity implements View.OnClickListener, ApiListener, AuthListener {

    // -------------- Objects, Variables -------------- //

    private Subscription loginSubscription;


    // -------------------- Views --------------------- //

    private ProgressDialog progressDialog;


    // ------------------ LifeCycle ------------------- //

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        findViewById(R.id.button_linked_in).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add this line to your existing onActivityResult() method
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (loginSubscription != null && !loginSubscription.isUnsubscribed())
            loginSubscription.unsubscribe();
    }


    // ------------------ Listeners ------------------- //

    @Override
    public void onClick(View view) {
        progressDialog = ProgressDialog.show(SignInActivity.this, null, "Please wait ...", true, false);
        ProfileManager.signWithLinkedIn(SignInActivity.this);
    }

    @Override
    public void onApiSuccess(ApiResponse apiResponse) {
        login(apiResponse);
    }

    @Override
    public void onApiError(LIApiError LIApiError) {
        showConnectionErrorAlertDialog();
    }

    @Override
    public void onAuthSuccess() {
        // Authentication was successful.
        // Now, get profile data
        String url = "https://api.linkedin.com/v1/people/~:(first-name,last-name,id,picture-urls::(original),positions,summary,headline)?format=json";

        APIHelper apiHelper = APIHelper.getInstance(this);
        apiHelper.getRequest(getApplicationContext(), url, this);
    }

    @Override
    public void onAuthError(LIAuthError error) {
        showConnectionErrorAlertDialog();
    }

    // ------------------- Methods -------------------- //

    public static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
    }

    public void showConnectionErrorAlertDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        Toast.makeText(this, R.string.Whoops_an_error_has_occured__Check_your_internet_connection, Toast.LENGTH_LONG).show();
    }

    public void showNavigationActivity() {
        startActivity(new Intent(SignInActivity.this, NavigationActivity.class));
        finish();
    }


    // ------------------ AsyncTasks ------------------ //

    private void login(ApiResponse apiResponse) {
        LinkedInCoworker linkedInCoworker = new Gson().fromJson(apiResponse.getResponseDataAsString(), LinkedInCoworker.class);
        SharedPreferencesManager.setLinkedInId(SignInActivity.this, linkedInCoworker.linkedInId);
        SharedPreferencesManager.saveProfile(SignInActivity.this, linkedInCoworker.getCoworker());

        loginSubscription = CoworkerManager.login(linkedInCoworker.getCoworker())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


//    private class LoginAsyncTask extends AsyncTask<ApiResponse, Void, Boolean> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Boolean doInBackground(ApiResponse... apiResponses) {
//            LinkedInCoworker linkedInCoworker = new Gson().fromJson(apiResponses[0].getResponseDataAsString(), LinkedInCoworker.class);
//            SharedPreferencesManager.setLinkedInId(SignInActivity.this, linkedInCoworker.linkedInId);
//            SharedPreferencesManager.saveProfile(SignInActivity.this, linkedInCoworker.getCoworker());
//            Boolean success = CoworkerManager.login(linkedInCoworker.getCoworker());
//
//            return success;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean success) {
//            super.onPostExecute(success);
//            progressDialog.dismiss();
//
//            if (success) {
//                showNavigationActivity();
//            }
//        }
//    }

}