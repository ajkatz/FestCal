package com.fest.cal.schedule.compat

import com.fest.cal.schedule.ScheduleArtist
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** JUnit mirror of the compatibility cases in `verify/SelfCheck.kt`. */
class CompatibilityTest {

    private fun profile(id: String, vararg pairs: Pair<String, Float>) =
        MusicProfile(id, pairs.map { ArtistAffinity(ScheduleArtist(it.first), it.second) })

    private val alice = profile("alice", "Subtronics" to 5f, "CloZee" to 4f, "Wooli" to 3f)

    @Test fun identicalTasteScoresOne() {
        val bob = profile("bob", "subtronics" to 5f, "CloZee" to 4f, "Wooli" to 3f)
        val c = CompatibilityAnalyzer.compare(alice, bob)
        assertTrue(c.score > 0.999f)
        assertTrue(c.overlap > 0.999f)
        assertEquals(3, c.sharedArtists.size)
    }

    @Test fun disjointTasteScoresZero() {
        val carol = profile("carol", "Excision" to 5f, "Knock2" to 4f)
        val c = CompatibilityAnalyzer.compare(alice, carol)
        assertEquals(0f, c.score)
        assertEquals(0f, c.overlap)
        assertTrue(c.sharedArtists.isEmpty())
    }

    @Test fun partialOverlapIsBetweenAndNormalized() {
        val dave = profile("dave", "Subtronics" to 5f, "Excision" to 5f)
        val c = CompatibilityAnalyzer.compare(alice, dave)
        assertEquals(0.5f, c.score, 0.001f)
        assertEquals(0.25f, c.overlap, 0.001f)
        assertEquals(listOf("subtronics"), c.sharedArtists.map { it.key })
    }

    @Test fun emptyProfileIsSafe() {
        val c = CompatibilityAnalyzer.compare(profile("x"), alice)
        assertEquals(0f, c.score)
        assertTrue(c.sharedArtists.isEmpty())
    }
}
