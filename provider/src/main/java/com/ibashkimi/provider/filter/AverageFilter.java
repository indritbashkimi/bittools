package com.ibashkimi.provider.filter;

public class AverageFilter implements Filter {
    private int size;
    private double total = 0d;
    private int index = 0;
    private double samples[];

    public AverageFilter(int size) {
        this.size = size;
        samples = new double[size];
        for (int i = 0; i < size; i++)
            samples[i] = 0d;
    }

    @Override
    public double doJob(double input) {
        add(input);
        return getAverage();
    }

    public void add(double x) {
        total -= samples[index];
        samples[index] = x;
        total += x;
        if (++index == size)
            index = 0;
    }

    public double getAverage() {
        return total / size;
    }
}
