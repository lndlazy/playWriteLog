package com.example.writelog;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.writelog.utils.WriteUtil;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    protected PlayerView playView;
    protected ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        playView = findViewById(R.id.playView);

        reqPermission();

        //initializePlayer("https://minigame.vip/Uploads/images/2021/09/18/1631951892_page_img.mp4");

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        SystemClock.sleep(10000);
                        WriteUtil.saveTimeInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            Log.d(TAG, "====onResume====");

            if (playView != null && playView.getPlayer() != null) {
                if (!playView.getPlayer().isPlaying()) {
                    Log.d(TAG, " 没有在播放 ");
                    player.setPlayWhenReady(true);
                    playView.onResume();
                } else {
                    Log.d(TAG, "playView 正在播放 ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            Log.d(TAG, "====onPause====");
            if (playView != null) {
                //playView.getPlayer().stop();

                if (player != null) {
                    player.setPlayWhenReady(false);
                    //player.seekTo(0);
                }
                playView.onPause();
            } else {
                Log.d(TAG, "playView 为空 ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageWrap message) {

        if (message == null)
            return;

        if (!TextUtils.isEmpty(message.getFilePath())) {

        } else {

            List<String> list = message.getList();

            if (list == null)
                return;

            for (String pathStr : list) {

            }

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            Log.d(TAG, "====onDestroy====");

            EventBus.getDefault().unregister(this);

            releaseExoPlayer();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void releaseExoPlayer() {
        try {
//            if (factory != null) {
//                factory.release();
//            }

            if (playView != null && playView.getPlayer() != null) {
                playView.getPlayer().stop();
                playView.getPlayer().release();
                playView.setPlayer(null);
            }

            if (player != null) {
                player.release();
                player = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private ExoPlayer player;

    private int errorCount = 0;

    private Player.EventListener listener = new Player.EventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            //Log.d(TAG , "==>onPlayerStateChanged: " + playWhenReady + " " + playbackState);

            if (playbackState == Player.STATE_ENDED) {
//                    hideLoading();
                Log.d(TAG, "播放状态 结束==>播放完成 " + playWhenReady + " " + playbackState);
            } else if (playbackState == Player.STATE_IDLE) {
                Log.d(TAG, "播放状态 准备==>STATE_IDLE:  空闲 没有可播放的视频");
            } else if (playbackState == Player.STATE_BUFFERING) {
//                    showLoading("视频加载中...");
                Log.d(TAG, "开始缓冲时间:");
                Log.d(TAG, "播放状态 缓冲==>STATE_BUFFERING 需要加载");
            } else if (playbackState == Player.STATE_READY) {
                //hideLoading();
                dialog.dismiss();
                Log.d(TAG, "播放状态 准备==>STATE_READY 准备好了");
                Log.d(TAG, "准备好了开始播放时间:");
            }

        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            Player.EventListener.super.onIsPlayingChanged(isPlaying);

            if (isPlaying)
                errorCount = 0;

        }

        @Override
        public void onPlayerError(PlaybackException error) {
            Player.EventListener.super.onPlayerError(error);

            Log.e(TAG, "播放失败==>onPlayerError: " + error.getMessage());
            errorCount++;

            //播放失败此次在5次呢 重新加载播放
//            if (errorCount < 5) {
//                getPlayInfoFromCache();
//            }

        }


        //        @Override
//        public void onPlayerError(ExoPlaybackException error) {
//            Player.EventListener.super.onPlayerError(error);
//
//            Log.d(TAG , "播放失败==>onPlayerError: " + error.getMessage());
//            errorCount++;
//
//            //播放失败此次在5次呢 重新加载播放
//            if (errorCount < 5) {
//                getPlayInfoFromCache();
//            }
//
//        }


    };


//    private void getPlayInfoFromCache() {
//        try {
//            String playCache = SelfKVUtil.getPlayCache();
//
//            if (!TextUtils.isEmpty(playCache)) {
//                PlayInfo playInfo = new Gson().fromJson(playCache, PlayInfo.class);
//                if (playInfo != null) {
//                    playType(playInfo);
//                } else {
//                    Log.d(TAG, "，xx获取缓存播放信息为空");
////                    tvNoProgram.setVisibility(View.VISIBLE);
//                }
//            } else {
//                Log.d(TAG, "，获取缓存播放信息为空");
////                tvNoProgram.setVisibility(View.VISIBLE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d(TAG, "，获取缓存播放信息失败");
//        }
//
//    }


    private void initializePlayer(String url) {

        getMediaSource();

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setTitle("视频加载中...");
        }
        dialog.show();

        Log.d(TAG, "开始初始化:");

//        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context);
//        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//        TrackSelection.Factory videoTrackSelectionFactory =
//                new AdaptiveTrackSelection.Factory(bandwidthMeter);
//        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
//
//        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
//        factory = CacheDataSourceFactory.getFactory();
//        factory.init(SplashActivity.this, 1024 * 1024 * 1024, 5 * 1024 * 1024);
//        MediaSource audioSource = new ExtractorMediaSource(Uri.parse(url),
//                factory, new DefaultExtractorsFactory(), null, null);

        // 设置PlayerView的控制器
        playView.setUseController(true);

        // 初始化ExoPlayer实例
        player = new ExoPlayer.Builder(this).build();

        // 将Player与PlayerView绑定
        playView.setPlayer(player);

//        player.addListener(listener);
//        player.setRepeatMode(SimpleExoPlayer.REPEAT_MODE_ALL);
//        player.prepare(audioSource, true, false);
//
////        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(), new DefaultLoadControl());
////        player.setPlayWhenReady(true);
//        playView.setPlayer(player);

//        Uri playUri = Uri.parse("file:///sdcard/Pictures/WeiXin/a.mp4");
//        Uri playUri = Uri.parse(url);
//        DefaultHttpDataSourceFactory defaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory("user-agent");
//        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(playUri);
//        player.setRepeatMode(SimpleExoPlayer.REPEAT_MODE_ALL);
//        player.prepare(mediaSource, true, false);
    }

    private void playLocalVideo(String filePath) {
        // 构建一个DataSource工厂，用于播放本地文件
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this);

        // 构建MediaSource
        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.fromFile(new File(filePath)));

        // 准备播放器准备播放
        player.setPlayWhenReady(true);
        player.prepare(videoSource);
        // 开始播放
        player.play();
        player.addListener(listener);
    }

  String[] perms = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    private void reqPermission() {

        Log.d(TAG, "大于23？？" + (Build.VERSION.SDK_INT >= 23));

        if (Build.VERSION.SDK_INT >= 23) {// 6.0

            boolean hasAll = true;
            for (String p : perms) {

                if (ContextCompat.checkSelfPermission(MainActivity.this, p) != PackageManager.PERMISSION_GRANTED) {
                    hasAll = false;
                    Log.d(TAG, "----");
                    ActivityCompat.requestPermissions(this, perms, 0x02);
                    break;
                }
            }

            Log.d(TAG, "xxxx：：" + hasAll);

            if (hasAll) {
//                startJobScheduler();
                getMediaSource();
            }
        } else {
//            startJobScheduler();
            getMediaSource();
        }

    }

    // 带回授权结果

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "====onActivityResult==== ??? ");
        if (requestCode == 0x02) {
            // 检查是否有权限
//            if (Environment.isExternalStorageManager()) {
                Log.e(TAG, "授权成功??");
                // 授权成功
//                startJobScheduler();

                getMediaSource();


//            } else {
//                Log.e(TAG, "授权失败");
//
//                // 授权失败
//            }
        }else {
            Log.d(TAG, "requestCode ?? " + requestCode);
        }

    }

    private void getMediaSource() {

        Log.d(TAG, "开始获取多媒体资源");
        List<SourceBean> imagesAndVideosFromFolder = MyFileUtils.getImagesAndVideosFromFolder(Environment.getExternalStorageDirectory() + "/bai");

        if (imagesAndVideosFromFolder == null || imagesAndVideosFromFolder.size() == 0) {
            Log.d(TAG, "没有多媒体资源");
            return;
        }


//        for (String folder : imagesAndVideosFromFolder) {
//            Log.d(TAG, "多媒体资源:" + folder);
//        }
    }

//    private void startJobScheduler() {
////        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
////        JobInfo.Builder builder = new JobInfo.Builder(10,
////                new ComponentName(getPackageName(), JobSchedulerService.class.getName()));
////
////
////        // 7.0 以下的版本, 可以每隔 5000 毫秒执行一次任务
////        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
////            builder.setPeriodic(10000);
////        } else {
////            // 7.0 以上的版本 , 设置延迟 5 秒执行
////            // 该时间不能小于 JobInfo.getMinLatencyMillis 方法获取的最小值
////            builder.setMinimumLatency(10000);
////        }
////
////        // 开启定时任务
////        mJobScheduler.schedule(builder.build());
//        JobSchedulerService.scheduleJob(this);
//    }
}