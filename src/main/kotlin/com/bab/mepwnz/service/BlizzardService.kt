package com.bab.mepwnz.service

import com.bab.mepwnz.clients.BlizzardClient
import com.bab.mepwnz.models.AuctionHouse
import java.util.concurrent.ExecutorService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BlizzardService (
    private val blizzardClient: BlizzardClient,
    getGPTExecutor: ExecutorService
) {
    private val logger = LoggerFactory.getLogger(BlizzardService::class.java)
    private val dispatcher = getGPTExecutor.asCoroutineDispatcher()
    suspend fun getAuctionHouse(): AuctionHouse = coroutineScope {
        logger.debug("Doing the things in the service class")

        val response = async(dispatcher){
            withContext(Dispatchers.IO){
                blizzardClient.getAuctionHouse()
            }
        }
        return@coroutineScope response.await()
    }
}