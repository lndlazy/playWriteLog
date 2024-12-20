//package com.example.writelog.exocache;
//
//import android.content.Context;
//import android.os.Environment;
//
//import com.example.writelog.R;
//import com.example.writelog.utils.WriteUtil;
//import com.google.android.exoplayer2.upstream.DataSource;
//import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
//import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
//import com.google.android.exoplayer2.upstream.FileDataSource;
//import com.google.android.exoplayer2.upstream.HttpDataSource;
//import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
//import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
//import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
//import com.google.android.exoplayer2.upstream.cache.SimpleCache;
//import com.google.android.exoplayer2.util.Util;
//
//
//import java.io.File;
//
///**
// * Created by Android Studio.
// * User: linaidao
// * Date: 2024/3/4 19:44
// */
//public class CacheDataSourceFactory implements DataSource.Factory {
//    private Context context;
//    private DefaultDataSourceFactory defaultDatasourceFactory;
//    private long maxFileSize, maxCacheSize;
//
//    private static CacheDataSourceFactory factory = new CacheDataSourceFactory();
//
//    public static CacheDataSourceFactory getFactory() {
//        return factory;
//    }
//
//
//    public void init(Context context, long maxCacheSize, long maxFileSize) {
//
//        this.context = context;
//        this.maxCacheSize = maxCacheSize;
//        this.maxFileSize = maxFileSize;
//        String userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));
//        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//        defaultDatasourceFactory = new DefaultDataSourceFactory(this.context,
//                bandwidthMeter,
//
//        new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter));
//    }
//
//    private SimpleCache simpleCache;
//
//    @Override
//    public DataSource createDataSource() {
//        LeastRecentlyUsedCacheEvictor evictor = new LeastRecentlyUsedCacheEvictor(maxCacheSize);
//        if (simpleCache == null)
//            simpleCache = new SimpleCache(new File( Environment.getExternalStorageDirectory(), "media"), evictor);
//        return new CacheDataSource(simpleCache, defaultDatasourceFactory.createDataSource(),
//                new FileDataSource(), new CacheDataSink(simpleCache, maxFileSize),
//                CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null);
//    }
//
//
//    public void release() {
//        if (simpleCache!= null) {
//            simpleCache.release();
//            simpleCache = null;
//        }
//    }
//}
//
