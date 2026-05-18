package com.kevinrabbe.quietlog.domain.util

import com.kevinrabbe.quietlog.domain.model.RepeatRule
import java.util.Calendar

object TimeUtils {

    /**
     * Calculates the next occurrence of an event based on a repeat rule.
     * 
     * @param baseTimeMillis The reference time (e.g., the originally selected time).
     * @param rule The repeat rule to apply.
     * @param ensureFuture If true, the returned time will always be after [now].
     * @param now The current time to compare against (defaults to System.currentTimeMillis()).
     */
    fun calculateNextOccurrence(
        baseTimeMillis: Long,
        rule: RepeatRule,
        ensureFuture: Boolean = true,
        now: Long = System.currentTimeMillis()
    ): Long {
        if (rule == RepeatRule.NONE) return baseTimeMillis

        val cal = Calendar.getInstance().apply { timeInMillis = baseTimeMillis }

        when (rule) {
            RepeatRule.DAILY, RepeatRule.WEEKLY, RepeatRule.MONTHLY -> {
                // Initial time is set to baseTimeMillis. We'll adjust it in the loop below if ensureFuture is true.
            }
            RepeatRule.MONDAY, RepeatRule.TUESDAY, RepeatRule.WEDNESDAY,
            RepeatRule.THURSDAY, RepeatRule.FRIDAY, RepeatRule.SATURDAY, RepeatRule.SUNDAY -> {
                val targetDayOfWeek = when (rule) {
                    RepeatRule.SUNDAY -> Calendar.SUNDAY
                    RepeatRule.MONDAY -> Calendar.MONDAY
                    RepeatRule.TUESDAY -> Calendar.TUESDAY
                    RepeatRule.WEDNESDAY -> Calendar.WEDNESDAY
                    RepeatRule.THURSDAY -> Calendar.THURSDAY
                    RepeatRule.FRIDAY -> Calendar.FRIDAY
                    RepeatRule.SATURDAY -> Calendar.SATURDAY
                    else -> -1
                }

                if (targetDayOfWeek != -1) {
                    val currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
                    var daysToAdd = targetDayOfWeek - currentDayOfWeek
                    // If target day is before today, or today but we need it in the future and base time is past
                    if (daysToAdd < 0) {
                        daysToAdd += 7
                    }
                    cal.add(Calendar.DAY_OF_YEAR, daysToAdd)
                }
            }
            else -> {}
        }

        // Final check to ensure it's in the future if requested
        if (ensureFuture && cal.timeInMillis <= now) {
             while (cal.timeInMillis <= now) {
                 when (rule) {
                     RepeatRule.DAILY -> cal.add(Calendar.DAY_OF_YEAR, 1)
                     RepeatRule.WEEKLY, RepeatRule.MONDAY, RepeatRule.TUESDAY, RepeatRule.WEDNESDAY,
                     RepeatRule.THURSDAY, RepeatRule.FRIDAY, RepeatRule.SATURDAY, RepeatRule.SUNDAY -> cal.add(Calendar.WEEK_OF_YEAR, 1)
                     RepeatRule.MONTHLY -> cal.add(Calendar.MONTH, 1)
                     else -> break
                 }
             }
        }

        return cal.timeInMillis
    }
}
