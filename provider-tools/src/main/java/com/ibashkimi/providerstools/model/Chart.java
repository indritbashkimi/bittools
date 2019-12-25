package com.ibashkimi.providerstools.model;

import com.ibashkimi.provider.provider.ProviderListener;


public interface Chart extends ProviderListener {

    void setMinValue(float minValue);

    void setMaxValue(float maxValue);
}
