package com.fest.cal.data

import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class Festival(
    val name: String,
    val promotionImageUrl: String,
    val lineupImageUrl: String,
    val dateRange: Pair<LocalDate, LocalDate>,
    val artistList: List<Performance>
): Serializable {
    fun getDayList(): List<LocalDate> {
        val list = mutableListOf<LocalDate>()
        var festivalDate = dateRange.first
        val daysBetween = dateRange.daysBetween()
        for (i in 0..daysBetween) {
            list.add(festivalDate)
            festivalDate = festivalDate.plusDays(1)
        }
        return list
    }

    fun getSelectionTabs(): List<String> {
        if (dateRange.daysBetween() == 0L) {
            return getDayList().map { it.dayOfWeek.name.substring(0, 3) }
        }
        val list = mutableListOf("ALL")
        list.addAll(getDayList().map { it.dayOfWeek.name.substring(0, 3) })
        return list
    }

    fun getDateRangeString(): String {
        val datePattern = DateTimeFormatter.ofPattern("MMMM d")
        if (dateRange.daysBetween() == 0L) {
            return dateRange.first.format(datePattern)
        }
        return "${dateRange.first.format(datePattern)} - ${dateRange.second.format(datePattern)}"
    }
}

private fun Pair<LocalDate, LocalDate>.daysBetween(): Long {
    return ChronoUnit.DAYS.between(this.first, this.second)
}

enum class Stages(val stageName: String) {
    TRIPOLEE("Tripolee"),
    RANCH_ARENA("Ranch Arena"),
    CAROUSEL_CLUB("Carousel Club"),
    SHERWOOD_COURT("Sherwood Court"),
    THE_OBSERVATORY("The Observatory")
}

val TestFestival2024 = Festival(
    "Test Festival 2024",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSnzosfIQt1egqQHIeJrZG-Fzh_XXljYiMPw&s",
    "https://media.web.aegpresents.com/content/content_images/467/M2gRyUqTueOcpYlAdNf5FYMUlyvV4hn7I2yeryG2.jpg",
    Pair(LocalDate.of(2024, 9, 20), LocalDate.of(2024, 9, 20)),
    listOf(
        Performance(John_Summit, LocalDate.of(2024, 9, 20), null, null)
    )
)

val ElectricForest2024 = Festival(
    "Electric Forest 2024",
    "https://media.web.aegpresents.com/content/electric-forest-2023/logo-electric-forest-main.png",
    "https://media.web.aegpresents.com/content/content_images/467/M2gRyUqTueOcpYlAdNf5FYMUlyvV4hn7I2yeryG2.jpg",
    Pair(LocalDate.of(2024, 6,20), LocalDate.of(2024, 6, 23)),
    listOf(
        Performance(String_Cheese_Incident, LocalDate.of(2024, 6, 21), null, null, Stages.RANCH_ARENA.stageName),
        Performance(String_Cheese_Incident, LocalDate.of(2024, 6, 22), null, null, Stages.RANCH_ARENA.stageName),
        Performance(Everything_Always, LocalDate.of(2024, 6, 20), null, null),
        Performance(Nelly_Furtado, LocalDate.of(2024, 6, 20), null, null),
        Performance(The_Disco_Biscuits, LocalDate.of(2024, 6, 20), null, null),
        Performance(Ben_Bohmer, LocalDate.of(2024, 6, 20), null, null),
        Performance(Knock2, LocalDate.of(2024, 6, 20), null, null),
        Performance(Pretty_Lights, LocalDate.of(2024, 6, 21), null, null),
        Performance(Seven_Lions, LocalDate.of(2024, 6, 21), null, null),
        Performance(Ludacris, LocalDate.of(2024, 6, 21), null, null),
        Performance(Black_Tiger_Sex_Machine, LocalDate.of(2024, 6, 21), null, null, Stages.TRIPOLEE.stageName),
        Performance(Subtronics, LocalDate.of(2024, 6, 22), null, null),
        Performance(John_Summit, LocalDate.of(2024, 6, 22), null, null),
        Performance(Lszee, LocalDate.of(2024, 6, 22), null, null),
        Performance(Excision, LocalDate.of(2024, 6, 23), null, null),
        Performance(Charlotte_De_Witte, LocalDate.of(2024, 6, 23), null, null),
        Performance(Gigantic_Nghtmre, LocalDate.of(2024, 6, 23), null, null),
        Performance(Umphreys_Mcgee, LocalDate.of(2024, 6, 23), null, null),
        Performance(Barclay_Crenshaw, null, null, null, null, 1),
        Performance(Alleycvt, LocalDate.of(2024, 6, 21), null, null, Stages.TRIPOLEE.stageName, 2),
        Performance(Atliens, LocalDate.of(2024, 6, 21), null, null, Stages.TRIPOLEE.stageName, 1),
        Performance(Boogie_T, LocalDate.of(2024, 6, 21), null, null, Stages.TRIPOLEE.stageName, 1),
        Performance(Canabliss, LocalDate.of(2024, 6, 21), null, null, Stages.TRIPOLEE.stageName, 2),
        Performance(Caspa, LocalDate.of(2024, 6, 21), null, null, Stages.TRIPOLEE.stageName, 2),
        Performance(Dimension, LocalDate.of(2024, 6, 21), null, null, Stages.TRIPOLEE.stageName, 1),
        Performance(Ivy_Lab, LocalDate.of(2024, 6, 21), null, null, Stages.TRIPOLEE.stageName, 2),
        Performance(Level_Up, LocalDate.of(2024, 6, 21), null, null, Stages.TRIPOLEE.stageName, 2),
        Performance(Wooli, LocalDate.of(2024, 6, 21), null, null, Stages.TRIPOLEE.stageName, 1),
        Performance(LP_Giobbi, LocalDate.of(2024, 6, 23), null, null, Stages.CAROUSEL_CLUB.stageName, 1),
        Performance(Slayyyter, LocalDate.of(2024, 6, 23), null, null, Stages.CAROUSEL_CLUB.stageName, 2),
        Performance(Shaun_Ross, LocalDate.of(2024, 6, 23), null, null, Stages.CAROUSEL_CLUB.stageName, 2),
        Performance(Only_Fire, LocalDate.of(2024, 6, 23), null, null, Stages.CAROUSEL_CLUB.stageName, 2),
        Performance(Acraze, headlineTier = 1),
        Performance(Cannons, headlineTier = 1),
        Performance(Cassian, headlineTier = 1),
        Performance(Chase_and_Status, headlineTier = 1),
        Performance(Cuco, headlineTier = 1),
        Performance(Drama, headlineTier = 1),
        Performance(G_Jones, headlineTier = 1),
        Performance(Green_Velvet, headlineTier = 1),
        Performance(Hiatus_Kaiyote, headlineTier = 1),
        Performance(Kenny_Beats, headlineTier = 1),
        Performance(Lettuce, headlineTier = 1),
        Performance(Libianca, headlineTier = 1),
        Performance(Matroda, headlineTier = 1),
        Performance(Mau_P, headlineTier = 1),
        Performance(Neil_Frances, headlineTier = 1),
        Performance(Rawayana, headlineTier = 1),
        Performance(Sammy_Virji, headlineTier = 1),
        Performance(Sara_Landry, headlineTier = 1),
        Performance(Vini_Vici, LocalDate.of(2024, 6, 21), stage = Stages.THE_OBSERVATORY.stageName, headlineTier = 1),
        Performance(Whyte_Fang, headlineTier = 1),
        Performance(AK_Sports, headlineTier = 2),
        Performance(Ayybo, headlineTier = 2),
        Performance(Baggi, LocalDate.of(2024, 6, 21), stage = Stages.THE_OBSERVATORY.stageName, headlineTier = 2),
        Performance(Blastoyz, LocalDate.of(2024, 6, 21), stage = Stages.THE_OBSERVATORY.stageName, headlineTier = 2),
        Performance(Boogie_Trio, headlineTier = 2),
        Performance(Brandi_Cyrus, headlineTier = 2),
        Performance(Calussa, headlineTier = 2),
        Performance(Chaos_in_the_CBD, headlineTier = 2),
        Performance(Coco_and_Breezy, headlineTier = 2),
        Performance(Dirtwire, headlineTier = 2),
        Performance(Dixons_Violin, headlineTier = 2),
        Performance(DJ_Brownie, headlineTier = 2),
        Performance(DJ_Susan, headlineTier = 2),
        Performance(Dumstaphunk, headlineTier = 2),
        Performance(Eggy, headlineTier = 2),
        Performance(Emo_Nite, headlineTier = 2),
        Performance(Equanimous, headlineTier = 2),
        Performance(Goodboys, headlineTier = 2),
        Performance(Hamdi, headlineTier = 2),
        Performance(Harry, headlineTier = 2),
        Performance(Inzo, headlineTier = 2),
        Performance(Its_Murph, headlineTier = 2),
        Performance(Jason_Leech, headlineTier = 2),
        Performance(Jenna_Shaw, headlineTier = 2),
        Performance(Jjuujjuu, headlineTier = 2),
        Performance(Juelz, headlineTier = 2),
        Performance(Kallaghan, headlineTier = 2),
        Performance(Kiltro, headlineTier = 2),
        Performance(Layton_Giordanni, LocalDate.of(2024, 6, 21), stage = Stages.THE_OBSERVATORY.stageName, headlineTier = 2),
        Performance(Le_Youth, headlineTier = 2),
        Performance(League_of_Sound_Disciples, headlineTier = 2),
        Performance(Levity, headlineTier = 2),
        Performance(Little_Stranger, headlineTier = 2),
        Performance(Luci, headlineTier = 2),
        Performance(Lyny, headlineTier = 2),
        Performance(Maddy_O_Neal, headlineTier = 2),
        Performance(Masonic, headlineTier = 2),
        Performance(Major_League_Diz, headlineTier = 2),
        Performance(Marsh, headlineTier = 2),
        Performance(Mascolo, headlineTier = 2),
        Performance(Michael_Brun, headlineTier = 2),
        Performance(Mojave_Grey, headlineTier = 2),
        Performance(Moontricks, headlineTier = 2),
        Performance(Neoma, headlineTier = 2),
        Performance(Odd_Mob, headlineTier = 2),
        Performance(Omnom, headlineTier = 2),
        Performance(Oden_and_Fatzo, headlineTier = 2),
        Performance(Paperwater, headlineTier = 2),
        Performance(Peach_Tree_Rascals, headlineTier = 2),
        Performance(Politik, headlineTier = 2),
        Performance(Polyrhythmics, headlineTier = 2),
        Performance(Pretty_Pink, LocalDate.of(2024, 6, 21), stage = Stages.THE_OBSERVATORY.stageName, headlineTier = 2),
        Performance(Proxima_Parada, headlineTier = 2),
        Performance(Ranger_Trucco, headlineTier = 2),
        Performance(Rayben, headlineTier = 2),
        Performance(Redrum, LocalDate.of(2024, 6, 20), stage = Stages.THE_OBSERVATORY.stageName, headlineTier = 2),
        Performance(Shae_District, headlineTier = 2),
        Performance(Sultan, headlineTier = 2),
        Performance(Shepard, headlineTier = 2),
        Performance(Super_Future, LocalDate.of(2024, 6, 20), stage = Stages.THE_OBSERVATORY.stageName, headlineTier = 2),
        Performance(Swaylo, LocalDate.of(2024, 6, 21), stage = Stages.THE_OBSERVATORY.stageName, headlineTier = 2),
        Performance(Thought_Process, headlineTier = 2),
        Performance(Tripp_St, LocalDate.of(2024, 6, 20), stage = Stages.THE_OBSERVATORY.stageName, headlineTier = 2),
        Performance(Tsha, headlineTier = 2),
        Performance(Unusual_Demont, headlineTier = 2),
        Performance(Venbee, headlineTier = 2),
        Performance(VNSSA_B2B_Nala, headlineTier = 2),
        Performance(Westend, headlineTier = 2),
        Performance(Will_Clarke, headlineTier = 2),
        Performance(Zen_Selekta, LocalDate.of(2024, 6, 20), stage = Stages.THE_OBSERVATORY.stageName, headlineTier = 2),
    )
)