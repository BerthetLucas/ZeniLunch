package com.zenika.zenilunch.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zenika.zenilunch.R
import com.zenika.zenilunch.RestaurantUIModel
import com.zenika.zenilunch.ui.theme.PreviewZeniLunchTheme

@Composable
fun DetailScreen(
    popBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val restaurant by viewModel.restaurant.collectAsState()
    Restaurant(
        restaurant,
        popBack,
        viewModel::hideRestaurant,
        Modifier
            .fillMaxSize()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Restaurant(
    restaurant: RestaurantUIModel,
    popBack: () -> Unit,
    hideRestaurant: () -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    val option = when {
        restaurant.vegan -> stringResource(R.string.option, stringResource(R.string.vegan))
        restaurant.vegetarian -> stringResource(
            R.string.option,
            stringResource(R.string.vegetarian)
        )

        else -> stringResource(R.string.noOption)
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        }
    ) { innerPadding ->
        Column(
            modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = restaurant.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = restaurant.type,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = restaurant.price,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = option,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Button(onClick = { context.openGoogleMaps(restaurant) }) {
                Text(
                    text = stringResource(R.string.map),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Button(
                onClick = {
                    hideRestaurant()
                    popBack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = stringResource(R.string.hide),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

fun Context.openGoogleMaps(restaurant: RestaurantUIModel) {
    val officeLatitude = 45.766752337134754
    val officeLongitude = 4.858952442403011
    val latitude = restaurant.latitude
    val longitude = restaurant.longitude
    val url =
        "http://maps.google.com/maps?saddr=$officeLatitude,$officeLongitude&daddr=$latitude,$longitude"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.setPackage("com.google.android.apps.maps")

    startActivity(intent)
}

@Preview
@Composable
fun FullVeganPreview() {
    PreviewZeniLunchTheme {
        Restaurant(
            RestaurantUIModel(
                "Chez Audrey",
                "Dev Android",
                "€€€",
                vegetarian = true,
                vegan = true,
                latitude = 1.0,
                longitude = 2.0
            ),
            popBack = { Log.d("preview", "Restaurant caché") },
            hiltViewModel(),
            Modifier
        )
    }
}

@Preview
@Composable
fun FullMeatPreview() {
    PreviewZeniLunchTheme {
        Restaurant(
            RestaurantUIModel(
                "Chez Antho",
                "Couteau Suisse",
                "€€€€€",
                vegetarian = false,
                vegan = false,
                latitude = 1.0,
                longitude = 2.0
            ),
            popBack = { Log.d("preview", "Restaurant caché") },
            hiltViewModel(),
            Modifier
        )
    }
}