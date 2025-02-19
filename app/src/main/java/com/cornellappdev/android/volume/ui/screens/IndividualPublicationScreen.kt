package com.cornellappdev.android.volume.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.android.volume.analytics.NavigationSource
import com.cornellappdev.android.volume.data.models.Article
import com.cornellappdev.android.volume.ui.components.general.CreateArticleRow
import com.cornellappdev.android.volume.ui.components.general.CreateIndividualPublicationHeading
import com.cornellappdev.android.volume.ui.states.ArticlesRetrievalState
import com.cornellappdev.android.volume.ui.states.PublicationRetrievalState
import com.cornellappdev.android.volume.ui.theme.GrayThree
import com.cornellappdev.android.volume.ui.theme.VolumeOrange
import com.cornellappdev.android.volume.ui.viewmodels.IndividualPublicationViewModel

@Composable
fun IndividualPublicationScreen(
    individualPublicationViewModel: IndividualPublicationViewModel = hiltViewModel(),
    onArticleClick: (Article, NavigationSource) -> Unit
) {
    val publicationUiState = individualPublicationViewModel.publicationUiState

    LazyColumn {
        item {
            when (val publicationState = publicationUiState.publicationState) {
                PublicationRetrievalState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = VolumeOrange)
                    }
                }
                PublicationRetrievalState.Error -> {

                }
                is PublicationRetrievalState.Success -> {
                    CreateIndividualPublicationHeading(
                        publication = publicationState.publication,
                        publicationUiState.isFollowed
                    ) { isFollowing ->
                        if (isFollowing) {
                            individualPublicationViewModel.followPublication()
                        } else {
                            individualPublicationViewModel.unfollowPublication()
                        }
                    }
                }
            }
        }
        item {
            Row {
                Spacer(Modifier.weight(1f, true))
                Divider(Modifier.weight(1f, true), color = GrayThree, thickness = 2.dp)
                Spacer(Modifier.weight(1f, true))
            }
        }
        item {
            when (val articlesByPublicationState = publicationUiState.articlesByPublicationState) {
                ArticlesRetrievalState.Loading -> {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(start = 12.dp, top = 12.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = VolumeOrange)
                    }
                }
                ArticlesRetrievalState.Error -> {

                }
                is ArticlesRetrievalState.Success -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(top = 20.dp, start = 12.dp, end = 12.dp)
                    ) {
                        articlesByPublicationState.articles.forEach { article ->
                            CreateArticleRow(
                                article
                            ) {
                                onArticleClick(
                                    article,
                                    NavigationSource.OTHER_ARTICLES
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


