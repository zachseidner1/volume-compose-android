package com.cornellappdev.android.volume.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import com.cornellappdev.android.volume.R
import com.cornellappdev.android.volume.analytics.NavigationSource
import com.cornellappdev.android.volume.data.models.Article
import com.cornellappdev.android.volume.ui.components.general.CreateArticleRow
import com.cornellappdev.android.volume.ui.states.ArticlesRetrievalState
import com.cornellappdev.android.volume.ui.theme.VolumeOffWhite
import com.cornellappdev.android.volume.ui.theme.VolumeOrange
import com.cornellappdev.android.volume.ui.theme.lato
import com.cornellappdev.android.volume.ui.theme.notoserif
import com.cornellappdev.android.volume.ui.viewmodels.BookmarkViewModel
import com.cornellappdev.android.volume.util.BookmarkStatus
import com.cornellappdev.android.volume.util.FinalBookmarkStatus

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookmarkScreen(
    bookmarkViewModel: BookmarkViewModel = hiltViewModel(),
    savedStateHandle: SavedStateHandle,
    onArticleClick: (Article, NavigationSource) -> Unit,
    onSettingsClick: () -> Unit
) {
    val bookmarkUiState = bookmarkViewModel.bookmarkUiState

    // The ArticleWebViewScreen sends a BookmarkStatus to the BookmarkScreen if the bookmark state
    // of any of the articles change. Since you can open articles from the bookmark screen and change
    // the bookmark state from there, it's important that the changes are reflected)
    val bookmarkStatus = savedStateHandle.getLiveData<BookmarkStatus>("bookmarkStatus")
        .observeAsState()

    if (bookmarkStatus.value?.status == FinalBookmarkStatus.BOOKMARKED_TO_UNBOOKMARKED) {
        bookmarkStatus.value?.articleId?.let { bookmarkViewModel.removeArticle(it) }
    }

    Scaffold(topBar = {
        Row(
            modifier = Modifier.padding(start = 12.dp, top = 20.dp),
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "Bookmarks",
                    fontFamily = notoserif,
                    fontWeight = FontWeight.Medium,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Left
                )
                Image(
                    painter = painterResource(R.drawable.ic_period),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 3.dp, bottom = 10.dp)
                        .scale(1.05F)
                )
            }
            Spacer(Modifier.weight(1f, true))
            IconButton(onClick = onSettingsClick) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = Color(0xFF838383)
                )
            }
        }
    }, content = { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val articleState = bookmarkUiState.articlesState) {
                ArticlesRetrievalState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = VolumeOrange)
                    }
                }
                ArticlesRetrievalState.Error -> {
                    // TODO
                }
                is ArticlesRetrievalState.Success -> {
                    Column {
                        Column(modifier = Modifier.padding(start = 12.dp, top = 15.dp)) {
                            Text(
                                text = "Saved Articles",
                                fontFamily = notoserif,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Image(
                                painter = painterResource(R.drawable.ic_underline_other_article),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .scale(1.05F)
                            )
                        }

                        if (articleState.articles.isEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_volume_bars_orange_large),
                                    contentDescription = null
                                )

                                Text(
                                    text = "Nothing to see here!",
                                    fontFamily = notoserif,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = "You have no saved articles.",
                                    fontFamily = lato,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .padding(start = 12.dp, end = 12.dp, top = 20.dp),
                            ) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(20.dp),
                                ) {
                                    items(articleState.articles) { article ->
                                        val dismissState = rememberDismissState()
                                        if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                                            bookmarkViewModel.removeArticle(article.id)
                                        }

                                        SwipeToDismiss(
                                            state = dismissState,
                                            directions = setOf(
                                                DismissDirection.EndToStart
                                            ),

                                            background = {
                                                val backgroundColor by animateColorAsState(
                                                    when (dismissState.targetValue) {
                                                        DismissValue.Default -> VolumeOffWhite
                                                        else -> VolumeOrange
                                                    }
                                                )

                                                val iconColor by animateColorAsState(
                                                    when (dismissState.targetValue) {
                                                        DismissValue.Default -> VolumeOrange
                                                        else -> VolumeOffWhite
                                                    }
                                                )

                                                val size by animateDpAsState(targetValue = if (dismissState.targetValue == DismissValue.Default) 24.dp else 48.dp)

                                                Box(
                                                    Modifier
                                                        .fillMaxSize()
                                                        .background(backgroundColor)
                                                        .padding(horizontal = Dp(20f)),
                                                    contentAlignment = Alignment.CenterEnd
                                                ) {
                                                    Icon(
                                                        Icons.Filled.Bookmark,
                                                        contentDescription = "Unbookmark",
                                                        modifier = Modifier.size(size),
                                                        tint = iconColor
                                                    )
                                                }
                                            },
                                            dismissContent = {
                                                CreateArticleRow(
                                                    article = article,
                                                    isABookmarkedArticle = true
                                                ) {
                                                    onArticleClick(
                                                        article,
                                                        NavigationSource.BOOKMARK_ARTICLES
                                                    )
                                                }
                                            })
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    })
}
