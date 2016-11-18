package ng.iceetech2016.teamui.ixtech.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import ng.iceetech2016.teamui.ixtech.R;
import ng.iceetech2016.teamui.ixtech.app.VolleyController;
import ng.iceetech2016.teamui.ixtech.util.Messager;
import ng.iceetech2016.teamui.ixtech.util.SettingsPreference;
import ng.iceetech2016.teamui.ixtech.util.iXTechUtils;

public class AdminSignIn extends AppCompatActivity {
    public static final String TAG = "PinLockView";
    public static final String API_LOCATION = "21";
    private String access, url="http://www.uitilities.com/iCeeTech2016/api/AdminLogin.php";

    @Bind(R.id.pin_lock_view) PinLockView mPinLockView;
    @Bind(R.id.indicator_dots) IndicatorDots mIndicatorDots;
    @Bind(R.id.coordLay) CoordinatorLayout snackbarLayout;
    private ProgressDialog pDialog;

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            access=pin;
            SignIn();
        }

        @Override
        public void onEmpty() {
            Snackbar.make(snackbarLayout,"Please enter your access token!",
                    Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            //Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_sign_in);
        ButterKnife.bind(this);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);

    }

    public void SignIn(){
        showProgressDialog();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressDialog();
                        try{
                            JSONObject res= new JSONObject(response);
                            String resp= res.getString(iXTechUtils.SUCCESS);
                            if (resp.equalsIgnoreCase("1")){
                                GrabMoreInfo();
                            }else{
                                mPinLockView.resetPinLockView();
                                Snackbar.make(snackbarLayout,"Invalid Access Token. Please Try " +
                                        "Again!",
                                        Snackbar.LENGTH_LONG).show();
                            }
                        } catch (Exception e){
                            hideProgressDialog();
                            Log.e("AdminLogin Error",e.toString());
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Log.e(TAG, "Error: " + error.toString());
                mPinLockView.resetPinLockView();
                if (error instanceof NoConnectionError)
                    new Messager(AdminSignIn.this).NoInternetDialog();
                else if (error instanceof TimeoutError)
                    new Messager(AdminSignIn.this).TimeoutErrorDialog();
                else
                    new Messager(AdminSignIn.this).ErrorDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                //params.put("api_location",API_LOCATION);
                params.put("access_token",access);
                return params;
            }
        };
        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(jsonObjReq, TAG);
    }


    private void GrabMoreInfo(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.fill_form, null);

        final EditText ed= (EditText)customView.findViewById(R.id.instName);
        final EditText ed2= (EditText)customView.findViewById(R.id.instEmail);
        ed.setVisibility(View.GONE);


        new MaterialStyledDialog(this)
                .setDescription("Please enter your contact email below")
                .setCustomView(customView)
                .withDarkerOverlay(true)
                .setCancelable(false)
                .setTitle("Contact Info.")
                .withIconAnimation(true)
                .setIcon(R.drawable.ic_us)
                .setPositive(getString(R.string.diag_ok), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String email= ed2.getText().toString();
                        if (email.equals(""))
                            new Messager(AdminSignIn.this).ToastMessage("Both fields are required");
                        else{
                            SettingsPreference settingsPreference = new SettingsPreference
                                    (AdminSignIn.this);
                            settingsPreference.SetSecUserSession(email);
                            Intent i= new Intent(AdminSignIn.this,AdminMainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        }

                    }
                }).show();

    }

    private void showProgressDialog(){
        pDialog = new ProgressDialog(this,R.style.ProgressDialogTheme);
        pDialog.setMessage("Requesting access...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void hideProgressDialog(){
        if (pDialog!=null && pDialog.isShowing())
            pDialog.dismiss();
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
}
