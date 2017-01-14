Android-RxLocationSettings
==========================

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android--RxLocationSettings-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5038)
[![JitPack](https://jitpack.io/v/jetradarmobile/android-rxlocationsettings.svg)](https://jitpack.io/#jetradarmobile/android-rxlocationsettings)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![TravisCI](https://travis-ci.org/JetradarMobile/android-rxlocationsettings.svg?branch=master)](https://travis-ci.org/JetradarMobile/android-rxlocationsettings)
[![AndroidDev Digest](https://img.shields.io/badge/AndroidDev%20Digest-%23126-blue.svg?style=flat)](https://www.androiddevdigest.com/digest-126/)


An easy way to ensure the settings before requesting location using RxJava.

![image](https://raw.githubusercontent.com/JetradarMobile/android-rxlocationsettings/master/art/rxlocationsettings.png)


Compatibility
-------------

This library is compatible from API 14 (Android 4.0).


Download
--------

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

Add the dependency

```groovy
dependencies {
    compile 'com.github.jetradarmobile:android-rxlocationsettings:1.1.0'
}
```


Usage
-----

```java
LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
    .addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
    .build();
RxLocationSettings.with(this).ensure(locationSettingsRequest)
    .subscribe(enabled -> ...);
```

See [sample](https://github.com/JetradarMobile/android-rxlocationsettings/tree/master/rxlocationsettings-sample) project for more information.

Credits
-------

This library was inspired by [RxPermissions](https://github.com/tbruyelle/RxPermissions).


License
-------

    Copyright 2017 JetRadar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
