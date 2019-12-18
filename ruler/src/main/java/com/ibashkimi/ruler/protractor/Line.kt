package com.ibashkimi.ruler.protractor

import kotlin.math.asin


data class Line(var sin: Float = 0f, var cos: Float = 0f) {
    val angle: Float
        get() {
            return when (sin) {
                1.0f -> 90f
                else -> {
                    val degrees: Double = Math.toDegrees(asin(sin.toDouble()))
                    (if (cos >= 0) degrees else 180.0 - degrees).toFloat()
                }
            }
        }
}
