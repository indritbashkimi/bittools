package com.ibashkimi.providerstools.model;

import androidx.annotation.NonNull;

import com.ibashkimi.provider.provider.ProviderListener;
import com.ibashkimi.providerstools.model.DisplayParams;

public interface ProviderDisplay extends ProviderListener {
    void setDisplayParams(@NonNull DisplayParams params);
}
