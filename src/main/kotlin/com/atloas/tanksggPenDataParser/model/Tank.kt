package com.atloas.tanksggPenDataParser.model

data class Tank(val name: String, val tier: Tier, val type: TankType, var guns: MutableList<Gun>) {

    fun getGunByName(gunName: String): Gun? {
        for (gun in guns) {
            if (gun.name == gunName) {
                return gun
            }
        }
        return null
    }
}

enum class TankType {
    LT,
    MT,
    HT,
    TD,
    SPG;

    companion object {
        fun fromString(type: String): TankType {
            return when (type) {
                "light" -> LT
                "medium" -> MT
                "heavy" -> HT
                "td" -> TD
                "spg" -> SPG
                else -> throw IllegalArgumentException("Invalid tank type: $type")
            }
        }
    }
}

enum class Tier {
    I,
    II,
    III,
    IV,
    V,
    VI,
    VII,
    VIII,
    IX,
    X;

    companion object {
        fun fromInt(tier: Int): Tier {
            return when (tier) {
                1 -> I
                2 -> II
                3 -> III
                4 -> IV
                5 -> V
                6 -> VI
                7 -> VII
                8 -> VIII
                9 -> IX
                10 -> X
                else -> throw IllegalArgumentException("Invalid tier: $tier!")
            }
        }
    }
}