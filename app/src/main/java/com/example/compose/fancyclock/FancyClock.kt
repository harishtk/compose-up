package com.example.compose.fancyclock

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.HalfSizeShape
import com.example.compose.R
import com.example.compose.WithLifecycle
import com.example.compose.designcomponents.angleGradientBackground
import com.example.compose.ui.theme.GradientColors
import com.example.compose.ui.theme.MaterialColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun FancyClockRoute() {
    val context = LocalContext.current
    val tickSound = remember { MediaPlayer.create(context, R.raw.fx_tick) }
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialColor.BlueGrey900),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FancyClock(
            Modifier.fillMaxWidth(0.8f)
            .aspectRatio(1f), onTick = { tickSound.start() })
    }
}

@Composable
fun FancyClock(
    modifier: Modifier = Modifier,
    onTick: suspend CoroutineScope.() -> Unit
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center,
    ) {
        AnimatedGradient(
            gradientColors = GradientColors(
                top = MaterialColor.BlueGrey900,
                bottom = MaterialColor.BlueGrey800,
            )
        )

        TextClock(
            modifier = Modifier.align(Alignment.Center),
            onTick = onTick
        )
    }
}

@Composable
fun AnimatedGradient(
    modifier: Modifier = Modifier,
    gradientColors: GradientColors,
) {
    var startAnimation by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (startAnimation) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3_000, easing = LinearEasing),
        ),
        label = "Gradient Rotation State"
    )
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .angleGradientBackground(
                listOf(
                    gradientColors.top,
                    gradientColors.bottom,
                ), rotationState
            ),
    )
    LaunchedEffect(key1 = Unit) {
        startAnimation = true
    }
}

@Composable
fun TextClock(
    modifier: Modifier,
    onTick: suspend CoroutineScope.() -> Unit,
) {
    var seconds by remember {
        mutableLongStateOf(0L)
    }

    WithLifecycle {
        while (true) {
            delay(1000)
            seconds += 1L
        }
    }

    val date = remember(seconds) {
        val simpleDateFmt = SimpleDateFormat.getTimeInstance()
        simpleDateFmt.format(Date())
    }
    AnimatedText(
        text = date,
        style = MaterialTheme.typography.displayMedium.copy(
            fontWeight = FontWeight.W600,
            letterSpacing = 2.sp
        ),
        color = Color.White,
        modifier = modifier
    )
    LaunchedEffect(key1 = date, block = onTick)
}

@Composable
fun AnimatedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = Color.White,
    enableMirror: Boolean = false,
) {
    var oldText by remember { mutableStateOf(text) }
    SideEffect {
        oldText = text
    }

    var textHeight by remember { mutableIntStateOf(0) }
    Column(
        verticalArrangement = Arrangement.Center,
    ) {
        Row(modifier) {
            for (i in text.indices) {
                val oldChar = oldText.getOrNull(i)
                val newChar = text[i]
                val char = if (oldChar == newChar) {
                    oldText[i]
                } else {
                    text[i]
                }
                AnimatedContent(
                    targetState = char,
                    transitionSpec = {
                        slideInVertically { it } togetherWith slideOutVertically { -it }
                    },
                    label = "Character Animation $char"
                ) { targetChar ->
                    Text(
                        text = targetChar.toString(),
                        style = style,
                        softWrap = false,
                        color = color,
                        modifier = Modifier
                            .onSizeChanged {
                                textHeight = it.height
                            }
                    )
                }
            }
        }
        if (enableMirror) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier
                .graphicsLayer {
                    translationY = -(textHeight / 2).toFloat()
                    rotationX = 180f
                    rotationY = -10f
                }
            ) {
                for (i in text.indices) {
                    val oldChar = oldText.getOrNull(i)
                    val newChar = text[i]
                    val char = if (oldChar == newChar) {
                        oldText[i]
                    } else {
                        text[i]
                    }

                    AnimatedContent(
                        targetState = char,
                        transitionSpec = {
                            slideInVertically { it / 4 } togetherWith slideOutVertically { -it / 4 }
                        },
                        label = "Character Animation $char"
                    ) { targetChar ->
                        Text(
                            text = targetChar.toString(),
                            style = style.copy(
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color.White.copy(alpha = 0.2f),
//                                    Color.White.copy(alpha = 0.75f),
                                        Color.White,
                                    ),
                                    start = Offset.Zero,
                                    end = Offset(0f, Float.POSITIVE_INFINITY),
                                )
                            ),
                            softWrap = false,
                            modifier = Modifier
                                .graphicsLayer {

                                },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Mirror(content: @Composable () -> Unit) {
    Column {
        content()
        Box(modifier = Modifier
            .graphicsLayer {
                alpha = 0.99f
                rotationX = 180f
            }
            .drawWithContent {
                val colors = listOf(Color.Transparent, Color.White)
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(colors),
                    blendMode = BlendMode.DstIn
                )
            }
            .blur(radiusX = 1.dp, radiusY = 3.dp, BlurredEdgeTreatment.Unbounded)
            .clip(
                HalfSizeShape
            )
        ) {
            content()
        }
    }
}

@Preview(widthDp = 200, heightDp = 200, apiLevel = 33)
@Composable
private fun AnimatedGradientPreview() {
    Box {
        AnimatedGradient(
            gradientColors = GradientColors(
                top = MaterialColor.BlueGrey900,
                bottom = MaterialColor.BlueGrey800,
            )
        )
    }
}