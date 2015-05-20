package com.segment.analytics;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.segment.analytics.internal.Utils;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AnalyticsStressTest extends AndroidTestCase {

  protected Context context;
  protected static final String WRITE_KEY = "l8v1ga655b";

  @Override protected void setUp() {
    context = new RenamingDelegatingContext(getContext(), "test_");
  }

  @Override public void tearDown() throws IOException {
    // Make sure any extra elements left over are flushed
    // analyticsInstance.flush();
    sleepUntilFlushTimerExpires();
  }

  public void testQueueFileUnderStress_track() {
    Analytics analytics =
        new Analytics.Builder(context, WRITE_KEY).logLevel(Analytics.LogLevel.BASIC)
            .flushInterval(300, TimeUnit.SECONDS)
            .flushQueueSize(SegmentDispatcher.MAX_QUEUE_SIZE * 2)
            .build();

    int maxQueueSize = SegmentDispatcher.MAX_QUEUE_SIZE;
    int aboveTheMax = 100;
    int veryLargeFlushSize = maxQueueSize + aboveTheMax;

    //Avoid hitting the FLush Size
    for (int i = 1; i <= veryLargeFlushSize - 1; i++) {
      analytics.track("FJK QueueFileStressTest - testQueueFileUnderStress_track", null, null);
      analytics.screen("FJK QueueFileStressTest - testQueueFileUnderStress_screen", null);
    }
    analytics.flush();
  }

  public void sleepUntilFlushTimerExpires() {
    sleep(Utils.DEFAULT_FLUSH_INTERVAL + 3000);
  }

  public void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException ignored) {
    }
  }
}
