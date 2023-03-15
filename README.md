# UiTestGlaze 🍰
Easy-to-write and solid Android UI tests

## How to
Here are some example how to use UiTestGlaze

### Tap
Tap on an element with the text `"Click me"`:

```kotlin
UiTestGlaze().tap(UiElement.Text("Click me"))
```

or with the id `R.id.click_me`

```kotlin
UiTestGlaze().tap(UiElement.Id(R.id.click_me))
```
### Scroll
Scroll vertically inside a recycler list with id `R.id.recycler_list`:

```kotlin
UiTestGlaze().scroll(ScrollOption.Vertical(UiElement.Id(R.id.recycler_list)))
```

Scroll vertically to the UI element with text `"Find me!"`:

```kotlin
UiTestGlaze().scroll(ScrollOption.VerticalToElement(toUiElement = UiElement.Text("Find me!"), inUiElementId = UiElement.Id(R.id.recycler_list)))
```

### Assert
Assert view with id `R.id.visible` is visible:

```kotlin
UiTestGlaze().assert(assertion = Assertion.Visible(UiElement.Id(R.id.visible)), optional = false)
```

Assert view with id `R.id.visible` is not visible:

```kotlin
UiTestGlaze().assert(assertion = Assertion.NotVisible(UiElement.Id(R.id.visible)), optional = false)
```

### Enter text
Enter `"Hello World!"` into a text field with id `R.id.text_field`:

```kotlin
UiTestGlaze().inputText(text = "Hello World!", uiElement = UiElement.Id(R.id.text_field))
```

### Press Key
Press a back key:

```kotlin
UiTestGlaze().pressKey(PressKey.Back)
```

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
