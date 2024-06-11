//package com.fest.cal.spotify
//
//import android.app.Activity
//import android.content.Context
//import com.spotify.sdk.android.auth.AuthorizationClient
//import com.spotify.sdk.android.auth.AuthorizationRequest
//import com.spotify.sdk.android.auth.AuthorizationResponse
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.runBlocking
//
//const val AUTH_REQUEST = 1001
//class BaseSpotifyApi(context: Context): SpotifyApi {
//    var bearerToken: String?
//    val authRepository =  SpotifyAuthRepository(context)
//
//    init {
//        bearerToken = runBlocking(Dispatchers.IO) {
//            getAuthData()?.authToken
//        }
//    }
//
//    override suspend fun getAuthData(): SpotifyAuthData? {
//        val data = authRepository.spotifyAuthData.first()
//        return data?.let {
//            bearerToken = it.authToken
//            SpotifyAuthData.Builder(
//                it.authToken,
//                it.expiryMs,
//                it.displayName,
//                it.imageUrl
//            ).build()
//        }
//    }
//
//    override suspend fun authenthicate(activity: Activity) {
//        val builder = AuthorizationRequest.Builder(
//            "festcal",
//            AuthorizationResponse.Type.TOKEN,
//            "festcal://spotify-callback"
//        )
//        builder.setScopes(arrayOf("playlist-modify-private"))
//        val request = builder.build()
//        AuthorizationClient.openLoginActivity(activity, AUTH_REQUEST, request)
//    }
//
//    override suspend fun unauthenticate() {
//        TODO("Not yet implemented")
//    }
//}