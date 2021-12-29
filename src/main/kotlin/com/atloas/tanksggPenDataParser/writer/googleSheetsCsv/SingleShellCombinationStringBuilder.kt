package com.atloas.tanksggPenDataParser.writer.googleSheetsCsv

import com.atloas.tanksggPenDataParser.model.Gun
import com.atloas.tanksggPenDataParser.model.Tank
import com.atloas.tanksggPenDataParser.model.TankType
import com.atloas.tanksggPenDataParser.model.Tier

class SingleShellCombinationStringBuilder {
    private var previousTier = Tier.I
    private var previousType = TankType.LT
    private var first = true
    private val builder = StringBuilder()
    private val lineComposer = LineComposer()

    fun append(tank: Tank, gun: Gun) {
        val currentType = tank.type
        val currentTier = tank.tier
        if (first) {
            appendTypeAndTier(currentType, currentTier)
            first = false
        } else {
            if (previousType != currentType) {
                appendTypeAndTier(currentType, currentTier)
            } else if (previousTier != currentTier) {
                appendTier(currentTier)
            }
        }
        previousType = currentType
        previousTier = currentTier
        builder.append(lineComposer.compose(tank, gun))
    }

    override fun toString(): String {
        return builder.toString()
    }

    private fun appendTier(tier: Tier) {
        builder.append("${tier.name}\n")
    }

    private fun appendTypeAndTier(type: TankType, tier: Tier) {
        builder.append("${type.name}\n")
        builder.append("${tier.name}\n")
    }
}