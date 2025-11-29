import io.androidpoet.dhyantoast.Configuration
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget


@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.nexus.plugin)
  alias(libs.plugins.baseline.profile)
}

apply(from = "$rootDir/scripts/publish-module.gradle.kts")

mavenPublishing {
  val artifactId = "dhyāntoast"
  coordinates(
    Configuration.artifactGroup,
    artifactId,
    rootProject.extra.get("libVersion").toString(),
  )

  pom {
    name.set(artifactId)
    description.set(
      "🍞 A powerful and elegant toast notification library for Compose Multiplatform with gestures, animations, and theming support",
    )
  }
}

kotlin {
  androidTarget { publishLibraryVariants("release") }
  jvm("desktop")
  iosX64()
  iosArm64()
  iosSimulatorArm64()
  macosX64()
  macosArm64()
  js(IR) {
    browser()
    nodejs()
  }
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
    nodejs()
    binaries.executable()
  }

  @Suppress("OPT_IN_USAGE")
  applyHierarchyTemplate {
    common {
      group("jvm") {
        withAndroidTarget()
        withJvm()
      }
      group("skia") {
        withJvm()
        group("darwin") {
          group("apple") {
            group("ios") {
              withIosX64()
              withIosArm64()
              withIosSimulatorArm64()
            }
            group("macos") {
              withMacosX64()
              withMacosArm64()
            }
          }
          withJs()
          withWasmJs()
        }

      }
    }
  }

  targets.configureEach {
    compilations.configureEach {
      compilerOptions.configure {
        // https://youtrack.jetbrains.com/issue/KT-61573
        freeCompilerArgs.add("-Xexpect-actual-classes")
      }
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(compose.ui)
        implementation(compose.material3)
        implementation(compose.runtime)
        implementation(compose.animation)
        implementation(compose.materialIconsExtended)
        implementation(libs.composeIcons.featherIcons)
      }
    }
  }

  explicitApi()
}
composeCompiler {
  enableStrongSkippingMode = true
}
android {
  compileSdk = Configuration.compileSdk
  namespace = "io.androidpoet.dhyantoast"
  defaultConfig {
    minSdk = Configuration.minSdk
  }

  buildFeatures {
    compose = true
    buildConfig = false
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  packaging {
    resources {
      excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
  }

  lint {
    abortOnError = false
  }
}

baselineProfile {
  baselineProfileOutputDir = "../../src/androidMain"
  filter {
    include("io.androidpoet.dhyantoast.**")
  }
}

dependencies {
  baselineProfile(project(":baselineprofile"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_1_8)
    freeCompilerArgs.addAll(
      listOf(
        "-Xexplicit-api=strict",
        "-Xopt-in=androidx.compose.material3.ExperimentalMaterial3Api",
      )
    )
  }
}
