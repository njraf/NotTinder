package com.example.nottinder

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp


@Composable
fun Modifier.roundedBorder(): Modifier = this.border(
    width = 2.dp,
    color = Color.Black,
    shape = RoundedCornerShape(dimensionResource(R.dimen.image_radius))
)
