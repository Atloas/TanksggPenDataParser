package com.atloas.tanksggPenDataParser.writer.googleSheetsCsv

import com.atloas.tanksggPenDataParser.model.Gun
import com.atloas.tanksggPenDataParser.model.Tank

class LineComposer {
    fun compose(tank: Tank, gun: Gun): String {
        return "\"${tank.name}\",\"${gun.name}\",\"${gun.shells[0].penetrationAt100}\",\"${gun.shells[1].penetrationAt100}\",\"\",\"\",\"${gun.shells[0].penetrationAt500}\",\"${gun.shells[1].penetrationAt500}\"\n"
    }
}