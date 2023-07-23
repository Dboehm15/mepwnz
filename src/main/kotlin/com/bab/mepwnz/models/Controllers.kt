package com.bab.mepwnz.models

data class AuctionHouseRequest(
    val id: Int? = null,
    val serverName: String? = null
)

data class AuctionHouseResponse(
    val msg: String? = null,
    val auctionCount: Int? = null,
    val auctionHouse: AuctionHouse? = null
)

data class AhListing(
    val id: Int,
    val name: String,
    val buyout: Long,
    val bid: Long? = null,
    val quantity: Int,
    val timeLeft: String
)

class AhListings(
    val listings: List<AhListing>? = emptyList()
)