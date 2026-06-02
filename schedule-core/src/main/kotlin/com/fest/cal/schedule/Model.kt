package com.fest.cal.schedule

import java.time.LocalDate
import java.time.LocalTime

/**
 * The platform-agnostic, dependency-free core for unifying festival/concert
 * schedules into one view and planning a personal route through them.
 *
 * Pure Kotlin (no Android, no Room/Compose) so it builds + unit-tests on the JVM
 * — the differentiating logic ("integrate all schedules", "lineups by day",
 * "play festivals" conflict planning) is verifiable without an emulator and is
 * equally usable by a future backend. The `:app` module maps its own
 * `Performance`/`Festival`/`Artist` types onto these (an adapter, separate task).
 */

/** A performing act. `name` is the cross-source identity key. */
data class ScheduleArtist(
    val name: String,
    val spotifyId: String? = null,
) {
    /** Normalized key for cross-festival dedup: trimmed, lowercased, whitespace-collapsed. */
    val key: String
        get() = name.trim().lowercase().replace(WHITESPACE, " ")

    private companion object {
        val WHITESPACE = Regex("\\s+")
    }
}

/**
 * One artist's set at a festival on a given day, optionally timed + staged.
 *
 * Conflict math uses [start] + [durationMinutes] in absolute minutes (so a set
 * running past midnight is handled), and assumes a set's time is within its
 * labeled [day] (lineups conventionally label a post-midnight set under the day
 * it starts). Cross-day-boundary labeling is a future refinement.
 */
data class SetSlot(
    val artist: ScheduleArtist,
    val festivalId: String,
    val day: LocalDate,
    val start: LocalTime? = null,
    val durationMinutes: Int? = null,
    val stage: String? = null,
    val headlineTier: Int = 0,
) {
    /** Display end time (wraps within a day), when both start + duration are known. */
    val end: LocalTime? get() = start?.let { s -> durationMinutes?.let { s.plusMinutes(it.toLong()) } }

    /** Half-open [startMinute, endMinute) from midnight for overlap math, or null if untimed. */
    internal fun intervalMinutes(): IntRange? {
        val s = start ?: return null
        val d = durationMinutes ?: return null
        val startMin = s.hour * 60 + s.minute
        return startMin until (startMin + d)
    }
}

/** A single festival's lineup. */
data class FestivalLineup(
    val festivalId: String,
    val name: String,
    val sets: List<SetSlot>,
)
