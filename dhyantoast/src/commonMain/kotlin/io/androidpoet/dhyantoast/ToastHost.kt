/*
 * Designed and developed by 2024 androidpoet (Ranbir Singh)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.androidpoet.dhyantoast

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * A composable that displays a stack of toasts with animations and gestures.
 * Similar to SnackbarHost in Jetpack Compose.
 *
 * @param hostState The state object to be used to manage the toasts
 * @param modifier Modifier for the container
 * @param alignment Where toasts appear on screen
 * @param autoDismissEnabled Whether to automatically dismiss toasts
 * @param autoDismissDelay Duration before a toast auto-dismisses (only applies if autoDismissEnabled is true)
 * @param visibleCount Maximum number of visible toasts when not expanded
 * @param categories Filter which toast categories to display (null for all)
 * @param maxWidth Maximum width of the toast container
 * @param contentPadding Additional padding around the toast container
 * @param showCloseButton Whether to show a close button on each toast
 * @param theme Styling configuration for toasts (colors, icons, shapes)
 * @param toast The composable used to display each toast. Provides default UI if not specified.
 */
@Composable
public fun ToastHost(
  hostState: ToastHostState,
  modifier: Modifier = Modifier,
  alignment: ToastAlignment = ToastAlignment.Top,
  autoDismissEnabled: Boolean = true,
  autoDismissDelay: Long = 3000L,
  visibleCount: Int = 3,
  categories: List<ToastCategory>? = null,
  maxWidth: Dp = 400.dp,
  contentPadding: PaddingValues = PaddingValues(16.dp),
  showCloseButton: Boolean = false,
  theme: ToastTheme = ToastTheme.Default,
  toast: @Composable (ToastData) -> Unit = { toastData ->
    DefaultToast(
      toastData,
      theme,
      showCloseButton,
    )
  },
) {
  val density = LocalDensity.current

  var isPaused by remember { mutableStateOf(false) }

  val filteredToasts by remember {
    derivedStateOf {
      if (categories.isNullOrEmpty()) {
        hostState.toasts
      } else {
        hostState.toasts.filter { it.category in categories }
      }
    }
  }

  LaunchedEffect(hostState.willDeleteIndices.size) {
    val toDelete = hostState.toasts.filter { hostState.isMarkedForDeletion(it) }
    if (toDelete.isNotEmpty()) {
      delay(600)
      toDelete.forEach { hostState.removeToast(it) }
    }
  }

  LaunchedEffect(autoDismissEnabled, autoDismissDelay) {
    if (!autoDismissEnabled || autoDismissDelay <= 0) return@LaunchedEffect

    while (true) {
      delay(autoDismissDelay)

      withFrameMillis {
        if (!hostState.isDragging() && hostState.toasts.isNotEmpty()) {
          val currentToasts = if (categories.isNullOrEmpty()) {
            hostState.toasts.toList()
          } else {
            hostState.toasts.filter { it.category in categories }
          }

          val visibleToasts = currentToasts.filterNot { hostState.isMarkedForDeletion(it) }

          visibleToasts.firstOrNull()?.let { oldestToast ->
            hostState.hide(oldestToast.id)
          }
        }
      }
    }
  }

  BoxWithConstraints(
    modifier = modifier.fillMaxSize(),
    contentAlignment = alignment.toComposeAlignment(),
  ) {
    val containerWidth = if (constraints.maxWidth.dp < 600.dp) constraints.maxWidth.dp else maxWidth

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues(density)
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues(density)

    val topInset = if (alignment.isTop) statusBarPadding.calculateTopPadding() else 0.dp
    val bottomInset =
      if (alignment.isBottom) navigationBarPadding.calculateBottomPadding() else 0.dp

    Box(
      modifier = Modifier.width(containerWidth).padding(
        top = contentPadding.calculateTopPadding() + topInset,
        bottom = contentPadding.calculateBottomPadding() + bottomInset,
        start = contentPadding.calculateLeftPadding(LayoutDirection.Ltr),
        end = contentPadding.calculateRightPadding(LayoutDirection.Ltr),
      ),
    ) {
      filteredToasts.forEachIndexed { index, toastData ->
        key(toastData.id) {
          ToastItem(
            toastData = toastData,
            index = index,
            totalCount = filteredToasts.size,
            visibleCount = visibleCount,
            alignment = alignment,
            isPaused = isPaused,
            onPauseToggle = { isPaused = !isPaused },
            gap = if (isPaused) 16.dp else 8.dp,
            hostState = hostState,
            toastContent = toast,
          )
        }
      }
    }
  }
}

@Composable
private fun ToastItem(
  toastData: ToastDataImpl,
  index: Int,
  totalCount: Int,
  visibleCount: Int,
  alignment: ToastAlignment,
  isPaused: Boolean,
  onPauseToggle: () -> Unit,
  gap: Dp,
  hostState: ToastHostState,
  toastContent: @Composable (ToastData) -> Unit,
) {
  val density = LocalDensity.current

  val hasAppeared = hostState.hasAppeared(toastData)
  val isMarkedForDeletion = hostState.isMarkedForDeletion(toastData)

  LaunchedEffect(toastData.id) {
    if (!hasAppeared) {
      delay(50)
      hostState.setIndexAppeared(toastData, index)
    }
  }

  val visibleToasts = remember(totalCount, hostState.willDeleteIndices.size, isMarkedForDeletion) {
    (0 until totalCount).filterNot { i ->
      hostState.toasts.getOrNull(i)?.let { hostState.isMarkedForDeletion(it) } == true
    }
  }

  val positionedIndex = remember(index, visibleToasts, isMarkedForDeletion) {
    val myVisibleIndex = visibleToasts.indexOf(index)
    if (myVisibleIndex >= 0) visibleToasts.size - myVisibleIndex - 1 else 0
  }

  var dragOffset by remember { mutableStateOf(0f) }
  var isDragging by remember { mutableStateOf(false) }

  val springSpec = spring<Float>(
    dampingRatio = 0.85f,
    stiffness = 300f,
  )
  val fastSpringSpec = spring<Float>(
    dampingRatio = 0.9f,
    stiffness = 400f,
  )

  val gapPx = with(density) { gap.toPx() }
  val heightPx = with(density) { toastData.height.toPx() }

  val targetOffsetY = when {
    !hasAppeared -> if (alignment.isTop) -34f else 34f
    isMarkedForDeletion -> {
      val baseOffset = -(heightPx + gapPx * 2)
      val stackOffset = positionedIndex * (if (isPaused) heightPx + gapPx else gapPx)
      if (alignment.isTop) baseOffset + stackOffset else -(baseOffset + stackOffset)
    }

    else -> {
      val stackOffset = positionedIndex * (if (isPaused) heightPx + gapPx else gapPx)
      if (alignment.isTop) stackOffset else -stackOffset
    }
  }

  val targetScale = when {
    !hasAppeared -> 0.97f
    isPaused -> 1f
    else -> 1f - 0.03f * positionedIndex
  }

  val targetAlpha = when {
    isMarkedForDeletion -> 0f
    !hasAppeared -> 0f
    positionedIndex >= visibleCount -> 0f
    else -> 1f
  }

  val animatedOffsetY by animateFloatAsState(
    targetValue = targetOffsetY,
    animationSpec = if (!hasAppeared) fastSpringSpec else springSpec,
    label = "offsetY",
  )

  val animatedScale by animateFloatAsState(
    targetValue = targetScale,
    animationSpec = springSpec,
    label = "scale",
  )

  val animatedAlpha by animateFloatAsState(
    targetValue = targetAlpha,
    animationSpec = if (!hasAppeared) tween(400, easing = FastOutSlowInEasing) else springSpec,
    label = "alpha",
  )

  val animatedDragOffset by animateFloatAsState(
    targetValue = if (isDragging) dragOffset else 0f,
    animationSpec = if (isDragging) snap() else springSpec,
    label = "dragOffset",
  )

  val dragAlpha = if (isDragging) {
    val threshold = 20f
    when {
      alignment.isTop && dragOffset < -threshold -> 0f
      alignment.isBottom && dragOffset > threshold -> 0f
      else -> 1f
    }
  } else {
    1f
  }

  val finalAlpha by animateFloatAsState(
    targetValue = animatedAlpha * dragAlpha,
    animationSpec = springSpec,
    label = "finalAlpha",
  )

  if (finalAlpha <= 0f && !hasAppeared) return

  Box(
    modifier = Modifier.offset {
      IntOffset(
        0,
        (animatedOffsetY + animatedDragOffset).roundToInt(),
      )
    }.scale(animatedScale).alpha(finalAlpha.coerceIn(0f, 1f)).pointerInput(toastData.id) {
      detectTapGestures(
        onTap = {
          onPauseToggle()
        },
      )
    }.pointerInput(toastData.id, alignment) {
      detectDragGestures(onDragStart = {
        isDragging = true
        hostState.setDragging(toastData, true)
      }, onDragEnd = {
        val velocity = dragOffset
        val shouldDismiss = when {
          alignment.isTop && velocity < -50 -> true
          alignment.isBottom && velocity > 50 -> true
          else -> false
        }

        if (shouldDismiss) {
          hostState.hide(toastData.id)
        }

        isDragging = false
        dragOffset = 0f
        hostState.setDragging(toastData, false)
      }, onDragCancel = {
        isDragging = false
        dragOffset = 0f
        hostState.setDragging(toastData, false)
      }, onDrag = { change, dragAmount ->
        change.consume()
        dragOffset += dragAmount.y
      })
    }.fillMaxWidth().height(toastData.height + gap),
  ) {
    Box(
      modifier = Modifier.align(
        when {
          alignment.isTop -> Alignment.TopCenter
          else -> Alignment.BottomCenter
        },
      ).fillMaxWidth(),
    ) {
      toastContent(toastData)
    }
  }
}

/**
 * Default toast UI with theme and close button support.
 * You can provide your own toast composable to ToastHost for custom UI.
 */
@Composable
public fun DefaultToast(
  toastData: ToastData,
  theme: ToastTheme = ToastTheme.Default,
  showCloseButton: Boolean = false,
) {
  val categoryStyle = when (toastData.category) {
    ToastCategory.Success -> theme.success
    ToastCategory.Error -> theme.error
    ToastCategory.Warning -> theme.warning
    ToastCategory.General -> null
    is ToastCategory.Custom -> null
  }

  val backgroundColor = categoryStyle?.backgroundColor ?: theme.default.backgroundColor
  val textColor = categoryStyle?.textColor ?: theme.default.textColor
  val icon = categoryStyle?.icon ?: toastData.icon

  Surface(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
    shape = theme.default.shape,
    tonalElevation = theme.default.tonalElevation,
    shadowElevation = theme.default.elevation,
    color = backgroundColor,
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Start,
    ) {
      if (icon != null) {
        when (icon) {
          is ImageVector -> {
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = textColor,
              modifier = Modifier.size(24.dp),
            )
          }

          is Painter -> {
            Icon(
              painter = icon,
              contentDescription = null,
              tint = textColor,
              modifier = Modifier.size(24.dp),
            )
          }
        }
        Spacer(modifier = Modifier.width(12.dp))
      }

      Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.Start,
      ) {
        toastData.title?.let { title ->
          Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = textColor,
          )
          Spacer(modifier = Modifier.height(4.dp))
        }
        Text(
          text = toastData.message,
          style = MaterialTheme.typography.bodyMedium,
          color = textColor,
        )
      }

      if (showCloseButton) {
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
          modifier = Modifier
            .size(24.dp)
            .pointerInput(toastData.id) {
              detectTapGestures(
                onTap = { toastData.dismiss() },
              )
            },
          shape = CircleShape,
          color = textColor.copy(alpha = 0.2f),
        ) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
          ) {
            Text(
              text = "×",
              style = MaterialTheme.typography.titleMedium,
              color = textColor,
              fontWeight = FontWeight.Bold,
            )
          }
        }
      }
    }
  }
}
