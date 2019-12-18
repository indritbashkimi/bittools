package com.ibashkimi.provider.filter;

public class EmaFilter implements Filter {
    private double emaFilter;
    private double lastValue;

    public EmaFilter() {
        this(0.6f);
    }

    public EmaFilter(double emaFilter) {
        this.emaFilter = emaFilter;
        this.lastValue = 0.0f;
    }

    public double doJob(double value) {
        lastValue = emaFilter * value + (1.0f - emaFilter) * lastValue;
        return lastValue;
    }

}
