package com.ibashkimi.providerstools.data;

import androidx.annotation.NonNull;

import com.ibashkimi.provider.provider.ProviderListener;

public interface ProviderDisplay extends ProviderListener {
    void setDisplayParams(@NonNull DisplayParams params);
}
