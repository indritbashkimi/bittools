package com.ibashkimi.provider.filter;

import java.util.ArrayDeque;

public class AngleLowPassFilter implements Filter {

    private double size;

    private double sumSin, sumCos;

    private ArrayDeque<Double> queue = new ArrayDeque<>();

    public AngleLowPassFilter(int queueSize) {
        this.size = queueSize;
    }

    private void add(double radians) {
        sumSin += Math.sin(radians);
        sumCos += Math.cos(radians);
        queue.add(radians);

        if (queue.size() > size) {
            double old = queue.poll();
            sumSin -= Math.sin(old);
            sumCos -= Math.cos(old);
        }
    }

    private float average() {
        int size = queue.size();
        return (float) Math.atan2(sumSin / size, sumCos / size);
    }

    public double doJob(double radians) {
        add(radians);
        return average();
    }
}