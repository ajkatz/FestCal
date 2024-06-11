//package com.fest.cal.spotify
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.core.Serializer
//import androidx.datastore.dataStore
//import kotlinx.coroutines.flow.Flow
//import java.io.InputStream
//import java.io.OutputStream
//import com.fest.cal.SpotifyAuthProto.SpotifyAuthData
//import kotlinx.coroutines.flow.map
//
//class SpotifyAuthRepository(private val context: Context) {
//
//    val spotifyAuthData: Flow<SpotifyAuthData?> = context.spotifyAuthDataStore.data.map { it }
//
//    suspend fun updateAuthToken(authToken: String) {
//        context.spotifyAuthDataStore.updateData { currentData ->
//            currentData?.toBuilder()?.setAuthToken(authToken)?.build()
//        }
//    }
//
//    suspend fun updateDisplayName(displayName: String?) {
//        context.spotifyAuthDataStore.updateData { currentData ->
//            currentData?.toBuilder()?.setDisplayName(displayName)?.build()
//        }
//    }
//
//    suspend fun updateImageUrl(imageUrl: String?) {
//        context.spotifyAuthDataStore.updateData { currentData ->
//            currentData?.toBuilder()?.setImageUrl(imageUrl)?.build()
//        }
//    }
//}
//
//val Context.spotifyAuthDataStore: DataStore<SpotifyAuthData?> by dataStore(
//    fileName = "spotify_auth_data.pb",
//    serializer = SpotifyAuthDataSerializer
//)
//
//object SpotifyAuthDataSerializer : Serializer<SpotifyAuthData?> {
//    override val defaultValue: SpotifyAuthData? = null
//
//    override suspend fun readFrom(input: InputStream): SpotifyAuthData {
//        return SpotifyAuthData.parseFrom(input)
//    }
//
//    override suspend fun writeTo(t: SpotifyAuthData?, output: OutputStream) {
//        t?.writeTo(output)
//    }
//}