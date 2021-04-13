package store.pengu.mobile.views.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import store.pengu.mobile.R

@Composable
fun LoginScreenLogo() {
    Column(modifier = Modifier.padding(top = 75.dp)) {
        Surface(
            modifier = Modifier
                .height(128.dp)
                .width(128.dp)
                .clip(CircleShape)
        ) {
            Image(
                painterResource(R.drawable.pengulogo),
                contentDescription = "PenguShop",
                contentScale = ContentScale.Crop,
            )
        }

        val logoFont = MaterialTheme.typography.h6
        Text(
            text = "PenguStore", fontSize = logoFont.fontSize,
            fontWeight = logoFont.fontWeight,
            modifier = Modifier
                .padding(top = 10.dp)
        )
    }
}
