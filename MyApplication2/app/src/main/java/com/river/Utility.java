package com.river;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public  class Utility {

    public static String getProperties(Context c, String key){
        Properties urlProp;
        Properties props = new Properties();
        try{
            InputStream in = c.getAssets().open("serverConfig");
            props.load(in);
        }catch(Exception e){
            e.printStackTrace();
        }
        return props.getProperty(key);
    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static boolean isServerReachable(Context ctx, String url_str) {
        HttpURLConnection conn = null;
        boolean isConn = false;
        try{
            URL url = new URL(url_str);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000*5);
            if(conn.getResponseCode() == 200) {
                isConn = true;
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return isConn;
    }

}
