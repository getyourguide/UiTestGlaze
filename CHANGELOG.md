# Change Log

[//]: # (https://keepachangelog.com/en/1.1.0/)

## [Unreleased]

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

[unreleased]: https://github.com/getyourguide/UiTestGlaze/compare/0.4.0...HEAD
[0.4.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.4.0
[0.3.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.3.0
[0.2.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.2.0
[0.1.0]: https://github.com/getyourguide/UiTestGlaze/releases/tag/0.1.0
