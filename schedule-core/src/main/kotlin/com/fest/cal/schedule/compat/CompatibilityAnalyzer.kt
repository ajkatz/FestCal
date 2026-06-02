package com.fest.cal.schedule.compat

import com.fest.cal.schedule.ScheduleArtist
import kotlin.math.sqrt

/**
 * Computes [Compatibility] between two [MusicProfile]s.
 *
 * - **score**: cosine similarity of the weighted affinity vectors over the union
 *   of artists — scale-invariant, so different rating ranges still compare
 *   sensibly. Clamped to 0...1 (negative-affinity "dislikes" can only pull it
 *   toward 0, never below).
 * - **overlap**: Jaccard of the artist sets (how much their catalogs intersect).
 * - **sharedArtists**: the intersection, ranked by combined affinity — the
 *   "you both love…" highlights.
 */
object CompatibilityAnalyzer {

    fun compare(a: MusicProfile, b: MusicProfile): Compatibility {
        val aw = a.weightByKey()
        val bw = b.weightByKey()
        val union = aw.keys + bw.keys
        if (union.isEmpty()) return Compatibility.NONE

        var dot = 0f
        var normA = 0f
        var normB = 0f
        for (key in union) {
            val x = aw[key] ?: 0f
            val y = bw[key] ?: 0f
            dot += x * y
            normA += x * x
            normB += y * y
        }
        val cosine = if (normA > 0f && normB > 0f) dot / (sqrt(normA) * sqrt(normB)) else 0f
        val score = cosine.coerceIn(0f, 1f)

        val shared = aw.keys intersect bw.keys
        val overlap = shared.size.toFloat() / union.size

        val artistByKey = artistLookup(a, b)
        val sharedArtists = shared
            .sortedByDescending { (aw[it] ?: 0f) + (bw[it] ?: 0f) }
            .mapNotNull { artistByKey[it] }

        return Compatibility(score = score, overlap = overlap, sharedArtists = sharedArtists)
    }

    /** One [ScheduleArtist] per key, preferring an entry that carries a spotifyId. */
    private fun artistLookup(a: MusicProfile, b: MusicProfile): Map<String, ScheduleArtist> {
        val out = HashMap<String, ScheduleArtist>()
        for (aff in a.affinities + b.affinities) {
            val existing = out[aff.artist.key]
            if (existing == null || (existing.spotifyId == null && aff.artist.spotifyId != null)) {
                out[aff.artist.key] = aff.artist
            }
        }
        return out
    }
}
