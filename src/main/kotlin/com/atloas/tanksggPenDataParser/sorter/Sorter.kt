package com.atloas.tanksggPenDataParser.sorter

import com.atloas.tanksggPenDataParser.model.Tank

class Sorter {
    fun sort(tanks: MutableList<Tank>): MutableList<Tank> {
        var result = sortTanksByTypeTierName(tanks)
        result = sortShellsByPen(result)
        return result
    }

    private fun sortTanksByTypeTierName(tanks: MutableList<Tank>): MutableList<Tank> {
        return tanks.sortedWith(compareBy({ it.type }, { it.tier }, { it.name })).toMutableList()
    }

    private fun sortShellsByPen(tanks: MutableList<Tank>): MutableList<Tank> {
        for (tank in tanks) {
            for (gun in tank.guns) {
                gun.shells.sortBy { it.penetrationAt100 }
            }
        }
        return tanks
    }
}