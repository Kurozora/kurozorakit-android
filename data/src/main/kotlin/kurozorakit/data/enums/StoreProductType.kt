package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Categorizes in-app store products for transaction history and receipt verification.
 */
@Serializable(with = StoreProductTypeSerializer::class)
enum class StoreProductType(val rawValue: Int) {
    consumable(0),
    nonConsumable(1),
    nonRenewingSubscription(2),
    autoRenewingSubscription(3);

    companion object {
        fun fromInt(value: Int): StoreProductType? = entries.find { it.rawValue == value }
    }
}

object StoreProductTypeSerializer : IntValueEnumSerializer<StoreProductType>(
    serialName = "StoreProductType",
    entries = StoreProductType.entries,
    valueSelector = { it.rawValue },
    fallback = StoreProductType.consumable,
)
