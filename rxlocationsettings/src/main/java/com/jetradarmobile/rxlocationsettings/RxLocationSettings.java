/*
 * Copyright (C) 2017 JetRadar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jetradarmobile.rxlocationsettings;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.google.android.gms.location.LocationSettingsRequest;
import rx.Single;

public class RxLocationSettings {
  @NonNull private final RxLocationSettingsFragment rxLocationSettingsFragment;

  private RxLocationSettings(@NonNull FragmentManager fragmentManager) {
    rxLocationSettingsFragment = RxLocationSettingsFragment.from(fragmentManager);
  }

  @NonNull
  public Single<Boolean> checkLocationSettings(@NonNull LocationSettingsRequest request) {
    return rxLocationSettingsFragment.checkLocationSettings(request);
  }

  @NonNull
  public static RxLocationSettings with(@NonNull FragmentActivity activity) {
    return new RxLocationSettings(activity.getSupportFragmentManager());
  }
}