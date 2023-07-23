package com.bab.mepwnz.configs

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class BlizzardTokenBucket(
    private val capacity: Long,
    refillRate: Long
) {
    private var tokens: Long = 0
    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    init {
        executor.scheduleAtFixedRate({
            if (tokens < capacity) tokens++
        }, 0, refillRate, TimeUnit.MILLISECONDS)
    }

    @Synchronized
    fun tryConsume(): Boolean {
        return if (tokens > 0) {
            tokens--
            true
        } else {
            false
        }
    }

    fun shutdown() {
        executor.shutdown()
    }
}
