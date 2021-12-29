package com.atloas.tanksggPenDataParser.writer.googleSheetsCsv

import com.atloas.tanksggPenDataParser.model.ShellType
import com.atloas.tanksggPenDataParser.model.Tank
import com.atloas.tanksggPenDataParser.writer.Writer
import org.apache.commons.configuration2.PropertiesConfiguration
import java.io.FileOutputStream

class GoogleSheetsCsvWriter(propertiesConfiguration: PropertiesConfiguration) : Writer {

    private val fileLocation = propertiesConfiguration.getString("writer.googleSheetsCsv.fileLocation")
    private val apApcrFileName = propertiesConfiguration.getString("writer.googleSheetsCsv.apApcrFileName")
    private val apHeatFileName = propertiesConfiguration.getString("writer.googleSheetsCsv.apHeatFileName")
    private val apcrHeatFileName = propertiesConfiguration.getString("writer.googleSheetsCsv.apcrHeatFileName")
    private val fileSuffix = propertiesConfiguration.getString("writer.googleSheetsCsv.fileSuffix")

    override fun write(tanks: List<Tank>) {
        val strings = assembleStrings(tanks)
        writeToFiles(strings)
    }

    private fun assembleStrings(tanks: List<Tank>): Map<String, String> {
        val apApcrBuilder = SingleShellCombinationStringBuilder()
        val apHeatBuilder = SingleShellCombinationStringBuilder()
        val apcrHeatBuilder = SingleShellCombinationStringBuilder()
        for (tank in tanks) {
            for (gun in tank.guns) {
                when (gun.shells[0].type to gun.shells[1].type) {
                    ShellType.AP to ShellType.APCR -> apApcrBuilder.append(tank, gun)
                    ShellType.AP to ShellType.HEAT -> apHeatBuilder.append(tank, gun)
                    ShellType.APCR to ShellType.HEAT -> apcrHeatBuilder.append(tank, gun)
                    else -> throw IllegalStateException("Invalid shell configuration detected in printer!")
                }
            }
        }
        return mapOf(
            "AP-APCR" to apApcrBuilder.toString(),
            "AP-HEAT" to apHeatBuilder.toString(),
            "APCR-HEAT" to apcrHeatBuilder.toString()
        )
    }

    private fun writeToFiles(strings: Map<String, String>) {
        FileOutputStream(fileLocation + apApcrFileName + fileSuffix).use { stream ->
            stream.writer().use { writer ->
                writer.write(strings.getValue("AP-APCR"))
            }
        }
        FileOutputStream(fileLocation + apHeatFileName + fileSuffix).use { stream ->
            stream.writer().use { writer ->
                writer.write(strings.getValue("AP-HEAT"))
            }
        }
        FileOutputStream(fileLocation + apcrHeatFileName + fileSuffix).use { stream ->
            stream.writer().use { writer ->
                writer.write(strings.getValue("APCR-HEAT"))
            }
        }
    }
}
