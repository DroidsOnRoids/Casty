package pl.droidsonroids.casty;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.app.MediaRouteButton;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;

public class Casty implements CastyPlayer.OnMediaLoadedListener {
    static String receiverId = CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID;
    static CastOptions customCastOptions;

    private SessionManagerListener<CastSession> sessionManagerListener;
    private OnConnectChangeListener onConnectChangeListener;
    private OnCastSessionUpdatedListener onCastSessionUpdatedListener;

    private CastSession castSession;
    private CastyPlayer castyPlayer;
    private Activity activity;

    public static void configure(@NonNull String receiverId) {
        Casty.receiverId = receiverId;
    }

    public static void configure(@NonNull CastOptions castOptions) {
        Casty.customCastOptions = castOptions;
    }

    public static Casty create(@NonNull Activity activity) {
        return new Casty(activity);
    }

    private Casty(@NonNull Activity activity) {
        this.activity = activity;
        sessionManagerListener = createSessionManagerListener();
        castyPlayer = new CastyPlayer(this);
        activity.getApplication().registerActivityLifecycleCallbacks(createActivityCallbacks());
        CastContext.getSharedInstance(activity);
    }

    public CastyPlayer getPlayer() {
        return castyPlayer;
    }

    public boolean isConnected() {
        return castSession != null;
    }

    @UiThread
    public void addMediaRouteMenuItem(@NonNull Menu menu) {
        activity.getMenuInflater().inflate(R.menu.casty_discovery, menu);
        setUpMediaRouteMenuItem(menu);
    }

    @UiThread
    public void setUpMediaRouteButton(@NonNull MediaRouteButton mediaRouteButton) {
        CastButtonFactory.setUpMediaRouteButton(activity, mediaRouteButton);
    }

    @UiThread
    public Casty withMiniController() {
        addMiniController();
        return this;
    }

    @UiThread
    public void addMiniController() {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View rootView = contentView.getChildAt(0);
        LinearLayout linearLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams linearLayoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(linearLayoutParams);

        contentView.removeView(rootView);

        ViewGroup.LayoutParams oldRootParams = rootView.getLayoutParams();
        LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(oldRootParams.width, 0, 1f);
        rootView.setLayoutParams(rootParams);

        linearLayout.addView(rootView);
        activity.getLayoutInflater().inflate(R.layout.mini_controller, linearLayout, true);
        activity.setContentView(linearLayout);
    }

    public void setOnConnectChangeListener(@Nullable OnConnectChangeListener onConnectChangeListener) {
        this.onConnectChangeListener = onConnectChangeListener;
    }

    public void setOnCastSessionUpdatedListener(@Nullable OnCastSessionUpdatedListener onCastSessionUpdatedListener) {
        this.onCastSessionUpdatedListener = onCastSessionUpdatedListener;
    }

    private void setUpMediaRouteMenuItem(Menu menu) {
        CastButtonFactory.setUpMediaRouteButton(activity, menu, R.id.casty_media_route_menu_item);
    }

    private SessionManagerListener<CastSession> createSessionManagerListener() {
        return new SessionManagerListener<CastSession>() {
            @Override
            public void onSessionStarted(CastSession castSession, String s) {
                activity.invalidateOptionsMenu();
                onConnected(castSession);
            }

            @Override
            public void onSessionEnded(CastSession castSession, int i) {
                activity.invalidateOptionsMenu();
                onDisconnected();
            }

            @Override
            public void onSessionResumed(CastSession castSession, boolean b) {
                activity.invalidateOptionsMenu();
                onConnected(castSession);
            }

            @Override
            public void onSessionStarting(CastSession castSession) {
                //no-op
            }

            @Override
            public void onSessionStartFailed(CastSession castSession, int i) {
                //no-op
            }

            @Override
            public void onSessionEnding(CastSession castSession) {
                //no-op
            }

            @Override
            public void onSessionResuming(CastSession castSession, String s) {
                //no-op
            }

            @Override
            public void onSessionResumeFailed(CastSession castSession, int i) {
                //no-op
            }

            @Override
            public void onSessionSuspended(CastSession castSession, int i) {
                //no-op
            }
        };
    }

    private void onConnected(CastSession castSession) {
        this.castSession = castSession;
        castyPlayer.setRemoteMediaClient(castSession.getRemoteMediaClient());
        if (onConnectChangeListener != null) onConnectChangeListener.onConnected();
        if (onCastSessionUpdatedListener != null) onCastSessionUpdatedListener.onCastSessionUpdated(castSession);
    }

    private void onDisconnected() {
        this.castSession = null;
        if (onConnectChangeListener != null) onConnectChangeListener.onDisconnected();
        if (onCastSessionUpdatedListener != null) onCastSessionUpdatedListener.onCastSessionUpdated(null);
    }

    private Application.ActivityLifecycleCallbacks createActivityCallbacks() {
        return new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                //no-op
            }

            @Override
            public void onActivityStarted(Activity activity) {
                //no-op
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (Casty.this.activity == activity) {
                    handleCurrentCastSession();
                    registerSessionManagerListener();
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (Casty.this.activity == activity) unregisterSessionManagerListener();
            }

            @Override
            public void onActivityStopped(Activity activity) {
                //no-op
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                //no-op
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if(Casty.this.activity == activity) {
                    activity.getApplication().unregisterActivityLifecycleCallbacks(this);
                }
            }
        };
    }

    private void registerSessionManagerListener() {
        CastContext.getSharedInstance(activity).getSessionManager().addSessionManagerListener(sessionManagerListener, CastSession.class);
    }

    private void unregisterSessionManagerListener() {
        CastContext.getSharedInstance(activity).getSessionManager().removeSessionManagerListener(sessionManagerListener, CastSession.class);
    }

    private void handleCurrentCastSession() {
        CastSession newCastSession = CastContext.getSharedInstance(activity).getSessionManager().getCurrentCastSession();
        if (castSession == null) {
            if (newCastSession != null) {
                onConnected(newCastSession);
            }
        } else {
            if (newCastSession == null) {
                onDisconnected();
            } else if (newCastSession != castSession) {
                onConnected(newCastSession);
            }
        }
    }

    @Override
    public void onMediaLoaded() {
        startExpandedControlsActivity();
    }

    private void startExpandedControlsActivity() {
        Intent intent = new Intent(activity, ExpandedControlsActivity.class);
        activity.startActivity(intent);
    }

    public interface OnConnectChangeListener {
        void onConnected();

        void onDisconnected();
    }

    public interface OnCastSessionUpdatedListener {
        void onCastSessionUpdated(CastSession castSession);
    }
}
