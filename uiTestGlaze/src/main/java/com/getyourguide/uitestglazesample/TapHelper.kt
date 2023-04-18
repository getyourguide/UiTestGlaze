package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice

internal class TapHelper(private val config: UiTestGlaze.Config) {

    fun tap(
        uiElementIdentifier: UiElementIdentifier,
        optional: Boolean,
        retryCount: Int,
        longPress: Boolean,
        hierarchy: TreeNode,
        device: UiDevice
    ) {
        val foundUiElement =
            FindUiElementHelper.getUiElement(
                uiElementIdentifier,
                hierarchy,
                optional,
                device,
                config.timeoutToGetAnUiElement
            ) ?: return
        tapOnTreeNode(foundUiElement, optional, retryCount, longPress, device)
    }

    private fun tapOnTreeNode(
        uiElement: UiElement,
        optional: Boolean,
        retryCount: Int,
        longPress: Boolean,
        device: UiDevice
    ) {
        tap(
            uiElement.x + (uiElement.width) / 2,
            uiElement.y + (uiElement.height) / 2,
            optional,
            retryCount,
            longPress,
            device
        )
    }


    private fun tap(
        x: Int,
        y: Int,
        optional: Boolean,
        retryCount: Int,
        longPress: Boolean,
        device: UiDevice
    ) {
        var currentTry = 1
        while (currentTry <= retryCount) {
            if (tap(x, y, longPress, device)) {
                return
            } else {
                Thread.sleep(200)
                currentTry++
            }
        }
        if (!optional) {
            throw IllegalStateException("Couldn't tap element at position: x:$x y:$y")
        }
    }

    private fun tap(x: Int, y: Int, longPress: Boolean, device: UiDevice): Boolean {
        val hierarchyBeforeTap = GetHierarchyHelper.getHierarchy(device)

        if (longPress) {
            device.swipe(x, y, x, y, 200)
        } else {
            device.click(x, y)
        }

        val hierarchyAfterTap =
            HierarchySettleHelper.waitTillHierarchySettles(
                config.loadingResourceIds,
                device,
                config.waitTillLoadingViewsGoneTimeout,
                config.waitTillHierarchySettlesTimeout,
                config.timeoutToGetAnUiElement
            )

        return hierarchyAfterTap != hierarchyBeforeTap
    }
}
