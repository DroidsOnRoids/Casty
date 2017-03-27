package pl.droidsonroids.casty;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

/**
 * Extensible {@link AppCompatActivity}, which helps with setting widgets
 */
public abstract class CastyActivity extends AppCompatActivity {
    protected Casty casty;

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        casty = Casty.create(this);
    }

    @CallSuper
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (findViewById(R.id.casty_mini_controller) == null) {
            casty.addMiniController();
        }
        casty.addMediaRouteMenuItem(menu);
        return true;
    }
}
