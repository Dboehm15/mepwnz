package com.bab.mepwnz.controllers

import com.bab.mepwnz.models.AuctionHouse
import com.bab.mepwnz.service.BlizzardService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class BlizzardControllers(
    private val blizzardService: BlizzardService
) {

    @GetMapping("/test")
    suspend fun test(/*@RequestBody convoNamesRequest: ConvoNamesRequest*/): AuctionHouse {
        return blizzardService.getAuctionHouse()
    }
}