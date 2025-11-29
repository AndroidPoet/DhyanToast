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

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Interface to represent data for a toast.
 * Similar to SnackbarData in Jetpack Compose.
 */
public interface ToastData {
  public val id: String
  public val message: String
  public val title: String?
  public val icon: Any?
  public val category: ToastCategory
  public val height: Dp
  public fun dismiss()
}

/**
 * Internal implementation of ToastData.
 */
internal class ToastDataImpl(
  override val id: String,
  override val message: String,
  override val title: String?,
  override val icon: Any?,
  override val category: ToastCategory,
  override val height: Dp = 64.dp,
  private val onDismiss: () -> Unit,
) : ToastData {
  override fun dismiss() = onDismiss()
}
