package com.ysy.pc4100;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ycexample.ycapp.YcActivity;

public class MainLauncher extends Activity {
    private WebView mWebViewMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_launcher);
        mWebViewMap = (WebView) this.findViewById(R.id.webview_map);
        mWebViewMap.setWebViewClient(new MyWebViewClient());


        mWebViewMap.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                // TODO Auto-generated method stub
                return super.onJsAlert(view, url, message, result);
            }
        });


        WebSettings webSettings = mWebViewMap.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setUseWideViewPort(false);
//        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);




        mWebViewMap.loadUrl("file:///android_asset/web/index.html");
//        mWebViewMap.loadUrl("https://www.sina.com.cn/");
        mWebViewMap.addJavascriptInterface(this, "android");

        this.findViewById(R.id.btnJava2Js).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // --java调用js 格式：javascript:方法名字(参数)
                mWebViewMap.loadUrl("javascript:javacalljswithargs("+ "'java2js'" +")");
            }
        });
    }

    @JavascriptInterface
    public void startFunction() {
        Toast.makeText(this, "startFunction ", Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void startFunction(String s){
        Toast.makeText(this, "startFunction " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(this.getClass().getName(),"key is pressed: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebViewMap.canGoBack()) {

            if (mWebViewMap.canGoBack()){
                Log.d(this.getClass().getName(), "Current Url is : " + mWebViewMap.getUrl());
                mWebViewMap.goBack();// 返回前一个页面
                Log.d(this.getClass().getName(), "After back, current Url is : " + mWebViewMap.getUrl());
                return true;
            }else {
                onBackPressed();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private long clickTime = 0; // 第一次点击的时间

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(this, "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
        }

    }


}

class MyWebViewClient extends WebViewClient {

    //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
    //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
    //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        System.out.print("Print: shouldOverrideUrlLoading...");

        if( url.startsWith("http:") || !url.startsWith("https:") ) {
            view.loadUrl(url);
            return true;
        }
        return false;
    }

}
