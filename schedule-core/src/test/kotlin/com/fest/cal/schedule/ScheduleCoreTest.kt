package com.fest.cal.schedule

import java.time.LocalDate
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * JUnit mirror of `verify/SelfCheck.kt` for the Gradle build (`./gradlew
 * :schedule-core:test`). Keep the two in sync.
 */
class ScheduleCoreTest {
    private val d1 = LocalDate.of(2026, 7, 24)
    private val d2 = LocalDate.of(2026, 7, 25)

    private val f1 = FestivalLineup(
        "f1", "Fest One",
        listOf(
            SetSlot(ScheduleArtist("Subtronics"), "f1", d1, LocalTime.of(20, 0), 60, "Main"),
            SetSlot(ScheduleArtist("CloZee"), "f1", d1, LocalTime.of(20, 30), 60, "Side"),
            SetSlot(ScheduleArtist("Wooli"), "f1", d2, LocalTime.of(18, 0), 60, "Main"),
        ),
    )
    private val f2 = FestivalLineup(
        "f2", "Fest Two",
        listOf(
            SetSlot(ScheduleArtist("  subtronics "), "f2", d1, LocalTime.of(22, 0), 60, "Stage A"),
            SetSlot(ScheduleArtist("Excision"), "f2", d2, LocalTime.of(21, 0), 60, "Stage A"),
        ),
    )
    private val unified = UnifiedSchedule.unify(listOf(f1, f2))

    @Test fun unifyMergesAllSets() {
        assertEquals(5, unified.sets.size)
    }

    @Test fun byDaySortsAndMergesAcrossFestivals() {
        val byDay = unified.byDay()
        assertEquals(listOf(d1, d2), byDay.keys.toList())
        assertEquals(3, byDay.getValue(d1).size)
        assertEquals(
            listOf(LocalTime.of(20, 0), LocalTime.of(20, 30), LocalTime.of(22, 0)),
            byDay.getValue(d1).map { it.start },
        )
    }

    @Test fun artistsDedupAcrossFestivalsOnNormalizedName() {
        assertEquals(1, unified.artists().count { it.key == "subtronics" })
        assertEquals(setOf("f1", "f2"), unified.festivalsFor("Subtronics"))
        assertEquals(2, unified.setsFor("subtronics").size)
    }

    @Test fun overlapDetection() {
        assertTrue(SchedulePlanner.overlaps(f1.sets[0], f1.sets[1]), "same-day overlap")
        assertFalse(SchedulePlanner.overlaps(f1.sets[0], f2.sets[0]), "same-day, no overlap")
        assertFalse(SchedulePlanner.overlaps(f1.sets[0], f1.sets[2]), "different day")
        val untimed = SetSlot(ScheduleArtist("TBA"), "f1", d1)
        assertFalse(SchedulePlanner.overlaps(f1.sets[0], untimed), "untimed")
    }

    @Test fun conflictPlanning() {
        val plan = listOf(f1.sets[0], f1.sets[1], f2.sets[0])
        assertEquals(1, SchedulePlanner.conflicts(plan).size)
        assertFalse(SchedulePlanner.isConflictFree(plan))
        assertTrue(SchedulePlanner.isConflictFree(listOf(f1.sets[0], f2.sets[0])))
    }

    @Test fun bestPlanMaximizesWeightClashFree() {
        val untimed = SetSlot(ScheduleArtist("Surprise"), "f1", d1)
        val plan = SchedulePlanner.bestPlan(
            listOf(
                WantedSet(f1.sets[0], 5f),
                WantedSet(f1.sets[1], 3f), // clashes f1.sets[0], lower weight → dropped
                WantedSet(f2.sets[0], 1f),
                WantedSet(f1.sets[2], 1f),
                WantedSet(untimed, 2f),
            ),
        )
        assertEquals(9f, plan.totalWeight, 0.001f)
        assertEquals(listOf(f1.sets[1]), plan.dropped)
        assertTrue(SchedulePlanner.isConflictFree(plan.chosen))
        assertTrue(untimed in plan.chosen)
    }

    @Test fun bestPlanEqualWeightsMaximizesCount() {
        val plan = SchedulePlanner.bestPlan(
            listOf(WantedSet(f1.sets[0]), WantedSet(f1.sets[1]), WantedSet(f2.sets[0])),
        )
        assertEquals(2, plan.chosen.size)
    }
}
