package pl.droidsonroids.casty;

import android.support.annotation.NonNull;
import com.google.android.gms.cast.MediaInfo;

class CastyPlayerNoOp extends CastyPlayer {
    @Override
    public void play() {
        //no-op
    }

    @Override
    public void pause() {
        //no-op
    }

    @Override
    public void seek(long time) {
        //no-op
    }

    @Override
    public void togglePlayPause() {
        //no-op
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public boolean isBuffering() {
        return false;
    }

    @Override
    public boolean loadMediaAndPlay(@NonNull MediaData mediaData) {
        return false;
    }

    @Override
    public boolean loadMediaAndPlay(@NonNull MediaInfo mediaInfo) {
        return false;
    }

    @Override
    public boolean loadMediaAndPlay(@NonNull MediaInfo mediaInfo, boolean autoPlay, long position) {
        return false;
    }

    @Override
    public boolean loadMediaAndPlayInBackground(@NonNull MediaData mediaData) {
        return false;
    }

    @Override
    public boolean loadMediaAndPlayInBackground(@NonNull MediaInfo mediaInfo) {
        return false;
    }

    @Override
    public boolean loadMediaAndPlayInBackground(@NonNull MediaInfo mediaInfo, boolean autoPlay, long position) {
        return false;
    }
}
