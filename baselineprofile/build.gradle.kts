import io.androidpoet.dhyantoast.Configuration

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id(
    libs.plugins.android.test
      .get()
      .pluginId,
  )
  id(
    libs.plugins.kotlin.android
      .get()
      .pluginId,
  )
  id(
    libs.plugins.baseline.profile
      .get()
      .pluginId,
  )
}

android {
  namespace = "io.androidpoet.dhyantoast.baselineprofile"
  compileSdk = Configuration.compileSdk

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = libs.versions.jvmTarget.get()
  }

  defaultConfig {
    minSdk = 24
    targetSdk = Configuration.targetSdk
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  targetProjectPath = ":baselineprofile-app"
}

// This is the plugin configuration. Everything is optional. Defaults are in the
// comments. In this example, you use connected devices to generate profiles.
baselineProfile {

  // This enables using connected devices to generate profiles. The default is true.
  // When using connected devices, they must be rooted or API 33 and higher.
  useConnectedDevices = true
}

dependencies {
  implementation(libs.androidx.test.runner)
  implementation(libs.androidx.test.uiautomator)
  implementation(libs.androidx.benchmark.macro)
  implementation(libs.androidx.profileinstaller)
}
