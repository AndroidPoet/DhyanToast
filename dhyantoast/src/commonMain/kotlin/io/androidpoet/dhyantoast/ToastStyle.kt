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

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Styling configuration for toasts
 */
public data class ToastStyle(
  val backgroundColor: Color = Color(0xFF2D2D2D),
  val textColor: Color = Color.White,
  val shape: Shape = RoundedCornerShape(12.dp),
  val elevation: Dp = 8.dp,
  val tonalElevation: Dp = 4.dp,
) {
  public companion object {
    public val Default: ToastStyle = ToastStyle()
  }
}

/**
 * Category-specific styling for toasts
 */
public data class ToastCategoryStyle(
  val backgroundColor: Color,
  val textColor: Color = Color.White,
  val icon: Any? = null,
)

/**
 * Complete toast theme configuration
 */
public data class ToastTheme(
  val default: ToastStyle = ToastStyle.Default,
  val success: ToastCategoryStyle? = null,
  val error: ToastCategoryStyle? = null,
  val warning: ToastCategoryStyle? = null,
  val info: ToastCategoryStyle? = null,
) {
  public companion object {
    public val Default: ToastTheme = ToastTheme()
  }
}
