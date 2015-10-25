# Recycler Fast Scroll

Widget for `RecyclerView` fast scrolling, like Android's built-in fast scroll option for `ListView`.

Currently only supports verically-scrolling `LayoutManager`s.  
Planned features are listed at the [issues page](https://github.com/plusCubed/recycler-fast-scroll/issues).  
You can download the sample APK from the [releases page](https://github.com/plusCubed/recycler-fast-scroll/releases).

### Dependency
[![Release](https://img.shields.io/github/release/plusCubed/recycler-fast-scroll.svg?label=JitPack)](https://jitpack.io/#com.pluscubed/recycler-fast-scroll)  
Add the jitpack.io repo and this library to `build.gradle`:
```Gradle
repositories {
  // ...
  maven { url "https://jitpack.io" }
}

dependencies {
  // ...
  compile 'com.github.plusCubed:recycler-fast-scroll:{latest-version}'
}
```

### Usage
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
    fastScroller.setRecyclerView(recyclerView);
    ```

See the sample project for complete demos.

### Customization
 - `setPressedHandleColor(int color)`: Color of pressed handle. Defaults to `?colorAccent`.

<br/>
<br/>
Originally based off of [danoz73/RecyclerViewFastScroller](https://github.com/danoz73/RecyclerViewFastScroller).
