package com.example.writelog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.blankj.utilcode.util.FileUtils;
import com.elvishew.xlog.XLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyFileUtils {


    private static final String TAG = "MyFileUtils:";

    public static List<String> getAllExternalSdcardPath() {
        List<String> PathList = new ArrayList<String>();

        String firstPath = Environment.getExternalStorageDirectory().getPath();
         XLog.d(TAG +"getAllExternalSdcardPath , firstPath = " + firstPath);

        try {
            // 运行mount命令，获取命令的输出，得到系统中挂载的所有目录
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                // 将常见的linux分区过滤掉
                if (line.contains("proc") || line.contains("tmpfs") || line.contains("media") || line.contains("asec") || line.contains("secure") || line.contains("system") || line.contains("cache")
                        || line.contains("sys") || line.contains("data") || line.contains("shell") || line.contains("root") || line.contains("acct") || line.contains("misc") || line.contains("obb")) {
                    continue;
                }

                // 下面这些分区是我们需要的
                if (line.contains("fat") || line.contains("fuse") || (line.contains("ntfs"))) {
                    // 将mount命令获取的列表分割，items[0]为设备名，items[1]为挂载路径
                    String items[] = line.split(" ");
                    if (items != null && items.length > 1) {
                        String path = items[1].toLowerCase(Locale.getDefault());
                        // 添加一些判断，确保是sd卡，如果是otg等挂载方式，可以具体分析并添加判断条件
                        if (path != null && !PathList.contains(path) && path.contains("sd"))
                            PathList.add(items[1]);
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!PathList.contains(firstPath)) {
            PathList.add(firstPath);
        }

        return PathList;
    }


    // 获取指定目录下所有图片和视频文件的路径
    public static List<SourceBean> getImagesAndVideosFromFolder(String directoryPath) {
        List<SourceBean> mediaFiles = new ArrayList<>();

        XLog.d(TAG + "查找这个目录下的文件:" + directoryPath);

        List<File> files1 = FileUtils.listFilesInDir(directoryPath);
         XLog.d(TAG +"目录下的文件个数:" + files1.size());
        for (File file : files1) {


             XLog.d(TAG +"目录下的文件:" + file.getAbsolutePath() + "," + file.getName());
            boolean isImageFile = isImageFile(file);

            if (isImageFile) {
                SourceBean sourceBean = new SourceBean(0, file.getAbsolutePath(), 0);
                mediaFiles.add(sourceBean);
                 XLog.d(TAG +"是图片文件");
                continue;
            }

            boolean isVideoFile = isVideoFile(file);

            if (isVideoFile) {
                 XLog.d(TAG +"是视频文件");
                SourceBean sourceBean = new SourceBean(1, file.getAbsolutePath(), 0);
                mediaFiles.add(sourceBean);

            }

        }

//        while (cursor.moveToNext()) {
//            @SuppressLint("Range") String dataColumn = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            mediaFiles.add(dataColumn);
//        }
//        cursor.close();

//        // 直接遍历文件系统
//        File dir = new File(directoryPath);
//        if (dir.exists() && dir.isDirectory()) {
//            File[] files = dir.listFiles();
//            if (files != null) {
//                for (File file : files) {
//                    if (file.isFile()) {
//                        String mimeType = getMimeType(file.getAbsolutePath());
//                        if ("image/".startsWith(mimeType) || "video/".startsWith(mimeType)) {
//                            mediaFiles.add(file.getAbsolutePath());
//                        }
//                    }
//                }
//            }
//        }
        return mediaFiles;


    }

    // 获取文件的MIME类型
    private static String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }


    private static boolean isImageFile(File file) {
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        return mimeType != null && mimeType.startsWith("image");
    }

    private static boolean isVideoFile(File file) {
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        return mimeType != null && mimeType.startsWith("video");
    }

}
