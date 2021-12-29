package com.atloas.tanksggPenDataParser.provider

import com.atloas.tanksggPenDataParser.model.*
import org.apache.commons.configuration2.PropertiesConfiguration

class CsvProvider(propertiesConfiguration: PropertiesConfiguration) : Provider {

    private val filePath = propertiesConfiguration.getString("provider.csv.fileName")

    override fun provide(): MutableList<Tank> {
        val lines = readFile()
        return parse(lines)
    }

    private fun readFile(): List<String> {
        return this::class.java.getResourceAsStream(filePath)!!.bufferedReader().readLines()
    }

    private fun parse(lines: List<String>): MutableList<Tank> {
        val tanks = mutableMapOf<String, Tank>()
        for (line in lines) {
            val preparedLine = prepareLine(line)
            val elements = mapValues(preparedLine)
            if (!tanks.containsKey(elements.getValue(Column.TANK_NAME))) {
                tanks[elements.getValue(Column.TANK_NAME)] = createTank(elements)
            }
            val tank = tanks.getValue(elements.getValue(Column.TANK_NAME))
            var gun = tank.getGunByName(elements.getValue(Column.GUN_NAME))
            if (gun == null) {
                gun = createGun(elements)
                tank.guns.add(gun)
            }
            val shell = createShell(elements)
            // A naive check to avoid hidden duplicate guns from adding multiple copies of a shell. Would still cause inconsistencies if the duplicate gun had a different selection of shells.
            if (!gun.shells.contains(shell)) {
                gun.shells.add(shell)
            }
        }
        return tanks.values.toMutableList()
    }

    private fun createTank(elements: Map<Column, String>): Tank {
        val name = elements.getValue(Column.TANK_NAME)
        val tier = Tier.fromInt(elements.getValue(Column.TANK_TIER).toInt())
        val type = TankType.fromString(elements.getValue(Column.TANK_TYPE))
        val guns = mutableListOf<Gun>()
        return Tank(name, tier, type, guns)
    }

    private fun createGun(elements: Map<Column, String>): Gun {
        val name = elements.getValue(Column.GUN_NAME)
        val shells = mutableListOf<Shell>()
        return Gun(name, shells)
    }

    private fun createShell(elements: Map<Column, String>): Shell {
        val shellType = when (elements.getValue(Column.SHELL_TYPE)) {
            "ARMOR_PIERCING" -> ShellType.AP
            "ARMOR_PIERCING_CR" -> ShellType.APCR
            "HOLLOW_CHARGE" -> ShellType.HEAT
            "HIGH_EXPLOSIVE" -> ShellType.HE
            else -> throw IllegalArgumentException("Invalid shell type detected: ${elements.getValue(Column.SHELL_TYPE)}")
        }
        val penAt100 = elements.getValue(Column.PEN_AT_100).toFloat()
        val penAt500 = elements.getValue(Column.PEN_AT_500).toFloat()
        return Shell(shellType, penAt100, penAt500)
    }

    private fun prepareLine(line: String): String {
        // Looks for a comma surrounded by non-quotation mark symbols, replaces it with the same two symbols but without the comma
        var preparedLine = line.replace(Regex("[^\"],[^\"]")) { it.value.replace(",", "") }
        preparedLine = preparedLine.replace("\"", "")
        return preparedLine
    }

    private fun mapValues(line: String): Map<Column, String> {
        val values = line.split(",")
        // Probably never gonna trigger with prepareLine doing what it does
        assert(values.size == 7) { "Assertion failed: improper line split, too many commas. Line: [$line]" }
        return mapOf(
            Column.TANK_NAME to values[Column.TANK_NAME.ordinal],
            Column.TANK_TIER to values[Column.TANK_TIER.ordinal],
            Column.TANK_TYPE to values[Column.TANK_TYPE.ordinal],
            Column.GUN_NAME to values[Column.GUN_NAME.ordinal],
            Column.SHELL_TYPE to values[Column.SHELL_TYPE.ordinal],
            Column.PEN_AT_100 to values[Column.PEN_AT_100.ordinal],
            Column.PEN_AT_500 to values[Column.PEN_AT_500.ordinal]
        )
    }

    private enum class Column {
        // Strict ordering
        TANK_NAME,
        TANK_TIER,
        TANK_TYPE,
        GUN_NAME,
        SHELL_TYPE,
        PEN_AT_100,
        PEN_AT_500
    }
}