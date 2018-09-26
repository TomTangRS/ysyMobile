
package com.example.jquerymap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.example.ttom.myapplication.R;

@SuppressLint("SetJavaScriptEnabled")
public class Main2Activity extends Activity {

    private WebView mWebViewMap;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mWebViewMap = (WebView) this.findViewById(R.id.webview_map);
        mWebViewMap.setWebViewClient(new MyWebViewClient());
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


        mWebViewMap.loadUrl("file:///android_asset/index.html");
//        mWebViewMap.loadUrl("http://mb.scszyjs.com/JT3/toIndex");
        mWebViewMap.addJavascriptInterface(this, "android");

        this.findViewById(R.id.btnJava2Js).setOnClickListener(new OnClickListener() {

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
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
        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        System.out.print("Print: shouldOverrideUrlLoading 2...");

        String url = request.getUrl().toString();
        if (url.startsWith("http:") || url.startsWith("https:")) {
            //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
            //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
            //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(url);
                return true;
            }
        }
        return true;
    }
}
