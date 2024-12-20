//package com.example.writelog;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.app.job.JobInfo;
//import android.app.job.JobParameters;
//import android.app.job.JobScheduler;
//import android.app.job.JobService;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.util.Log;
//
//import com.example.writelog.utils.WriteUtil;
//
//public class JobSchedulerService extends JobService {
//
//    private static final String  TAG = "JobSchedulerService";
//
//    @Override
//    public boolean onStartJob(JobParameters jobParameters) {
//
//        // 返回true，表示该工作耗时，同时工作处理完成后需要调用onStopJob销毁（jobFinished）
//        // 返回false，任务运行不需要很长时间，到return时已完成任务处理
//        Log.e(TAG, "onStartJob ==>>>> ");
////        mJobHandler.sendMessage(Message.obtain(mJobHandler, 1));
//
//        WriteUtil.saveUserInfo();
//        scheduleJob(getApplicationContext()); // 重新调度作业
//        return false;
//
//
//    }
//
//    @Override
//    public boolean onStopJob(JobParameters jobParameters) {
//        // 有且仅有onStartJob返回值为true时，才会调用onStopJob来销毁job
//        // 返回false来销毁这个工作
//        return false;
//    }
//
//
////    // 创建一个handler来处理对应的job
////    private Handler mJobHandler = new Handler(new Handler.Callback() {
////        // 在Handler中，需要实现handleMessage(Message msg)方法来处理任务逻辑。
////        @Override
////        public boolean handleMessage(Message msg) {
//////            Toast.makeText(getApplicationContext(), "JobService task running", Toast.LENGTH_SHORT).show();
////            // 调用jobFinished
//////            jobFinished((JobParameters) msg.obj, false);
////
////
////            WriteUtil.saveUserInfo();
////            return true;
////        }
////    });
//
//    public static void scheduleJob(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
//            ComponentName componentName = new ComponentName(context, JobSchedulerService.class);
//            JobInfo jobInfo = new JobInfo.Builder(100, componentName)
//                    .setMinimumLatency(10000)
//                    .setOverrideDeadline(10000)
//                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                    .build();
//            jobScheduler.schedule(jobInfo);
//        } else {
//            // 在 Android N 以下版本，使用 AlarmManager 实现定时任务
//            Intent intent = new Intent(context, JobSchedulerService.class);
//            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent);
//        }
//    }
//
//}
