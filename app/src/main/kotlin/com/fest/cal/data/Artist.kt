package com.fest.cal.data

import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

open class Artist(
    @PrimaryKey val name: String,
    val spotifyId: String? = null,
    @TypeConverters(ArtistListConverter::class)
    val collaborativeArtists: List<Artist>,
): Serializable {
    data class SoloArtist(
        val soloName: String,
        val soloSpotifyId: String? = null,
    ): Artist(soloName, soloName, listOf())

    data class CollabArtist(
        val collabName: String,
        val collabSpotifyId: String? = null,
        val artists: List<Artist>,
    ): Artist(collabName, collabSpotifyId, artists)
}

class ArtistListConverter {
    @TypeConverter
    fun fromArtistList(artists: List<Artist>): String {
        return Gson().toJson(artists)
    }

    @TypeConverter
    fun toArtistList(artistsString: String): List<Artist> {
        val type = object : TypeToken<List<Artist>>() {}.type
        return Gson().fromJson(artistsString, type)
    }
}

val Claude_VonStroke = Artist.SoloArtist("Claude VonStroke", null)
val Big_Gigantic = Artist.SoloArtist("Big Gigantic", null)
val Nghtmre = Artist.SoloArtist("Nghtmre", null)
val String_Cheese_Incident = Artist.SoloArtist("String Cheese Incident", null)
val John_Summit = Artist.SoloArtist("John Summit", null)
val Dom_Dolla = Artist.SoloArtist("Dom Dolla", null)
val LSDream = Artist.SoloArtist("LSDream", null)
val CloZee = Artist.SoloArtist("CloZee", null)
val Everything_Always = Artist.CollabArtist("Everything Always", null, listOf(John_Summit, Dom_Dolla))
val Nelly_Furtado = Artist.SoloArtist("Nelly Furtado", null)
val The_Disco_Biscuits = Artist.SoloArtist("The Disco Biscuits", null)
val Ben_Bohmer = Artist.SoloArtist("Ben Böhmer", null)
val Knock2 = Artist.SoloArtist("Knock2", null)
val Pretty_Lights = Artist.SoloArtist("Pretty Lights", null)
val Seven_Lions = Artist.SoloArtist("Seven Lions", null)
val Ludacris = Artist.SoloArtist("Ludacris", null)
val Black_Tiger_Sex_Machine = Artist.SoloArtist("Black Tiger Sex Machine", null)
val Subtronics = Artist.SoloArtist("Subtronics", null)
val Lszee = Artist.CollabArtist("LSZEE", null, listOf(LSDream, CloZee))
val Excision = Artist.SoloArtist("Excision", null)
val Charlotte_De_Witte = Artist.SoloArtist("Charlotte De Witte", null)
val Gigantic_Nghtmre = Artist.CollabArtist("Gigantic Nghtmre", null, listOf(Big_Gigantic, Nghtmre))
val Umphreys_Mcgee = Artist.SoloArtist("Umphrey's McGee", null)
val Barclay_Crenshaw = Artist.CollabArtist("Barclay Crenshaw", null, listOf(Claude_VonStroke))
val Alleycvt = Artist.SoloArtist("ALLEYCVT")
val Atliens = Artist.SoloArtist("Atliens")
val Boogie_T = Artist.SoloArtist("Boogie T")
val Canabliss = Artist.SoloArtist("Canabliss")
val Caspa = Artist.SoloArtist("Caspa")
val Dimension = Artist.SoloArtist("Dimension")
val Ivy_Lab = Artist.SoloArtist("Ivy Lab")
val Level_Up = Artist.SoloArtist("Level Up")
val Wooli = Artist.SoloArtist("Wooli")
val LP_Giobbi = Artist.SoloArtist("LP Giobbi")
val Slayyyter = Artist.SoloArtist("Slayyyter")
val Shaun_Ross = Artist.SoloArtist("Shaun Ross")
val Only_Fire = Artist.SoloArtist("Only Fire")
val Acraze = Artist.SoloArtist("Acraze")
val Cannons = Artist.SoloArtist("Cannons")
val Cassian = Artist.SoloArtist("Cassian")
val Chase_and_Status = Artist.SoloArtist("Chase & Status")
val Cuco = Artist.SoloArtist("Cuco")
val Drama = Artist.SoloArtist("Drama")
val G_Jones = Artist.SoloArtist("G Jones")
val Green_Velvet = Artist.SoloArtist("Green Velvet")
val Hiatus_Kaiyote = Artist.SoloArtist("Hiatus Kaiyote")
val Kenny_Beats = Artist.SoloArtist("Kenny Beats")
val Lettuce = Artist.SoloArtist("Lettuce")
val Libianca = Artist.SoloArtist("Libianca")
val Matroda = Artist.SoloArtist("Matroda")
val Mau_P = Artist.SoloArtist("Mau P")
val Neil_Frances = Artist.SoloArtist("Neil Frances")
val Rawayana = Artist.SoloArtist("Rawayana")
val Sammy_Virji = Artist.SoloArtist("Sammy Virji")
val Sara_Landry = Artist.SoloArtist("Sara Landry")
val Vini_Vici = Artist.SoloArtist("Vini Vici")
val Whyte_Fang = Artist.SoloArtist("Whyte Fang")
val AK_Sports = Artist.SoloArtist("AK Sports")
val Ayybo = Artist.SoloArtist("Ayybo")
val Baggi = Artist.SoloArtist("Baggi")
val Blastoyz = Artist.SoloArtist("Blastoyz")
val Boogie_Trio = Artist.CollabArtist("Boogie T.Rio", null, listOf(Boogie_T))
val Brandi_Cyrus = Artist.SoloArtist("Brandi Cyrus")
val Calussa = Artist.SoloArtist("Calussa")
val Chaos_in_the_CBD = Artist.SoloArtist("Chaos in the CBD")
val Coco_and_Breezy = Artist.SoloArtist("Coco & Breezy")
val Dirtwire = Artist.SoloArtist("Dirtwire")
val Dixons_Violin = Artist.SoloArtist("Dixon's Violin")
val DJ_Brownie = Artist.SoloArtist("DJ Brownie")
val DJ_Susan = Artist.SoloArtist("DJ Susan")
val Dumstaphunk = Artist.SoloArtist("Dumstaphunk")
val Eggy = Artist.SoloArtist("Eggy")
val Emo_Nite = Artist.SoloArtist("Emo Nite")
val Equanimous = Artist.SoloArtist("Equanimous")
val Goodboys = Artist.SoloArtist("Goodboys")
val Hamdi = Artist.SoloArtist("Hamdi")
val Harry = Artist.SoloArtist("H&rry")
val Inzo = Artist.SoloArtist("Inzo")
val Its_Murph = Artist.SoloArtist("It's Murph")
val Jason_Leech = Artist.SoloArtist("Jason Leech")
val Jenna_Shaw = Artist.SoloArtist("Jenna Shaw")
val Jjuujjuu = Artist.SoloArtist("Jjuujjuu")
val Juelz = Artist.SoloArtist("Juelz")
val Kallaghan = Artist.SoloArtist("Kallaghan")
val Kiltro = Artist.SoloArtist("Kiltro")
val Layton_Giordanni = Artist.SoloArtist("Layton Giordanni")
val Le_Youth = Artist.SoloArtist("Le Youth")
val League_of_Sound_Disciples = Artist.SoloArtist("League of Sound Disciples")
val Levity = Artist.SoloArtist("Levity")
val Little_Stranger = Artist.SoloArtist("Little Stranger")
val Luci = Artist.SoloArtist("Luci")
val Lyny = Artist.SoloArtist("Lyny")
val Maddy_O_Neal = Artist.SoloArtist("Maddy O'Neal")
val Masonic = Artist.SoloArtist("Masonic")
val Major_League_Diz = Artist.SoloArtist("Major League Diz")
val Marsh = Artist.SoloArtist("Marsh")
val Mascolo = Artist.SoloArtist("Mascolo")
val Michael_Brun = Artist.SoloArtist("Michaël Brun")
val Mojave_Grey = Artist.SoloArtist("Mojave Grey")
val Moontricks = Artist.SoloArtist("Moontricks")
val Neoma = Artist.SoloArtist("Neoma")
val Odd_Mob = Artist.SoloArtist("Odd Mob")
val Omnom = Artist.SoloArtist("Omnom")
val Oden_and_Fatzo = Artist.SoloArtist("Oden & Fatzo")
val Paperwater = Artist.SoloArtist("Paperwater")
val Peach_Tree_Rascals = Artist.SoloArtist("Peach Tree Rascals")
val Politik = Artist.SoloArtist("Politik")
val Polyrhythmics = Artist.SoloArtist("Polyrhythmics")
val Pretty_Pink = Artist.SoloArtist("Pretty Pink")
val Proxima_Parada = Artist.SoloArtist("Próxima Parada")
val Ranger_Trucco = Artist.SoloArtist("Ranger Trucco")
val Rayben = Artist.SoloArtist("Rayben")
val Redrum = Artist.SoloArtist("Redrum")
val Shae_District = Artist.SoloArtist("Shae District")
val Sultan = Artist.SoloArtist("Sultan")
val Shepard = Artist.SoloArtist("Shepard")
val Super_Future = Artist.SoloArtist("Super Future")
val Swaylo = Artist.SoloArtist("Swayló")
val Thought_Process = Artist.SoloArtist("Thought Process")
val Tripp_St = Artist.SoloArtist("Tripp St.")
val Tsha = Artist.SoloArtist("TSHA")
val Unusual_Demont = Artist.SoloArtist("Unusual Demont")
val Venbee = Artist.SoloArtist("Venbee")
val Vnssa = Artist.SoloArtist("VNSSA")
val Nala = Artist.SoloArtist("Nala")
val VNSSA_B2B_Nala = Artist.CollabArtist("VNSSA B2B Nala", null, listOf(Vnssa, Nala))
val Westend = Artist.SoloArtist("Westend")
val Will_Clarke = Artist.SoloArtist("Will Clarke")
val Zen_Selekta = Artist.SoloArtist("Zen Selekta")
