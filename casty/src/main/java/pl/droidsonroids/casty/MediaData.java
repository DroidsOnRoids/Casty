package pl.droidsonroids.casty;

import android.net.Uri;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.common.images.WebImage;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MediaData {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STREAM_TYPE_NONE, STREAM_TYPE_BUFFERED, STREAM_TYPE_LIVE})
    public @interface StreamType {}
    public static final int STREAM_TYPE_NONE = 0;
    public static final int STREAM_TYPE_BUFFERED = 1;
    public static final int STREAM_TYPE_LIVE = 2;
    public static final int STREAM_TYPE_INVALID = -1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MEDIA_TYPE_GENERIC, MEDIA_TYPE_MOVIE, MEDIA_TYPE_TV_SHOW, MEDIA_TYPE_MUSIC_TRACK, MEDIA_TYPE_PHOTO, MEDIA_TYPE_USER})
    public @interface MediaType {}
    public static final int MEDIA_TYPE_GENERIC = 0;
    public static final int MEDIA_TYPE_MOVIE = 1;
    public static final int MEDIA_TYPE_TV_SHOW = 2;
    public static final int MEDIA_TYPE_MUSIC_TRACK = 3;
    public static final int MEDIA_TYPE_PHOTO = 4;
    public static final int MEDIA_TYPE_USER = 100;

    public static final long UNKNOWN_DURATION = -1L;

    private String url;
    private int streamType = STREAM_TYPE_NONE;
    private String contentType;
    private long streamDuration = UNKNOWN_DURATION;

    private int mediaType = MEDIA_TYPE_GENERIC;
    private String title;
    private String subtitle;

    boolean autoPlay = true;
    long position;

    private List<String> imageUrls;

    private MediaData(String url) {
        this.url = url;
        imageUrls = new ArrayList<>();
    }

    private void setStreamType(int streamType) {
        this.streamType = streamType;
    }

    private void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private void setStreamDuration(long streamDuration) {
        this.streamDuration = streamDuration;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    private void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    private void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    private void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    private void setPosition(long position) {
        this.position = position;
    }

    MediaInfo createMediaInfo() {
        MediaMetadata mediaMetadata = new MediaMetadata(mediaType);

        if (!TextUtils.isEmpty(title)) mediaMetadata.putString(MediaMetadata.KEY_TITLE, title);
        if (!TextUtils.isEmpty(subtitle)) mediaMetadata.putString(MediaMetadata.KEY_SUBTITLE, subtitle);

        for (String imageUrl : imageUrls) {
            mediaMetadata.addImage(new WebImage(Uri.parse(imageUrl)));
        }

        return new MediaInfo.Builder(url)
                .setStreamType(streamType)
                .setContentType(contentType)
                .setStreamDuration(streamDuration)
                .setMetadata(mediaMetadata)
                .build();
    }

    public static class Builder {
        private final MediaData mediaData;

        public Builder(String url) {
            mediaData = new MediaData(url);
        }

        public Builder setStreamType(@StreamType int streamType) {
            mediaData.setStreamType(streamType);
            return this;
        }

        public Builder setContentType(String contentType) {
            mediaData.setContentType(contentType);
            return this;
        }

        public Builder setStreamDuration(long streamDuration) {
            mediaData.setStreamDuration(streamDuration);
            return this;
        }

        public Builder setTitle(String title) {
            mediaData.setTitle(title);
            return this;
        }

        public Builder setSubtitle(String subtitle) {
            mediaData.setSubtitle(subtitle);
            return this;
        }

        public Builder setMediaType(@MediaType int mediaType) {
            mediaData.setMediaType(mediaType);
            return this;
        }

        public Builder addPhotoUrl(String photoUrl) {
            mediaData.imageUrls.add(photoUrl);
            return this;
        }

        public Builder setAutoPlay(boolean autoPlay) {
            mediaData.autoPlay = autoPlay;
            return this;
        }

        public Builder setPosition(long position) {
            mediaData.position = position;
            return this;
        }

        public MediaData build() {
            return this.mediaData;
        }
    }
}
