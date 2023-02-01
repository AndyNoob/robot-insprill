package net.insprill.robotinsprill.extension

import dev.kord.common.Color

fun String.color(): Color {
    val raw = this.trimStart('#')
    val r = raw.substring(0, 2).toInt(16)
    val g = raw.substring(2, 4).toInt(16)
    val b = raw.substring(4, 6).toInt(16)
    return Color(r, g, b)
}
