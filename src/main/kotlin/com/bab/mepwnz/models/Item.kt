package com.bab.mepwnz.models

data class ItemData(
    val id: Int = 0,
    val inventory_type: InventoryType = InventoryType(),
    val is_equippable: Boolean = false,
    val is_stackable: Boolean = false,
    val item_class: ItemClass = ItemClass(),
    val item_subclass: ItemSubclass = ItemSubclass(),
    val level: Int = 0,
    val max_count: Int = 0,
    val media: Media = Media(),
    val name: String = "",
    val purchase_price: Int = 0,
    val purchase_quantity: Int = 0,
    val quality: QualityX = QualityX(),
    val required_level: Int = 0,
    val sell_price: Int = 0
)

data class InventoryType(
    val name: String = "",
    val type: String = ""
)

data class ItemClass(
    val id: Int = 0,
    val key: Key = Key(),
    val name: String = ""
)

data class ItemSubclass(
    val id: Int = 0,
    val key: Key = Key(),
    val name: String = ""
)

data class Media(
    val id: Int = 0,
    val key: Key = Key()
)



data class QualityX(
    val name: String = "",
    val type: String = ""
)