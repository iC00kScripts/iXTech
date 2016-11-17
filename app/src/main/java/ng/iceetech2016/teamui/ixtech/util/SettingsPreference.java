package ng.iceetech2016.teamui.ixtech.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by unorthodox on 21/08/16.
 * This is the settings preference class, which is used to enable and disable various options in
 * the app such as showing tips, resetting logged in user, enable/disable notifications, and others
 */
public class SettingsPreference {
    // Shared Preferences reference
    private SharedPreferences pref,catPref;
    // Editor reference for Shared preferences
    private SharedPreferences.Editor editor, catEditor;
    // Context
    private Context _context;
    // Shared pref mode
    private int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREFER_NAME = "iXTech";
    private static final String PREF_USER_CATEGORY = "AppUserCategory";
    //sharedpref Show Tip key
    private static final String TIPS_PREF = "ENABLE TIPS";
    private static final String LOGIN_PREF = "LOGIN STATUS";
    private static final String SEC_LOGIN_PREF = "ADMIN LOGIN STATUS";
    public static final String USER_NAME = "NAME";
    public static final String USER_EMAIL = "EMAIL";
    public static final String USER_PHONE="PHONE";
    public static final String USER_SCHOOL="SCHOOL";
    public static final String USER_CATEGORY = "CATEGORY";
    public static final String USER_INSTITUTION = "INSITUTION";

    public SettingsPreference(Context _context){
        this._context=_context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        catPref = _context.getSharedPreferences(PREF_USER_CATEGORY, PRIVATE_MODE);
        editor = pref.edit();
        catEditor=catPref.edit();
    }
    public void SetTipPref(Boolean bool){
        editor.putBoolean(TIPS_PREF, bool);
        editor.apply();
    }
    public boolean ShowTip(){
        return pref.getBoolean(TIPS_PREF, true);
    }

    public void SetUser(Boolean bool){
        editor.putBoolean(LOGIN_PREF,bool);
        editor.apply();
    }


    public void persistUser(String Phone, String School){
        editor.putString(USER_PHONE,Phone);
        editor.putString(USER_SCHOOL,School);
        editor.apply();
    }

    public boolean IsUserLogged(){
        return pref.getBoolean(LOGIN_PREF,false);
    }
    public boolean IsSecUserLogged(){
        return pref.getBoolean(SEC_LOGIN_PREF,false);
    }
    public void SetUserSession(String name,String email,String institution){
        editor.putString(USER_NAME,name);
        editor.putString(USER_EMAIL,email);
        editor.putString(USER_INSTITUTION,institution);
        editor.apply();
    }

    public void SetUserCategory( String  category ){
        catEditor.putString(USER_CATEGORY,category);
        catEditor.apply();
    }

    public String GetUserCategory(){
        return catPref.getString(USER_CATEGORY, "empty");
    }
    public HashMap<String,String> GetUserSession(){
        //Use hashmap to store user credentials and return to where needed
        HashMap<String, String> user = new HashMap<>();
        user.put(USER_NAME, pref.getString(USER_NAME, ""));
        user.put(USER_EMAIL,pref.getString(USER_EMAIL,""));
        user.put(USER_INSTITUTION,pref.getString(USER_INSTITUTION,""));
        return user;
    }
    public void ClearUserSession(){
        editor.clear();
        editor.apply();
    }
}
