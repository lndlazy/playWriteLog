package com.example.writelog.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.writelog.MApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WriteUtil {


    private static final String TAG = "WriteUtil";

    public static boolean saveUserInfo() {

        try {
            // 判断当前的手机是否有sd卡
            String state = Environment.getExternalStorageState();

            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                // 已经挂载了sd卡
                return false;
            }

            File sdCardFile = MApplication.getInstance().getExternalCacheDir();
            File file = new File(sdCardFile, "writeData.txt");

            FileOutputStream fos = new FileOutputStream(file);

            String currentTime = new Date().toLocaleString();
            String data = currentTime;

            fos.write(data.getBytes());

            //Log.e(TAG, "写入数据时间成功" + currentTime);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean saveTimeInfo() {

        try {
            // 判断当前的手机是否有sd卡
            String state = Environment.getExternalStorageState();

            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                Log.d(TAG, "没有挂载sd卡");
                // 已经挂载了sd卡
                return false;
            }

            File sdCardFile = MApplication.getInstance().getExternalCacheDir();
            File file = new File(sdCardFile, "TimeData.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);

            String currentTime = new Date().toLocaleString();
            String data = currentTime;

            fos.write(data.getBytes());

            Log.e(TAG, "写入time数据时间成功" + currentTime);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static Map<String, String> getUserInfo(Context context) {

        try {
            // 判断当前的手机是否有sd卡
            String state = Environment.getExternalStorageState();

            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                // 已经挂载了sd卡
                return null;
            }

            File sdCardFile = MApplication.getInstance().getExternalCacheDir();

            File file = new File(sdCardFile, "data.txt");

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String text = br.readLine();

            br.close();

            if (!TextUtils.isEmpty(text)) {
                Map<String, String> userInfoMap = new HashMap<String, String>();
                String[] split = text.split("##");
                userInfoMap.put("number", split[0]);
                userInfoMap.put("password", split[1]);
                return userInfoMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
