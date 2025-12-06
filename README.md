

<div align="center">

# DhyānToast
🍞 A powerful and elegant toast notification library for Compose Multiplatform with gestures, animations, and theming support.

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![GitHub](https://user-images.githubusercontent.com/13647384/162662962-82e3c1eb-baf8-4e21-ad26-d4c4e3c31e44.svg)](https://github.com/AndroidPoet)

<div style="display: inline-block; padding: 12px; background: #1c1c1e; border-radius: 36px; box-shadow: 0 25px 50px -12px rgba(0,0,0,0.5);">
  <div style="width: 240px; height: 520px; background: #000; border-radius: 28px; overflow: hidden; position: relative;">
    <img src="https://github.com/user-attachments/assets/0075a179-5c13-4f1c-8357-69b6b41bab9a" width="240" style="border-radius: 28px;"/>
  </div>
</div>

</div>

## Features

- 🎯 **Snackbar-style API** - Familiar API similar to Material3 Snackbar
- 📚 **Stacked Toasts** - Display multiple toasts with smooth animations
- 👆 **Gesture Support** - Tap to pause, swipe to dismiss
- 🎨 **Category-based Theming** - Success, Error, Warning, General, and Custom categories
- ⏱️ **Auto-dismiss** - Configurable auto-dismiss with pause on interaction
- 🎭 **Smooth Animations** - Spring-based animations for natural feel
- 📱 **Compose Multiplatform** - Android, iOS, Desktop, and Web support
- 🔧 **Highly Customizable** - Custom themes, colors, shapes, and icons
- 🏗️ **Easy Integration** - Works seamlessly with Material3 Scaffold

## Quick Start

```kotlin
@Composable
fun App() {
  val toastHostState = rememberToastHostState()
  val scope = rememberCoroutineScope()

  ToastScaffold(
    toastHost = { ToastHost(hostState = toastHostState) }
  ) { paddingValues ->
    Button(
      onClick = {
        scope.launch {
          toastHostState.showToast(
            message = "Hello, Toast!",
            category = ToastCategory.Success
          )
        }
      }
    ) {
      Text("Show Toast")
    }
  }
}
```

## Include in your project

[![Maven Central](https://img.shields.io/maven-central/v/io.github.androidpoet/dhyantoast.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.github.androidpoet/dhyantoast)

### Gradle

Add the dependency below to your **module**'s `build.gradle` file:

```gradle
dependencies {
    implementation("io.github.androidpoet:dhyantoast:$version")
}
```

### Kotlin Multiplatform

Add the dependency below to your **module**'s `build.gradle.kts` file:

```kotlin
sourceSets {
    commonMain.dependencies {
        implementation("io.github.androidpoet:dhyantoast:$version")
    }
}
```

Or using the older syntax:

```kotlin
sourceSets {
    val commonMain by getting {
        dependencies {
            implementation("io.github.androidpoet:dhyantoast:$version")
        }
    }
}
```

## Usage

DhyānToast follows the same pattern as Material3's Snackbar API, making it familiar and easy to use.

### Basic Setup

```kotlin
@Composable
fun MyApp() {
  // 1. Create toast host state
  val toastHostState = rememberToastHostState()
  val scope = rememberCoroutineScope()

  // 2. Use ToastScaffold instead of regular Scaffold
  ToastScaffold(
    toastHost = {
      ToastHost(
        hostState = toastHostState,
        alignment = ToastAlignment.Bottom,
        autoDismissEnabled = true,
        autoDismissDelay = 3000L
      )
    }
  ) { paddingValues ->
    // Your content here
    Button(
      onClick = {
        scope.launch {
          // 3. Show toasts from coroutines
          toastHostState.showToast(
            message = "Operation completed!",
            category = ToastCategory.Success
          )
        }
      }
    ) {
      Text("Show Toast")
    }
  }
}
```

### Show Different Toast Categories

```kotlin
// Success toast
toastHostState.showToast(
  message = "Operation completed successfully!",
  title = "Success",
  category = ToastCategory.Success
)

// Error toast
toastHostState.showToast(
  message = "Something went wrong!",
  title = "Error",
  category = ToastCategory.Error
)

// Warning toast
toastHostState.showToast(
  message = "Please check your input",
  title = "Warning",
  category = ToastCategory.Warning
)

// General/Info toast
toastHostState.showToast(
  message = "Here's some useful information",
  category = ToastCategory.General
)

// Custom category
toastHostState.showToast(
  message = "Custom notification",
  category = ToastCategory.Custom("analytics")
)
```

### Customizing Toast Theme

```kotlin
val toastTheme = ToastTheme(
  success = ToastCategoryStyle(
    backgroundColor = Color(0xFF4CAF50),
    textColor = Color.White,
    icon = Icons.Default.Check
  ),
  error = ToastCategoryStyle(
    backgroundColor = Color(0xFFF44336),
    textColor = Color.White,
    icon = Icons.Default.Close
  ),
  warning = ToastCategoryStyle(
    backgroundColor = Color(0xFFFF9800),
    textColor = Color.White,
    icon = Icons.Default.Warning
  )
)

ToastHost(
  hostState = toastHostState,
  theme = toastTheme,
  showCloseButton = true,
  alignment = ToastAlignment.Top,
  visibleCount = 3
)
```

### Advanced Configuration

```kotlin
ToastHost(
  hostState = toastHostState,
  alignment = ToastAlignment.Top,          // Top or Bottom
  autoDismissEnabled = true,               // Auto-dismiss toasts
  autoDismissDelay = 3000L,               // 3 seconds
  visibleCount = 3,                        // Max visible toasts in stack
  categories = listOf(                     // Filter specific categories
    ToastCategory.Success,
    ToastCategory.Error
  ),
  maxWidth = 400.dp,                       // Max width on larger screens
  contentPadding = PaddingValues(16.dp),  // Padding around toasts
  showCloseButton = true,                  // Show close button
  theme = toastTheme                       // Custom theme
)
```

### Custom Toast UI

You can provide your own toast composable for completely custom UI:

```kotlin
ToastHost(
  hostState = toastHostState,
  toast = { toastData ->
    // Your custom toast UI
    Surface(
      modifier = Modifier.fillMaxWidth().padding(8.dp),
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.primary
    ) {
      Row(modifier = Modifier.padding(16.dp)) {
        Text(
          text = toastData.message,
          color = MaterialTheme.colorScheme.onPrimary
        )
      }
    }
  }
)
```

### Managing Toasts

```kotlin
// Clear all toasts
toastHostState.clearAll()

// Dismiss a specific toast
toastData.dismiss()
```

## Gestures & Interactions

DhyānToast supports intuitive gesture-based interactions:

### Tap to Pause
- Tap any toast to pause auto-dismiss for all toasts
- Toasts will expand to show all visible items in the stack
- Tap again to resume auto-dismiss

### Swipe to Dismiss
- Swipe up (when aligned to top) to dismiss a toast
- Swipe down (when aligned to bottom) to dismiss a toast
- Smooth spring-based animations follow your finger

### Close Button
- Optional close button (×) on each toast
- Enable with `showCloseButton = true`

### Animations
- Spring-based animations for natural feel
- Smooth stacking animations when toasts appear/disappear
- Fade and scale effects for visual polish
- Configurable dampingRatio and stiffness for custom feel

## Platform Support

DhyānToast works on all Compose Multiplatform targets:

| Platform | Status |
|----------|--------|
| Android | ✅ Supported |
| iOS | ✅ Supported |
| Desktop (JVM) | ✅ Supported |
| Web (JS/WASM) | ✅ Supported |
| macOS | ✅ Supported |

## DhyānToast vs Snackbar

While both use similar APIs, they serve different purposes:

| Feature | DhyānToast | Material3 Snackbar |
|---------|------------|-------------------|
| **Multiple notifications** | ✅ Stacked toasts | ❌ One at a time |
| **Gesture interactions** | ✅ Tap to pause, swipe to dismiss | ❌ Limited |
| **Auto-dismiss** | ✅ Configurable per toast | ⚠️ Fixed duration |
| **Category theming** | ✅ Success, Error, Warning, etc. | ❌ Manual styling |
| **Stack management** | ✅ Visible count control | N/A |
| **Alignment** | ✅ Top or Bottom | ⚠️ Bottom only |
| **Use case** | Non-critical notifications | Actions with undo |

**Use DhyānToast when:**
- You need to show multiple notifications simultaneously
- You want category-based styling (success, error, warning)
- You need gesture-based interactions
- You want notifications at the top of the screen

**Use Snackbar when:**
- You need a single, action-oriented message
- You want to provide an "Undo" action
- You prefer Material Design's standard bottom placement
- You need integration with Material3's standard components

## API Components

### ToastHostState
- `showToast()` - Show a new toast notification
- `clearAll()` - Clear all active toasts
- Similar to `SnackbarHostState` from Material3

### ToastHost
- Main composable for displaying toasts
- Configurable alignment, auto-dismiss, theming
- Support for custom toast UI

### ToastScaffold
- Drop-in replacement for Material3 Scaffold
- Adds `toastHost` parameter while maintaining all Scaffold features
- Works with `topBar`, `bottomBar`, `snackbarHost`, `floatingActionButton`, etc.

### ToastCategory
- `ToastCategory.Success` - Success notifications
- `ToastCategory.Error` - Error messages
- `ToastCategory.Warning` - Warning alerts
- `ToastCategory.General` - General information
- `ToastCategory.Custom(name)` - Custom categories

### ToastAlignment
- `ToastAlignment.Top` - Show toasts at top of screen
- `ToastAlignment.Bottom` - Show toasts at bottom of screen

## Why DhyānToast?

"Dhyān" (ध्यान) means "attention" or "focus" in Sanskrit. This library is designed to grab user attention elegantly with:

- **Non-intrusive** - Toasts appear smoothly without blocking content
- **Stackable** - Multiple notifications without overwhelming the UI
- **Dismissable** - Easy gesture controls for user convenience
- **Themed** - Category-based styling for instant recognition
- **Accessible** - Clear visual hierarchy and readable text

## Best Practices

### Performance
- Use `visibleCount` to limit visible toasts and maintain performance
- Clear toasts when navigating away: `toastHostState.clearAll()`
- Use appropriate `autoDismissDelay` to avoid overwhelming users

### UX Guidelines
- **Success**: Use for completed actions (save, upload, sync)
- **Error**: Use for failures that need attention (network errors, validation)
- **Warning**: Use for potential issues (low storage, deprecated features)
- **General**: Use for informational messages (tips, updates)

### Accessibility
- Keep messages concise and scannable
- Use titles for additional context
- Icons help with quick visual identification
- Auto-dismiss gives users time to read
- Tap to pause allows users to read at their own pace

### Example Toast Messages
```kotlin
// Good - Clear and actionable
toastHostState.showToast(
  title = "File saved",
  message = "Document.pdf saved successfully",
  category = ToastCategory.Success
)

// Avoid - Too verbose
toastHostState.showToast(
  title = "Success",
  message = "The file that you have been working on has been successfully saved to your device storage",
  category = ToastCategory.Success
)
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Support

### Find this repository useful? :heart:

Support it by joining __[stargazers](https://github.com/AndroidPoet/DhyanToast/stargazers)__ for this repository. :star:
Also, __[follow me](https://github.com/AndroidPoet)__ on GitHub for more cool projects!

<a href="https://www.buymeacoffee.com/AndroidPoet" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/default-orange.png" alt="Buy Me A Coffee" height="41" width="174"></a>

## License

```xml
Copyright 2025 AndroidPoet (Ranbir Singh)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
```
