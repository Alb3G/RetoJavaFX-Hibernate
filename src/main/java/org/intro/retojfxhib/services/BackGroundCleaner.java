package org.intro.retojfxhib.services;

import org.intro.retojfxhib.utils.Util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BackGroundCleaner {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void startBackGroundCleaner() {
        scheduler.scheduleAtFixedRate(Util::emptyReportsDir, 0, 5, TimeUnit.MINUTES);
    }

    public static void stopBackGroundCleaner() {
        scheduler.shutdown();
        try {
            if(!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
