package com.ibashkimi.provider.provider;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface ProviderStateListener {
    int EVENT_PROVIDER_ENABLED = 0;
    int EVENT_PROVIDER_DISABLED = 1;
    int EVENT_PROVIDER_PAUSED = 2;
    int EVENT_PROVIDER_RESUMED = 3;

    void onStateChanged(@ProviderEvent int event);

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({EVENT_PROVIDER_ENABLED, EVENT_PROVIDER_DISABLED, EVENT_PROVIDER_PAUSED, EVENT_PROVIDER_RESUMED})
    @interface ProviderEvent {
    }
}
