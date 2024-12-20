package com.example.writelog.receivers;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.elvishew.xlog.XLog;
import com.example.writelog.HomeActivity;
import com.example.writelog.MApplication;
import com.example.writelog.MessageWrap;
import com.example.writelog.MyFileUtils;
import com.example.writelog.service.MyJobService;
import com.example.writelog.service.MyService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class USBReceiver extends BroadcastReceiver {

    private static final String TAG = "USBReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        XLog.e(TAG + "usb 事件==>>> " + action);
        if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
//            String mountPath = intent.getData().getPath();
//            XLog.d(TAG + "u盘路径mountPath = " + mountPath);
//            if (!TextUtils.isEmpty(mountPath)) {
//                //读取到U盘路径再做其他业务逻辑
//                MessageWrap messageWrap = new MessageWrap();
//                messageWrap.setFilePath(mountPath);
//                EventBus.getDefault().post(messageWrap);
//            }
            Log.d(TAG, "onReceive:  + ACTION_MEDIA_MOUNTED");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    try {
//                        Intent startIntent = new Intent(context, HomeActivity.class);
//                        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(startIntent);
//                        Log.d(TAG, "onReceive: 启动HomeActivity");
//                    } catch (Exception e) {
//                        Log.d(TAG, "启动失败 ：" + e.getMessage());
//                        e.printStackTrace();
//                    }

                    startHome(context);

//                    scheduleJob(context);
                }
            }, 1000 * 10);

        } else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED) || action.equals(Intent.ACTION_MEDIA_EJECT)) {
//            Toast.makeText(context, "No services information detected !", Toast.LENGTH_SHORT).show();

            Log.d(TAG, "onReceive: ACTION_MEDIA_UNMOUNTED or  ACTION_MEDIA_EJECT");
        } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            //如果是开机完成，则需要调用另外的方法获取U盘的路径
            Log.d(TAG, "onReceive: 开机启动  android.intent.action.BOOT_COMPLETED");


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    try {
//                        Intent startIntent = new Intent(context, HomeActivity.class);
//                        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(startIntent);
//                        Log.d(TAG, "onReceive: 启动HomeActivity");
//                    } catch (Exception e) {
//                        Log.d(TAG, "启动失败 ：" + e.getMessage());
//                        e.printStackTrace();
//                    }

                    startHome(context);

//                    scheduleJob(context);
                }
            }, 1000 * 10);

//            List<String> allExternalSdcardPath = MyFileUtils.getAllExternalSdcardPath();

//            if (allExternalSdcardPath == null || allExternalSdcardPath.size() == 0) {
//                XLog.d(TAG  +  "没有U盘挂载");
//            }
//
//            XLog.d(TAG  +  "挂载的U盘个数:" + allExternalSdcardPath.size());
//            MessageWrap messageWrap = new MessageWrap();
//
//            if (allExternalSdcardPath.size() == 1) {
//                messageWrap.setFilePath(allExternalSdcardPath.get(0));
//            } else {
//                messageWrap.setList(allExternalSdcardPath);
//            }
//            EventBus.getDefault().post(messageWrap);

//            for (String s : allExternalSdcardPath) {
//                XLog.d(TAG  +  "U盘地址:" + s);
//            }

        }
    }

    private synchronized void startHome(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (isActivityRunning(activityManager, HomeActivity.class.getName())) {
            return;
        }
        Intent i = new Intent(MApplication.getInstance(), MyService.class);
        context.startForegroundService(i);

    }

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

    private void scheduleJob(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            //JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(context, MyJobService.class));

            JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(context, MyJobService.class))
                    .setPersisted(true)
                    .setMinimumLatency(1)
                    .build();
            jobScheduler.schedule(jobInfo);

            // 设置任务执行的条件，这里设置为设备开机后延迟一段时间执行
//            builder.setMinimumLatency(8000); // 延迟5秒，可根据实际情况调整
//            builder.setPersisted(true);
//            if (jobScheduler.schedule(builder.build()) <= 0) {
//                // 任务调度失败
//                Log.d(TAG, "任务调度失败");
//            }else {
//
//                Log.d(TAG, "任务调度成功");
//            }

        }
    }

}
