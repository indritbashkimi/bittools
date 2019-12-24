package com.ibashkimi.provider.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.AbstractFlow
import kotlinx.coroutines.flow.FlowCollector

class ValueProducerFlow<T>(
    val values: Array<T>,
    val delay: Long,
    val initialDelay: Long = 0,
    var index: Int = 0,
    val reverse: Boolean = true,
    var isReversing: Boolean = false
) : AbstractFlow<T>() {

    override suspend fun collectSafely(collector: FlowCollector<T>) {
        if (initialDelay > 0)
            delay(initialDelay)
        while (true) {
            collector.emit(values[nextIndex()])
            delay(delay)
        }
    }

    private fun nextIndex(): Int {
        if (!reverse) {
            index = if (index == values.size - 1) 0 else index + 1
            return index
        }
        if (isReversing) {
            if (index == 0) {
                index = 1
                isReversing = false
            } else {
                index--
            }
        } else {
            if (index == values.size - 1) {
                index = values.size - 2
                isReversing = true
            } else {
                index++
            }
        }
        return index
    }
}