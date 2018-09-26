package com.river;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.content.Intent;

import com.example.ttom.myapplication.R;

public class SplashActivity extends AppCompatActivity {
    private static final int FAILURE = 0; // 失败
    private static final int SUCCESS = 1; // 成功
    private static final int OFFLINE = 2; // 如果支持离线阅读，进入离线模式

    private static final int SHOW_TIME_MIN = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... params) {
                int result;
//                ... ...
                long startTime = System.currentTimeMillis();
                result = loadingCache();
                long loadingTime = System.currentTimeMillis() - startTime;
                if (loadingTime < SHOW_TIME_MIN) {
                    try {
                        Thread.sleep(SHOW_TIME_MIN - loadingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                ... ...
                return result;
            }

            protected void onPostExecute(Integer result) {
                if (result == SUCCESS) {
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, com.example.jquerymap.Main2Activity.class);
                    startActivity(intent);
                    finish();
                    //两个参数分别表示进入的动画,退出的动画
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {

                }
            }

            ;
        }.execute(new Void[]{});
    }

    private int loadingCache() {
        if (!Utility.isNetworkAvailable(SplashActivity.this)) {
            //TODO
            return OFFLINE;
        }

//        if (Utility.isServerReachable(SplashActivity))
//        if (BaseApplication.mNetWorkState == NetworkUtils.NETWORN_NONE) {
//            return OFFLINE;
//        }
//        ... ...
        return SUCCESS;
    }


}
