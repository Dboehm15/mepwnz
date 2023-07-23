package com.bab.mepwnz.models

data class RealmMetaData(
    val maxPageSize: Int = 0,
    val page: Int = 0,
    val pageCount: Int = 0,
    val pageSize: Int = 0,
    val results: List<Result> = listOf()
)

data class Result(
    val `data`: Data = Data(),
    val key: Key = Key()
)

data class Data(
    val has_queue: Boolean = false,
    val id: Int = 0,
    val population: Population = Population(),
    val realms: List<Realm> = listOf(),
    val status: Status = Status()
)

data class Key(
    val href: String = ""
)

data class Population(
    val name: Name = Name(),
    val type: String = ""
)

data class Realm(
    val category: Category = Category(),
    val id: Int = 0,
    val is_tournament: Boolean = false,
    val locale: String = "",
    val name: Name = Name(),
    val region: Region = Region(),
    val slug: String = "",
    val timezone: String = "",
    val type: Type = Type()
)

data class Status(
    val name: Name = Name(),
    val type: String = ""
)

data class Name(
    val de_DE: String = "",
    val en_GB: String = "",
    val en_US: String = "",
    val es_ES: String = "",
    val es_MX: String = "",
    val fr_FR: String = "",
    val it_IT: String = "",
    val ko_KR: String = "",
    val pt_BR: String = "",
    val ru_RU: String = "",
    val zh_CN: String = "",
    val zh_TW: String = ""
)

data class Category(
    val de_DE: String = "",
    val en_GB: String = "",
    val en_US: String = "",
    val es_ES: String = "",
    val es_MX: String = "",
    val fr_FR: String = "",
    val it_IT: String = "",
    val ko_KR: String = "",
    val pt_BR: String = "",
    val ru_RU: String = "",
    val zh_CN: String = "",
    val zh_TW: String = ""
)

data class Region(
    val id: Int = 0,
    val name: Name = Name()
)

data class Type(
    val name: Name = Name(),
    val type: String = ""
)