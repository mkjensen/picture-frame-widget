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

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import java.io.File;

public class WidgetProvider extends AppWidgetProvider {

  @Override
  public void onUpdate(@NonNull Context context, @NonNull AppWidgetManager appWidgetManager,
                       @NonNull int[] appWidgetIds) {
    for (int widgetId : appWidgetIds) {
      File imageFile = Pfw.getImageFile(context, widgetId);
      if (imageFile.exists()) {
        setImage(context, appWidgetManager, widgetId, imageFile.getAbsolutePath());
      }
    }
  }

  static void setImage(@NonNull Context context, @Nullable AppWidgetManager manager, int widgetId,
                       @NonNull String imagePath) {
    if (manager == null) {
      manager = AppWidgetManager.getInstance(context);
    }
    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
    setBitmap(context, manager, widgetId, bitmap);
  }

  private static void setBitmap(@NonNull Context context, @NonNull AppWidgetManager manager,
                                int widgetId, @NonNull Bitmap bitmap) {
    RemoteViews views = getRemoteViews(context);
    views.setImageViewBitmap(R.id.widget_image, bitmap);
    manager.updateAppWidget(widgetId, views);
  }

  @NonNull
  private static RemoteViews getRemoteViews(@NonNull Context context) {
    return new RemoteViews(context.getPackageName(), R.layout.widget);
  }
}
