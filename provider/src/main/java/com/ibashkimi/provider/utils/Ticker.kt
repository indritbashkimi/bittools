package com.ibashkimi.provider.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun ticks(delay: Long, initialDelay: Long = 0): Flow<Unit> = flow {
    if (initialDelay > 0)
        delay(initialDelay)
    while (true) {
        emit(Unit)
        delay(delay)
    }
}