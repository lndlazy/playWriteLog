package com.example.writelog;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.elvishew.xlog.XLog;
import com.example.writelog.utils.ThreadPoolUtil;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SourceView extends RelativeLayout {

    private static final String TAG = "SourceView:";
    private Context context;
    private ImageView imageView;
    private PlayerView playerView;

    private List<SourceBean> sourceList;
    private ScheduledFuture scheduledFuture;
    private Handler handler = new Handler(Looper.myLooper());

    public SourceView(Context context) {
        this(context, null);
    }

    public SourceView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SourceView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public SourceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(attrs, defStyleAttr);

    }

    public List<SourceBean> getSourceList() {
        return sourceList;
    }

    public void setSourceList(List<SourceBean> sourceList) {
        this.sourceList = sourceList;
    }

    private void init(AttributeSet attrs, int defStyleAttr) {

        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_custom_source, this, true);
//        addView(inflate);
        imageView = inflate.findViewById(R.id.imageView);
        playerView = inflate.findViewById(R.id.playerView);

        initPlayer();


    }


    private List<String> picList = new ArrayList<>();
    private List<String> videoList = new ArrayList<>();


    /**
     * 1：只有视频
     * 2：只有图片
     * 3：既有图片也有视频
     */
    private int currentPayType = 0;

    public void startPlay() {

        try {
            if (sourceList == null || sourceList.size() == 0) {
                XLog.d(TAG + "沒有資源文件");
                return;
            }

            stopPlayResource();

            classifySource();

            if (videoList.size() > 0 && picList.size() == 0) {
                //只有視頻
                currentPayType = 1;
                player.setRepeatMode(SimpleExoPlayer.REPEAT_MODE_ALL);//设置循环播放
                playVideo();

            } else if (picList.size() > 0 && videoList.size() == 0) {
                //只有图片
                currentPayType = 2;
                playPic();
            } else {
                // 既有图片也有视频
                currentPayType = 3;
                player.setRepeatMode(SimpleExoPlayer.REPEAT_MODE_OFF);
                playPic();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ExoPlayer player;

    private void initPlayer() {

        playerView.setUseController(false);
//        playerView.setControllerShowTimeoutMs();//可以设置一个显示时间
        // 初始化ExoPlayer实例
        player = new ExoPlayer.Builder(context.getApplicationContext()).build();

        // 将Player与PlayerView绑定
        playerView.setPlayer(player);
    }

    private int playIndex = 0;

    private synchronized void playPic() {
        try {
            imageView.setVisibility(VISIBLE);
            playerView.setVisibility(INVISIBLE);
            playIndex = 0;
            try {
                if (scheduledFuture != null)
                    scheduledFuture.cancel(true);
            } catch (Exception e) {
                XLog.e(TAG + "中斷圖片播放失敗! " + e.getMessage());
                e.printStackTrace();
            }

            if (picList.size() == 1) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(picList.get(playIndex)));
//                Glide.with(context).load(new File(picList.get(playIndex))).into(imageView);
            } else {
                scheduledFuture = ThreadPoolUtil.getInstance().scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    XLog.d(TAG + "playIndex::" + playIndex);
                                    imageView.setImageBitmap(BitmapFactory.decodeFile(picList.get(playIndex)));
//                                    Glide.with(context).load(new File(picList.get(playIndex))).into(imageView);

                                    if (playIndex >= picList.size() - 1) {

                                        if (currentPayType == 3) {
                                            stopPicPlay();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        XLog.d(TAG + "图片播放结束，开始播放视频??? delay");
                                                        playVideo();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, timeDelay);
                                            return;
                                        }
                                        playIndex = 0;
                                    } else {
                                        playIndex++;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }, 0, timeDelay, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static final long timeDelay = 1000 * 5;

    private void playVideo() {
        try {
            imageView.setVisibility(INVISIBLE);
            playerView.setVisibility(VISIBLE);
            imageView.setImageBitmap(null);
            playLocalVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    protected CacheDataSourceFactory factory;
//
//    private void t1() {
//        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//        TrackSelection.Factory videoTrackSelectionFactory =
//                new AdaptiveTrackSelection.Factory(bandwidthMeter);
//        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
//
//        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
//        factory = CacheDataSourceFactory.getFactory();
//        factory.init(context, 1024 * 1024 * 1024, 5 * 1024 * 1024);
//
//        List<MediaSource> mediaSources = new ArrayList<>();
//
//        for (int i = 0; i < videoList.size(); i++) {
//        MediaSource audioSource = new ExtractorMediaSource(Uri.fromFile(new File(videoList.get(i))),
//                factory, new DefaultExtractorsFactory(), null, null);
//            mediaSources.add(audioSource);
//        }
//        player.setPlayWhenReady(true);
//        player.addListener(listener);
////        player.setRepeatMode(SimpleExoPlayer.REPEAT_MODE_ALL);
////        player.pr
//        player.prepare(mediaSources, true, false);
//
//
////        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(), new DefaultLoadControl());
////        player.setPlayWhenReady(true);
//        playView.setPlayer(player);
//
//    }

    private void playLocalVideo() {

        // 构建一个DataSource工厂，用于播放本地文件
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context);

        List<MediaSource> mediaSources = new ArrayList<>();

        for (int i = 0; i < videoList.size(); i++) {
            // 构建MediaSource
            MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.fromFile(new File(videoList.get(i))));
            mediaSources.add(videoSource);
        }

        try {
            ConcatenatingMediaSource concatenatedSource = new ConcatenatingMediaSource(mediaSources.toArray(new MediaSource[mediaSources.size()]));
            // 准备播放器准备播放
            player.setPlayWhenReady(true);
            player.prepare(concatenatedSource, true, false);
            // 开始播放
            player.play();
            player.addListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onPlayerViewResume() {
        try {

            XLog.d("====onResume====");

            if (playerView != null && playerView.getPlayer() != null) {
                if (!playerView.getPlayer().isPlaying()) {
                    XLog.d(TAG + " 没有在播放 ");
                    player.setPlayWhenReady(true);
                    playerView.onResume();
                } else {
                    XLog.d(TAG + "playView 正在播放 ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onPlayerViewPause() {
        try {
            XLog.d("====onPause====");
            if (playerView != null) {
                //playView.getPlayer().stop();

                if (player != null) {
                    player.setPlayWhenReady(false);
                    //player.seekTo(0);
                }
                playerView.onPause();
            } else {
                XLog.d(TAG + "playView 为空 ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPlayResource() {
        stopPicPlay();
        stopVideoPlay();
    }

    public void stopVideoPlay() {
        try {

            if (playerView != null && playerView.getPlayer() != null) {
                playerView.getPlayer().stop();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPicPlay() {
        try {
            if (scheduledFuture != null)
                scheduledFuture.cancel(true);
        } catch (Exception e) {
            XLog.e(TAG + "中斷圖片播放失敗! " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void releaseExoPlayer() {
        try {

            if (playerView != null && playerView.getPlayer() != null) {
                playerView.getPlayer().stop();
                playerView.getPlayer().release();
                playerView.setPlayer(null);
            }

            if (player != null) {
                player.release();
                player = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Player.EventListener listener = new Player.EventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            //Log.d(TAG , "==>onPlayerStateChanged: " + playWhenReady + " " + playbackState);

            if (playbackState == Player.STATE_ENDED) {
//                    hideLoading();
                XLog.d(TAG + "播放状态 结束==>播放完成 " + playWhenReady + " " + playbackState);

                if (currentPayType == 3) {
                    //如果当前是图片和视频混合的 开始播放图片
                    stopVideoPlay();
                    playPic();
                }

            } else if (playbackState == Player.STATE_IDLE) {
                XLog.d(TAG + "播放状态 准备==>STATE_IDLE:  空闲 没有可播放的视频");
            } else if (playbackState == Player.STATE_BUFFERING) {
//                    showLoading("视频加载中...");
                XLog.d(TAG + "开始缓冲时间:");
                XLog.d(TAG + "播放状态 缓冲==>STATE_BUFFERING 需要加载");
            } else if (playbackState == Player.STATE_READY) {
                //hideLoading();

                XLog.d(TAG + "播放状态 准备==>STATE_READY 准备好了");
                XLog.d(TAG + "准备好了开始播放时间:");
            }

        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            Player.EventListener.super.onIsPlayingChanged(isPlaying);


        }

        @Override
        public void onPlayerError(PlaybackException error) {
            Player.EventListener.super.onPlayerError(error);

            XLog.e(TAG + "播放失败==>onPlayerError: " + error.getMessage());

        }


    };

    private void classifySource() {

        for (SourceBean sourceBean : sourceList) {

            if (sourceBean.getType() == 0) {
                //圖片
                picList.add(sourceBean.getPath());
            } else if (sourceBean.getType() == 1) {
                videoList.add(sourceBean.getPath());
            }

        }

    }


}
