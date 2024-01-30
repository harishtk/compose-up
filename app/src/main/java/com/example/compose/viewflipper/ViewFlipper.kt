package com.example.compose.viewflipper

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.designsystem.component.ComposeUpBackground
import com.example.compose.ui.theme.ComposeUpTheme
import com.example.compose.viewflipper.FlipAnimationType.HORIZONTAL_ANTI_CLOCKWISE
import com.example.compose.viewflipper.FlipAnimationType.HORIZONTAL_CLOCKWISE
import com.example.compose.viewflipper.FlipAnimationType.VERTICAL_ANTI_CLOCKWISE
import com.example.compose.viewflipper.FlipAnimationType.VERTICAL_CLOCKWISE

@Composable
fun ViewFlipperRoute() {

}

@Composable
private fun ViewFlipper(
    modifier: Modifier = Modifier,
    flippableController: FlippableController = rememberFlippableController(),
    frontView: @Composable BoxScope.() -> Unit = {},
    backView: @Composable BoxScope.() -> Unit = {},
    flipAnimationType: FlipAnimationType = FlipAnimationType.VERTICAL_CLOCKWISE,
) {
    Box(modifier = modifier) {
        val isFlipped = flippableController.currentState == FlippableState.BACK
        FrontView(
            isFlipped = isFlipped,
            flipAnimationType = flipAnimationType,
            content = frontView
        )
        BackView(
            isFlipped = isFlipped,
            flipAnimationType = flipAnimationType,
            content = backView
        )
    }
}

@Composable
private fun FrontView(
    modifier: Modifier = Modifier,
    isFlipped: Boolean,
    flipAnimationType: FlipAnimationType = FlipAnimationType.VERTICAL_CLOCKWISE,
    content: @Composable BoxScope.() -> Unit
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (isFlipped) 1f else 0f,
        animationSpec = tween(
            FlipAnimationDurationMillis,
            delayMillis = if (isFlipped) 0 else FlipAnimationDurationMillis
        ),
        label = "Front View Animation State"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                rotationX = animationProgress * (flipAnimationType.rotate(isFlipped).rotationX)
                rotationY = animationProgress * (flipAnimationType.rotate(isFlipped).rotationY)
                cameraDistance = 8 * density
            },
        content = content
    )
}

@Composable
private fun BackView(
    modifier: Modifier = Modifier,
    isFlipped: Boolean,
    flipAnimationType: FlipAnimationType = FlipAnimationType.VERTICAL_CLOCKWISE,
    content: @Composable BoxScope.() -> Unit
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (isFlipped) 0f else 1f,
        animationSpec = tween(
            FlipAnimationDurationMillis,
            delayMillis = if (isFlipped) FlipAnimationDurationMillis else 0
        ),
        label = "Back View Animation State"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                rotationX = animationProgress * -(flipAnimationType.rotate(isFlipped).rotationX)
                rotationY = animationProgress * -(flipAnimationType.rotate(isFlipped).rotationY)
                cameraDistance = 8 * density
            },
        content = content
    )
}

@Preview(apiLevel = 33)
@Composable
private fun ViewFlipperPreview() {
    ComposeUpTheme {
        ComposeUpBackground {
            val flippableController = rememberFlippableController()

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                repeat(5) {
                    ListItem()
                }
            }
        }
    }
}

@Composable
private fun ListItem() {
    val flippableController = rememberFlippableController()
    ViewFlipper(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { flippableController.flip() },
        flippableController = flippableController,
        frontView = { SampleItemView(isFlipped = flippableController.isFlipped) },
        backView = { SampleDetailView(isFlipped = flippableController.isFlipped) },
        flipAnimationType = VERTICAL_ANTI_CLOCKWISE
    )
}

@Composable
private fun SampleItemView(
    modifier: Modifier = Modifier,
    isFlipped: Boolean,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
    ) {
        Text(
            text = "Front View",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun SampleDetailView(
    modifier: Modifier = Modifier,
    isFlipped: Boolean,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
    ) {
        Text(
            text = "Back View",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.padding(16.dp),
        )
        AnimatedVisibility(visible = isFlipped) {
            Column {
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                )

                Text(
                    text = "Description 2",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                )
            }
        }
    }
}

private const val FlipAnimationDurationMillis = 300

/**
 * An Enum class for animation type of [ViewFlipper]. It has these 4 states:
 * [HORIZONTAL_CLOCKWISE], [HORIZONTAL_ANTI_CLOCKWISE], [VERTICAL_CLOCKWISE], and [VERTICAL_ANTI_CLOCKWISE]
 */
enum class FlipAnimationType {
    /**
     * Rotates the [ViewFlipper] horizontally in the clockwise direction
     */
    HORIZONTAL_CLOCKWISE,

    /**
     * Rotates the [ViewFlipper] horizontally in the anti-clockwise direction
     */
    HORIZONTAL_ANTI_CLOCKWISE,

    /**
     * Rotates the [ViewFlipper] vertically in the clockwise direction
     */
    VERTICAL_CLOCKWISE,

    /**
     * Rotates the [ViewFlipper] vertically in the anti-clockwise direction
     */
    VERTICAL_ANTI_CLOCKWISE;

    val rotation: Float
        get() = when (this) {
            HORIZONTAL_CLOCKWISE,
            VERTICAL_CLOCKWISE -> 90f

            HORIZONTAL_ANTI_CLOCKWISE,
            VERTICAL_ANTI_CLOCKWISE -> -90f
        }

    val rotationX: Float
        get() = when (this) {
            HORIZONTAL_CLOCKWISE,
            HORIZONTAL_ANTI_CLOCKWISE -> 0f

            VERTICAL_CLOCKWISE -> 90f
            VERTICAL_ANTI_CLOCKWISE -> -90f
        }
    val rotationY: Float
        get() = when (this) {
            VERTICAL_CLOCKWISE,
            VERTICAL_ANTI_CLOCKWISE -> 0f

            HORIZONTAL_CLOCKWISE -> 90f
            HORIZONTAL_ANTI_CLOCKWISE -> -90f
        }
}

private fun FlipAnimationType.rotate(isFlipped: Boolean): FlipAnimationType {
    return when(this) {
        HORIZONTAL_CLOCKWISE -> if (isFlipped) HORIZONTAL_ANTI_CLOCKWISE else HORIZONTAL_CLOCKWISE
        VERTICAL_CLOCKWISE -> if (isFlipped) VERTICAL_ANTI_CLOCKWISE else VERTICAL_CLOCKWISE
        HORIZONTAL_ANTI_CLOCKWISE -> if (isFlipped) HORIZONTAL_CLOCKWISE else HORIZONTAL_ANTI_CLOCKWISE
        VERTICAL_ANTI_CLOCKWISE -> if (isFlipped) VERTICAL_CLOCKWISE else VERTICAL_ANTI_CLOCKWISE
    }
}

private val FlippableController.isFlipped
    get() =
        currentState == FlippableState.BACK


