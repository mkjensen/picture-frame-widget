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
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

public class PfwAppWidgetProvider extends AppWidgetProvider {

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    SharedPreferences preferences = Pfw.getPreferences(context);
    for (int i : appWidgetIds) {
      String imagePath = preferences.getString(Pfw.getImagePathPreferenceKey(i), null);
      if (imagePath != null) {
        setImage(context, appWidgetManager, i, imagePath);
      } else {
        showError(context, appWidgetManager, i);
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

  private static void showError(@NonNull Context context, @NonNull AppWidgetManager manager,
                                int widgetId) {
    RemoteViews views = getRemoteViews(context);
    views.setImageViewResource(R.id.widget_image, R.mipmap.ic_launcher);
    manager.updateAppWidget(widgetId, views);
  }
}
