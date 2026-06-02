import com.fest.cal.schedule.FestivalLineup
import com.fest.cal.schedule.ScheduleArtist
import com.fest.cal.schedule.SchedulePlanner
import com.fest.cal.schedule.SetSlot
import com.fest.cal.schedule.UnifiedSchedule
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

    if (failures == 0) {
        println("\nALL SELF-CHECKS PASSED")
    } else {
        println("\n$failures CHECK(S) FAILED")
        kotlin.system.exitProcess(1)
    }
}
