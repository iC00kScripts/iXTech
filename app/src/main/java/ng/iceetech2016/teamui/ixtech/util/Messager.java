package ng.iceetech2016.teamui.ixtech.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import ng.iceetech2016.teamui.ixtech.R;


/**
 * Created by unorthodox on 03/07/16.
 * * A  class to handle the generic display of messages using the Bottom dialog
 */
public class Messager {

    private Context _context; MaterialStyledDialog mdiag;
    public Messager(Context context){
        _context=context;
        mdiag = new MaterialStyledDialog(context);
    }

    /*public void showAboutDialog(){
        LayoutInflater inflater=(LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mdiag
                .setTitle(_context.getResources().getString(R.string.about_app))
                .setCustomView(inflater.inflate(R.layout.dialog_about_app,null),5,0,5,5)
                .withIconAnimation(true)
                //.setStyle(Style.HEADER_WITH_ICON)
                .setScrollable(true,10)
                .setCancelable(true)
                .withDialogAnimation(true, Duration.NORMAL.SLOW)
                .setIcon(ContextCompat.getDrawable(_context, R.drawable.ic_info))
                .setPositive(_context.getResources().getString(R.string.diag_ok),null)
                .build();
        mdiag.show();
    }*/
    public void showDialog(String title, String Message){
        mdiag
                .setIcon(_context.getResources().getDrawable(R.drawable.ic_info))
                .withDivider(true)
                .setTitle(title)
                .withIconAnimation(true)
                .setScrollable(true,6)
                .withDialogAnimation(true, Duration.NORMAL)
                .setCancelable(true)
                .setPositive(_context.getString(R.string.diag_ok),null)
                .setDescription(Message)
                .show();
    }
    public void showDialog(int title, int Message){
        mdiag
                .setIcon(_context.getResources().getDrawable(R.drawable.ic_info))
                .withDivider(true)
                .setTitle(_context.getString(title))
                .withIconAnimation(true)
                .setScrollable(true,6)
                .withDialogAnimation(true, Duration.NORMAL)
                .setCancelable(true)
                .setPositive(_context.getString(R.string.diag_ok),null)
                .setDescription(_context.getString(Message))
                .show();
    }

    public void ErrorDialog(){
        mdiag
                .setIcon(_context.getResources().getDrawable(R.drawable.ic_error))
                .setCancelable(false)
                .withDivider(true)
                .withIconAnimation(true)
                .setScrollable(true,6)
                .setDescription(_context.getString(R.string.error))
                .withDialogAnimation(true,Duration.SLOW)
                .setTitle("Server Error!")
                .setPositive(_context.getString(R.string.diag_ok),null)
                .show();
    }

    public void TimeoutErrorDialog(){
        mdiag
                .setIcon(_context.getResources().getDrawable(R.drawable.ic_error))
                .setCancelable(false)
                .withDivider(true)
                .withIconAnimation(true)
                .setScrollable(true,6)
                .setDescription(_context.getString(R.string.timeouterror))
                .withDialogAnimation(true,Duration.SLOW)
                .setTitle("Connection Timed Out!")
                .setPositive(_context.getString(R.string.diag_ok),null)
                .show();
    }

    public void NoInternetDialog(){
        mdiag
                .setIcon(_context.getResources().getDrawable(R.drawable.ic_error))
                .setCancelable(false)
                .withDivider(true)
                .setScrollable(true,6)
                .withIconAnimation(true)
                .setDescription(_context.getString(R.string.needinternet))
                .withDialogAnimation(true,Duration.SLOW)
                .setTitle("No Internet!")
                .setPositive(_context.getString(R.string.diag_ok),null)
                .show();
    }

    public void ToastMessage(String Message){
        Toast toast = Toast.makeText(_context, Message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public void ToastMessage(int Message){
        Toast toast = Toast.makeText(_context, _context.getString(Message), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
