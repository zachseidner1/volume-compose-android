package com.cornellappdev.volume.ui.components.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.volume.R
import com.cornellappdev.volume.ui.components.general.CreateHorizontalPublicationRow
import com.cornellappdev.volume.ui.theme.GrayOne
import com.cornellappdev.volume.ui.theme.VolumeOrange
import com.cornellappdev.volume.ui.theme.lato
import com.cornellappdev.volume.ui.theme.notoserif
import com.cornellappdev.volume.ui.viewmodels.OnboardingViewModel
import kotlinx.coroutines.delay

@Composable
fun SecondPage(
    onboardingViewModel: OnboardingViewModel,
    creatingUser: MutableState<Boolean>,
    onProceedClicked: () -> Unit
) {
    val allPublicationState = onboardingViewModel.allPublicationsState.collectAsState().value
    val creatingUserState = onboardingViewModel.creatingUserState.collectAsState().value
    val lazyListState = rememberLazyListState()
    val buttonClicked = rememberSaveable { mutableStateOf(false) }
    val proceedEnabled = rememberSaveable { mutableStateOf(false) }

    // Responsible for fading out the second page
    if (buttonClicked.value) {
        LaunchedEffect(key1 = Unit, block = {
            creatingUser.value = false
            delay(2500L)
            onboardingViewModel.createUser()
        })
    }

    AnimatedVisibility(
        visible = creatingUser.value,
        enter = fadeIn(
            initialAlpha = 0f,
            animationSpec = tween(durationMillis = 2500)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 2500)
        )
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.volume_title),
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "Follow student publications that you",
                textAlign = TextAlign.Center,
                fontFamily = notoserif,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier.padding(bottom = 30.dp),
                text = "are interested in",
                textAlign = TextAlign.Center,
                fontFamily = notoserif,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )

            BoxWithConstraints {
                Canvas(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(bottom = 24.dp)
                        .width(maxWidth / 2),
                ) {
                    drawLine(
                        color = Color(0xFFEEEEEE),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 2f
                    )
                }
            }

            when (allPublicationState.publicationsRetrievalState) {
                OnboardingViewModel.PublicationsRetrievalState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = VolumeOrange)
                    }
                }
                OnboardingViewModel.PublicationsRetrievalState.Error -> {
                    // TODO Prompt to try again, fetchAllPublications manually (it's public)
                }
                is OnboardingViewModel.PublicationsRetrievalState.Success -> {

                    BoxWithConstraints {
                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .heightIn(max = maxHeight - 200.dp),
                            state = lazyListState,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            items(
                                items = allPublicationState.publicationsRetrievalState.publications,
                                key = { publication ->
                                    publication.id
                                }) { publication ->
                                // Clicking on row IN onboarding should not lead to IndividualPublicationScreen. They are not
                                // an official user yet so they shouldn't be interacting with the articles.
                                CreateHorizontalPublicationRow(publication = publication) { publicationFromCallback, isFollowing ->
                                    if (isFollowing) {
                                        onboardingViewModel.listOfPubsFollowed.add(
                                            publicationFromCallback.id
                                        )
                                    } else {
                                        onboardingViewModel.listOfPubsFollowed.remove(
                                            publicationFromCallback.id
                                        )
                                    }

                                    proceedEnabled.value =
                                        !onboardingViewModel.listOfPubsFollowed.isEmpty()
                                }
                            }
                        }

                        // Gradient overlay to the bottom of the Search results LazyColumn
                        androidx.compose.animation.AnimatedVisibility(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .height(100.dp)
                                .fillMaxWidth(),
                            enter = fadeIn(),
                            exit = fadeOut(),
                            // The gradient overlay is only visible when the user hasn't scrolled to the end
                            // so the gradient isn't blocking the final publication
                            visible = !lazyListState.isScrolledToTheEnd()
                        ) {
                            Spacer(
                                Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.White
                                            )
                                        )
                                    )
                            )
                        }
                    }
                }
            }

            val proceedColor = if (proceedEnabled.value) VolumeOrange else GrayOne
            OutlinedButton(
                modifier = Modifier.padding(top = 40.dp),
                enabled = proceedEnabled.value,
                onClick = {
                    buttonClicked.value = true
                },
                border = if (proceedEnabled.value) BorderStroke(
                    2.dp,
                    VolumeOrange
                ) else BorderStroke(2.dp, GrayOne),
                contentPadding = PaddingValues(horizontal = 32.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = proceedColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Start reading", color = proceedColor,
                    fontFamily = lato,
                    letterSpacing = (-1).sp,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }

    when (creatingUserState.createUserState) {
        OnboardingViewModel.CreateUserState.Error -> {
            buttonClicked.value = false
            creatingUser.value = true
        }
        OnboardingViewModel.CreateUserState.Pending -> {
            // Wait to be finished
        }
        is OnboardingViewModel.CreateUserState.Success -> {
            onboardingViewModel.updateOnboardingCompleted()
            onProceedClicked.invoke()
        }
    }
}

/**
 * Detects when the given LazyList has scrolled to the end.
 */
fun LazyListState.isScrolledToTheEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
