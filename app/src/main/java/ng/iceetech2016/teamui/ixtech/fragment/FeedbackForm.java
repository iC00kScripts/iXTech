package ng.iceetech2016.teamui.ixtech.fragment;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.iceetech2016.teamui.ixtech.R;
import ng.iceetech2016.teamui.ixtech.activity.CDNetViewPostActivity;
import ng.iceetech2016.teamui.ixtech.activity.UserViewPostActivity;
import ng.iceetech2016.teamui.ixtech.app.VolleyController;
import ng.iceetech2016.teamui.ixtech.util.Messager;
import ng.iceetech2016.teamui.ixtech.util.SettingsPreference;
import ng.iceetech2016.teamui.ixtech.util.iXTechUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackForm extends DialogFragment {

    @Bind(R.id.uName)TextView Name;
    @Bind(R.id.uEmail)TextView Email;
    @Bind(R.id.llayout)LinearLayout layout;
    @Bind(R.id.fragInfo)TextView Frag;
    @Bind(R.id.proceed)AppCompatButton button;
    @Bind(R.id.uFeedback)EditText Feedback;

    private String feedback,name,email,institution;
    private ProgressDialog pDialog;

    private String API_LOCATION="44";
    private static String TAG= "FEEDBACK";
    private String type = "",url= "",sName,sEmail;


    public static FeedbackForm newInstance(String type){
        FeedbackForm feedbackForm= new FeedbackForm();
        Bundle bundle = new Bundle();
        bundle.putString(iXTechUtils.POST_TYPE,type);
        feedbackForm.setArguments(bundle);
        return feedbackForm;
    }

    public FeedbackForm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_feedback_form, container, false);
        ButterKnife.bind(this,v);
        type= getArguments().getString(iXTechUtils.POST_TYPE);
        if(type.equals("User"))
            getDialog().setTitle(getString(R.string.addcomm));
        else
            getDialog().setTitle(getString(R.string.addtitle));
        //getDialog().getWindow().setIcon(R.drawable.ic_setting_dark);
        getDialog().setCancelable(true);

        // Title divider
        final Resources res = getResources();
        final int titleDividerId = res.getIdentifier("titleDivider", "id", "android");
        final View titleDivider = getDialog().findViewById(titleDividerId);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(res.getColor(R.color.white));
        }

        if(type.equals("User")) {
            Map<String, String> userDetails = new SettingsPreference(getActivity()).GetUserSession();
            Name.setText(userDetails.get(SettingsPreference.USER_NAME));
            Email.setText(userDetails.get(SettingsPreference.USER_EMAIL));
            institution = userDetails.get(SettingsPreference.USER_INSTITUTION);
        } else{
            Map<String, String> userDetails = new SettingsPreference(getActivity())
                    .GetSecUserSession();
            //sName=userDetails.get(SettingsPreference.SEC_USER_NAME);
            sEmail=userDetails.get(SettingsPreference.SEC_USER_EMAIL);
        }

        if (type.equals("CDNet")) {
            url="http://www.uitilities.com/iCeeTech2016/api/AdminPost.php";
            Frag.setText("Enter the announcement in the field provided below");
            layout.setVisibility(View.GONE);
            button.setText("ANNOUNCE");
            Feedback.setHint("Announcement here");
        } else{
            url="http://www.uitilities.com/iCeeTech2016/api/UserPost.php";
        }

        return v;
    }

    private boolean validate(){
        boolean valid = true;
        feedback = Feedback.getText().toString();
        if(type.equals("User")) {
            name = Name.getText().toString();
            email = Email.getText().toString();
        }

        if(!type.equals("CDNet")&&(name.equals("")||email.equals(""))){
            new Messager(getActivity()).ToastMessage("Reset User and Try again!");
            valid=false;
        }

        if (feedback.equalsIgnoreCase("")) {
            Feedback.setError(getString(R.string.errorEntry));
            valid=false;
        }
        return valid;
    }

    private Map params(){
        Map<String, String> params = new HashMap<>();
        //params.put("api_location",API_LOCATION);
        params.put("message", feedback);
        //params.put("cname",sName);
        if(type.equals("User"))
            params.put("institution",institution);
        params.put("email",sEmail);
        return params;
    }

    private void showProgressDialog(){
        pDialog = new ProgressDialog(getActivity(),R.style.ProgressDialogTheme);
        if(type.equals("User"))
            pDialog.setMessage("Posting Feedback...");
        else
            pDialog.setMessage("Posting Announcement...");
        pDialog.setIndeterminate(true);
        //pDialog.setInverseBackgroundForced(true);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void hideProgressDialog(){
        if (pDialog!=null && pDialog.isShowing())
            pDialog.dismiss();
    }

    @OnClick(R.id.proceed)
    public void DoSend(){
        if (validate()){
            SendCommentOver();
        }
    }

    private void SendCommentOver(){
        showProgressDialog();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new
                Response.Listener<String>() {

                    @Override
                    public void onResponse(String resp) {
                        Log.d(TAG, resp);
                        try{

                            JSONObject s= new JSONObject(resp);
                            String response= s.getString(iXTechUtils.SUCCESS);

                            if (response.equalsIgnoreCase("1")){
                                hideProgressDialog();
                                getDialog().dismiss();
                                if(type.equals("User")) {
                                    new Messager(getActivity()).ToastMessage("Feedback was submitted " +
                                            "successfully");
                                    ((UserViewPostActivity) getActivity()).onRefresh();
                                }else{
                                    new Messager(getActivity()).ToastMessage("Announcement was " +
                                            "posted");
                                    ((CDNetViewPostActivity) getActivity()).onRefresh();
                                }
                            } else{
                                Log.d("ixTech", resp);
                                hideProgressDialog();
                                new Messager(getActivity()).ToastMessage("An Error occurred. " +
                                        "Please try again");
                            }
                        } catch (Exception e){
                            hideProgressDialog();
                            Log.e(TAG,"JSON parsing Error-->"+e.toString());
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Log.e(TAG, "Error: " + error.toString());

                if (error instanceof NoConnectionError)
                    new Messager(getActivity()).NoInternetDialog();
                else if (error instanceof TimeoutError)
                    new Messager(getActivity()).TimeoutErrorDialog();
                else
                    new Messager(getActivity()).ErrorDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                return params();
            }
        };
        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(jsonObjReq, TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void onResume() {
        // Store access variables for window
        Window window = getDialog().getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        Point size = getDisplaySize(display);
        // Set the width of the dialog proportional to 95% of the screen width
        window.setLayout((int) (size.x * 0.95), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    @TargetApi(13)
    private static Point getDisplaySize(final Display display) {
        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point;
    }
}