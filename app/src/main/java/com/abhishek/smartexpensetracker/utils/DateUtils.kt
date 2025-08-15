package com.abhishek.smartexpensetracker.utils

import java.util.*

object DateUtils {
    fun startOfDayMillis(epochMillis: Long): Long {
        val c = Calendar.getInstance().apply { timeInMillis = epochMillis }
        c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0); c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0)
        return c.timeInMillis
    }

    fun endOfDayMillis(epochMillis: Long): Long {
        val c = Calendar.getInstance().apply { timeInMillis = epochMillis }
        c.set(Calendar.HOUR_OF_DAY, 23); c.set(Calendar.MINUTE, 59); c.set(Calendar.SECOND, 59); c.set(Calendar.MILLISECOND, 999)
        return c.timeInMillis
    }
}
