import com.fest.cal.schedule.FestivalLineup
import com.fest.cal.schedule.ScheduleArtist
import com.fest.cal.schedule.SchedulePlanner
import com.fest.cal.schedule.SetSlot
import com.fest.cal.schedule.UnifiedSchedule
import com.fest.cal.schedule.WantedSet
import com.fest.cal.schedule.compat.ArtistAffinity
import com.fest.cal.schedule.compat.CompatibilityAnalyzer
import com.fest.cal.schedule.compat.MusicProfile
import java.time.LocalDate
import java.time.LocalTime

// Command-line verification for :schedule-core. The full FestCal Gradle build
// needs a sibling repo (AndroidKotlinCommon) + an emulator-less JVM test run; to
// verify the pure logic here, compile the module sources with this file and run:
//
//   kotlinc schedule-core/src/main/kotlin/com/fest/cal/schedule/*.kt \
//           schedule-core/verify/SelfCheck.kt -include-runtime -d /tmp/sc.jar
//   java -jar /tmp/sc.jar
//
// The same assertions exist as JUnit tests under src/test for the Gradle build.

private var failures = 0

private fun check(cond: Boolean, msg: String) {
    if (cond) println("  ok  $msg") else { failures++; System.err.println("  FAIL $msg") }
}

fun main() {
    val d1 = LocalDate.of(2026, 7, 24)
    val d2 = LocalDate.of(2026, 7, 25)
    val subtronics = ScheduleArtist("Subtronics")
    val clozee = ScheduleArtist("CloZee")

    val f1 = FestivalLineup(
        "f1", "Fest One",
        listOf(
            SetSlot(subtronics, "f1", d1, LocalTime.of(20, 0), 60, "Main"),
            SetSlot(clozee, "f1", d1, LocalTime.of(20, 30), 60, "Side"), // overlaps subtronics
            SetSlot(ScheduleArtist("Wooli"), "f1", d2, LocalTime.of(18, 0), 60, "Main"),
        ),
    )
    // Same artist (different case + whitespace) appears at a second festival.
    val f2 = FestivalLineup(
        "f2", "Fest Two",
        listOf(
            SetSlot(ScheduleArtist("  subtronics "), "f2", d1, LocalTime.of(22, 0), 60, "Stage A"),
            SetSlot(ScheduleArtist("Excision"), "f2", d2, LocalTime.of(21, 0), 60, "Stage A"),
        ),
    )
    val u = UnifiedSchedule.unify(listOf(f1, f2))

    check(u.sets.size == 5, "unify merges all sets")
    val byDay = u.byDay()
    check(byDay.keys.toList() == listOf(d1, d2), "byDay sorted across festivals")
    check(byDay[d1]!!.size == 3, "day1 merges 3 sets from both festivals")
    check(
        byDay[d1]!!.map { it.start } ==
            listOf(LocalTime.of(20, 0), LocalTime.of(20, 30), LocalTime.of(22, 0)),
        "day1 ordered by start time",
    )

    val artists = u.artists()
    check(artists.count { it.key == "subtronics" } == 1, "subtronics deduped across festivals")
    check(u.festivalsFor("Subtronics") == setOf("f1", "f2"), "festivalsFor finds both (name-key)")
    check(u.setsFor("subtronics").size == 2, "setsFor matches across festivals, normalized")

    check(SchedulePlanner.overlaps(f1.sets[0], f1.sets[1]), "overlapping sets detected")
    check(!SchedulePlanner.overlaps(f1.sets[0], f2.sets[0]), "non-overlapping same day → no conflict")
    check(!SchedulePlanner.overlaps(f1.sets[0], f1.sets[2]), "different day → no conflict")
    val untimed = SetSlot(ScheduleArtist("TBA"), "f1", d1)
    check(!SchedulePlanner.overlaps(f1.sets[0], untimed), "untimed set → no conflict")

    val plan = listOf(f1.sets[0], f1.sets[1], f2.sets[0])
    check(SchedulePlanner.conflicts(plan).size == 1, "plan has exactly one conflict")
    check(!SchedulePlanner.isConflictFree(plan), "plan with overlap is not conflict-free")
    check(
        SchedulePlanner.isConflictFree(listOf(f1.sets[0], f2.sets[0])),
        "non-overlapping plan is conflict-free",
    )

    // --- compatibility ---
    fun profile(id: String, vararg pairs: Pair<String, Float>) =
        MusicProfile(id, pairs.map { ArtistAffinity(ScheduleArtist(it.first), it.second) })

    val alice = profile("alice", "Subtronics" to 5f, "CloZee" to 4f, "Wooli" to 3f)
    val bob = profile("bob", "subtronics" to 5f, "CloZee" to 4f, "Wooli" to 3f) // same taste, diff case
    val carol = profile("carol", "Excision" to 5f, "Knock2" to 4f) // disjoint
    val dave = profile("dave", "Subtronics" to 5f, "Excision" to 5f) // partial

    val same = CompatibilityAnalyzer.compare(alice, bob)
    check(same.score > 0.999f, "compat: identical taste → score ~1")
    check(same.overlap > 0.999f, "compat: identical taste → overlap 1")
    check(same.sharedArtists.size == 3, "compat: identical → all shared")

    val none = CompatibilityAnalyzer.compare(alice, carol)
    check(none.score == 0f && none.overlap == 0f, "compat: disjoint → score/overlap 0")
    check(none.sharedArtists.isEmpty(), "compat: disjoint → no shared")

    val partial = CompatibilityAnalyzer.compare(alice, dave)
    check(partial.score in 0.4f..0.6f, "compat: half-overlap vectors → ~0.5")
    check(partial.overlap == 0.25f, "compat: 1 shared of 4 → 0.25 overlap")
    check(
        partial.sharedArtists.map { it.key } == listOf("subtronics"),
        "compat: shared artist matched on normalized key",
    )

    val empty = CompatibilityAnalyzer.compare(profile("x"), alice)
    check(empty.score == 0f && empty.sharedArtists.isEmpty(), "compat: empty profile → none, no crash")

    // --- auto-plan optimizer ---
    val untimedWanted = SetSlot(ScheduleArtist("Surprise"), "f1", d1) // no time
    val wanted = listOf(
        WantedSet(f1.sets[0], 5f), // 20:00-21:00 d1
        WantedSet(f1.sets[1], 3f), // 20:30-21:30 d1 (clashes sets[0])
        WantedSet(f2.sets[0], 1f), // 22:00-23:00 d1
        WantedSet(f1.sets[2], 1f), // d2 18:00
        WantedSet(untimedWanted, 2f),
    )
    val autoPlan = SchedulePlanner.bestPlan(wanted)
    check(autoPlan.totalWeight == 9f, "plan: maximizes weight (d1 5+1, d2 1, untimed 2 = 9)")
    check(autoPlan.dropped == listOf(f1.sets[1]), "plan: drops the lower-weight clash")
    check(SchedulePlanner.isConflictFree(autoPlan.chosen), "plan: result is clash-free")
    check(untimedWanted in autoPlan.chosen, "plan: untimed wanted always kept")

    val equal = SchedulePlanner.bestPlan(
        listOf(WantedSet(f1.sets[0]), WantedSet(f1.sets[1]), WantedSet(f2.sets[0])),
    )
    check(equal.chosen.size == 2, "plan: equal weights → maximize count (2 of 3 clashing)")

    if (failures == 0) {
        println("\nALL SELF-CHECKS PASSED")
    } else {
        println("\n$failures CHECK(S) FAILED")
        kotlin.system.exitProcess(1)
    }
}
