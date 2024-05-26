package com.allformats.video.player.downloader.ds_tube_android_util.classes;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Vid_player_DelayTask {
    private long delay;
    private ScheduledFuture<?> futureTask;
    private Runnable runnable;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public Vid_player_DelayTask(Runnable runnable, long j) {
        this.runnable = runnable;
        this.delay = j;
    }

    public void start() {
        ScheduledFuture<?> scheduledFuture = this.futureTask;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        this.futureTask = this.scheduledExecutorService.schedule(this.runnable, this.delay, TimeUnit.MILLISECONDS);
    }

    public void cancel() {
        ScheduledFuture<?> scheduledFuture = this.futureTask;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }
}
