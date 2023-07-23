package com.bab.mepwnz.models

data class AuctionHouseRequest(
    val id: Int? = null,
    val serverName: String? = null
)

data class AuctionHouseResponse(
    val msg: String? = null,
    val auctionHouse: AuctionHouse? = null
)