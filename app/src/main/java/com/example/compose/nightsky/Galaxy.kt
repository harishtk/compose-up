package com.example.compose.nightsky

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.sin
import kotlin.random.Random

private data class Star(
    var x: Float,
    var y: Float,
    var alpha: Float,
) {
    private val initialAlpha = alpha

    fun update(value: Float) {
        val x = (value - initialAlpha).toDouble()
        val newAlpha = 0.5f + (0.5f * sin(x).toFloat())
        alpha = newAlpha
    }
}

@Composable
fun Space(
    modifier: Modifier = Modifier
) {
    var startAnimation by remember { mutableStateOf(false) }

    val infiniteAnimatedProgress by animateFloatAsState(
        targetValue = if (startAnimation) 2f * Math.PI.toFloat() else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3_000),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "Infinite Progress State"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
    ) {
        val (width, height) = with(LocalDensity.current) {
            maxWidth.toPx() to maxHeight.toPx()
        }
        val stars = remember {
            buildList {
                repeat(1_000) {
                    val x = (Math.random() * width).toFloat()
                    val y = (Math.random() * height).toFloat()
                    val alpha = (Math.random() * 2.0 * Math.PI).toFloat()
                    add(Star(x, y, alpha))
                }
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()

                        onDrawBehind {
                            val colors = listOf(Color.Black, Color.Black.copy(0.55f))
                            drawRect(
                                brush = Brush.radialGradient(
                                    colors
                                ),
                                topLeft = Offset.Zero,
                                size = size,
                                style = Fill,
                                blendMode = BlendMode.Src
                            )

                            val colors2 = listOf(Color.Transparent, Color.Transparent, Color(0xff7df9ff).copy(0.05f))
                            drawCircle(
                                brush = Brush.linearGradient(
                                    colors2
                                ),
                                radius = width,
                                center = Offset(width / 2, height),
                                style = Fill,
                                blendMode = BlendMode.Difference
                            )
                        }
                    }
                },
        ) {
            stars.forEach { star ->
                star.update(infiniteAnimatedProgress)
                drawCircle(
                    color = Color.White,
                    center = Offset(star.x, star.y),
                    radius = 2f,
                    alpha = star.alpha,
                )
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        startAnimation = true
    }
}

@Preview
@Composable
private fun GalaxyPreview() {
    Box {
        Space()
    }
}

val randomColor
    get() = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))

fun IntRange.random() =
    Random.nextInt((endInclusive + 1) - start) + start

