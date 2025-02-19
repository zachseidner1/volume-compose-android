package com.cornellappdev.android.volume.ui.components.general

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.android.volume.data.models.Magazine
import com.cornellappdev.android.volume.ui.theme.GrayOne
import com.cornellappdev.android.volume.ui.theme.lato
import com.cornellappdev.android.volume.ui.theme.notoserif
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState


private const val TAG = "MagazineComponents"
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun CreateMagazineColumn (
    magazine: Magazine,
) {
    val pdfState = rememberVerticalPdfReaderState(
        resource = ResourceType.Remote(magazine.pdfURL),
        isZoomEnable = false
    )

    Column (
        Modifier
            .padding(10.dp)
            .wrapContentHeight()
            .clickable {
                Log.d(TAG, "CreateArticleColumn: ${magazine.title} Clicked!")
                // TODO implement on click.
            }) {


        // Magazine image

        Surface (modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .shadow(8.dp)) {
            VerticalPDFReader(state = pdfState, modifier = Modifier.shimmerEffect().fillMaxSize())
        }
        // Magazine publisher text
        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 2.dp),
            text = magazine.publication.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontFamily = notoserif,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
        )
        // Magazine title text
        Text(
            modifier = Modifier.width(150.dp),
            text = magazine.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontFamily = lato,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        // Shoutouts and time since published text
        Text(
            text = "${magazine.semester.uppercase()} • ${magazine.shoutouts.toInt()} shout-outs",
            fontFamily = lato,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            color = GrayOne
        )
    }
}