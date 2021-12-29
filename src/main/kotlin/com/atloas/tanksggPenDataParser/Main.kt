package com.atloas.tanksggPenDataParser

import com.atloas.tanksggPenDataParser.filter.PenFalloffFilter
import com.atloas.tanksggPenDataParser.provider.CsvProvider
import com.atloas.tanksggPenDataParser.sorter.Sorter
import com.atloas.tanksggPenDataParser.writer.googleSheetsCsv.GoogleSheetsCsvWriter
import org.apache.commons.configuration2.builder.fluent.Configurations
import java.io.File

fun main() {
    val configurations = Configurations()
    val configuration = configurations.properties(File("/application.properties"))

    val provider = CsvProvider(configuration)
    val filter = PenFalloffFilter()
    val sorter = Sorter()
    val writer = GoogleSheetsCsvWriter(configuration)

    var tanks = provider.provide()
    tanks = filter.filter(tanks)
    tanks = sorter.sort(tanks)
    writer.write(tanks)
}