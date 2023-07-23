package com.bab.mepwnz.service

import com.bab.mepwnz.clients.BlizzardClient
import com.bab.mepwnz.models.AuctionHouse
import com.bab.mepwnz.models.RealmMetaData
import java.util.concurrent.ExecutorService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BlizzardService (
    private val blizzardClient: BlizzardClient,
) {
    private val logger = LoggerFactory.getLogger(BlizzardService::class.java)
    suspend fun getAuctionHouse(realmId: Int): Mono<AuctionHouse>  {
        logger.debug("Doing the things in the service class")
        val results = blizzardClient.getAuctionHouse(realmId)
/*
        results.flatMapMany { auctionHouse ->
            Flux.fromIterable(auctionHouse.auctions)
        }
        .doOnNext { auctionItem ->

        }*/

        return results
    }

    suspend fun getServerId(serverName: String): Mono<Int>  {
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