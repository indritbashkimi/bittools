package com.ibashkimi.provider.implementor

import com.ibashkimi.provider.providerdata.UnidimensionalData

import com.ibashkimi.provider.utils.TickerWithHandler
import com.ibashkimi.provider.utils.ValueProducer
import com.ibashkimi.provider.utils.ValueProducerReverse


class SimulatedImplementor(
    values: FloatArray,
    private var delay: Long,
    val data: UnidimensionalData = UnidimensionalData()
) : AbstractSensorImplementor(), TickerWithHandler.TickListener {

    private var producer: ValueProducer<Float> = ValueProducerReverse(values.toTypedArray())

    private var timer: TickerWithHandler? = null

    override val isSimulated: Boolean = true

    override val isSupported: Boolean = true

    @Synchronized
    override fun startListening() {
        if (timer == null) {
            timer = TickerWithHandler(delay)
            timer!!.register(this)
        }
        timer!!.start()
    }

    @Synchronized
    override fun stopListening() {
        if (timer != null) {
            timer!!.unregister(this)
            timer!!.stop()
            timer = null
        }
    }

    override fun onTick() {
        data.values[0] = producer.get().toDouble()
        listener.onDataChanged(data)
    }
}