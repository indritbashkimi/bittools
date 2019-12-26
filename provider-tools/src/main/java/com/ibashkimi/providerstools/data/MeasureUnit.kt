package com.ibashkimi.providerstools.data

import androidx.annotation.StringRes
import com.ibashkimi.providerstools.R

enum class MeasureUnit(@StringRes val symbol: Int, @StringRes val title: Int) {
    CELSIUS(
        R.string.unit_celsius,
        R.string.unit_celsius_full
    ),
    FAHRENHEIT(
        R.string.unit_fahrenheit,
        R.string.unit_fahrenheit_full
    ),
    KELVIN(
        R.string.unit_kelvin,
        R.string.unit_kelvin_full
    ),
    H_PASCAL(
        R.string.unit_hpa,
        R.string.unit_hpa_full
    ),
    ATM(
        R.string.unit_atm,
        R.string.unit_atm_full
    ),
    TORR(
        R.string.unit_torr,
        R.string.unit_torr_full
    ),
    M_S_2(
        R.string.unit_m_s_2,
        R.string.unit_m_s_2_full
    ),
    LX(
        R.string.unit_lx,
        R.string.unit_lx_full
    ),
    DEGREE(
        R.string.unit_degree,
        R.string.unit_degree_full
    ),
    U_TESLA(
        R.string.unit_micro_tesla,
        R.string.unit_micro_tesla_full
    ),
    PERCENT(
        R.string.unit_percent,
        R.string.unit_percent_full
    );
}