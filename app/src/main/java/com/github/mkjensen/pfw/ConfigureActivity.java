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
import com.google.firebase.crash.FirebaseCrash;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

public class ConfigureActivity extends AppCompatActivity {

  private static final String TAG = "ConfigureActivity";

  private static final int OPEN_IMAGE_REQUEST_CODE = 42;

  private FirebaseAnalytics analytics;

  private int widgetId;

  private File imageFile;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // User may cancel (e.g. press back button) at any time. Default to canceled result.
    setResult(RESULT_CANCELED);

    initAnalytics();
    if (!initWidgetId()) {
      return;
    }
    initImageFile();
    openImage();
  }

  private void initAnalytics() {
    analytics = FirebaseAnalytics.getInstance(this);
    PfwAnalytics.widgetConfigureBegin(analytics);
  }

  private boolean initWidgetId() {
    int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      Pfw.e(TAG, "Invalid widget ID specified: widgetId=%d", widgetId);
      FirebaseCrash.report(new Exception());
      finish();
      return false;
    }
    this.widgetId = widgetId;
    return true;
  }

  private void initImageFile() {
    imageFile = Pfw.getImageFile(this, widgetId);
  }

  private void openImage() {
    PfwAnalytics.imageOpenBegin(analytics);
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    intent.setType(Pfw.ALL_IMAGES_MIME_TYPE);
    startActivityForResult(intent, OPEN_IMAGE_REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == OPEN_IMAGE_REQUEST_CODE) {
      imageOpened(resultCode, data);
    } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      imageCropped(resultCode);
    }
  }

  private void imageOpened(int resultCode, @Nullable Intent data) {
    if (resultCode != RESULT_OK || data == null) {
      CropImageView.CropResult result = CropImage.getActivityResult(data);
      Pfw.e(TAG, "An error occurred while opening image: resultCode=%d", resultCode);
      FirebaseCrash.report(result != null && result.getError() != null ?
          result.getError() : new Exception());
      finish();
      return;
    }
    PfwAnalytics.imageOpenComplete(analytics);
    cropImage(data.getData());
  }

  private void cropImage(@NonNull Uri imageUri) {
    PfwAnalytics.imageCropBegin(analytics);
    // TODO: Set options such that cropping results in optimal image for widget
    CropImage.activity(imageUri)
        .setOutputUri(Uri.fromFile(imageFile))
        .start(this);
  }

  private void imageCropped(int resultCode) {
    if (resultCode != RESULT_OK) {
      Pfw.e(TAG, "An error occurred while cropping image: resultCode=%d", resultCode);
      FirebaseCrash.report(new Exception());
      finish();
      return;
    }
    PfwAnalytics.imageCropComplete(analytics);
    finishSuccessfully();
  }

  private void finishSuccessfully() {
    WidgetProvider.setImage(this, null, widgetId, imageFile.getAbsolutePath());
    Intent intent = new Intent();
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
    setResult(RESULT_OK, intent);
    PfwAnalytics.widgetConfigureComplete(analytics);
    finish();
  }
}
