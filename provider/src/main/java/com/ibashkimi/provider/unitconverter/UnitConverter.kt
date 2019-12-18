package com.ibashkimi.provider.unitconverter

/**
 * Angle. Default measurementUnit: degree
 */
fun degreeToRadian(v: Double): Double = v * Math.PI / 180

fun degreeToGradient(v: Double): Double = 10 * v / 9

fun degreeToMin(v: Double): Double = TODO()

fun degreeToSec(v: Double): Double = TODO()

fun radianToDegree(v: Double): Double = 180 * v / Math.PI

fun gradientToDegree(v: Double): Double = 9 * v / 100

fun minToDegree(v: Double): Double = TODO()

fun secToDegree(v: Double): Double = TODO()

/**
 * Pressure. Default measurementUnit: Pascal
 */

fun hpaToAtm(v: Double): Double = v * 100 / 101325

fun hpaToHpa(v: Double): Double = v * 100 / 100

fun hpaToKpa(v: Double): Double = v * 100 / 1000

fun hpaToMpa(v: Double): Double = v * 100 / 1000000

fun hpaToBar(v: Double): Double = v * 100 / 100000

fun hpaToTorr(v: Double): Double = v * 100 * 760 / 101325

/**
 * Temperature. Default: Celsius
 */
fun celsiusToFahrenheit(temp: Double): Double = temp * 1.8 + 32

fun celsiusToKelvin(temp: Double): Double = temp + 275.15

fun celsiusToRankine(temp: Double): Double = celsiusToFahrenheit(temp) + 459.67

fun celsiusToReaumur(temp: Double): Double = (celsiusToFahrenheit(temp) - 32) / 2.25