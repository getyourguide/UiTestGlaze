package com.getyourguide.uitestglaze

import androidx.test.uiautomator.UiDevice

object ScrollHelper {

    fun scroll(scrollOption: ScrollOption, device: UiDevice) {
        when (scrollOption) {
            is ScrollOption.Manual -> {
                scrollManual(scrollOption, device.displayWidth, device.displayHeight, device)
            }
            is ScrollOption.VerticalToElement -> {
                scrollVerticalToElement(
                    scrollOption.toUiElement,
                    scrollOption.inUiElementId,
                    device
                )
            }
            is ScrollOption.Horizontal -> {
                scrollHorizontal(device, scrollOption.inUiElementId)
            }
            is ScrollOption.Vertical -> {
                scrollVertical(device, scrollOption.inUiElementId)
            }
            is ScrollOption.HorizontalToElement -> scrollHorizontalToElement(
                scrollOption.toUiElement,
                scrollOption.inUiElementId,
                device
            )
        }
    }

    private fun scrollHorizontalToElement(
        toUiElement: UiElement,
        inUiElementId: UiElement.Id,
        device: UiDevice
    ) {
        scroll(device, inUiElementId, toUiElement) {
            scrollHorizontal(device, inUiElementId)
        }
    }

    private fun scrollVertical(device: UiDevice, inUiElementId: UiElement.Id) {
        val foundInUIElement = FindUiElementHelper.getUiElement(
            inUiElementId,
            GetHierarchyHelper.getHierarchy(device),
            true,
            device
        ) ?: throw IllegalStateException("Could not find element to scroll in")
        val startAndEndXPosition = foundInUIElement.x + foundInUIElement.width / 2
        val startYPosition = foundInUIElement.y + foundInUIElement.height * 0.7
        val endYPosition = foundInUIElement.y + foundInUIElement.height * 0.3
        device.executeShellCommand("input swipe $startAndEndXPosition $startYPosition $startAndEndXPosition $endYPosition")
    }

    private fun scrollHorizontal(device: UiDevice, inUiElementId: UiElement.Id) {
        val foundInUIElement = FindUiElementHelper.getUiElement(
            inUiElementId,
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

    private fun scrollVerticalToElement(
        toUiElement: UiElement,
        inUiElementId: UiElement.Id,
        device: UiDevice
    ) {
        scroll(device, inUiElementId, toUiElement) {
            scrollVertical(device, inUiElementId)
        }
    }

    private fun scroll(
        device: UiDevice,
        inUiElementId: UiElement.Id,
        toUiElement: UiElement,
        scrollDirection: () -> Unit
    ) {
        val hierarchy = GetHierarchyHelper.getHierarchy(device)
        FindUiElementHelper.getUiElement(inUiElementId, hierarchy, true, device)
            ?: throw IllegalStateException("Could not find element to scroll in")

        val uiElement = FindUiElementHelper.getUiElement(toUiElement, hierarchy, true, device)
        if (uiElement == null || uiElement.height == 0) {
            var toFindUiElement: FoundUiElement? = null
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
