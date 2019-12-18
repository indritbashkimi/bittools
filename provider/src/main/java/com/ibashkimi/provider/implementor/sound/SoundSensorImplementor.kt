package com.ibashkimi.provider.implementor.sound

import com.ibashkimi.provider.filter.Filter
import com.ibashkimi.provider.implementor.AbstractSensorImplementor
import com.ibashkimi.provider.providerdata.SoundLevelData
import com.ibashkimi.provider.utils.TickerWithHandler
import java.io.IOException
import kotlin.math.log10


class SoundSensorImplementor(
    private val delayMillis: Long,
    private val filter: Filter,
    val data: SoundLevelData = SoundLevelData()
) : AbstractSensorImplementor(), TickerWithHandler.TickListener {

    private var soundMeter: SoundMeter = SoundMeterImpl()

    private var timer: TickerWithHandler? = null

    private var listening: Boolean = false

    override val isSupported: Boolean = true

    override val isSimulated: Boolean = false

    override fun startListening() {
        if (!soundMeter.isOn) {
            try {
                soundMeter.start()
            } catch (e: IOException) {
                e.printStackTrace()
                return
            }

        }
        if (timer == null) {
            timer = TickerWithHandler(delayMillis)
            timer!!.register(this)
        }
        timer!!.start()
        listening = true
    }

    override fun stopListening() {
        if (timer != null) {
            timer!!.unregister(this)
            timer!!.stop()
            timer = null
        }
        soundMeter.stop()
        listening = false
    }

    private fun amplitudeToDecibel(amplitude: Double): Double = 20 * log10((amplitude))

    override fun onTick() {
        data.values[0] = amplitudeToDecibel(filter.doJob(soundMeter.amplitude))
        listener.onDataChanged(data)
    }
}