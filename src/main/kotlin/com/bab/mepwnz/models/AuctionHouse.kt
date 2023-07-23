package com.bab.mepwnz.models

data class AuctionHouse(
    val _links: Links = Links(),
    val auctions: List<Auction> = listOf(),
    val commodities: Commodities = Commodities(),
    val connected_realm: ConnectedRealm = ConnectedRealm()
)

data class Links(
    val self: Self = Self()
)

data class Auction(
    val bid: Long? = null,
    val buyout: Long = 0,
    val id: Int = 0,
    val item: Item = Item(),
    val quantity: Int = 0,
    val time_left: String = ""
)

data class Commodities(
    val href: String = ""
)

data class ConnectedRealm(
    val href: String = ""
)

data class Self(
    val href: String = ""
)

data class Item(
    val bonus_lists: List<Int>? = null,
    val context: Int? = null,
    val id: Int = 0,
    val modifiers: List<Modifier>? = null,
    val pet_breed_id: Int? = null,
    val pet_level: Int? = null,
    val pet_quality_id: Int? = null,
    val pet_species_id: Int? = null
)

data class Modifier(
    val type: Int = 0,
    val value: Int = 0
)