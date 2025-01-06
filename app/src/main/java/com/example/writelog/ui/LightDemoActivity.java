package com.example.writelog.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.elvishew.xlog.XLog;
import com.example.writelog.R;
import com.example.writelog.utils.RootExecution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;
import pub.devrel.easypermissions.EasyPermissions;

public class LightDemoActivity extends AppCompatActivity {


    private static final String TAG = "LightDemoActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_light_demo);
        reqPermission();

         executeAdbCommand();


    }

    public void executeAdbCommand() {
//        try {
//            // 以Root权限执行命令
//            Process process = Runtime.getRuntime().exec("su");
//            // 获取命令输出流
//            OutputStream stdin = process.getOutputStream();
//            // 要执行的ADB命令
//            String adbCommand = "adb shell ls";
//            stdin.write(adbCommand.getBytes());
//            stdin.flush();
//            stdin.close();
//            // 读取命令执行结果
//            InputStream stderr = process.getErrorStream();
//            InputStream stdout = process.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                Log.d(TAG, "执行结果：" + line);
//            }
//            process.waitFor();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            if (process.waitFor() == 0) {
                // 确保进程正常等待输入
                outputStream.writeBytes("chmod 777 /system/app/app-release.apk\n");
                outputStream.flush();
                outputStream.writeBytes("echo 5 > sys/class/panel-simple/brightness\n");

                outputStream.flush();
                outputStream.writeBytes("exit\n");
                outputStream.flush();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    // 处理命令输出
                    Log.d(TAG, "输出结果:" + line);
                }
            } else {
                Log.d(TAG, " xxxx 没有获取到root权限  Failed to obtain root permission.");
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    Log.d(TAG, "错误信息Error: " + errorLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    String[] perms = {Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.SYSTEM_ALERT_WINDOW};

    private static final int RC_CAMERA_PERM = 33;

    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 1;

    private void reqPermission() {

        //EasyPermissions.hasPermissions()
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 权限已授予，执行相关操作
            //startCamera();
        } else {
            // 请求权限
            EasyPermissions.requestPermissions(this, getString(R.string.need_photo_permission),
                    RC_CAMERA_PERM, perms);
        }

    }

    public void one(View view) {
        adjustLight(1);
    }

    public void two(View view) {
        adjustLight(2);
    }

    public void three(View view) {
        adjustLight(3);
    }

    public void four(View view) {
        adjustLight(4);
    }

    public void five(View view) {
        adjustLight(5);
    }


//    public void adjustLight(int lightLevel) {
//
//
////        if (RootExecution.isRooted()) {
////            RootExecution.executeCommand("echo " + lightLevel + " > /sys/class/panel-simple/brightness");
////        } else {
////            System.out.println("设备未ROOT，请先ROOT设备");
////        }
//
////        try {
////            // 打开文件进行写入操作
////            BufferedWriter writer = new BufferedWriter(new FileWriter("/sys/class/panel-simple/brightness"));
////            // 写入亮度值
////            writer.write("" + lightLevel);
////            // 刷新并关闭写入流
////            writer.flush();
////            writer.close();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//    }

    //
    private void adjustLight(int lightLevel) {

//        try {
//            Process process = Runtime.getRuntime().exec("su");
//
//            //Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
//            DataOutputStream os = new DataOutputStream(process.getOutputStream());
//            os.writeBytes("echo " + lightLevel + " > sys/class/panel-simple/brightness");
//            os.writeBytes("exit\n");
//            os.flush();
//            Log.d(TAG, "执行完毕");
//            int exitValue = process.waitFor();
//            if (exitValue == 0) {
//                Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
//                Log.d("MyApp", "命令执行成功");
//            } else {
//                Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
//                Log.e("MyApp", "命令执行失败");
//            }
//        } catch (Exception e) {
//
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }


        new Thread(new Runnable() {
            @Override
            public void run() {

//                String apkPath = "/system/app/app-release.apk";
//                String command = "chmod 777 " + apkPath;
                String command = "echo " + lightLevel + " > sys/class/panel-simple/brightness";

                try {
                    List<String> run = Shell.SU.run(command);
                    Log.d(TAG, "执行结果:" + run.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

}
