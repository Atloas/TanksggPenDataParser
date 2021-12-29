package com.atloas.tanksggPenDataParser.provider

import com.atloas.tanksggPenDataParser.model.Tank

interface Provider {
    fun provide(): MutableList<Tank>
}