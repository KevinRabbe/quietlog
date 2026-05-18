package com.kevinrabbe.quietlog.domain.util

import com.kevinrabbe.quietlog.domain.model.RepeatRule
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class TimeUtilsTest {

    @Test
    fun `calculateNextOccurrence with NONE returns base time`() {
        val baseTime = 1000L
        val result = TimeUtils.calculateNextOccurrence(baseTime, RepeatRule.NONE)
        assertEquals(baseTime, result)
    }

    @Test
    fun `calculateNextOccurrence DAILY with base time in future returns base time`() {
        val now = 1000L
        val baseTime = 2000L
        val result = TimeUtils.calculateNextOccurrence(baseTime, RepeatRule.DAILY, now = now)
        assertEquals(baseTime, result)
    }

    @Test
    fun `calculateNextOccurrence DAILY with base time in past returns next day`() {
        val now = 2000L
        val baseTime = 1000L
        val result = TimeUtils.calculateNextOccurrence(baseTime, RepeatRule.DAILY, now = now)
        
        val expected = baseTime + (24 * 60 * 60 * 1000L)
        assertEquals(expected, result)
    }

    @Test
    fun `calculateNextOccurrence MONDAY when base time is Sunday returns next Monday`() {
        // May 17, 2026 is a Sunday
        val sunday = Calendar.getInstance().apply {
            set(2026, Calendar.MAY, 17, 10, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        
        val result = TimeUtils.calculateNextOccurrence(sunday, RepeatRule.MONDAY, now = sunday)
        
        val expectedMonday = Calendar.getInstance().apply {
            set(2026, Calendar.MAY, 18, 10, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        
        assertEquals(expectedMonday, result)
    }

    @Test
    fun `calculateNextOccurrence MONDAY when base time is Monday past returns next week Monday`() {
        // May 18, 2026 is a Monday
        val mondayTime = Calendar.getInstance().apply {
            set(2026, Calendar.MAY, 18, 10, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        
        val now = mondayTime + 1000L // 1 second after
        
        val result = TimeUtils.calculateNextOccurrence(mondayTime, RepeatRule.MONDAY, now = now)
        
        val nextMonday = Calendar.getInstance().apply {
            set(2026, Calendar.MAY, 25, 10, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        
        assertEquals(nextMonday, result)
    }
}
