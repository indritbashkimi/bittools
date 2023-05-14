package com.ibashkimi.shared

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class Tool : Parcelable {
    ACCELEROMETER,
    BAROMETER,
    COMPASS,
    HYGROMETER,
    LEVEL,
    LIGHT,
    MAGNETOMETER,
    RULER,
    PROTRACTOR,
    THERMOMETER;
}