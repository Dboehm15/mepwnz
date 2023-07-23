package com.bab.mepwnz.clients

import com.bab.mepwnz.models.AuctionHouse
import com.bab.mepwnz.models.RealmMetaData
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono


@Component
@ConfigurationProperties("")
class BlizzardClient (
    private val blizzardClient: WebClient,
) {
    lateinit var access_token: String
    fun getAuctionHouse(realmId: Int): Mono<AuctionHouse> {
        return blizzardClient.get()
            .uri("data/wow/connected-realm/$realmId/auctions?namespace=dynamic-us&locale=en_US&access_token=$access_token")
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<AuctionHouse>() {})
    }

    fun getRealmMetaData(): Mono<RealmMetaData> {
        return blizzardClient.get()
            .uri("data/wow/search/connected-realm?namespace=dynamic-us&status.type=UP&orderby=id&_page=1&_page=1&access_token=$access_token")
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<RealmMetaData>() {})
    }
}