package ng.iceetech2016.teamui.ixtech.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.javiersantos.bottomdialogs.BottomDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.iceetech2016.teamui.ixtech.R;
import ng.iceetech2016.teamui.ixtech.util.Messager;
import ng.iceetech2016.teamui.ixtech.util.iXTechUtils;

public class AdminMainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("iXTech: Admin Home");


    }

    @OnClick(R.id.UserPosts)
    public void UserPosts(){
        Intent i= new Intent(this,UserViewPostActivity.class);
        i.putExtra(iXTechUtils.POST_TYPE,"CDNet");
        startActivity(i);
    }

    @OnClick (R.id.cdnetPost)
    public void CDNetPosts(){
        Intent i= new Intent(this,CDNetViewPostActivity.class);
        i.putExtra(iXTechUtils.POST_TYPE,"CDNet");
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_cgnet:
                CDNetPosts();
                break;
            case R.id.menu_user:
                UserPosts();
                break;
            case R.id.menu_settings:
                new Messager(this).ToastMessage("Todo: Functionality");
                break;
            case R.id.menu_abt:
                new Messager(this).showAboutDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
