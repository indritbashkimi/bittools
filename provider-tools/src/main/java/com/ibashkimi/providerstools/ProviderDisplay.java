package com.ibashkimi.providerstools;

import androidx.annotation.NonNull;

public interface ProviderDisplay extends com.ibashkimi.provider.provider.ProviderListener {
    void setDisplayParams(@NonNull DisplayParams params);
}
