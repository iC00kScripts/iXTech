package ng.iceetech2016.teamui.ixtech.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


/**
 * Created by unorthodox on 21/08/16.
 * A multipurpose static class for various util functions that might be required in the application
 */
public class iXTechUtils {
    public static final String url="http://www.uitilities.com/UICampusReportsApp/UICampusReportsAPI.php";
    public static final String SUCCESS="success";
    public static final String POST_TYPE="TYPE";
    private static final String TAG="RESOLVE_COMPLAINTS";

    public static void SaveJSONResponse(Context context,String json,String filename){
        //static method to persist most recent JSONArray response by writing it to text file
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput
                    (filename+".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(json);
            outputStreamWriter.close();
        }
        catch (IOException | NullPointerException e) {
            Log.e("SaveJSONException", "File write failed: " + e.toString());
        }
    }

    public static JSONArray ReadJSONFile(Context context, String filename){
        //static method to return the most recently saved JSONArray response
        JSONArray ret=null;

        try {
            InputStream inputStream = context.openFileInput(filename+".txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = new JSONArray(stringBuilder.toString());
            }
        } catch (FileNotFoundException |JSONException e) {
            Log.e("ReadJSONFile", e.toString());
        } catch (IOException e) {
            Log.e("ReadJSONFile", "Can not read file: " + e.toString());
        }
        return ret;
    }
}
