//package com.fest.cal.spotify
//
//import android.content.Context
//import android.content.Intent
//import androidx.activity.result.contract.ActivityResultContract
//import com.spotify.sdk.android.auth.AuthorizationClient
//import com.spotify.sdk.android.auth.AuthorizationResponse
//
//class SpotifyAuthContract : ActivityResultContract<Intent, AuthorizationResponse>() {
//    override fun createIntent(context: Context, input: Intent): Intent {
//        return input
//    }
//
//    override fun parseResult(resultCode: Int, intent: Intent?): AuthorizationResponse {
//        return AuthorizationClient.getResponse(resultCode, intent)
//    }
//}