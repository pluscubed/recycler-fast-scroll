# Recycler Fast Scroll
Widget for `RecyclerView` fast scrolling, like Android's built-in `FastScroller` for `AbsListView`.

Currently only supports verically-scrolling `LayoutManager`s.  
Planned features are listed at the [issues page](https://github.com/plusCubed/recycler-fast-scroll/issues).

Development occurs on the "develop" branch.

### Dependency
1. Add jitpack.io to your repositories in `build.gradle`:
    ```Gradle
    repositories {
      // ...
      maven { url "https://jitpack.io" }
    }
    ```

2. Add the library to your dependencies:
    ```Gradle
    dependencies {
      // ...
      compile 'com.plusCubed:recycler-fast-scroll:{latest-version}'
    }
    ```

Originally based off of [danoz73/RecyclerViewFastScroller](https://github.com/danoz73/RecyclerViewFastScroller).
