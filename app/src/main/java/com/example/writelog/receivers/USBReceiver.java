package com.example.writelog.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.elvishew.xlog.XLog;
import com.example.writelog.MessageWrap;
import com.example.writelog.MyFileUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class USBReceiver extends BroadcastReceiver {

    private static final String TAG = "USBReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        XLog.e(TAG  +  "usb 事件==>>> " + action);
        if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
            String mountPath = intent.getData().getPath();
            XLog.d(TAG  +  "u盘路径mountPath = " + mountPath);
            if (!TextUtils.isEmpty(mountPath)) {
                //读取到U盘路径再做其他业务逻辑
                MessageWrap messageWrap = new MessageWrap();
                messageWrap.setFilePath(mountPath);
                EventBus.getDefault().post(messageWrap);
            }
        } else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED) || action.equals(Intent.ACTION_MEDIA_EJECT)) {
//            Toast.makeText(context, "No services information detected !", Toast.LENGTH_SHORT).show();
        } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            //如果是开机完成，则需要调用另外的方法获取U盘的路径

            List<String> allExternalSdcardPath = MyFileUtils.getAllExternalSdcardPath();

            if (allExternalSdcardPath == null || allExternalSdcardPath.size() == 0) {
                XLog.d(TAG  +  "没有U盘挂载");
            }

            XLog.d(TAG  +  "挂载的U盘个数:" + allExternalSdcardPath.size());
            MessageWrap messageWrap = new MessageWrap();

            if (allExternalSdcardPath.size() == 1) {
                messageWrap.setFilePath(allExternalSdcardPath.get(0));
            } else {
                messageWrap.setList(allExternalSdcardPath);
            }
            EventBus.getDefault().post(messageWrap);

//            for (String s : allExternalSdcardPath) {
//                XLog.d(TAG  +  "U盘地址:" + s);
//            }

        }
    }
}
