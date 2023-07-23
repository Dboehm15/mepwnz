package com.bab.mepwnz.clients

import com.bab.mepwnz.configs.BlizzardTokenBucket
import com.bab.mepwnz.models.AuctionHouse
import com.bab.mepwnz.models.ItemData
import com.bab.mepwnz.models.RealmMetaData
import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono


@Component
@ConfigurationProperties("")
class BlizzardClient (
    private val blizzardClient: WebClient,
) {
    val dotenv = dotenv()
    private val access_token = dotenv["ACCESS_TOKEN"]
    private val tokenBucketPerSecond = BlizzardTokenBucket(100, 1000)
    private val tokenBucketPerHour = BlizzardTokenBucket(36000, 3600 * 1000)
    fun getAuctionHouse(realmId: Int): Mono<AuctionHouse> {
        rateLimit()
        return blizzardClient.get()
            .uri("data/wow/connected-realm/$realmId/auctions?namespace=dynamic-us&locale=en_US&access_token=$access_token")
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<AuctionHouse>() {})
    }

    fun getRealmMetaData(): Mono<RealmMetaData> {
        rateLimit()
        return blizzardClient.get()
            .uri("data/wow/search/connected-realm?namespace=dynamic-us&status.type=UP&orderby=id&_page=1&_page=1&access_token=$access_token")
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<RealmMetaData>() {})
    }

    fun getItemData(itemId: Int): Mono<ItemData> {
        rateLimit()
        return blizzardClient.get()
            .uri("data/wow/item/$itemId?namespace=static-us&locale=en_US&access_token=$access_token")
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<ItemData>() {})
    }

    private fun rateLimit() {
        if (!tokenBucketPerSecond.tryConsume() || !tokenBucketPerHour.tryConsume()) {
            Thread.sleep(1000)
        }
    }
}