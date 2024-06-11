package com.fest.cal.activity.festlvalselection

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.fest.cal.R
import com.fest.cal.activity.compose.ActivityHeader
import com.fest.cal.activity.compose.SpotifyView
import com.fest.cal.activity.festlvalview.FestivalViewActivity
import com.fest.cal.data.Festival
import java.time.format.DateTimeFormatter

class FestivalSelectionActivity : FragmentActivity() {
    private val fragmentContainerId = View.generateViewId()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelProvider = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
        val viewModel = viewModelProvider.get(FestivalSelectionViewModel::class.java)

        setContent {
            FestivalSelectionContent(viewModel, fragmentContainerId, this)
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
fun FestivalSelectionContent(viewModel: FestivalSelectionViewModel, containerId: Int, activity: FestivalSelectionActivity) {
    val festivals = viewModel.festivals.collectAsState().value
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ActivityHeader(
            text = activity.resources.getString(R.string.select_a_festival),
            resources = activity.resources
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        ) {
            items(festivals) { item ->
                FestivalSelection(item, activity)
            }
        }
    }
    SpotifyView(containerId)
}

@Composable
fun FestivalSelection(festival: Festival, activity: Activity) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(6.dp))
            .clickable {
                openFestivalView(festival, activity)
            }
    ) {
        Column {
            AsyncImage(
                model = festival.promotionImageUrl,
                contentDescription = festival.name,
                modifier = Modifier
                    .height(96.dp)
                    .fillMaxWidth()
            )
            Text(
                modifier = Modifier.padding(8.dp, 0.dp),
                text = festival.name,
                fontSize = activity.resources.getDimension(R.dimen.subtext_font_size).sp
            )
            val datePattern = DateTimeFormatter.ofPattern("MMMM d")
            Text(
                modifier = Modifier.padding(8.dp, 0.dp),
                text = festival.getDateRangeString(),
                fontSize = activity.resources.getDimension(R.dimen.subtext_font_size).sp
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

    }
}

fun openFestivalView(festival: Festival, activity: Activity) {
    val intent = Intent(activity, FestivalViewActivity::class.java)
    intent.putExtra("data", festival)
    activity.startActivity(intent)
}
