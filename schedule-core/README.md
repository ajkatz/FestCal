# :schedule-core

The platform-agnostic heart of FestCal's differentiator: unify many festival /
concert lineups into **one** schedule and plan a personal route through it.

Pure Kotlin/JVM — **no Android, Room, or Compose** — so it builds and unit-tests
on the JVM without an emulator, and a future backend can reuse it unchanged. The
`:app` module maps its own `Performance`/`Festival`/`Artist` types onto these
(adapter = a separate task).

## What's here

- **`Model`** — `ScheduleArtist` (name-keyed identity, normalized for dedup),
  `SetSlot` (artist + festival + day + time + stage), `FestivalLineup`.
- **`UnifiedSchedule`** — `unify(lineups)`, `byDay()` (merged, sorted),
  `artists()` (deduped across festivals), `festivalsFor(name)` / `setsFor(name)`
  (cross-festival discovery — "who's playing where").
- **`SchedulePlanner`** — `overlaps` / `conflicts` / `isConflictFree`: the
  "play festivals" planner that flags set-time clashes in a chosen plan.

## Testing

Under the full Gradle build (needs the sibling `AndroidKotlinCommon` repo the
app's `:colors`/`:platform` modules reference):

```sh
./gradlew :schedule-core:test
```

To verify the logic without the full project / an emulator (e.g. with only the
Kotlin CLI), compile the sources with the self-check harness and run it:

```sh
kotlinc schedule-core/src/main/kotlin/com/fest/cal/schedule/*.kt \
        schedule-core/verify/SelfCheck.kt -include-runtime -d /tmp/sc.jar
java -jar /tmp/sc.jar
```

`verify/SelfCheck.kt` and `src/test/.../ScheduleCoreTest.kt` assert the same
behavior — keep them in sync.

## Next

- An adapter in `:app` mapping `Festival`/`Performance` → `FestivalLineup`/`SetSlot`.
- Live ingestion sources (festival lineup APIs / scrapers) producing
  `FestivalLineup`s — the hard data-acquisition half.
