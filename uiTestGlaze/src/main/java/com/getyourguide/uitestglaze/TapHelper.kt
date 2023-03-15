package com.getyourguide.uitestglaze

import androidx.test.uiautomator.UiDevice

internal class TapHelper(private val config: UiTestGlaze.Config) {

    fun tap(
        uiElement: UiElement,
        optional: Boolean,
        retryCount: Int,
        longPress: Boolean,
        hierarchy: TreeNode,
        device: UiDevice
    ) {
        val foundUiElement =
            FindUiElementHelper.getUiElement(uiElement, hierarchy, optional, device) ?: return
        tapOnTreeNode(foundUiElement, optional, retryCount, longPress, device)
    }

    private fun tapOnTreeNode(
        foundUiElement: FoundUiElement,
        optional: Boolean,
        retryCount: Int,
        longPress: Boolean,
        device: UiDevice
    ) {
        tap(
            foundUiElement.x + (foundUiElement.width) / 2,
            foundUiElement.y + (foundUiElement.height) / 2,
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
            HierarchySettleHelper.waitTillHierarchySettles(config.loadingResourceIds, device)

        return hierarchyAfterTap != hierarchyBeforeTap
    }
}


internal data class FoundUiElement(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val resourceId: String? = null,
    val text: String? = null
)
