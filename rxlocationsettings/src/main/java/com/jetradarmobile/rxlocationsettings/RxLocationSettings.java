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

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Single;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

public class RxLocationSettings {
  @NonNull private final ReactiveLocationProvider rxLocationProvider;
  @NonNull private final ResolutionFragment resolutionFragment;

  private RxLocationSettings(@NonNull FragmentActivity activity) {
    rxLocationProvider = new ReactiveLocationProvider(activity.getApplication());
    resolutionFragment = ResolutionFragment.from(activity.getSupportFragmentManager());
  }

  @NonNull
  public Single<Boolean> ensure(@NonNull LocationSettingsRequest request) {
    return rxLocationProvider.checkLocationSettings(request).toSingle()
        .flatMap(new Func1<LocationSettingsResult, Single<Boolean>>() {
          @Override
          public Single<Boolean> call(LocationSettingsResult result) {
            Status status = result.getStatus();
            if (status.hasResolution()) {
              return resolutionFragment.startResolutionForResult(status.getResolution());
            }
            return Single.just(status.isSuccess());
          }
        });
  }

  @NonNull
  public static RxLocationSettings with(@NonNull FragmentActivity activity) {
    return new RxLocationSettings(activity);
  }

  public static class ResolutionFragment extends Fragment {
    private static final String TAG = "RxLocationSettings";
    private static final int LOCATION_SETTINGS_REQUEST = 21;

    private final PublishSubject<Boolean> resolutionResult = PublishSubject.create();

    @NonNull
    static ResolutionFragment from(@NonNull FragmentManager fragmentManager) {
      ResolutionFragment resolutionFragment = (ResolutionFragment) fragmentManager.findFragmentByTag(TAG);
      if (resolutionFragment == null) {
        resolutionFragment = new ResolutionFragment();
        fragmentManager
            .beginTransaction()
            .add(resolutionFragment, TAG)
            .commit();
        fragmentManager.executePendingTransactions();
      }
      return resolutionFragment;
    }

    public ResolutionFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
      super.onDestroy();
      resolutionResult.onCompleted();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == LOCATION_SETTINGS_REQUEST) {
        boolean isLocationSettingsEnabled = (resultCode == Activity.RESULT_OK);
        resolutionResult.onNext(isLocationSettingsEnabled);
      }
    }

    private Single<Boolean> startResolutionForResult(PendingIntent resolution) {
      try {
        startIntentSenderForResult(resolution.getIntentSender(), LOCATION_SETTINGS_REQUEST, null, 0, 0, 0, null);
        return resolutionResult.firstOrDefault(false).toSingle();
      } catch (IntentSender.SendIntentException e) {
        Log.w(TAG, "Failed to start resolution for location settings result", e);
        return Single.just(false);
      }
    }
  }
}