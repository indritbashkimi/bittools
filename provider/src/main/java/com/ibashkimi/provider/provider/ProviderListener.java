package com.ibashkimi.provider.provider;

import com.ibashkimi.provider.providerdata.SensorData;

public interface ProviderListener {
    void onDataChanged(SensorData data);
}
