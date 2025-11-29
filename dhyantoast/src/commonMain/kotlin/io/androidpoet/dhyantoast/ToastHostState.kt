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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * State holder for toast management.
 * Similar to SnackbarHostState in Jetpack Compose.
 */
@Stable
public class ToastHostState {
  private val mutex = Mutex()

  private val toastsState = mutableStateListOf<ToastDataImpl>()
  internal val toasts: List<ToastDataImpl> get() = toastsState

  private val willDeleteIndicesState = mutableStateMapOf<String, Boolean>()
  internal val willDeleteIndices: Map<String, Boolean> get() = willDeleteIndicesState

  private val draggingIndicesState = mutableStateMapOf<String, Boolean>()
  internal val draggingIndices: Map<String, Boolean> get() = draggingIndicesState

  private val indexMapState = mutableStateMapOf<String, Int>()
  internal val indexMap: Map<String, Int> get() = indexMapState

  /**
   * Shows a toast with the given parameters.
   * Similar to SnackbarHostState.showSnackbar()
   *
   * @param message The message to show in the toast
   * @param title Optional title for the toast
   * @param icon Optional icon for the toast
   * @param category The category of the toast (affects styling/filtering)
   * @param height The height of the toast
   */
  @OptIn(ExperimentalUuidApi::class)
  public suspend fun showToast(
    message: String,
    title: String? = null,
    icon: Any? = null,
    category: ToastCategory = ToastCategory.General,
    height: Dp = 64.dp,
  ) {
    mutex.withLock {
      val id = Uuid.random().toString()
      val toast = ToastDataImpl(
        id = id,
        message = message,
        title = title,
        icon = icon,
        category = category,
        height = height,
        onDismiss = { hide(id) },
      )
      toastsState.add(toast)
    }
  }

  /**
   * Hides a toast by its ID.
   */
  internal fun hide(toastId: String) {
    willDeleteIndicesState[toastId] = true
  }

  internal fun removeToast(toast: ToastDataImpl) {
    toastsState.remove(toast)
    willDeleteIndicesState.remove(toast.id)
    indexMapState.remove(toast.id)
  }

  internal fun setDragging(toast: ToastDataImpl, isDragging: Boolean) {
    if (isDragging) {
      draggingIndicesState[toast.id] = true
    } else {
      draggingIndicesState.remove(toast.id)
    }
  }

  internal fun setIndexAppeared(toast: ToastDataImpl, index: Int) {
    indexMapState[toast.id] = index
  }

  internal fun hasAppeared(toast: ToastDataImpl): Boolean = indexMapState.containsKey(toast.id)

  internal fun isMarkedForDeletion(toast: ToastDataImpl): Boolean =
    willDeleteIndicesState[toast.id] == true

  internal fun isDragging(): Boolean = draggingIndicesState.isNotEmpty()

  /**
   * Clears all toasts.
   */
  public fun clearAll() {
    toastsState.clear()
    willDeleteIndicesState.clear()
    draggingIndicesState.clear()
    indexMapState.clear()
  }
}

@Composable
public fun rememberToastHostState(): ToastHostState = remember { ToastHostState() }
