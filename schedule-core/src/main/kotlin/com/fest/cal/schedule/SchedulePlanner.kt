package com.fest.cal.schedule

/** A clash between two sets a user wants to catch. */
data class Conflict(val a: SetSlot, val b: SetSlot)

/** A set the user wants to see, with how much they want it (higher = preferred). */
data class WantedSet(val slot: SetSlot, val weight: Float = 1f)

/** The auto-built plan: a clash-free pick that maximizes total preference. */
data class PlanResult(
    val chosen: List<SetSlot>,
    val dropped: List<SetSlot>,
    val totalWeight: Float,
)

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

    /**
     * Auto-build the best clash-free plan from a wishlist — "play festivals"
     * resolved, not just detected. Maximizes total [WantedSet.weight] (default
     * weight 1 ⇒ "see the most acts"). Untimed sets can't clash, so they're
     * always kept; timed sets are optimized per day via weighted interval
     * scheduling (you can only be one place at a time within a day).
     */
    fun bestPlan(wanted: List<WantedSet>): PlanResult {
        val chosen = mutableListOf<SetSlot>()
        var total = 0f

        val (timed, untimed) = wanted.partition { it.slot.intervalMinutes() != null }
        untimed.forEach { chosen += it.slot; total += it.weight }

        timed.groupBy { it.slot.day }.forEach { (_, dayItems) ->
            val (picks, value) = maxWeightNonOverlapping(dayItems)
            chosen += picks
            total += value
        }

        val keep = chosen.toHashSet()
        val dropped = wanted.map { it.slot }.filterNot { it in keep }
        return PlanResult(chosen = chosen, dropped = dropped, totalWeight = total)
    }

    /** Weighted interval scheduling over one day's timed sets (intervals are
     *  half-open minute ranges). Returns the max-weight non-overlapping pick. */
    private fun maxWeightNonOverlapping(items: List<WantedSet>): Pair<List<SetSlot>, Float> {
        if (items.isEmpty()) return emptyList<SetSlot>() to 0f
        data class Iv(val slot: SetSlot, val w: Float, val start: Int, val end: Int)
        val ivs = items.map {
            val r = it.slot.intervalMinutes()!!
            Iv(it.slot, it.weight, r.first, r.last + 1) // exclusive end
        }.sortedBy { it.end }
        val n = ivs.size
        // p[i] = index of the latest interval that finishes at/before ivs[i] starts, else -1.
        val p = IntArray(n) { i ->
            var lo = 0; var hi = i - 1; var res = -1
            while (lo <= hi) {
                val mid = (lo + hi) / 2
                if (ivs[mid].end <= ivs[i].start) { res = mid; lo = mid + 1 } else hi = mid - 1
            }
            res
        }
        val dp = FloatArray(n + 1) // dp[i] = best value using ivs[0 until i]
        for (i in 1..n) {
            val include = ivs[i - 1].w + dp[p[i - 1] + 1]
            dp[i] = maxOf(dp[i - 1], include)
        }
        val picks = mutableListOf<SetSlot>()
        var i = n
        while (i > 0) {
            val include = ivs[i - 1].w + dp[p[i - 1] + 1]
            if (include >= dp[i - 1]) { picks += ivs[i - 1].slot; i = p[i - 1] + 1 } else i -= 1
        }
        return picks.reversed() to dp[n]
    }
}
