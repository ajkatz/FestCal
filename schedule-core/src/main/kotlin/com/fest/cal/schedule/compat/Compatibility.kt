package com.fest.cal.schedule.compat

import com.fest.cal.schedule.ScheduleArtist

/**
 * Music-taste compatibility — a festcal social pillar ("see how compatible your
 * tastes are", explicitly NOT a dating site). Pure + dependency-free so it
 * unit-tests on the JVM and a backend or the app can reuse it; identity reuses
 * `:schedule-core`'s normalized [ScheduleArtist] so it lines up with the
 * unified-schedule data.
 */

/** One artist a user is into, with a non-negative affinity weight (e.g. a
 *  normalized rating, or a count of sets seen/planned). */
data class ArtistAffinity(val artist: ScheduleArtist, val weight: Float)

/** A user's taste vector: their artist affinities. */
data class MusicProfile(val userId: String, val affinities: List<ArtistAffinity>) {
    /** Summed weight per normalized artist key (dedups repeats within a profile). */
    fun weightByKey(): Map<String, Float> {
        val out = LinkedHashMap<String, Float>()
        for (a in affinities) out[a.artist.key] = (out[a.artist.key] ?: 0f) + a.weight
        return out
    }
}

/** The result of comparing two profiles. */
data class Compatibility(
    /** Overall taste match, 0...1 (cosine similarity of the affinity vectors). */
    val score: Float,
    /** Set overlap, 0...1 (Jaccard of the artist sets, ignoring weights). */
    val overlap: Float,
    /** Artists both are into, ranked by combined affinity (shared highlights). */
    val sharedArtists: List<ScheduleArtist>,
) {
    companion object {
        val NONE = Compatibility(0f, 0f, emptyList())
    }
}
