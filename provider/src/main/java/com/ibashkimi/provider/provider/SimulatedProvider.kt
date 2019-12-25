package com.ibashkimi.provider.provider

import com.ibashkimi.provider.providerdata.UnidimensionalData
import com.ibashkimi.provider.utils.ValueProducerFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext


class SimulatedProvider(
    private val values: FloatArray,
    private val delay: Long,
    val data: UnidimensionalData = UnidimensionalData()
) : AbstractProvider() {

    override val isSimulated: Boolean = true

    override val isSupported: Boolean = true

    private var tickerJob: Job? = null

    private val scope = CoroutineScope(Dispatchers.IO)

    private val producerFlow = ValueProducerFlow(values.toTypedArray(), delay)

    @Synchronized
    override fun startListening() {
        super.startListening()
        tickerJob?.cancel()
        tickerJob = producerFlow
            .onEach {
                data.values[0] = it.toDouble()
                withContext(Dispatchers.Main) {
                    onDataChanged(data)
                }
            }
            .launchIn(scope)
    }

    @Synchronized
    override fun stopListening() {
        super.stopListening()
        tickerJob?.cancel()
        tickerJob = null
    }
}