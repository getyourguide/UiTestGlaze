# UiTestGlaze 🍰
Are you tired of dealing with flaky, time-consuming unreadable Android UI tests?

UiTestGlaze offers stable and effortless testing, making the process enjoyable again. With a focus on readability and ease of use, UiTestGlaze is the perfect solution for your UI testing needs. Whether you want to use it for an entire test or just certain parts, UiTestGlaze has you covered. Don't waste any more time on frustrating UI tests.

## Download
Import UiTestGlaze as a testing dependency:

```gradle
androidTestImplementation('io.github.getyourguide:uitestglaze:LATEST_VERSION')
```

## API

### Config
UiTestGlaze accepts a `Config` option. With this config it's possible to define the log level, timeouts and provide loading views. When UiTestGlaze detects a provided loading view it will automatically wait till the loading view is gone. Of course, you can also define a timeout how long UiTestGlaze should wait till an error is thrown. E.g.:

```kotlin
UiTestGlaze(config = UiTestGlaze.Config(
		loadingResourceIds = listOf(R.id.loading_view),
		waitTillLoadingViewsGoneTimeout = 30.seconds
	)
)
```

### Tap
Tap on an element with the text `"Click me"`:

```kotlin
UiTestGlaze().tap(UiElementIdentifier.Text("Click me"))
```

or with the id `R.id.click_me`

```kotlin
UiTestGlaze().tap(UiElementIdentifier.Id(R.id.click_me))
```
It's also possible to use the following *UiElementIdentifier* to find a view:

- **TestTag**. Mainly used to find Jetpack Compose views. Check [this](https://developer.android.com/jetpack/compose/testing#uiautomator-interop) documentation for further information.
- **TextResource**. If you don't want to use hardcoded strings and prefer to use a string resource.
- **TextRegex**. Use Regex to find a view.
- **ChildFrom**. To specify where to search for a view.

### Scroll
Scroll vertically inside a recycler list with id `R.id.recycler_list`:

```kotlin
UiTestGlaze().scroll(ScrollOption.VerticalDown(UiElement.Id(R.id.recycler_list)))
```

Scroll vertically to the UI element with text `"Find me!"`:

```kotlin
UiTestGlaze().scroll(
	ScrollOption.VerticalDownToElement(
		toUiElement = UiElementIdentifier.Text("Find me!"),
		inUiElementId = UiElementIdentifier.Id(R.id.recycler_list))
)
```

*ScrollOption* also provides the following options:

- **HorizontalRight**. For horizontal scrollable views.
- **Manual**. To completely specify how the UI should be scrolled.
- **HorizontalRightToElement**. Scroll horizontally to a given view.

### Assert
Assert view with id `R.id.visible` is visible:

```kotlin
UiTestGlaze().assert(assertion = Assertion.Visible(UiElement.Id(R.id.visible)), optional = false)
```

Assert view with id `R.id.visible` is not visible:

```kotlin
UiTestGlaze().assert(assertion = Assertion.NotVisible(UiElement.Id(R.id.visible)), optional = false)
```

Further *Assertion* options are:

- **Checked**. If a view (e.g., a checkbox) is checked.
- **NotChecked**. If a view (e.g., a checkbox) is not checked. 
- **Enabled**. If a view (e.g., a button) is enabled.
- **NotEnabled**. If a view (e.g., a button) is not enabled.

### Enter text
Enter `"Hello World!"` into a text field with id `R.id.text_field`:

```kotlin
UiTestGlaze().inputText(text = "Hello World!", uiElementIdentifier = UiElementIdentifier.Id(R.id.text_field))
```
The same *UiElementIdentifier* described in the *Tap* section is also useable here.

### Press Key
Press a back key:

```kotlin
UiTestGlaze().pressKey(PressKey.Back)
```
The following keys can be pressed:

- **Enter**
- **Backspace**
- **Home**
- **Lock**
- **VolumeUp**
- **VolumeDown**

### Find a view
It's possible to find a view. The result will contain the UiElement's children, the text, resourceId and more.

```kotlin
UiTestGlaze().find(UiElementIdentifier.Id(R.id.list, true))
```

### Something is missing or not working?
Submit an [Issue](https://github.com/getyourguide/UiTestGlaze/issues/new)!

## License

```
Copyright 2023 GetYourGuide

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
