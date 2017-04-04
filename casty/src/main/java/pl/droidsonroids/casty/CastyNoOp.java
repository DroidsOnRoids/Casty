package pl.droidsonroids.casty;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.MediaRouteButton;
import android.view.Menu;

class CastyNoOp extends Casty {
    private CastyPlayer castyPlayer;

    CastyNoOp() {
        castyPlayer = new CastyPlayerNoOp();
    }

    @Override
    public CastyPlayer getPlayer() {
        return castyPlayer;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void addMediaRouteMenuItem(@NonNull Menu menu) {
        //no-op
    }

    @Override
    public void setUpMediaRouteButton(@NonNull MediaRouteButton mediaRouteButton) {
        //no-op
    }

    @Override
    public Casty withMiniController() {
        return this;
    }

    @Override
    public void addMiniController() {
        //no-op
    }

    @Override
    public void setOnConnectChangeListener(@Nullable OnConnectChangeListener onConnectChangeListener) {
        //no-op
    }

    @Override
    public void setOnCastSessionUpdatedListener(@Nullable OnCastSessionUpdatedListener onCastSessionUpdatedListener) {
        //no-op
    }
}
