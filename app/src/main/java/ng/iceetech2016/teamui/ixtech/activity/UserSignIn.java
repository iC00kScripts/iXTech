package ng.iceetech2016.teamui.ixtech.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.iceetech2016.teamui.ixtech.R;
import ng.iceetech2016.teamui.ixtech.util.SettingsPreference;


public class UserSignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    @Bind(R.id.signinText)TextView t;
    @Bind(R.id.sign_in_button) SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN=4;private boolean logout=false; private Bundle params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in);
        ButterKnife.bind(this);

        Animation animateText= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        t.startAnimation(animateText);

        // Configure sign-in to request the user's ID & email address
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        /** CONFIGURE THE SIGN IN BUTTON **/
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());

        params = getIntent().getExtras();

        if(params!=null){
            if(params.getBoolean("Logout")){
                logout=true;
                SignIn();
            }
        }
    }

    @OnClick(R.id.sign_in_button)
    public void SignIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Google SignIN", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            if(logout) {
                SignOut();
                RevokeAccess();
                logout=false;
            }
            else {
                // Signed in successfully, save info in sharedpreference
                GoogleSignInAccount acct = result.getSignInAccount();
                //if (acct.getEmail().endsWith("ui.edu.ng")) {
                    SettingsPreference settingsPreference = new SettingsPreference(this);
                    settingsPreference.SetUser(true);
                    settingsPreference.SetUserSession(acct.getDisplayName());
                    Intent in = new Intent(getApplicationContext(), UserMainActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();
            }
        } else {
            SignOut();
            RevokeAccess();
            new BottomDialog.Builder(this)
                    .setTitle("Sign-In Failed")
                    .setContent("Google sign-in attempt failed. " +
                            "Please try again later. Thank You")
                    .setCancelable(true)
                    .setPositiveText(R.string.diag_ok)
                    .onPositive(null)
                    .show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("Google SignIN", "onConnectionFailed:" + connectionResult);
        new BottomDialog.Builder(this)
                .setTitle("Error Signing In")
                .setContent("An unknown error has occurred while signing in. Please try again" +
                        " in a few minutes. Thank You!")
                .setCancelable(true)
                .setIcon(R.drawable.ic_error)
                .setPositiveText(R.string.diag_ok)
                .onPositive(null)
                .show();
    }

    private void SignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d("GOOGLE Sign_in","User Signed out!");
                    }
                });
    }
    private void RevokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d("GOOGLE Sign_in","User Access Revoked");
                    }
                });
    }

    @Override
    public void onBackPressed(){
        new BottomDialog.Builder(this)
                .setTitle("Exit")
                .setContent("Do you want to exit the application?")
                .setCancelable(true)
                .setIcon(R.drawable.ic_exit)
                .setPositiveText(R.string.diag_yes)
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        finish();
                    }
                })
                .show();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
