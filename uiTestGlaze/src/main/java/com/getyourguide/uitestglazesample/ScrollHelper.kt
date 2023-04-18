package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice

object ScrollHelper {

    fun scroll(scrollOption: ScrollOption, device: UiDevice) {
        when (scrollOption) {
            is ScrollOption.Manual -> {
                scrollManual(scrollOption, device.displayWidth, device.displayHeight, device)
            }

            is ScrollOption.VerticalDownToElement -> {
                scrollVerticalDownToElement(
                    scrollOption.toUiElement,
                    scrollOption.inUiElement,
                    device
                )
            }

            is ScrollOption.HorizontalRight -> {
                scrollHorizontalRight(device, scrollOption.inUiElement)
            }

            is ScrollOption.VerticalDown -> {
                scrollVerticalDown(device, scrollOption.inUiElement)
            }

            is ScrollOption.HorizontalRightToElement -> scrollHorizontalRightToElement(
                scrollOption.toUiElement,
                scrollOption.inUiElement,
                device
            )
        }
    }

    private fun scrollHorizontalRightToElement(
        toUiElement: UiElementIdentifier,
        inUiElement: UiElementIdentifier,
        device: UiDevice
    ) {
        scroll(device, inUiElement, toUiElement) {
            scrollHorizontalRight(device, inUiElement)
        }
    }

    private fun scrollVerticalDown(device: UiDevice, inUiElement: UiElementIdentifier) {
        val foundInUIElement = FindUiElementHelper.getUiElement(
            inUiElement,
            GetHierarchyHelper.getHierarchy(device),
            true,
            device
        ) ?: throw IllegalStateException("Could not find element to scroll in")
        val startAndEndXPosition = foundInUIElement.x + foundInUIElement.width / 2
        val startYPosition = foundInUIElement.y + foundInUIElement.height * 0.7
        val endYPosition = foundInUIElement.y + foundInUIElement.height * 0.3
        device.executeShellCommand("input swipe $startAndEndXPosition $startYPosition $startAndEndXPosition $endYPosition")
    }

    private fun scrollHorizontalRight(device: UiDevice, inUiElement: UiElementIdentifier) {
        val foundInUIElement = FindUiElementHelper.getUiElement(
            inUiElement,
            GetHierarchyHelper.getHierarchy(device),
            true,
            device
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
        device: UiDevice
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
        toUiElement: UiElementIdentifier,
        inUiElement: UiElementIdentifier,
        device: UiDevice
    ) {
        scroll(device, inUiElement, toUiElement) {
            scrollVerticalDown(device, inUiElement)
        }
    }

    private fun scroll(
        device: UiDevice,
        inUiElement: UiElementIdentifier,
        toUiElement: UiElementIdentifier,
        scrollDirection: () -> Unit
    ) {
        val hierarchy = GetHierarchyHelper.getHierarchy(device)
        FindUiElementHelper.getUiElement(inUiElement, hierarchy, true, device)
            ?: throw IllegalStateException("Could not find element to scroll in")

        val uiElement = FindUiElementHelper.getUiElement(toUiElement, hierarchy, true, device)
        if (uiElement == null || uiElement.height == 0) {
            var toFindUiElement: UiElement? = null
            while (toFindUiElement == null || toFindUiElement.height == 0) {
                val hierarchyBeforeScroll = GetHierarchyHelper.getHierarchy(device)
                scrollDirection()
                HierarchySettleHelper.waitTillHierarchySettles(emptyList(), device)
                if (hierarchyBeforeScroll == GetHierarchyHelper.getHierarchy(device)) {
                    throw IllegalStateException("Could not find element to scroll to")
                }
                toFindUiElement =
                    FindUiElementHelper.getUiElement(
                        toUiElement,
                        GetHierarchyHelper.getHierarchy(device),
                        true,
                        device
                    )
            }
        }
    }
}
