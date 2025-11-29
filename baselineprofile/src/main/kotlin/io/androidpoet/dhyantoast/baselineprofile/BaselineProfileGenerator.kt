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
package io.androidpoet.dhyantoast.baselineprofile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import org.junit.Rule
import org.junit.Test

@RequiresApi(Build.VERSION_CODES.P)
class BaselineProfileGenerator {
  @get:Rule
  val baselineProfileRule = BaselineProfileRule()

  private val targetPackage = "io.androidpoet.dhyantoast.baselineprofile.app"

  @Test
  fun generateBaselineProfile() = baselineProfileRule.collect(
    packageName = targetPackage,
  ) {
    pressHome()
    startActivityAndWait()
    device.waitForIdle()

    // Test showing success toast
    device.findObject(By.desc("Show Success Toast"))?.let { successButton ->
      successButton.click()
      device.waitForIdle()
      // Wait for toast to appear and auto-dismiss
      Thread.sleep(1500)
      device.waitForIdle()
    }

    // Test showing error toast
    device.findObject(By.desc("Show Error Toast"))?.let { errorButton ->
      errorButton.click()
      device.waitForIdle()
      Thread.sleep(1500)
      device.waitForIdle()
    }

    // Test showing warning toast
    device.findObject(By.desc("Show Warning Toast"))?.let { warningButton ->
      warningButton.click()
      device.waitForIdle()
      Thread.sleep(1500)
      device.waitForIdle()
    }

    // Test showing info toast
    device.findObject(By.desc("Show Info Toast"))?.let { infoButton ->
      infoButton.click()
      device.waitForIdle()
      Thread.sleep(1500)
      device.waitForIdle()
    }

    // Test showing multiple toasts rapidly
    device.findObject(By.desc("Show Success Toast"))?.click()
    device.waitForIdle()
    device.findObject(By.desc("Show Error Toast"))?.click()
    device.waitForIdle()
    device.findObject(By.desc("Show Warning Toast"))?.click()
    device.waitForIdle()

    // Let them stack and animate
    Thread.sleep(2000)
    device.waitForIdle()

    // Test clear all toasts
    device.findObject(By.desc("Clear All Toasts"))?.let { clearButton ->
      clearButton.click()
      device.waitForIdle()
    }

    // Test showing toasts again after clearing
    device.findObject(By.desc("Show Info Toast"))?.click()
    device.waitForIdle()
    Thread.sleep(500)
    device.findObject(By.desc("Show Success Toast"))?.click()
    device.waitForIdle()
    Thread.sleep(2000)
    device.waitForIdle()
  }
}
