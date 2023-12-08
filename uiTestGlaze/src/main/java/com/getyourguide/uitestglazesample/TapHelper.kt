package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice

internal class TapHelper(
    private val config: UiTestGlaze.Config,
    private val findUiElementHelper: FindUiElementHelper,
    private val hierarchySettleHelper: HierarchySettleHelper,
) {

    fun tap(
        uiElementIdentifier: UiElementIdentifier,
        optional: Boolean,
        retryCount: Int,
        longPress: Boolean,
        offsetX: Int,
        offsetY: Int,
        device: UiDevice,
    ) {
        var currentRetry = 0
        var hierarchy: TreeNode?
        var hierarchyAfterTap: TreeNode?
        do {
            hierarchy =
                hierarchySettleHelper.waitTillHierarchySettles(
                    config.loadingResourceIds,
                    device,
                    config.waitTillLoadingViewsGoneTimeout,
                    config.waitTillHierarchySettlesTimeout,
                )

            val foundUiElement =
                findUiElementHelper.getUiElement(
                    uiElementIdentifier,
                    hierarchy,
                    optional,
                    device,
                ) ?: return

            tapOnTreeNode(foundUiElement, longPress, offsetX, offsetY, device)

            hierarchyAfterTap =
                hierarchySettleHelper.waitTillHierarchySettles(
                    emptyList(),
                    device,
                    config.waitTillLoadingViewsGoneTimeout,
                    config.waitTillHierarchySettlesTimeout,
                )
            currentRetry++
        } while (hierarchy == hierarchyAfterTap && currentRetry <= retryCount)

        if (hierarchy == hierarchyAfterTap && !optional) {
            throw IllegalStateException("Couldn't tap element $uiElementIdentifier")
        }
    }

    private fun tapOnTreeNode(
        uiElement: UiElement,
        longPress: Boolean,
        offsetX: Int,
        offsetY: Int,
        device: UiDevice,
    ) {
        tap(
            uiElement.x + (uiElement.width) / 2 + offsetX,
            uiElement.y + (uiElement.height) / 2 + offsetY,
            longPress,
            device,
        )
    }

    fun tap(x: Int, y: Int, longPress: Boolean, device: UiDevice) {
        if (longPress) {
            device.swipe(x, y, x, y, 200)
        } else {
            device.click(x, y)
        }
    }
}
