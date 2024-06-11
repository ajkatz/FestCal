package com.fest.cal.activity.festlvalview

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.fest.cal.FestCalApplication
import com.fest.cal.activity.compose.ActivityHeader
import com.fest.cal.activity.compose.ArtistList
import com.fest.cal.activity.compose.SelectedArtistDialog
import com.fest.cal.activity.compose.SpotifyView
import com.fest.cal.activity.festlvalselection.FestivalSelectionViewModel
import com.fest.cal.data.Festival
import com.fest.cal.data.Performance
import com.fest.cal.data.generateStageArtistMap

class FestivalViewActivity: FragmentActivity() {
    val fragmentContainerId = View.generateViewId()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as FestCalApplication
        val festivalData = intent.getSerializableExtra("data") as Festival
        val vmp = FestivalViewViewModelFactory(festivalData, app.artistRatingRepository)
        val vm = vmp.create(FestivalViewViewModel::class.java)

        setContent {
            FestivalViewActivityContent(vm, fragmentContainerId, this)
        }

        if (savedInstanceState == null) {
            window.decorView.post {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                }
            }
        }
    }
}

@Composable
fun FestivalViewActivityContent(viewModel: FestivalViewViewModel, containerId: Int, activity: FestivalViewActivity) {
    val festival = viewModel.festival.collectAsState().value
    val selectedArtist = viewModel.selectedArtist.collectAsState().value
    val userRatings = viewModel.userRatings.collectAsState().value
    val relevantPerformances = viewModel.relevantPerformances.collectAsState().value
    
    if (selectedArtist != null) {
        SelectedArtistDialog(viewModel, selectedArtist, userRatings, relevantPerformances)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(bottom = 60.dp)
    ) {
        ActivityHeader(
            text = festival.name,
            resources = activity.resources
        )
        TabView(viewModel, festival, userRatings)
    }

    SpotifyView(containerId)
}

@Composable
fun TabView(
    viewModel: FestivalViewViewModel,
    festival: Festival,
    userRatings: Map<String, Int>,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    TabRow(
        selectedTabIndex = selectedTabIndex,
    ) {
        festival.getSelectionTabs().forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                text = { Text(text = title) }
            )
        }
    }
    val performances = festival.artistList
    val relevantPerformances: List<Performance>
    if (selectedTabIndex == 0) {
        relevantPerformances = performances
    } else {
        val date = festival.getDayList()[selectedTabIndex - 1]
        relevantPerformances = performances.filter { it.performanceDate == date }
    }
    viewModel.setRelevantPerformances(relevantPerformances.sortedBy { it.artist.name }.sortedBy { it.headlineTier })
    ArtistList(viewModel, userRatings, relevantPerformances.generateStageArtistMap())
}




