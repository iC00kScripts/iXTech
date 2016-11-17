package ng.iceetech2016.teamui.ixtech.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.iceetech2016.teamui.ixtech.R;
import ng.iceetech2016.teamui.ixtech.util.SettingsPreference;


public class UserCategory extends AppCompatActivity {
    private SettingsPreference settingsPreference;
    private Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_category);
        ButterKnife.bind(this);
        settingsPreference = new SettingsPreference(this);

    }

    @OnClick(R.id.regularUser)
    public void RegularUser(){
        //display the regular user activity, and write user category to shared preference for future purpose
        settingsPreference.SetUserCategory(getString(R.string.norm_user));
        in = new Intent(getApplicationContext(), UserSignIn.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        finish();
    }

    @OnClick(R.id.cdnetUser)
    public void CDNetUser(){
        settingsPreference.SetUserCategory(getString(R.string.cdnet_user));
        in = new Intent(getApplicationContext(), AdminSignIn.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        finish();
    }
}
