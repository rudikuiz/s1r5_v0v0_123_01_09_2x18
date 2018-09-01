package com.piramidsoft.sirs;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.piramidsoft.sirs.Utils.AppConf;
import com.piramidsoft.sirs.Utils.OwnProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.piramidsoft.sirs.Utils.AppConf.URL_TOP_UP;

public class WebTopUpActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.web)
    WebView mWebView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    String nama, nohp, nominal;
    OwnProgressDialog loading;
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_top_up);
        ButterKnife.bind(this);

        swipe.setOnRefreshListener(this);
        webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.getUseWideViewPort();

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                swipe.setRefreshing(false);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                loading.dismiss();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(URL_TOP_UP);
                swipe.setRefreshing(false);
                if (URL_TOP_UP.contains("")){
                    finish();
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loading.dismiss();
            }
        });
    }


    @Override
    public void onRefresh() {
        mWebView.reload();
    }
}
