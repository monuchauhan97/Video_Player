package plugin.adsdk;

import android.os.SystemClock;
import android.view.View;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class DebouncedOnClickListener implements View.OnClickListener {

    private final long minimumIntervalMillis;
    private long lastCLick;

    /**
     * Implement this in your subclass instead of onClick
     * @param v The view that was clicked
     */
    public abstract void onDebouncedClick(View v);

    /**
     * The one and only constructor
     * @param minimumIntervalMillis The minimum allowed time between clicks - any click sooner than this after a previous click will be rejected
     */
    public DebouncedOnClickListener(long minimumIntervalMillis) {
        this.minimumIntervalMillis = minimumIntervalMillis;
        this.lastCLick = 0L;
    }

    @Override
    public void onClick(View clickedView) {
        long currentTimestamp = SystemClock.uptimeMillis();

        if(Math.abs(currentTimestamp - lastCLick) > minimumIntervalMillis) {
            lastCLick = currentTimestamp;
            onDebouncedClick(clickedView);
        }
    }
}