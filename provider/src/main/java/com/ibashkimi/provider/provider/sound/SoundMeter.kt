package com.ibashkimi.provider.provider.sound

import java.io.IOException

interface SoundMeter {

    val amplitude: Double

    val isOn: Boolean

    @Throws(IOException::class)
    fun start()

    fun stop()
}
