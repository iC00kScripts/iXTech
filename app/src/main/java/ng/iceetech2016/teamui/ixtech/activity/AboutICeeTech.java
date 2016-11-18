package ng.iceetech2016.teamui.ixtech.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import ng.iceetech2016.teamui.ixtech.R;

public class AboutICeeTech extends AppCompatActivity {
    @Bind(R.id.webview) WebView wb;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_icee_tech);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("About CDNet");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        //MyWebViewClient
        WebSettings webSettings = wb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true); // allow pinch to zooom
        //webSettings.setDisplayZoomControls(false); // disable the default zoom controls on the
        // page

        wb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //wb.setWebViewClient(new MyWebViewClient());
        wb.loadUrl("file:///android_asset/index.html");
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            webView.loadUrl(url);
            return true;
        }


        @Override
        public void onPageStarted(WebView view, String dUrl, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, dUrl, favicon);
        }


        @Override
        public void onPageFinished(WebView view, String dUrl) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, dUrl);
        }
    }
}
