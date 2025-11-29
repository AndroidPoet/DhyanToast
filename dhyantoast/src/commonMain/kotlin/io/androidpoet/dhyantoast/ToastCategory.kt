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

/**
 * A category for grouping and filtering toasts.
 *
 * Categories allow you to organize toasts and display them in different
 * [ToastHost] composables based on their type.
 */
public sealed class ToastCategory(public val name: String) {
  public data object General : ToastCategory("general")
  public data object Success : ToastCategory("success")
  public data object Warning : ToastCategory("warning")
  public data object Error : ToastCategory("error")
  public data class Custom(val customName: String) : ToastCategory(customName)
}
