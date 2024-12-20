package com.example.writelog;

import android.app.Application;
import android.os.Environment;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.flattener.DefaultFlattener;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

public class MApplication extends Application {

    private LeastRecentlyUsedCacheEvictor cacheEvictor;
    private ExoDatabaseProvider provider;
    private SimpleCache simpleCache;

    public static MApplication instance;


    public static MApplication getInstance() {


        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initXLog(MApplication.getInstance().getExternalCacheDir() + "/xlog");
    }

    long cacheSize = 500 * 1024 * 1024;


    private void initXLog(String logPath) {

        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(LogLevel.ALL)
                .enableThreadInfo()
                .enableStackTrace(5)
                .enableBorder()
                .build();
        Printer printer = new AndroidPrinter(true);
        Printer filePrinter = new FilePrinter.Builder(logPath)
                .flattener(new DefaultFlattener())
                .fileNameGenerator(new DateFileNameGenerator())
                .backupStrategy(new NeverBackupStrategy())
                .build();
        XLog.init(config, printer, filePrinter);

        cacheEvictor = new LeastRecentlyUsedCacheEvictor(cacheSize);
        provider = new ExoDatabaseProvider(this);
        simpleCache = new SimpleCache(new File(MApplication.getInstance().getExternalCacheDir() + "/exocache"), cacheEvictor, provider);

    }







}
