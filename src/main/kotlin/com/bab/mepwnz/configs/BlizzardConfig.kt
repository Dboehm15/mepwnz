package com.bab.mepwnz.configs

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelOption
import io.netty.handler.ssl.SslContextBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.resources.ConnectionProvider
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import reactor.netty.http.client.HttpClient
import java.time.Duration
import reactor.netty.tcp.SslProvider
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicInteger

@ConfigurationProperties("")
class BlizzardConfig {
    lateinit var url: String
    private val logger = LoggerFactory.getLogger(BlizzardConfig::class.java)

    @Bean
    fun getBlizzardExecutor(): ExecutorService {
        return ThreadPoolExecutor(
            0,
            maxAsyncRequests,
            Long.MAX_VALUE,
            TimeUnit.MILLISECONDS,
            RateLimitedQueue(maxAsyncRequests, initialPoolSize, maxNewThreadsPerSecond)
        )
    }

    @Bean
    fun getBlizzardWebClient(): WebClient {
        val mapper = ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val provider = ConnectionProvider.builder("Blizzard")
            .maxConnections(maxAsyncRequests)
            .build()

        val httpClient = HttpClient
            .create(provider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 100000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .responseTimeout(Duration.ofSeconds(45))
            .secure { sslContextSpec ->
                sslContextSpec.sslContext(SslContextBuilder.forClient())
                    .defaultConfiguration(SslProvider.DefaultConfigurationType.TCP)
                    .handshakeTimeout(Duration.ofSeconds(30))
            }

        val exchangeStrategies = ExchangeStrategies.builder()
            .codecs { configurer: ClientCodecConfigurer ->
                configurer.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(mapper))
                configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(mapper))
                //  20 MB max response size
                configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 20)
            }
            .build()

        val webClient = WebClient.builder()
            .baseUrl(url)
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .exchangeStrategies(exchangeStrategies)
            .build()

        httpClient.warmup().block()
        return webClient
    }
    companion object {
        private const val maxAsyncRequests: Int = 500
        private const val initialPoolSize: Int = 50
        private const val maxNewThreadsPerSecond: Double = 6.048
    }
}

class RateLimitedQueue<E>(
    private val maxAsyncRequests: Int,
    private val initialPoolSize: Int,
    maxNewThreadsPerSecond: Double,
) : LinkedBlockingQueue<E>(Int.MAX_VALUE) {
    private val newThreadWaitMillis = (initialPoolSize.toDouble() * 1000.0 / (maxNewThreadsPerSecond)).toLong()
    private val threadCreatedTimeQueue = LinkedList<Long>()
    private val poolSize = AtomicInteger(0)

    override fun offer(e: E): Boolean {
        val now = System.currentTimeMillis()

        while (threadCreatedTimeQueue.isNotEmpty()) {
            val threadCreatedTime = threadCreatedTimeQueue.peekFirst() ?: break
            if (threadCreatedTime < now - newThreadWaitMillis) {
                threadCreatedTimeQueue.removeFirst()
            } else {
                break
            }
        }

        return if (threadCreatedTimeQueue.size < initialPoolSize && poolSize.get() < maxAsyncRequests) {
            threadCreatedTimeQueue.addLast(now)
            poolSize.incrementAndGet()
            false
        } else {
            super.offer(e)
        }
    }
}