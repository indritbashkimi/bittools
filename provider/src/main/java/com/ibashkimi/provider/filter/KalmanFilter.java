package com.ibashkimi.provider.filter;

public class KalmanFilter implements Filter {
    private double q; //process noise covariance
    private double r; //measurement noise covariance
    private double x; //value
    private double p; //estimation error covariance
    private double k; //kalman gain

    public KalmanFilter() {
        this(0, 0, 0, 0);
    }

    public KalmanFilter(double q, double r, double x, double p) {
        this.q = q;
        this.r = r;
        this.x = x;
        this.p = p;
    }


    @Override
    public double doJob(double input) {
        p = p + q;
        k = p / (p + r);
        x = x + k * (input - x);
        p = (1 - k) * p;
        return x;
    }
}
