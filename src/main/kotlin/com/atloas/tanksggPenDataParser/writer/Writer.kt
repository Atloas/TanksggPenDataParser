package com.atloas.tanksggPenDataParser.writer

import com.atloas.tanksggPenDataParser.model.Tank

interface Writer {
    fun write(tanks: List<Tank>)
}