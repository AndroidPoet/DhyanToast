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
package io.androidpoet.dhyantoast.baselineprofile.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import io.androidpoet.dhyantoast.ToastAlignment
import io.androidpoet.dhyantoast.ToastCategory
import io.androidpoet.dhyantoast.ToastCategoryStyle
import io.androidpoet.dhyantoast.ToastHost
import io.androidpoet.dhyantoast.ToastScaffold
import io.androidpoet.dhyantoast.ToastTheme
import io.androidpoet.dhyantoast.baselineprofile.app.ui.theme.ToastDemoTheme
import io.androidpoet.dhyantoast.rememberToastHostState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      ToastDemoTheme {
        ToastDemoApp()
      }
    }
  }
}

@Composable
fun ToastDemoApp() {
  val toastHostState = rememberToastHostState()
  val scope = rememberCoroutineScope()

  val toastTheme = ToastTheme(
    success = ToastCategoryStyle(
      backgroundColor = Color(0xFF4CAF50),
      textColor = Color.White,
      icon = Icons.Default.Check,
    ),
    error = ToastCategoryStyle(
      backgroundColor = Color(0xFFF44336),
      textColor = Color.White,
      icon = Icons.Default.Close,
    ),
    warning = ToastCategoryStyle(
      backgroundColor = Color(0xFFFF9800),
      textColor = Color.White,
      icon = Icons.Default.Warning,
    ),
  )

  ToastScaffold(
    toastHost = {
      ToastHost(
        hostState = toastHostState,
        alignment = ToastAlignment.Bottom,
        autoDismissEnabled = true,
        autoDismissDelay = 1000L,
        visibleCount = 3,
        showCloseButton = true,
        contentPadding = PaddingValues(16.dp),
        theme = toastTheme,
      )
    },
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      Text(
        text = "Toast Demo",
        style = MaterialTheme.typography.headlineMedium,
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Toast type buttons
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Button(
          onClick = {
            scope.launch {
              toastHostState.showToast(
                message = "Operation completed successfully!",
                title = "Success",
                category = ToastCategory.Success,
              )
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
          modifier = Modifier
            .weight(1f)
            .semantics { contentDescription = "Show Success Toast" },
        ) {
          Text("Success")
        }
        Button(
          onClick = {
            scope.launch {
              toastHostState.showToast(
                message = "Something went wrong!",
                title = "Error",
                category = ToastCategory.Error,
              )
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
          modifier = Modifier
            .weight(1f)
            .semantics { contentDescription = "Show Error Toast" },
        ) {
          Text("Error")
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Button(
          onClick = {
            scope.launch {
              toastHostState.showToast(
                message = "Please check your input",
                title = "Warning",
                category = ToastCategory.Warning,
              )
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
          modifier = Modifier
            .weight(1f)
            .semantics { contentDescription = "Show Warning Toast" },
        ) {
          Text("Warning")
        }
        Button(
          onClick = {
            scope.launch {
              toastHostState.showToast(
                message = "Here's some useful information",
                title = "Info",
                category = ToastCategory.General,
              )
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
          modifier = Modifier
            .weight(1f)
            .semantics { contentDescription = "Show Info Toast" },
        ) {
          Text("Info")
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Clear all button
      OutlinedButton(
        onClick = { toastHostState.clearAll() },
        modifier = Modifier
          .fillMaxWidth()
          .semantics { contentDescription = "Clear All Toasts" },
      ) {
        Text("Clear All Toasts")
      }
    }
  }
}
