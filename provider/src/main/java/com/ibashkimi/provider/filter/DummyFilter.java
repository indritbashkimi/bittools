package com.ibashkimi.provider.filter;

public class DummyFilter implements Filter {

    @Override
    public double doJob(double input) {
        return input;
    }
}