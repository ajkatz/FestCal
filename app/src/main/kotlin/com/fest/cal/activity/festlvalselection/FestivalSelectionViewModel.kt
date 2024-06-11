package com.fest.cal.activity.festlvalselection

import androidx.lifecycle.ViewModel
import com.fest.cal.data.ElectricForest2024
import com.fest.cal.data.Festival
import com.fest.cal.data.TestFestival2024
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FestivalSelectionViewModel : ViewModel() {

    val _festivals = MutableStateFlow(listOf(ElectricForest2024, TestFestival2024))
    val festivals: StateFlow<List<Festival>> = _festivals

}