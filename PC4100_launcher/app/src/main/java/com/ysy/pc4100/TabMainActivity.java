package com.ysy.pc4100;

import android.app.TabActivity;
import android.content.Intent;
import android.widget.TabHost;
import android.os.Bundle;

import com.ycexample.ycapp.YcActivity;

public class TabMainActivity extends TabActivity {

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabHost = getTabHost();

        addTab("act1", "界面1", MainLauncher.class);
        addTab("act2", "界面2", SettingsActivity.class);
        addTab("act3", "界面3", MainLauncher.class);

        setContentView(tabHost);
    }

    /**
     * 添加Activity标签
     * @param tag	标识
     * @param title	标签标题
     * @param clazz 激活的界面
     */
    private void addTab(String tag, String title, Class clazz) {
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setIndicator(title);

        Intent intent = new Intent(getApplicationContext(),clazz);
        tabSpec.setContent(intent);
        tabHost.addTab(tabSpec);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
