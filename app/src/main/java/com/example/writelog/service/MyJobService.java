package com.example.writelog.service;

import android.app.ActivityManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.example.writelog.HomeActivity;
import com.example.writelog.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MyJobService extends JobService {

    private String TAG = "MyJobService";
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            Log.d(TAG, "handleMessage:   xxx   ");
            // 在这里启动你的服务或者Activity
            try {

//                try {
//                    // 构建adb shell am start命令来启动指定的Activity
//                    String adbCommand = "am start -n com.example.writelog/com.example.writelog.HomeActivity";
//                    String[] cmds = {"su", "am start -n com.example.writelog/com.example.writelog.HomeActivity"};
//                    // 使用Runtime类执行命令
//                    Process process = Runtime.getRuntime().exec(cmds);
//                    // 可以获取命令执行的输出流等进行后续处理，这里简单忽略
//                    int exitCode = process.waitFor();
//                    if (exitCode == 0) {
//                        // 命令执行成功，意味着尝试启动Activity的操作理论上已执行
//                        Log.d(TAG, "handleMessage:   启动成功   ");
//                    } else {
//                        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//                        String line;
//                        while ((line = errorReader.readLine()) != null) {
//                            Log.d(TAG, "Error: " + line);
//                        }
//                        // 命令执行失败，可能存在多种原因，比如adb未正确配置、Activity不存在等
//                        Log.d(TAG, "handleMessage:   启动失败   " + exitCode);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                        while (!isActivityRunning(activityManager, "com.example.writelog.HomeActivity")) {
//
//
////                                    try {
////                                        // 构建命令参数列表，模拟adb shell am start命令的参数
////                                        List<String> commandList = new ArrayList<>();
////                                        commandList.add("adb");
////                                        commandList.add("shell");
////                                        commandList.add("am");
////                                        commandList.add("start");
////                                        commandList.add("-n");
////                                        commandList.add("com.example.writelog/com.example.writelog.HomeActivity");
////
////                                        ProcessBuilder processBuilder = new ProcessBuilder(commandList);
////                                        Process process = processBuilder.start();
////
////                                        // 读取命令执行的输出信息
////                                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
////                                        String line;
////                                        while ((line = reader.readLine())!= null) {
////                                            // 可以在这里对输出内容进行分析处理，比如查看是否有错误提示等
////                                            System.out.println(line);
////                                        }
////
////                                        int exitCode = process.waitFor();
////                                        if (exitCode == 0) {
////                                            // 命令执行成功
////                                        } else {
////                                            // 命令执行失败
////                                        }
////                                    } catch (Exception e) {
////                                        e.printStackTrace();
////                                    }
//
////                            ComponentName componentName = new ComponentName("com.example.writelog", "com.example.writelog.HomeActivity");
////                            Intent intent = new Intent();
////                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                            intent.setComponent(componentName);
//
//
////                            try {
////                                // 构建adb shell am start命令来启动指定的Activity
////                                String adbCommand = "am start -n com.example.writelog/com.example.writelog.HomeActivity";
////                                // 使用Runtime类执行命令
////                                Process process = Runtime.getRuntime().exec(adbCommand);
////                                // 可以获取命令执行的输出流等进行后续处理，这里简单忽略
////                                int exitCode = process.waitFor();
////                                if (exitCode == 0) {
////                                    // 命令执行成功，意味着尝试启动Activity的操作理论上已执行
////                                } else {
////                                    // 命令执行失败，可能存在多种原因，比如adb未正确配置、Activity不存在等
////                                }
////                            } catch (Exception e) {
////                                e.printStackTrace();
////                            }
//
////                            Intent startIntent = new Intent(getApplicationContext(), HomeActivity.class);
//////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//////                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//////            }
////                            startActivity(startIntent);
//                            Log.d(TAG, "启动首页");
//                            SystemClock.sleep(1000 * 5);
//                        }
//
//                    }
//                }).start();

//                Intent startIntent = new Intent();
//                startIntent.setAction("com.home.OPEN_HOME_PAGE");
//                startIntent.addCategory("android.intent.category.DEFAULT");
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            }
//                startActivity(startIntent);
//                Log.d(TAG, "启动首页");


                            Intent startIntent = new Intent(getApplicationContext(), HomeActivity.class);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            }
                            startActivity(startIntent);
                            Log.d(TAG, "启动首页");






            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "启动失败 ：" + e.getMessage());
            }
            return true;
        }
    });


    private boolean isActivityRunning(ActivityManager activityManager, String activityClassName) {
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo taskInfo : runningTasks) {
            if (taskInfo.baseActivity.getClassName().equals(activityClassName)) {
                Log.d(TAG, "启动了HomeActivity");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Log.d(TAG, "onStartJob:   xxx   ");
        mHandler.sendEmptyMessage(0);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        Log.d(TAG, "onStopJob:   xxx   ");
        mHandler.removeMessages(0);
        return true;
    }
}