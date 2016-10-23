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

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import java.io.File;

public class WidgetProvider extends AppWidgetProvider {

  private FirebaseAnalytics analytics;

  @Override
  public void onReceive(Context context, Intent intent) {
    if (analytics == null) {
      analytics = FirebaseAnalytics.getInstance(context);
    }
    super.onReceive(context, intent);
  }

  @Override
  public void onEnabled(@NonNull Context context) {
    PfwAnalytics.widgetProviderEnabled(analytics);
  }

  @Override
  public void onRestored(@NonNull Context context, @NonNull int[] oldWidgetIds,
                         @NonNull int[] newWidgetIds) {
    PfwAnalytics.widgetProviderRestored(analytics);
  }

  @Override
  public void onUpdate(@NonNull Context context, @NonNull AppWidgetManager appWidgetManager,
                       @NonNull int[] appWidgetIds) {
    PfwAnalytics.widgetProviderUpdated(analytics);
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

  @Override
  public void onAppWidgetOptionsChanged(@NonNull Context context,
                                        @NonNull AppWidgetManager appWidgetManager, int appWidgetId,
                                        @NonNull Bundle newOptions) {
    PfwAnalytics.widgetProviderChanged(analytics);
  }

  @Override
  public void onDeleted(@NonNull Context context, @NonNull int[] appWidgetIds) {
    PfwAnalytics.widgetProviderDeleted(analytics);
    for (int widgetId : appWidgetIds) {
      File imageFile = Pfw.getImageFile(context, widgetId);
      imageFile.delete();
    }
  }

  @Override
  public void onDisabled(@NonNull Context context) {
    PfwAnalytics.widgetProviderDisabled(analytics);
  }
}
