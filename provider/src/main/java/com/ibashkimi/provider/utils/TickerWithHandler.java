package com.ibashkimi.provider.utils;

import android.os.Handler;
import android.os.SystemClock;

import java.util.HashSet;


public class TickerWithHandler {

    private boolean mStop;
    private long delay;
    private HashSet<TickListener> listeners;
    private Handler handler;
    private final Runnable mTicker = new Runnable() {
        public void run() {
            if (mStop) return;
            onTickGenerated();
            handler.postAtTime(mTicker, SystemClock.uptimeMillis() + delay);
        }
    };

    public TickerWithHandler(long delay) {
        this.handler = new Handler();
        this.delay = delay;
    }

    public boolean register(TickListener listener) {
        if (listeners == null) {
            listeners = new HashSet<>(1);
        }
        return listeners.add(listener);
    }

    public void unregister(TickListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    private void onTickGenerated() {
        if (!mStop) {
            for (TickListener listener : listeners) {
                listener.onTick();
            }
        }
    }

    public void start() {
        mStop = false;
        mTicker.run();
    }

    public void stop() {
        mStop = true;
    }

    public interface TickListener {
        void onTick();
    }

}
