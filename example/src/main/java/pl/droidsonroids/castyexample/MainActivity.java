package pl.droidsonroids.castyexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteButton;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import pl.droidsonroids.casty.Casty;
import pl.droidsonroids.casty.MediaData;

public class MainActivity extends AppCompatActivity {
    private Button playButton;
    private Casty casty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        casty = Casty.create(this)
                .withMiniController();
        setUpPlayButton();
        setUpMediaRouteButton();
    }

    private void setUpPlayButton() {
        playButton = (Button) findViewById(R.id.button_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casty.getPlayer().loadMediaAndPlay(createSampleMediaData());
            }
        });
        casty.setOnConnectChangeListener(new Casty.OnConnectChangeListener() {
            @Override
            public void onConnected() {
                playButton.setEnabled(true);
            }

            @Override
            public void onDisconnected() {
                playButton.setEnabled(false);
            }
        });
    }

    private void setUpMediaRouteButton() {
        MediaRouteButton mediaRouteButton = (MediaRouteButton) findViewById(R.id.media_route_button);
        casty.setUpMediaRouteButton(mediaRouteButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        casty.addMediaRouteMenuItem(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private static MediaData createSampleMediaData() {
        return new MediaData.Builder("http://distribution.bbb3d.renderfarming.net/video/mp4/bbb_sunflower_1080p_30fps_normal.mp4")
                .setStreamType(MediaData.STREAM_TYPE_BUFFERED)
                .setContentType("videos/mp4")
                .setMediaType(MediaData.MEDIA_TYPE_MOVIE)
                .setTitle("Sample title")
                .setSubtitle("Sample subtitle")
                .addPhotoUrl("https://peach.blender.org/wp-content/uploads/bbb-splash.png?x11217")
                .build();
    }
}
