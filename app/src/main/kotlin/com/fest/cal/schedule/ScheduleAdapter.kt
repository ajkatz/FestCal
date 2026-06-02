package com.fest.cal.schedule

import com.fest.cal.data.Artist
import com.fest.cal.data.Festival
import com.fest.cal.data.Performance
import java.time.LocalDate

/**
 * Maps the app's Android-side domain types (`Festival` / `Performance` /
 * `Artist`) onto the pure `:schedule-core` types, so the unify / by-day /
 * conflict-planning logic can run over the app's data.
 *
 * NOTE (authored without an Android build — this file imports the app's types,
 * so it only compiles in Android Studio): the `:schedule-core` logic itself is
 * verified; this is the thin bridge. Watch the two assumptions below.
 */

/** Slug used as the stable cross-festival id (the `Festival` model has no id). */
fun Festival.scheduleId(): String =
    name.trim().lowercase().replace(NON_SLUG, "-").trim('-').ifEmpty { "festival" }

fun Artist.toScheduleArtist(): ScheduleArtist =
    ScheduleArtist(name = name, spotifyId = spotifyId)

/**
 * @param festivalId stable id of the owning festival.
 * @param fallbackDay used when a performance has no date (assumed the festival's
 *   first day). Undated sets are kept (not dropped) so they still appear.
 */
fun Performance.toSetSlot(festivalId: String, fallbackDay: LocalDate): SetSlot =
    SetSlot(
        artist = artist.toScheduleArtist(),
        festivalId = festivalId,
        day = performanceDate ?: fallbackDay,
        start = performanceStartTime,
        durationMinutes = durationMinutes,
        stage = stage,
        headlineTier = headlineTier,
    )

fun Festival.toFestivalLineup(): FestivalLineup {
    val id = scheduleId()
    val fallbackDay = dateRange.first
    return FestivalLineup(
        festivalId = id,
        name = name,
        sets = artistList.map { it.toSetSlot(id, fallbackDay) },
    )
}

/** Bridge a set of the app's festivals into one merged, queryable schedule. */
fun List<Festival>.toUnifiedSchedule(): UnifiedSchedule =
    UnifiedSchedule.unify(map { it.toFestivalLineup() })

private val NON_SLUG = Regex("[^a-z0-9]+")
