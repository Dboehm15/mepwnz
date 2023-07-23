package com.bab.mepwnz.clients

import com.bab.mepwnz.models.AuctionHouse
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import org.springframework.web.util.UriComponentsBuilder


@Component
@ConfigurationProperties("")
class BlizzardClient (
    private val blizzardClient: WebClient,
) {
    // lateinit var token: String
    lateinit var access_token: String
    fun getAuctionHouse(): AuctionHouse {
        return blizzardClient.get()
            .uri("data/wow/connected-realm/11/auctions?namespace=dynamic-us&locale=en_US&access_token=$access_token")
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<AuctionHouse>() {})
            .block() as AuctionHouse
    }
}