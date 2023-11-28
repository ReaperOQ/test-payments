package ru.reaperoq.test_task.utils

import java.math.RoundingMode
import java.text.DecimalFormat

fun roundOffDecimal(number: Double): String {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(number)
}