/*
 * Copyright 2016 Martin Kamp Jensen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.mkjensen.pfw;

import com.google.firebase.analytics.FirebaseAnalytics;

import android.support.annotation.NonNull;

final class PfwAnalytics {

  private PfwAnalytics() {
  }

  public static void widgetConfigureBegin(@NonNull FirebaseAnalytics analytics) {
    logEvent(analytics, "widget_configure_begin");
  }

  public static void widgetConfigureComplete(@NonNull FirebaseAnalytics analytics) {
    logEvent(analytics, "widget_configure_complete");
  }

  public static void imageOpenBegin(@NonNull FirebaseAnalytics analytics) {
    logEvent(analytics, "image_open_begin");
  }

  public static void imageOpenComplete(@NonNull FirebaseAnalytics analytics) {
    logEvent(analytics, "image_open_complete");
  }

  public static void imageCropBegin(@NonNull FirebaseAnalytics analytics) {
    logEvent(analytics, "image_crop_begin");
  }

  public static void imageCropComplete(@NonNull FirebaseAnalytics analytics) {
    logEvent(analytics, "image_crop_complete");
  }

  public static void widgetProviderEnabled(@NonNull FirebaseAnalytics analytics) {
    logEvent(analytics, "widget_provider_enabled");
  }

  public static void widgetProviderRestored(@NonNull FirebaseAnalytics analytics) {
    logEvent(analytics, "widget_provider_restored");
  }

  public static void widgetProviderUpdated(@NonNull FirebaseAnalytics analytics) {
    logEvent(analytics, "widget_provider_updated");
  }

  public static void widgetProviderChanged(@NonNull FirebaseAnalytics analytics) {
    logEvent(analytics, "widget_provider_changed");
  }

  public static void widgetProviderDeleted(@NonNull FirebaseAnalytics analytics) {
    logEvent(analytics, "widget_provider_deleted");
  }

  public static void widgetProviderDisabled(@NonNull FirebaseAnalytics analytics) {
    logEvent(analytics, "widget_provider_disabled");
  }

  private static void logEvent(@NonNull FirebaseAnalytics analytics, @NonNull String name) {
    analytics.logEvent(name, null);
  }
}
