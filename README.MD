# Project no longer maintained

This project is no longer maintained.
If you have any questions contact opensource@droidsonroids.pl

# Casty
Casty is a small Android library that provides a simple media player for Chromecast. It's fully consistent with [Google Cast v3][Cast_v3].
## Installation
Insert the following dependency to `build.gradle` file of your project:
```gradle
dependencies {
    compile 'pl.droidsonroids:casty:1.0.8'
}
```
## Usage
Casty requires Google Play Services and I assume that the target device has it installed (if not this example won't work). In a bigger project I suggest you check it using [GoogleApiAvailability][Google_availability].

First, you need to initialize a Casty instance in every Activity you want to use it in:
```java
public class MainActivity extends AppCompatActivity {
    private Casty casty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        casty = Casty.create(this);
    }
}
```
If you want to add a [Mini Controller widget][Mini_Controller_Info], you can do it like this:
```java
casty = Casty.create(this)
    .withMiniController();
```
Alternatively you can place it in your layout XML, just like in the official [Google Cast example][Mini_Controller_Implementation] (remember to change fill_parent to match_parent 😆).

To support device discovery, add a menu item in overriden `onCreateOptionsMenu` method:
```java
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    casty.addMediaRouteMenuItem(menu);
    getMenuInflater().inflate(R.menu.your_menu, menu);
    return true;
}
```
Automatically, [Introduction Overlay][Introduction_Overlay] (with text "Use this button to connect with Chromecast") will be shown at the first device discovery. If you want to change it or add another language override string with id `casty_introduction_text` in a XML resource.

You can also add a discovery button anywhere else by placing it in your layout XML:
```xml
<android.support.v7.app.MediaRouteButton
    android:id="@+id/media_route_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```
And then set in up in your Activity:
```java
MediaRouteButton mediaRouteButton = (MediaRouteButton) findViewById(R.id.media_route_button);
casty.setUpMediaRouteButton(mediaRouteButton);
```
You can add the above functionality (except `MediaRouteButton`) simply by extending `CastyActivity`. It will add a `casty` field and set up the rest:
```java
public class MainActivity extends CastyActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (casty.isConnected()) {
            casty.getPlayer().loadMediaAndPlay(...)
        }
    }
}
```
All media player actions are included in a `CastyPlayer` object, which you can access by calling `casty.getPlayer()`.
When you are connected to the device, you can play media the following way:
```java
MediaData mediaData = new MediaData.Builder("http://distribution.bbb3d.renderfarming.net/video/mp4/bbb_sunflower_1080p_30fps_normal.mp4")
    .setStreamType(MediaData.STREAM_TYPE_BUFFERED) //required
    .setContentType("videos/mp4") //required
    .setMediaType(MediaData.MEDIA_TYPE_MOVIE)
    .setTitle("Sample title")
    .setSubtitle("Sample subtitle")
    .addPhotoUrl("https://peach.blender.org/wp-content/uploads/bbb-splash.png?x11217")
    .build();
casty.getPlayer().loadMediaAndPlay(mediaData);
```
Alternativly you can use `loadMediaAndPlay(MediaInfo, autoPlay, position)` similar to [Google Cast example][Cast_load_media].

To react on Chromecast connect and disconnect events, you can simply register a listener:
```java
casty.setOnConnectChangeListener(new Casty.OnConnectChangeListener() {
    @Override
    public void onConnected() {
        Log.d("Casty", "Connected with Chromecast");
    }
    
    @Override
    public void onDisconnected() {
        Log.d("Casty" "Disconnected from Chromecast");
    }
});
```
### Custom usage
In case the library doesn't fit you, I left the possibility to change everything like in Google Cast v3.
You can set receiver ID or even the whole `CastOptions` in your Application class:
```java
Casty.configure(receiverId); //or
Casty.configure(customCastOptions);
```
Get `CastContext` by calling:
```java
CastContext.getSharedInstance(context);
```
Get `CastSession` (so `RemoteMediaClient`) by register OnCastSessionUpdatedListener:
```java
casty.setOnCastSessionUpdatedListener(new Casty.OnCastSessionUpdatedListener() {
    @Override
    public void onCastSessionUpdated(CastSession castSession) {
        if (castSession != null) {
            RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
            //...
        }
    }
});
```
### License
[MIT][License]

[//]: #
   [Cast_v3]: <https://developers.google.com/cast/docs/developers>
   [Mini_Controller_Info]: <https://developers.google.com/cast/docs/design_checklist/sender#sender-mini-controller>
   [Mini_Controller_Implementation]: <https://developers.google.com/cast/docs/android_sender_integrate#add_mini_controller>
   [Cast_load_media]: <https://developers.google.com/cast/docs/android_sender_integrate#load_media>
   [Google_availability]: <https://developers.google.com/android/reference/com/google/android/gms/common/GoogleApiAvailability>
   [Introduction_Overlay]: <https://developers.google.com/cast/docs/design_checklist/cast-button#prompting>
   [License]: <https://github.com/DroidsOnRoids/Casty/blob/master/LICENSE>
