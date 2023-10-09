# Change Log

[//]: # (https://keepachangelog.com/en/1.1.0/)

## [Unreleased]

# [1.1.5] - 2023-10-09

Added:
- `Class' attribute to better understand the view hierarchy

# [1.1.4] - 2023-09-25

Fixed:
- When entering text with an `UiElementIdentifier` with index. Index was not used.

# [1.1.3] - 2023-09-14

Added:
- Option to scroll vertical up to an element `ScrollOption.VerticalUpToElement`
- Option to scroll vertical up `ScrollOption.VerticalUp`

# [1.1.2] - 2023-09-13

Changed:
- Scrolling has a timeout to check if view hierarchy has changed

# [1.1.1] - 2023-09-12

Added:
- Tap can now have an offset

Changed:
- Assertions now have a default timeout of 3 seconds to find an UI element

## [1.1.0] - 2023-08-16

Added:
- Assertion method which accepts a list of assertions

Changed:
- Assertions don't return a boolean anymore. Instead they will fail when they are not met. They also don't accept a boolean anymore if they are optional or not
- Config field `timeoutToGetAnUiElement` is removed. An UiElement will be found immediately or the test will fail
- `inputText()` method now accepts a timeout how long it should wait for the text to appear after input

## [1.0.2] - 2023-08-15

Added:
- Added spotless to the project to format the code
- Added a workflow to check if the library is still building

Changed:
- Updated Kotlin to 1.9.0
- Update gradlew wrapper to 8.2.1

## [1.0.1] - 2023-04-21

Changed:
- `LoadingResource` inside `Config` is now a sealed interface.
It's now possible to find a loading view with a test tag.

Added:
- `tap` function with raw x and y position to tap on a specific position on the screen

## [1.0.0] - 2023-04-18

Added:
- `UiElementIdentifier.PositionInHierarchy` to select an element with the position
- `waitTillLoadingViewsGoneTimeout` to the config
- `waitTillHierarchySettlesTimeout` to the config
- `timeoutToGetAnUiElement` to the config
- Documentation!
- `Assertion.Checked` and `Assertion.NotChecked` to check if a checkbox/switch is checked or not
- `Assertion.Enabled` and `Assertion.NotEnabled` to check if a view is enabled or not
- Option to provide an own way to log stuff

Changed:
- Filter out empty children with no `Attribute.TEXT` or `Attribute.RESOURCE_ID` in ViewHierarchy
- Allow all `UiElement` options to be used for `ScrollOption`
- Rename `UiElement` to `UiElementIdentifier`
- Rename `FoundUiElement` to `UiElement`

## [0.11.0] - 2023-04-05

Fixed:
- `TreeNode` inside `FoundUiElement` was always null

## [0.10.0] - 2023-04-04

Fixed:
- When using `UiElement.Id` for `input` `UiSelector` was using the wrong `text` method instead of `resourceId` to find an UiElement

## [0.9.0] - 2023-03-23

Added:
- Find UI element with a string resource id `UiElement.TextResource`
- Implemented `UiElement.TextRegex` find a UI element text with a Regex
- Implemented `UiElement.ChildFrom` to find a UI element inside a UI element
- Implemented `UiElement.TestTag` to find a UI element (especially Jetpack Compose elements) with a
  test tag

Changed:
- Rename `caseSensitive` property inside `UiElement.Text` to `ignoreCase`
- Made the `optional` boolean property inside `assert()` optional

## [0.8.0] - 2023-03-20

Added:
- `find` to get an UiElement
- Added `text`, `clickable`, `checked` and `enabled` properties to `FoundUiElement`

## [0.7.0] - 2023-03-20

Fixed:
- Input text wasn't entering the text in the correct field when there are multiple input fields with the same id

## [0.6.0] - 2023-03-17

Changed:
- Decrease timeout when checking if hierarchy has settled
- Use sequence to check if a loading view is shown to only evaluate list till first not null item is found

## [0.5.0] - 2023-03-17

Fixed:
- Repeat block when getting an UI element was not break out early

## [0.4.0] - 2023-03-17

Fixed:
- When loading view was detekt we entered an endless loop

Changed:
- Added a MAX_TRIES constant to the HierarchySettleHelper so we can time out after a while
- Added a MAX_TRIES constant to the GetHierarchyHelper so we can time out after a while

## [0.3.0] - 2023-03-17

Added:
- Possibility to dump the view hierarchy immediately or after the view hierarchy has settled


Changed:
- Renamed the ScrollOption options to better clarify the scroll direction

## [0.2.0] - 2023-03-16

New:

- Publishing is now automated via GitHub Actions

## [0.1.0] - 2023-03-16

Initial release.

[unreleased]: https://github.com/getyourguide/UiTestGlaze/compare/1.1.5...HEAD
[1.1.5]: https://github.com/getyourguide/UiTestGlaze/releases/tag/1.1.5
[1.1.4]: https://github.com/getyourguide/UiTestGlaze/releases/tag/1.1.4
[1.1.3]: https://github.com/getyourguide/UiTestGlaze/releases/tag/1.1.3
[1.1.2]: https://github.com/getyourguide/UiTestGlaze/releases/tag/1.1.2
[1.1.1]: https://github.com/getyourguide/UiTestGlaze/releases/tag/1.1.1
[1.1.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/1.1.0
[1.0.2]: https://github.com/getyourguide/UiTestGlaze/releases/tag/1.0.2
[1.0.1]: https://github.com/getyourguide/UiTestGlaze/releases/tag/1.0.1
[1.0.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/1.0.0
[0.11.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.11.0
[0.10.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.10.0
[0.9.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.9.0
[0.8.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.8.0
[0.7.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.7.0
[0.6.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.6.0
[0.5.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.5.0
[0.4.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.4.0
[0.3.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.3.0
[0.2.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.2.0
[0.1.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.1.0
