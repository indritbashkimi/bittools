package com.ibashkimi.provider.filter;

/**
 * Time smoothing constant for low-pass doJob 0 ≤ alpha ≤ 1 ; a smaller
 * value basically means more smoothing See: http://en.wikipedia.org/wiki
 * /Low-pass_filter#Discrete-time_realization
 */
public class LowPassFilter implements Filter {
    protected double alpha;
    private double lastOutput = 0;

    public LowPassFilter(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public double doJob(double input) {
        lastOutput = lastOutput + alpha * (input - lastOutput);
        return lastOutput;
    }
}