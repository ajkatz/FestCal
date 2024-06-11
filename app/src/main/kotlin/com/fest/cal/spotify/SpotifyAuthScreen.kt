//package com.fest.cal.spotify
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.fest.cal.R
//import com.spotify.sdk.android.auth.AuthorizationResponse
//
//@Composable
//fun SpotifyAuthScreen(
//    onAuthenticate: () -> Unit,
//    authResponse: AuthorizationResponse?
//) {
//    Box(modifier = Modifier.fillMaxSize()) {
//        Button(
//            modifier = Modifier.align(Alignment.BottomStart).padding(8.dp),
//            onClick = onAuthenticate,
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.spotify_black_logo),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(28.dp)
//                        .padding(end = 8.dp),
//                    contentScale = ContentScale.Fit
//                )
//                Text(
//                    fontSize = 18.sp,
//                    color = Color(0xFF191414),
//                    text = "Log in")
//            }
//        }
//
//        // Handle auth response
//        authResponse?.let { response ->
//            when (response.type) {
//                AuthorizationResponse.Type.TOKEN -> {
//                    val accessToken = response.accessToken
//                    // Handle the access token
//                    Text("Authenticated! Token: $accessToken")
//                }
//                AuthorizationResponse.Type.ERROR -> {
//                    // Handle the error
//                    Text("Authentication Error: ${response.error}")
//                }
//                else -> {
//                    // Handle other cases
//                    Text("Unknown response")
//                }
//            }
//        }
//    }
//}
