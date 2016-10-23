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

import com.google.firebase.crash.FirebaseCrash;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.Locale;

final class Pfw {

  @NonNull
  public static final String ALL_IMAGES_MIME_TYPE = "image/*";

  private Pfw() {
  }

  @NonNull
  public static File getImageFile(@NonNull Context context, int widgetId) {
    return new File(context.getFilesDir(), "image-" + widgetId);
  }

  public static void e(@NonNull String tag, @NonNull String messageFormat,
                       @NonNull Object... messageArgs) {
    log(Log.ERROR, tag, String.format(Locale.US, messageFormat, messageArgs));
  }

  public static void log(int level, @NonNull String tag, @NonNull String messageFormat,
                         @NonNull Object... messageArgs) {
    FirebaseCrash.logcat(level, tag, String.format(Locale.US, messageFormat, messageArgs));
  }
}
