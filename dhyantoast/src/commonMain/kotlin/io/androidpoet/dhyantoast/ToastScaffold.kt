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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * A wrapper around Material3 Scaffold that includes a toastHost parameter.
 * Retains all original Scaffold parameters while adding toast support.
 *
 * @param toastHost The composable to display toasts, typically a ToastHost
 * @param modifier The modifier to be applied to the Scaffold
 * @param topBar The top app bar of the Scaffold
 * @param bottomBar The bottom bar of the Scaffold
 * @param snackbarHost Component to host Snackbars that are pushed to be shown
 * @param floatingActionButton The floating action button of the Scaffold
 * @param floatingActionButtonPosition The position of the floating action button
 * @param containerColor The container color of the Scaffold
 * @param contentColor The preferred content color provided by this Scaffold
 * @param contentWindowInsets Window insets to be passed to content slot
 * @param content The main content of the Scaffold
 */
@Composable
public fun ToastScaffold(
  toastHost: @Composable () -> Unit = {},
  modifier: Modifier = Modifier,
  topBar: @Composable () -> Unit = {},
  bottomBar: @Composable () -> Unit = {},
  snackbarHost: @Composable () -> Unit = {},
  floatingActionButton: @Composable () -> Unit = {},
  floatingActionButtonPosition: FabPosition = FabPosition.End,
  containerColor: Color = MaterialTheme.colorScheme.background,
  contentColor: Color = contentColorFor(containerColor),
  contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
  content: @Composable (PaddingValues) -> Unit,
) {
  Scaffold(
    modifier = modifier,
    topBar = topBar,
    bottomBar = bottomBar,
    snackbarHost = snackbarHost,
    floatingActionButton = floatingActionButton,
    floatingActionButtonPosition = floatingActionButtonPosition,
    containerColor = containerColor,
    contentColor = contentColor,
    contentWindowInsets = contentWindowInsets,
  ) { paddingValues ->
    Box(modifier = Modifier.fillMaxSize()) {
      content(paddingValues)
      toastHost()
    }
  }
}
