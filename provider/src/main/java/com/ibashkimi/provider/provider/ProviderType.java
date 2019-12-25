package com.ibashkimi.provider.provider;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface ProviderType {
    int TYPE_ACCELEROMETER = 0;
    int TYPE_ALTIMETER = 1;
    int TYPE_BAROMETER = 2;
    int TYPE_COMPASS = 3;
    int TYPE_HYGROMETER = 4;
    int TYPE_LIGHT_METER = 5;
    int TYPE_LEVEL = 6;
    int TYPE_MAGNETOMETER = 7;
    int TYPE_ORIENTATION = 8;
    int TYPE_SPEED_METER = 9;
    int TYPE_SOUND_LEVEL_METER = 10;
    int TYPE_THERMOMETER = 11;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_ACCELEROMETER, TYPE_ALTIMETER, TYPE_BAROMETER, TYPE_COMPASS,
            TYPE_HYGROMETER, TYPE_LIGHT_METER, TYPE_LEVEL, TYPE_MAGNETOMETER,
            TYPE_ORIENTATION, TYPE_SPEED_METER, TYPE_SOUND_LEVEL_METER,
            TYPE_THERMOMETER})
    @interface Type {
    }
}
