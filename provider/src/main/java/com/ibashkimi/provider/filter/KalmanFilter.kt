package com.ibashkimi.provider.filter

class KalmanFilter(
    private val q: Double = 0.0, //process noise covariance
    private val r: Double = 0.0, //measurement noise covariance
    private var x: Double = 0.0, //value
    private var p: Double = 0.0  // estimation error covariance
) : Filter {

    private var k: Double = 0.0 //kalman gain

    override fun doJob(input: Double): Double {
        p += q
        k = p / (p + r)
        x += k * (input - x)
        p *= (1 - k)
        return x
    }

}