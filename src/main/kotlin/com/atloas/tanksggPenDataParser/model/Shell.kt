package com.atloas.tanksggPenDataParser.model

data class Shell(val type: ShellType, val penetrationAt100: Float, val penetrationAt500: Float)

enum class ShellType {
    AP,
    APCR,
    HEAT,
    HE
}