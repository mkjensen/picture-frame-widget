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
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

public class ConfigureActivity extends AppCompatActivity {

  private static final int OPEN_IMAGE_REQUEST_CODE = 42;

  private int widgetId;

  private File imageFile;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // User may cancel (e.g. press back button) at any time. Default to canceled result.
    setResult(RESULT_CANCELED);

    // Initialization.
    initWidgetId();
    initImageFile();

    // Request image from user.
    openImage();
  }

  private void initWidgetId() {
    int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      // Widget ID missing, abort.
      // TODO: Report problem
      finish();
    }
    this.widgetId = widgetId;
  }

  private void initImageFile() {
    imageFile = Pfw.getImageFile(this, widgetId);
  }

  private void openImage() {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    intent.setType(Pfw.ALL_IMAGES_MIME_TYPE);
    startActivityForResult(intent, OPEN_IMAGE_REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == OPEN_IMAGE_REQUEST_CODE) {
      imageOpened(resultCode, data);
    } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      imageCropped(resultCode);
    }
  }

  private void imageOpened(int resultCode, Intent data) {
    if (resultCode != RESULT_OK) {
      // TODO: Report problem
      new AlertDialog.Builder(this).setMessage("An error occurred while opening the image").show();
      finish();
      return;
    }
    cropImage(data.getData());
  }

  private void cropImage(Uri imageUri) {
    // TODO: Set options such that cropping results in optimal image for widget
    CropImage.activity(imageUri)
        .setOutputUri(Uri.fromFile(imageFile))
        .start(this);
  }

  private void imageCropped(int resultCode) {
    if (resultCode != RESULT_OK) {
      // TODO: Report problem
      new AlertDialog.Builder(this).setMessage("An error occurred while cropping the image").show();
      finish();
      return;
    }
    saveAndUpdateWidget();
  }

  private void saveAndUpdateWidget() {
    saveWidget();
    PfwAppWidgetProvider.setImage(this, null, widgetId, imageFile.getAbsolutePath());
    finishSuccessful();
  }

  private void saveWidget() {
    SharedPreferences.Editor editor = Pfw.getPreferences(this).edit();
    editor.putString(Pfw.getImagePathPreferenceKey(widgetId), imageFile.getAbsolutePath());
    editor.apply();
  }

  private void finishSuccessful() {
    Intent intent = new Intent();
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
    setResult(RESULT_OK, intent);
    finish();
  }
}
