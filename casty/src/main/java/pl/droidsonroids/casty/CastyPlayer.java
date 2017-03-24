package pl.droidsonroids.casty;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;

public class CastyPlayer {
    private RemoteMediaClient remoteMediaClient;
    private OnMediaLoadedListener onMediaLoadedListener;

    CastyPlayer(OnMediaLoadedListener onMediaLoadedListener) {
        this.onMediaLoadedListener = onMediaLoadedListener;
    }

    void setRemoteMediaClient(RemoteMediaClient remoteMediaClient) {
        this.remoteMediaClient = remoteMediaClient;
    }

    public void play() {
        if (remoteMediaClient != null && remoteMediaClient.isPaused()) remoteMediaClient.play();
    }

    public void pause() {
        if (remoteMediaClient != null && remoteMediaClient.isPlaying()) remoteMediaClient.pause();
    }

    public void seek(long time) {
        if (remoteMediaClient != null) remoteMediaClient.seek(time);
    }

    public void togglePlayPause() {
        if (remoteMediaClient != null) {
            if (remoteMediaClient.isPlaying()) {
                remoteMediaClient.pause();
            } else if (remoteMediaClient.isPaused()) {
                remoteMediaClient.play();
            }
        }
    }

    public boolean isPlaying() {
        return remoteMediaClient != null && remoteMediaClient.isPlaying();
    }

    public boolean isPaused() {
        return remoteMediaClient != null && remoteMediaClient.isPaused();
    }

    public boolean isBuffering() {
        return remoteMediaClient != null && remoteMediaClient.isBuffering();
    }

    @MainThread
    public boolean loadMediaAndPlay(@NonNull MediaData mediaData) {
        return loadMediaAndPlay(mediaData.createMediaInfo(), mediaData.autoPlay, mediaData.position);
    }

    @MainThread
    public boolean loadMediaAndPlay(@NonNull MediaInfo mediaInfo) {
        return loadMediaAndPlay(mediaInfo, true, 0);
    }

    @MainThread
    public boolean loadMediaAndPlay(@NonNull MediaInfo mediaInfo, boolean autoPlay, long position) {
        return playMediaBaseMethod(mediaInfo, autoPlay, position, false);
    }

    @MainThread
    public boolean loadMediaAndPlayInBackground(@NonNull MediaData mediaData) {
        return loadMediaAndPlayInBackground(mediaData.createMediaInfo(), mediaData.autoPlay, mediaData.position);
    }

    @MainThread
    public boolean loadMediaAndPlayInBackground(@NonNull MediaInfo mediaInfo) {
        return loadMediaAndPlayInBackground(mediaInfo, true, 0);
    }

    @MainThread
    public boolean loadMediaAndPlayInBackground(@NonNull MediaInfo mediaInfo, boolean autoPlay, long position) {
        return playMediaBaseMethod(mediaInfo, autoPlay, position, true);
    }

    private boolean playMediaBaseMethod(MediaInfo mediaInfo, boolean autoPlay, long position, boolean inBackground) {
        if (remoteMediaClient == null) {
            return false;
        }
        if (!inBackground) {
            remoteMediaClient.addListener(createRemoteMediaClientListener());
        }
        remoteMediaClient.load(mediaInfo, autoPlay, position);
        return true;
    }

    private RemoteMediaClient.Listener createRemoteMediaClientListener() {
        return new RemoteMediaClient.Listener() {
            @Override
            public void onStatusUpdated() {
                onMediaLoadedListener.onMediaLoaded();
                remoteMediaClient.removeListener(this);
            }

            @Override
            public void onMetadataUpdated() {
                //no-op
            }

            @Override
            public void onQueueStatusUpdated() {
                //no-op
            }

            @Override
            public void onPreloadStatusUpdated() {
                //no-op
            }

            @Override
            public void onSendingRemoteMediaRequest() {
                //no-op
            }

            @Override
            public void onAdBreakStatusUpdated() {
                //no-op
            }
        };
    }

    interface OnMediaLoadedListener {
        void onMediaLoaded();
    }
}
