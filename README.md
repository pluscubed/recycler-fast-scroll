Recycler Fast Scroll ![Icon](./sample/src/main/res/mipmap-mdpi/ic_launcher.png) 
=====

![Art](./art/small.gif)

[![Release](https://jitpack.io/v/com.github.pluscubed/recycler-fast-scroll.svg)](https://jitpack.io/#com.pluscubed/insets-dispatcher)  [![License](https://img.shields.io/github/license/pluscubed/recycler-fast-scroll.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

Widget for `RecyclerView` fast scrolling, like Android's built-in fast scroll option for `ListView`.  
Supports any vertically scrolling `LayoutManager`, as well as proper integration for `AppBarLayout`. 

Planned features are listed at the [issues page](https://github.com/plusCubed/recycler-fast-scroll/issues).  
You can download the sample APK from the [releases page](https://github.com/plusCubed/recycler-fast-scroll/releases).

### Sample
[![Get it on Google Play](http://i.imgur.com/PeDVOwW.png)](https://play.google.com/store/apps/details?id=com.pluscubed.recyclerfastscrollsample)

### Dependency

Add this to your module's `build.gradle` file:

```gradle
repositories {
	//...
	maven { url "https://jitpack.io" }
}
	
dependencies {
	//...
	compile 'com.github.pluscubed:recycler-fast-scroll:{latest-version}@aar'
}
```

The library is versioned according to [Semantic Versioning](http://semver.org/).

### Basic Usage
1. Add the widget to your layout file, e.g.:
    ```xml
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

        <com.pluscubed.recyclerfastscroll.RecyclerFastScroller
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:layout_gravity="end" />

    </FrameLayout>
    ```
    
2. Attach the `RecyclerFastScroller` to your `RecyclerView`:
    ```java
    fastScroller.attachRecyclerView(recyclerView);
    ```
    If the adapter hasn't been set when you attach the fast scroller, call `attachAdapter()` afterwords.

See the sample project for demos.

### AppBarLayout support
Use `attachAppBarLayout(coordinatorLayout, appBarLayout)`. See the sample project for the demo.

### Customization
| Method | Attribute | Description |
| --- | --- | --- |
| `setBarColor(int color)`| `rfs_barColor` | Color of scrollbar. Defaults to `colorControlNormal` attribute. Alpha of ~22% is applied to the drawable to match stock `ListView` fast scroller. |
| `setHandleNormalColor(int color)` | `rfs_handleNormalColor` | Color of handle. Defaults to `colorControlNormal` attribute. |
| `setHandlePressedColor(int color)` | `rfs_handlePressedColor` | Color of pressed handle. Defaults to `colorAccent` attribute. |
| `setTouchTargetWidth(int width)` | `rfs_touchTargetWidth` | Width of the touch target. Defaults to 24dp (while the Android docs recommend at least 48dp, 24dp is more practical considering it will block touch in the right of the `RecyclerView`). |
| `setHideDelay(int milliseconds)` | `rfs_hideDelay` | Hide delay in milliseconds. Defaults to 1500ms. |
| `setHidingEnabled(boolean enabled)` | `rfs_hidingEnabled` | Whether scrollbar is hidden after delay. Defaults to true. |
| `setOnHandleTouchListener(OnTouchListener listener)` | -- | Sets listener for handle touch events. |

Corresponding getters are also available.

####Attribute Usage
```xml
<com.pluscubed.recyclerfastscroll.RecyclerFastScroller
    ...
    app:{attributeName}="{value}"/>
```


### License
```
Copyright 2016 Daniel Ciao

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
