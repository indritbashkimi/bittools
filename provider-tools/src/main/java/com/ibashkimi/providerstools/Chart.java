package com.ibashkimi.providerstools;

import com.ibashkimi.provider.provider.ProviderListener;


public interface Chart extends ProviderListener {

    void setMinValue(float minValue);

    void setMaxValue(float maxValue);
}
