package com.segment.analytics.sample;

import android.app.Application;
import com.segment.analytics.Analytics;
import java.util.concurrent.TimeUnit;

public class SampleApp extends Application {

  private static final String ANALYTICS_WRITE_KEY = "l8v1ga655b";

  @Override public void onCreate() {
    super.onCreate();

    // Initialize a new instance of the Analytics client.
    Analytics.Builder builder = new Analytics.Builder(this, ANALYTICS_WRITE_KEY);
    if (BuildConfig.DEBUG) {
      builder.logLevel(Analytics.LogLevel.BASIC);
    }

    builder.flushInterval(5, TimeUnit.MINUTES).flushQueueSize(2000);

    // Set the initialized instance as a globally accessible instance.
    Analytics.setSingletonInstance(builder.build());

    // Now anytime you call Analytics.with, the custom instance will be returned.
    Analytics.with(this).track("App Launched");

    // Avoid hitting the fLush Size
    for (int i = 0; i <= 1100; i++) {
      Analytics.with(this).track("FJK QueueFileStressTest -testQueueFileUnderStress_track");
      Analytics.with(this).screen("FJK QueueFileStressTest -testQueueFileUnderStress_screen", null);
    }

    Analytics.with(this).flush();
  }
}
