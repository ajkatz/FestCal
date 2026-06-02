package com.fest.cal.schedule

/** A clash between two sets a user wants to catch. */
data class Conflict(val a: SetSlot, val b: SetSlot)

/**
 * Pure helpers for building a personal route through the schedule — the
 * "play festivals" planning the user wants. Conflicts are time overlaps within
 * a chosen set of slots.
 */
object SchedulePlanner {

    /**
     * Two sets overlap when they're on the same labeled [day], both are timed
     * (start + duration), and their half-open minute intervals intersect.
     * Untimed sets never report a conflict (we can't know).
     */
    fun overlaps(a: SetSlot, b: SetSlot): Boolean {
        if (a.day != b.day) return false
        val ai = a.intervalMinutes() ?: return false
        val bi = b.intervalMinutes() ?: return false
        // Half-open overlap: a.start < b.end && b.start < a.end.
        return ai.first < bi.last + 1 && bi.first < ai.last + 1
    }

    /** All conflicting pairs within a chosen plan. */
    fun conflicts(plan: List<SetSlot>): List<Conflict> {
        val out = mutableListOf<Conflict>()
        for (i in plan.indices) {
            for (j in i + 1 until plan.size) {
                if (overlaps(plan[i], plan[j])) out += Conflict(plan[i], plan[j])
            }
        }
        return out
    }

    /** True when no two sets in the plan clash. */
    fun isConflictFree(plan: List<SetSlot>): Boolean = conflicts(plan).isEmpty()
}
