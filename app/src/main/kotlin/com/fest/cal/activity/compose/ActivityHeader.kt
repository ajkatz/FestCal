package com.fest.cal.activity.compose

import android.content.res.Resources
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fest.cal.R

@Composable
fun ActivityHeader(text: String, resources: Resources) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        text = text,
        textAlign = TextAlign.Center,
        fontSize = resources.getDimension(R.dimen.header_font_size).sp
    )
}

@Composable
fun ActivityHeader(text: String, size: Int) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        text = text,
        textAlign = TextAlign.Center,
        fontSize = size.sp
    )
}