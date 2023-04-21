# Change Log

[//]: # (https://keepachangelog.com/en/1.1.0/)

## [Unreleased]

[1.0.1] - 2023-04-21

Changed:
- `LoadingResource` inside `Config` is now a sealed interface.
It's now possible to find a loading view with a test tag.

Added:
- `tap` function with raw x and y position to tap on a specific position on the screen

[1.0.0] - 2023-04-18

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

[0.11.0] - 2023-04-05

Fixed:
- `TreeNode` inside `FoundUiElement` was always null

[0.10.0] - 2023-04-04

Fixed:
- When using `UiElement.Id` for `input` `UiSelector` was using the wrong `text` method instead of `resourceId` to find an UiElement

[0.9.0] - 2023-03-23

Added:
- Find UI element with a string resource id `UiElement.TextResource`
- Implemented `UiElement.TextRegex` find a UI element text with a Regex
- Implemented `UiElement.ChildFrom` to find a UI element inside a UI element
- Implemented `UiElement.TestTag` to find a UI element (especially Jetpack Compose elements) with a
  test tag

Changed:
- Rename `caseSensitive` property inside `UiElement.Text` to `ignoreCase`
- Made the `optional` boolean property inside `assert()` optional

[0.8.0] - 2023-03-20

Added:
- `find` to get an UiElement
- Added `text`, `clickable`, `checked` and `enabled` properties to `FoundUiElement`

[0.7.0] - 2023-03-20

Fixed:
- Input text wasn't entering the text in the correct field when there are multiple input fields with the same id

[0.6.0] - 2023-03-17

Changed:
- Decrease timeout when checking if hierarchy has settled
- Use sequence to check if a loading view is shown to only evaluate list till first not null item is found

[0.5.0] - 2023-03-17

Fixed:
- Repeat block when getting an UI element was not break out early

[0.4.0] - 2023-03-17

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

[unreleased]: https://github.com/getyourguide/UiTestGlaze/compare/1.0.1...HEAD
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
