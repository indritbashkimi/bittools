package com.ibashkimi.provider.provider.sound

import com.ibashkimi.provider.filter.Filter
import com.ibashkimi.provider.provider.AbstractProvider
import com.ibashkimi.provider.providerdata.SoundLevelData
import com.ibashkimi.provider.utils.ticks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.IOException
import kotlin.math.log10

class SoundLevelProvider(
    private val delayMillis: Long,
    private val filter: Filter,
    val data: SoundLevelData = SoundLevelData()
) : AbstractProvider() {

    private var soundMeter: SoundMeter = SoundMeterImpl()

    private var tickerJob: Job? = null

    private val scope = CoroutineScope(Dispatchers.IO)

    override val isSupported: Boolean = true

    override val isSimulated: Boolean = false

    override fun startListening() {
        super.startListening()
        if (!soundMeter.isOn) {
            try {
                soundMeter.start()
            } catch (e: IOException) {
                e.printStackTrace()
                return
            }

        }
        tickerJob?.cancel()
        tickerJob = ticks(delayMillis).onEach { onTick() }.launchIn(scope)
    }

    override fun stopListening() {
        super.stopListening()
        tickerJob?.cancel()
        tickerJob = null
        soundMeter.stop()
    }

    private fun amplitudeToDecibel(amplitude: Double): Double = 20 * log10((amplitude))

    private fun onTick() {
        data.values[0] = amplitudeToDecibel(filter.doJob(soundMeter.amplitude))
        onDataChanged(data)
    }
}