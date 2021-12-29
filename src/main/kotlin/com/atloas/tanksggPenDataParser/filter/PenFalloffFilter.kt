package com.atloas.tanksggPenDataParser.filter

import com.atloas.tanksggPenDataParser.model.Shell
import com.atloas.tanksggPenDataParser.model.ShellType
import com.atloas.tanksggPenDataParser.model.Tank
import com.atloas.tanksggPenDataParser.model.TankType

class PenFalloffFilter {

    fun filter(tanks: MutableList<Tank>): MutableList<Tank> {
        var result = filterOutSpgs(tanks)
        result = filterOutHeShells(result)
        result = filterOutInvalidAmmoTypeConfigurations(result)
        return result
    }

    private fun filterOutSpgs(tanks: MutableList<Tank>): MutableList<Tank> {
        return tanks.filter { it.type != TankType.SPG }.toMutableList()
    }

    private fun filterOutHeShells(tanks: MutableList<Tank>): MutableList<Tank> {
        for (tank in tanks) {
            for (gun in tank.guns) {
                gun.shells = gun.shells.filter { it.type != ShellType.HE }.toMutableList()
            }
            tank.guns = tank.guns.filter { it.shells.size == 2 }.toMutableList()
        }
        return tanks.filter { it.guns.isNotEmpty() }.toMutableList()
    }

    private fun filterOutInvalidAmmoTypeConfigurations(tanks: MutableList<Tank>): MutableList<Tank> {
        for (tank in tanks) {
            tank.guns = tank.guns.filter { shellConfigurationPredicate(it.shells) }.toMutableList()
        }
        return tanks.filter { it.guns.isNotEmpty() }.toMutableList()
    }

    private fun shellConfigurationPredicate(shells: List<Shell>): Boolean {
        if (shells[0].type != shells[1].type) {
            // Basically check if the order of shells is (silver, gold) or (gold, silver)
            if (shells[0].penetrationAt100 < shells[1].penetrationAt100) {
                // Order is (silver, gold), reject weirdo shell combinations
                if (
                    (shells[0].type == ShellType.APCR && shells[1].type == ShellType.AP)
                    ||
                    (shells[0].type == ShellType.HEAT && shells[1].type == ShellType.APCR)
                ) {
                    return false
                }
            } else {
                // Order is (gold, silver), reject weirdo shell combinations
                if (
                    (shells[0].type == ShellType.AP && shells[1].type == ShellType.APCR)
                    ||
                    (shells[0].type == ShellType.APCR && shells[1].type == ShellType.HEAT)
                ) {
                    return false
                }
            }
            return true
        }
        return false
    }
}