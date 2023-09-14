package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice
import kotlin.time.Duration.Companion.seconds

internal class ScrollHelper(
    private val findUiElementHelper: FindUiElementHelper,
    private val getHierarchyHelper: GetHierarchyHelper,
    private val hierarchySettleHelper: HierarchySettleHelper,
) {

    fun scroll(scrollOption: ScrollOption, device: UiDevice, config: UiTestGlaze.Config) {
        when (scrollOption) {
            is ScrollOption.Manual -> {
                scrollManual(scrollOption, device.displayWidth, device.displayHeight, device)
            }

            is ScrollOption.VerticalDownToElement -> {
                scrollVerticalDownToElement(
                    config,
                    scrollOption.toUiElement,
                    scrollOption.inUiElement,
                    device,
                )
            }

            is ScrollOption.VerticalUpToElement -> {
                scrollVerticalUpToElement(
                    config,
                    scrollOption.toUiElement,
                    scrollOption.inUiElement,
                    device,
                )
            }

            is ScrollOption.HorizontalRight -> {
                scrollHorizontalRight(
                    device,
                    scrollOption.inUiElement,
                )
            }

            is ScrollOption.VerticalDown -> {
                scrollVerticalDown(device, scrollOption.inUiElement)
            }

            is ScrollOption.VerticalUp -> {
                scrollVerticalUp(device, scrollOption.inUiElement)
            }

            is ScrollOption.HorizontalRightToElement -> scrollHorizontalRightToElement(
                config,
                scrollOption.toUiElement,
                scrollOption.inUiElement,
                device,
            )
        }
    }

    private fun scrollHorizontalRightToElement(
        config: UiTestGlaze.Config,
        toUiElement: UiElementIdentifier,
        inUiElement: UiElementIdentifier,
        device: UiDevice,
    ) {
        scroll(config, device, inUiElement, toUiElement) {
            scrollHorizontalRight(device, inUiElement)
        }
    }

    private fun scrollVerticalDown(
        device: UiDevice,
        inUiElement: UiElementIdentifier,
    ) {
        val foundInUIElement = findUiElementHelper.getUiElement(
            inUiElement,
            getHierarchyHelper.getHierarchy(device),
            true,
            device,
        ) ?: throw IllegalStateException("Could not find element to scroll in")
        val startAndEndXPosition = foundInUIElement.x + foundInUIElement.width / 2
        val startYPosition = foundInUIElement.y + foundInUIElement.height * 0.7
        val endYPosition = foundInUIElement.y + foundInUIElement.height * 0.3
        device.executeShellCommand("input swipe $startAndEndXPosition $startYPosition $startAndEndXPosition $endYPosition")
    }

    private fun scrollVerticalUp(
        device: UiDevice,
        inUiElement: UiElementIdentifier,
    ) {
        val foundInUIElement = findUiElementHelper.getUiElement(
            inUiElement,
            getHierarchyHelper.getHierarchy(device),
            true,
            device,
        ) ?: throw IllegalStateException("Could not find element to scroll in")
        val startAndEndXPosition = foundInUIElement.x + foundInUIElement.width / 2
        val startYPosition = foundInUIElement.y + foundInUIElement.height * 0.3
        val endYPosition = foundInUIElement.y + foundInUIElement.height * 0.7
        device.executeShellCommand("input swipe $startAndEndXPosition $startYPosition $startAndEndXPosition $endYPosition")
    }

    private fun scrollHorizontalRight(
        device: UiDevice,
        inUiElement: UiElementIdentifier,
    ) {
        val foundInUIElement = findUiElementHelper.getUiElement(
            inUiElement,
            getHierarchyHelper.getHierarchy(device),
            true,
            device,
        ) ?: throw IllegalStateException("Could not find element to scroll in")
        val startAndEndYPosition = foundInUIElement.y + foundInUIElement.height / 2
        val startXPosition = foundInUIElement.x + foundInUIElement.width * 0.7
        val endXPosition = foundInUIElement.x + foundInUIElement.width * 0.3
        device.executeShellCommand("input swipe $startXPosition $startAndEndYPosition $endXPosition $startAndEndYPosition")
    }

    private fun scrollManual(
        scrollOption: ScrollOption.Manual,
        displayWidth: Int,
        displayHeight: Int,
        device: UiDevice,
    ) {
        if (scrollOption.startX > displayWidth ||
            scrollOption.startY > displayHeight ||
            scrollOption.endX > displayWidth ||
            scrollOption.endY > displayHeight ||
            scrollOption.startX < 0 ||
            scrollOption.startY < 0 ||
            scrollOption.endX < 0 ||
            scrollOption.endY < 0
        ) {
            throw IllegalArgumentException("Scroll coordinates are out of screen bounds")
        }
        device.executeShellCommand("input swipe ${scrollOption.startX} ${scrollOption.startY} ${scrollOption.endX} ${scrollOption.endY}")
    }

    private fun scrollVerticalDownToElement(
        config: UiTestGlaze.Config,
        toUiElement: UiElementIdentifier,
        inUiElement: UiElementIdentifier,
        device: UiDevice,
    ) {
        scroll(config, device, inUiElement, toUiElement) {
            scrollVerticalDown(device, inUiElement)
        }
    }

    private fun scrollVerticalUpToElement(
        config: UiTestGlaze.Config,
        toUiElement: UiElementIdentifier,
        inUiElement: UiElementIdentifier,
        device: UiDevice,
    ) {
        scroll(config, device, inUiElement, toUiElement) {
            scrollVerticalUp(device, inUiElement)
        }
    }

    private fun scroll(
        config: UiTestGlaze.Config,
        device: UiDevice,
        inUiElement: UiElementIdentifier,
        toUiElement: UiElementIdentifier,
        scrollDirection: () -> Unit,
    ) {
        val hierarchy = getHierarchyHelper.getHierarchy(device)
        findUiElementHelper.getUiElement(
            inUiElement,
            hierarchy,
            true,
            device,
        )
            ?: throw IllegalStateException("Could not find element to scroll in")

        val uiElement = findUiElementHelper.getUiElement(
            toUiElement,
            hierarchy,
            true,
            device,
        )
        if (uiElement == null || uiElement.height == 0) {
            var toFindUiElement: UiElement? = null
            while (toFindUiElement == null || toFindUiElement.height == 0) {
                val hierarchyBeforeScroll = getHierarchyHelper.getHierarchy(device)
                scrollDirection()
                val startTime = System.currentTimeMillis()
                var hierarchyAfterScroll: TreeNode?
                do {
                    hierarchySettleHelper.waitTillHierarchySettles(
                        emptyList(),
                        device,
                        config.waitTillLoadingViewsGoneTimeout,
                        config.waitTillHierarchySettlesTimeout,
                    )
                    hierarchyAfterScroll = getHierarchyHelper.getHierarchy(device)

                } while (hierarchyBeforeScroll == hierarchyAfterScroll &&
                    (System.currentTimeMillis() - startTime) < 3.seconds.inWholeMilliseconds
                )
                if (hierarchyBeforeScroll == hierarchyAfterScroll || hierarchyAfterScroll == null) {
                    throw IllegalStateException("Could not find element to scroll to")
                }
                toFindUiElement = findUiElementHelper.getUiElement(
                    toUiElement,
                    hierarchyAfterScroll,
                    true,
                    device,
                    3.seconds,
                )
            }
        }
    }
}
