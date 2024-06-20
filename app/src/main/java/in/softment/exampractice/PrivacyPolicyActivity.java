package in.softment.exampractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String url = "https://www.lipsum.com";

        WebView myWebView = (WebView) findViewById(R.id.webpage);



        myWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                frameLayout.setVisibility(View.VISIBLE);
                return true;
            }

        });
        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int progress) {
                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);

                if (progress == 100) {
                    frameLayout.setVisibility(View.GONE);



                }
                super.onProgressChanged(webView, progress);
            }
        });

        WebSettings webSettings = myWebView.getSettings();


        webSettings.setDomStorageEnabled(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setAllowContentAccess(true);
        myWebView.setScrollbarFadingEnabled(false);
        myWebView.loadUrl(url);
        myWebView.setScrollbarFadingEnabled(false);
        frameLayout = findViewById(R.id.frame);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setMax(100);
        progressBar.setProgress(0);


        ProgressBar progressBar1 = new ProgressBar(this);
        progressBar1.setMax(100);
        progressBar1.setProgress(0);
    }
}
