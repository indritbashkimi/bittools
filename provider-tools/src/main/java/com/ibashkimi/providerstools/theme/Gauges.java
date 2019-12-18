package com.ibashkimi.providerstools.theme;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.ibashkimi.providerstools.R;

public class Gauges {

    public static final String PREF_KEY_WIDGET_1 = "widget1";
    public static final String PREF_KEY_WIDGET_2 = "widget2";
    public static final String PREF_KEY_WIDGET_3 = "widget3";
    public static final String STYLE_GAUGE_1 = "gauge_1";
    public static final String STYLE_GAUGE_2 = "gauge_2";
    public static final String STYLE_GAUGE_3 = "gauge_3";
    public static final String STYLE_CHART_1 = "chart_1";
    public static final String STYLE_CHART_2 = "chart_2";
    public static final String STYLE_DIGITAL_1 = "digital_1";
    //public static final String STYLE_DIGITAL_2 = "digital_2";
    //public static final String STYLE_DIGITAL_3 = "digital_3";
    public static final String STYLE_LEVEL_1 = "level_1";
    public static final String STYLE_LEVEL_DIGITAL_1 = "level_digital_1";
    public static final String STYLE_COMPASS_1 = "compass_1";
    public static final String STYLE_COMPASS_2 = "compass_2";
    public static final String STYLE_COMPASS_3 = "compass_3";
    public static final String STYLE_COMPASS_4 = "compass_4";
    public static final String STYLE_COMPASS_DIGITAL_1 = "compass_digital_1";

    private Gauges() {
    }

    @LayoutRes
    public static int style(@NonNull String style) {
        switch (style) {
            case STYLE_GAUGE_1:
                return R.layout.gauge_style_1;
            case STYLE_GAUGE_2:
                return R.layout.gauge_style_2;
            case STYLE_GAUGE_3:
                return R.layout.gauge_style_3;
            case STYLE_CHART_1:
                return R.layout.chart_style_1;
            case STYLE_CHART_2:
                return R.layout.chart_style_2;
            case STYLE_DIGITAL_1:
                return R.layout.digital_style_1;
            case STYLE_LEVEL_1:
                return R.layout.gauge_level;
            case STYLE_LEVEL_DIGITAL_1:
                return R.layout.digital_level_1;
            case STYLE_COMPASS_1:
                return R.layout.compass_style_1;
            case STYLE_COMPASS_2:
                return R.layout.compass_style_2;
            case STYLE_COMPASS_3:
                return R.layout.compass_style_3;
            case STYLE_COMPASS_4:
                return R.layout.compass_style_4;
            case STYLE_COMPASS_DIGITAL_1:
                return R.layout.compass_digital_1;
            default:
                return -1;
        }
    }

    @LayoutRes
    public static int previewStyle(@NonNull String style) {
        switch (style) {
            case STYLE_GAUGE_1:
                return R.layout.gauge_style_1_preview;
            case STYLE_GAUGE_2:
                return R.layout.gauge_style_2_preview;
            case STYLE_GAUGE_3:
                return R.layout.gauge_style_3;
            case STYLE_CHART_1:
                return R.layout.chart_style_1_preview;
            case STYLE_CHART_2:
                return R.layout.chart_style_2_preview;
            case STYLE_DIGITAL_1:
                return R.layout.digital_style_1_preview;
            case STYLE_LEVEL_1:
                return R.layout.gauge_level;
            case STYLE_LEVEL_DIGITAL_1:
                return R.layout.digital_level_1_preview;
            case STYLE_COMPASS_1:
                return R.layout.compass_style_1_preview;
            case STYLE_COMPASS_2:
                return R.layout.compass_style_2_preview;
            case STYLE_COMPASS_3:
                return R.layout.compass_style_3_preview;
            case STYLE_COMPASS_4:
                return R.layout.compass_style_4_preview;
            case STYLE_COMPASS_DIGITAL_1:
                return R.layout.compass_digital_1_preview;
            default:
                return -1;
        }
    }
}
