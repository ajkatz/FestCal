//package com.fest.cal.spotify
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.activity.result.ActivityResultLauncher
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.platform.ComposeView
//import androidx.fragment.app.Fragment
//import com.spotify.sdk.android.auth.AuthorizationClient
//import com.spotify.sdk.android.auth.AuthorizationRequest
//import com.spotify.sdk.android.auth.AuthorizationResponse
//
//class SpotifyAuthFragment: Fragment() {
//
//    private lateinit var spotifyAuthLauncher: ActivityResultLauncher<Intent>
//    private var authResponse by mutableStateOf<AuthorizationResponse?>(null)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        spotifyAuthLauncher = registerForActivityResult(SpotifyAuthContract()) { response ->
//            handleSpotifyAuthResponse(response)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return ComposeView(requireContext()).apply {
//            setContent {
//                SpotifyAuthScreen(
//                    onAuthenticate = { authenticateSpotify() },
//                    authResponse = authResponse
//                )
//            }
//        }
//    }
//
//    private fun authenticateSpotify() {
//        val builder = AuthorizationRequest.Builder(
//            CLIENT_ID,
//            AuthorizationResponse.Type.TOKEN,
//            REDIRECT_URI
//        )
//        builder.setScopes(arrayOf("user-read-private", "streaming"))
//        val request = builder.build()
//        val intent = AuthorizationClient.createLoginActivityIntent(requireActivity(), request)
//        spotifyAuthLauncher.launch(intent)
//    }
//
//    private fun handleSpotifyAuthResponse(response: AuthorizationResponse) {
//        when (response.type) {
//            AuthorizationResponse.Type.TOKEN -> {
//                Log.e("katz", "token ${response.accessToken}")
//            }
//            AuthorizationResponse.Type.ERROR -> {
//                Log.e("katz", "error ${response.error}")
//            }
//            else -> {
//                Log.e("katz", "other ${response.state}")
//            }
//        }
//    }
//
//    companion object {
//        private const val CLIENT_ID = "35074ee7fb774924a3bce4b50a8ac97c"
//        private const val REDIRECT_URI = "festcal://spotify-callback"
//    }
//}