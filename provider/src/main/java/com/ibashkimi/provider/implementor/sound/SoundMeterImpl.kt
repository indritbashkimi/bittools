package com.ibashkimi.provider.implementor.sound

import android.media.MediaRecorder
import android.util.Log
import java.io.IOException


class SoundMeterImpl : SoundMeter, MediaRecorder.OnErrorListener {

    private val recorder = MediaRecorder()

    override var isOn: Boolean = false

    @Throws(IOException::class)
    override fun start() {
        with(recorder) {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile("/dev/null")
            setAudioChannels(1)
            prepare()
            start()
        }
        isOn = true
    }

    override fun stop() {
        recorder.stop()
        recorder.reset()
        // !! Don't do recorder.release(); Now the object cannot be reused
        isOn = false
    }

    override val amplitude: Double
        get() = recorder.maxAmplitude.toDouble()

    override fun onError(mr: MediaRecorder, what: Int, extra: Int) {
        Log.e("SoundMeterImpl", "Error!")
    }
}