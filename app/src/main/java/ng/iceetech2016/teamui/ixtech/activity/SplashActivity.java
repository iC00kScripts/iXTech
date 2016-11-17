package ng.iceetech2016.teamui.ixtech.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ng.iceetech2016.teamui.ixtech.R;
import ng.iceetech2016.teamui.ixtech.util.SettingsPreference;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i;

                String userCategory= new SettingsPreference(getApplicationContext()).GetUserCategory();
                if(userCategory.equalsIgnoreCase("empty")){
                    i = new Intent(getApplicationContext(), UserCategory.class);
                }
                else if(userCategory.equalsIgnoreCase("admin")) {
                    if (new SettingsPreference(getApplicationContext()).IsSecUserLogged())
                        i= new Intent(getApplicationContext(),AdminMainActivity.class);
                    else
                        i= new Intent(getApplicationContext(),AdminSignIn.class);
                }
                else{
                    //check if user is previously logged in or not
                    if(new SettingsPreference(getApplicationContext()).IsUserLogged())
                        i = new Intent(getApplicationContext(), UserMainActivity.class);
                    else
                        i = new Intent(getApplicationContext(), UserSignIn.class);
                }
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
