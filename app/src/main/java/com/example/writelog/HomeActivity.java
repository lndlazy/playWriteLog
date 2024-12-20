package com.example.writelog;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.speech.RecognitionService;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.FileUtils;
import com.elvishew.xlog.XLog;
import com.example.writelog.utils.WriteUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends Activity {

    private static final String TAG = "HomeActivity:";
    private Handler handler = new Handler(Looper.myLooper());
    private SourceView sourceView;

    private static final String SD_PATH = "/udisk0/bai";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);

        EventBus.getDefault().register(this);

        reqPermission();

//        RecognitionService

        sourceView = findViewById(R.id.sourceView);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                EventBus.getDefault().post(new MessageWrap());
//
//                if (sourceView != null) {
//                    sourceView.setSourceList(MyFileUtils.getImagesAndVideosFromFolder(Environment.getExternalStorageDirectory() + "/bai"));
//                    sourceView.startPlay();
//                }
//            }
//        }, 2000);


        if (sourceView != null) {
            sourceView.stopPlayResource();

            sourceView.setSourceList(MyFileUtils.getImagesAndVideosFromFolder("/mnt/usb_storage/USB_DISK1" + SD_PATH));
            sourceView.startPlay();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        SystemClock.sleep(1000);
                        WriteUtil.saveTimeInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();


//        String msg114 = "昼/夜";
//        String s114 = chineseToASCII(msg114);
//        Log.d(TAG, msg114 + "==>:" + s114);
//
//        String msg11 = "昼";
//        String s11 = chineseToASCII(msg11);
//        Log.d(TAG, msg11 + "==>:" + s11);
//
//
//        String msg112 = "夜";
//        String s112 = chineseToASCII(msg112);
//        Log.d(TAG, msg112 + "==>:" + s112);
//
//        String msg113 = "导航";
//        String s113 = chineseToASCII(msg113);
//        Log.d(TAG, msg113 + "==>:" + s113);
//
//        String msg = "亮度";
//        String s = chineseToASCII(msg);
//        Log.d(TAG, msg + "==>:" + s);
//
// String msg1 = "备用";
//        String s1 = chineseToASCII(msg1);
//        Log.d(TAG, msg1 + "==>:" + s1);
//
// String msg2 = "退出";
//        String s2 = chineseToASCII(msg2);
//        Log.d(TAG, msg2 + "==>:" + s2);
// String msg3 = "确认";
//        String s3 = chineseToASCII(msg3);
//        Log.d(TAG, msg3 + "==>:" + s3);


    }


//    public static String chineseToASCII(String str) {
////        StringBuilder sb = new StringBuilder();
////        byte[] bytes = str.getBytes();
////        for (byte b : bytes) {
////            sb.append(b);
////        }
////        return sb.toString();
//
//        StringBuilder sb = new StringBuilder();
//        char[] charArray = str.toCharArray();
//        for (char c : charArray) {
//            sb.append((int) c);
//        }
//        return sb.toString();
//    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sourceView != null)
            sourceView.onPlayerViewResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (sourceView != null)
            sourceView.onPlayerViewPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);

        if (sourceView != null)
            sourceView.releaseExoPlayer();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageWrap message) {

        XLog.d("onGetMessage====>>");
        if (message == null)
            return;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageReceiver(message);
            }
        }, 1000);

    }

    private void messageReceiver(MessageWrap message) {
        if (!TextUtils.isEmpty(message.getFilePath())) {
            XLog.d(TAG + "外接u盘地址:" + message.getFilePath());
            if (sourceView != null) {
                //  /mnt/usb_storage/USB_DISK1
                sourceView.stopPlayResource();
                sourceView.setSourceList(MyFileUtils.getImagesAndVideosFromFolder(message.getFilePath() + SD_PATH));
                sourceView.startPlay();
            }
        } else {

            List<String> list = message.getList();

            if (list == null)
                return;

            for (String pathStr : list) {

                boolean fileExists = FileUtils.isFileExists(pathStr + SD_PATH);
                if (fileExists) {
                    if (sourceView != null) {
                        sourceView.setSourceList(MyFileUtils.getImagesAndVideosFromFolder(message.getFilePath() + SD_PATH));
                        sourceView.startPlay();
                    }
                    break;
                }

            }

        }
    }


    String[] perms = {Manifest.permission.MANAGE_EXTERNAL_STORAGE ,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};

    private static final int RC_CAMERA_PERM = 33;


    private void reqPermission() {

        XLog.d(TAG + "大于23？？" + (Build.VERSION.SDK_INT >= 23));
        //ActivityCompat.requestPermissions(this, perms, 0x02);


        //EasyPermissions.hasPermissions()
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 权限已授予，执行相关操作
            //startCamera();
        } else {
            // 请求权限
            EasyPermissions.requestPermissions(this, getString(R.string.need_photo_permission),
                    RC_CAMERA_PERM, perms);
        }

//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            File newFile = new File("/storage/emulated/0/test.txt");
//            try {
//                if (newFile.createNewFile()) {
//                    // 文件创建成功
//                    Log.d(TAG, "文件创建成功");
//
//                } else {
//                    // 文件创建失败，可能是权限或其他原因
//                    Log.d(TAG, "文件创建失败，可能是权限或其他原因");
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            // 存储设备不可用
//            Log.d(TAG, "存储设备不可用");
//        }


//        //检查是否有读写权限
//        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Log.d(TAG, "没有读权限");
//            ActivityCompat.requestPermissions(this, perms, 0x02);
//        } else
//            Log.d(TAG, "有读权限");
//        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Log.d(TAG, "没有写权限");
//            ActivityCompat.requestPermissions(this, perms, 0x02);
//        } else
//            Log.d(TAG, "有写权限");


//        if (Build.VERSION.SDK_INT >= 23) {// 6.0
//
//            boolean hasAll = true;
//            for (String p : perms) {
//
//                if (ContextCompat.checkSelfPermission(HomeActivity.this, p) != PackageManager.PERMISSION_GRANTED) {
//                    hasAll = false;
//                    Log.d(TAG  + "----");
//                    ActivityCompat.requestPermissions(this, perms, 0x02);
//                    break;
//                }
//            }
//
//            Log.d(TAG  + "xxxx：：" + hasAll);
//
//
//        } else {
//
//        }

    }

    // 带回授权结果

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        XLog.d(TAG + "====onActivityResult====   ");
        if (requestCode == 0x02) {
            // 检查是否有权限
//            if (Environment.isExternalStorageManager()) {
            XLog.e(TAG + "授权成功??");
            // 授权成功


//            } else {
//                Log.e(TAG  + "授权失败");
//
//                // 授权失败
//            }
        } else {
            XLog.d(TAG + "requestCode ?? " + requestCode);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        System.exit(0);
    }
}
