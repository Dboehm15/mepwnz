package com.bab.mepwnz.service

import com.bab.mepwnz.clients.BlizzardClient
import com.bab.mepwnz.models.AuctionHouse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BlizzardService (
    private val blizzardClient: BlizzardClient,
) {
    private val logger = LoggerFactory.getLogger(BlizzardService::class.java)
    suspend fun getAuctionHouse(realmId: Int): Mono<AuctionHouse> {
        logger.info("Getting auction house data")
        return blizzardClient.getAuctionHouse(realmId)
    }

    suspend fun getServerId(serverName: String): Mono<Int>  {
        logger.info("Getting realmId")
        return blizzardClient.getRealmMetaData()
            .flatMapMany { metaData ->
                Flux.fromIterable(metaData.results)
            }
            .flatMap { results ->
                Flux.fromIterable(results.data.realms)
            }
            .filter { realm ->
                realm.name.en_US == serverName
            }
            .map { realm ->
                realm.id
            }
            .next()
    }
}