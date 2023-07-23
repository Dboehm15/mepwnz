package com.bab.mepwnz.controllers

import com.bab.mepwnz.models.AuctionHouseRequest
import com.bab.mepwnz.models.AuctionHouseResponse
import com.bab.mepwnz.service.BlizzardService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@CrossOrigin
@RestController
class BlizzardControllers(
    private val blizzardService: BlizzardService
) {

    @GetMapping("/test")
    suspend fun test(@RequestBody auctionHouseRequest: AuctionHouseRequest): AuctionHouseResponse? {
        val realmId = auctionHouseRequest.id
            ?: auctionHouseRequest.serverName?.let {
                blizzardService.getServerId(it)
            }?.block()

        return if (realmId == null) {
            AuctionHouseResponse(msg = "You did not send a valid serverName or Id")
        } else {
            blizzardService.getAuctionHouse(realmId)
                .map { auctionHouse ->
                    AuctionHouseResponse(auctionHouse = auctionHouse)
                }
                .onErrorReturn(AuctionHouseResponse(msg = "An error occurred"))
                .block()
        }
    }
}