package com.example.newsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.newsapp.R
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = getViewModel()

    val flowColor by viewModel.color.collectAsState(initial = 0xFFFFF)

    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val isLoading2 by viewModel.isLoading2.observeAsState(initial = false)

    val headState by viewModel.headNews.observeAsState(initial = emptyList())
    val dataState by viewModel.regularNews.observeAsState(initial = emptyList())

    LaunchedEffect(key1 = Unit) {
        viewModel.getHeadline()
        viewModel.doNetworkCall()
    }

    Column (
       modifier = Modifier
           .padding(horizontal = 14.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 14.dp)
        ) {
            MandiriImage()
            Text(
                text = "Mandiri News",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF474E70),
                modifier = Modifier.padding(horizontal = 14.dp)
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        ) {
            Column (
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Berita Terkini",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF474E70),
                )
                if (isLoading2) {
                    Column (
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 4.dp,
                            modifier = Modifier
                                .size(52.dp)
                                .padding(8.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                } else if (headState.isNotEmpty()) {
                    Column {
                        FeedsImage(
                            headState.getOrNull(0)?.urlToImage ?: "Unknown Url",
                            headState.getOrNull(0)?.title ?: "Unknown Title",
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .clip(shape = RoundedCornerShape(20.dp)),
                            ContentScale.FillWidth
                        )
                        Text(
                            text = headState.getOrNull(0)?.title ?: "Unknown Title",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF474E70),
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = headState.getOrNull(0)?.author ?: "Unknown Author",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF474E70)
                            )
                            Text(
                                text = headState.getOrNull(0)?.publishedAt ?: "Unknown Published",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF474E70)
                            )
                        }
                    }
                }
            }

        }
        Row {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp)
            ) {
                Text(
                    text = "Semua Berita",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF474E70),
                )
                if (dataState.isNotEmpty()) {
                    InfiniteScrollList(
                        itemCount = dataState.size,
                        isLoading = isLoading,
                        loadMoreItems = {
                            viewModel.doNetworkCall()
                        }
                    ) {index ->
                        dataState.getOrNull(index)?.let {
                            newsItem ->
                            Row (
                                Modifier.padding(vertical = 10.dp)
                            ) {
                                FeedsImage(
                                    newsItem.urlToImage,
                                    newsItem.title,
                                    Modifier
                                        .size(150.dp)
                                        .clip(shape = RoundedCornerShape(20.dp)),
                                    ContentScale.Crop
                                )
                                Column (
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .fillMaxHeight()
                                ) {
                                    Text(
                                        text = newsItem.title,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF474E70),
                                    )
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        newsItem.author?.let {
                                            Text(
                                                text = it,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF474E70)
                                            )
                                        }
                                        Text(
                                            text = newsItem.publishedAt,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF474E70)
                                        )
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun MandiriImage() {
    val imagePainter = painterResource(id = R.drawable.mandiri_logo) // Replace "my_image" with the name of your image file
    Image(
        painter = imagePainter,
        contentDescription = "Mandiri Image",
        modifier = Modifier
            .size(80.dp) // Set the size of the image
            .clip(shape = CircleShape) // Clip the image into a circular shape
    )
}

@Composable
fun FeedsImage(urlToImg: String?, title: String, modifier: Modifier, contentScale: ContentScale) {
    urlToImg?.let { imageUrl ->
        Log.v("imageUrl", imageUrl)
        val painter: Painter = rememberImagePainter(
            data = imageUrl,
        )
        Image(
            painter = painter,
            contentDescription = title,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}

@Composable
fun InfiniteScrollList(
    itemCount: Int,
    loadMoreItems: () -> Unit,
    isLoading: Boolean,
    content: @Composable (Int) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(state = listState) {
        items(itemCount) { index ->
            content(index)
            if (index == itemCount - 1) {
                loadMoreItems()
            }
        }
    }

    if (isLoading) {
        Column (
            modifier = Modifier.fillMaxWidth()
        ) {
            CircularProgressIndicator(
                strokeWidth = 4.dp,
                modifier = Modifier
                    .size(52.dp)
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}