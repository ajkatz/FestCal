//package com.fest.cal.spotify
//
//data class SpotifyAuthData(
//    val authToken: String,
//    val expiryMs: Long,
//    val displayName: String?,
//    val imageUrl: String?,
//) {
//
//    fun toBuilder(): Builder {
//        return Builder(
//            authToken,
//            expiryMs,
//            displayName,
//            imageUrl
//        )
//    }
//
//    class Builder(
//        var authToken: String,
//        var expiryMs: Long,
//        var displayName: String?,
//        var imageUrl: String?
//    ) {
//        fun build(): SpotifyAuthData {
//            return SpotifyAuthData(
//                authToken,
//                expiryMs,
//                displayName,
//                imageUrl
//            )
//        }
//
//        fun setAuthToken(authToken: String): Builder {
//            this.authToken = authToken
//            return this
//        }
//
//        fun setExpiryMs(expiryMs: Long): Builder {
//            this.expiryMs = expiryMs
//            return this
//        }
//
//        fun setDisplayName(displayName: String?): Builder {
//            this.displayName = displayName
//            return this
//        }
//
//        fun setImageUrl(imageUrl: String?): Builder {
//            this.imageUrl = imageUrl
//            return this
//        }
//    }
//}
//
