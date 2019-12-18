package com.ibashkimi.provider.filter;

public interface VectorFilter {
    double[] doJob(double[] input);

    void reset();
}
