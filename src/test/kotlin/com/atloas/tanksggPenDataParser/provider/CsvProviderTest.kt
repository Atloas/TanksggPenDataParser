package com.atloas.tanksggPenDataParser.provider

import io.mockk.impl.annotations.MockK
import org.apache.commons.configuration2.builder.fluent.Configurations
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import java.io.File

@MockK
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CsvProviderTest {

    private val configuration = Configurations().properties(File("/application-test.properties"))
    private val csvProvider = CsvProvider(configuration)

    @Test
    fun `should handle inputs with extra commas`() {
        assertDoesNotThrow { csvProvider.provide() }
    }
}