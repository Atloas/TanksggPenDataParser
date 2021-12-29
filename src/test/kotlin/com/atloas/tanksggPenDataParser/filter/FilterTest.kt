package com.atloas.tanksggPenDataParser.filter

import com.atloas.tanksggPenDataParser.model.Tank
import com.atloas.tanksggPenDataParser.model.TankType
import com.atloas.tanksggPenDataParser.provider.CsvProvider
import org.apache.commons.configuration2.builder.fluent.Configurations
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.File

class FilterTest {
    private val configuration = Configurations().properties(File("/application-test.properties"))
    private val csvProvider = CsvProvider(configuration)
    private val penFalloffFilter = PenFalloffFilter()

    // This shouldn't depend on csvProvider, but I can't be bothered to create this data structure manually
    private val result = penFalloffFilter.filter(csvProvider.provide())

    @Test
    fun `should let valid tanks through`() {
        var validTank: Tank? = null
        assertDoesNotThrow { validTank = result.first { it.name == "Valid Tank" } }
        assert(validTank!!.guns.size == 2)
    }

    @Test
    fun `should filter out spgs`() {
        val spgs = result.filter { it.type == TankType.SPG }
        assert(spgs.isEmpty())
    }

    @Test
    fun `should filter out invalid guns`() {
        var oneInvalidGunTank: Tank? = null
        assertDoesNotThrow { oneInvalidGunTank = result.first { it.name == "One Invalid Gun Tank" } }
        assert(oneInvalidGunTank!!.guns.size == 1)
        assertDoesNotThrow { oneInvalidGunTank!!.guns.first { it.name == "75 mm Gun M2" } }

        var limitedAmmoSelectionTank: Tank? = null
        assertDoesNotThrow { limitedAmmoSelectionTank = result.first { it.name == "Limited Ammo Selection Tank" } }
        assert(limitedAmmoSelectionTank!!.guns.size == 1)
        assertDoesNotThrow { limitedAmmoSelectionTank!!.guns.first { it.name == "37 mm Gun M5" } }

        assertThrows<NoSuchElementException> { result.first { it.name == "Twin-HE Gun Tank" } }
    }

    // TODO: Something for duplicate guns
}
