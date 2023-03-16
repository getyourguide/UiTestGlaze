package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice

internal object HierarchySettleHelper {

    fun waitTillHierarchySettles(
        loadingResourceIds: List<UiTestGlaze.Config.IdResource>,
        device: UiDevice
    ): TreeNode {
        Thread.sleep(300)
        var hierarchy = GetHierarchyHelper.getHierarchy(device)
        val isLoadingViewShown = loadingResourceIds.mapNotNull {
            FindUiElementHelper.getUiElement(
                UiElement.Id(it.id),
                hierarchy,
                true,
                device
            )
        }.isNotEmpty()
        while (isLoadingViewShown) {
            Logger.i("waitTillHierarchySettles loading detected sleep")
            Thread.sleep(200)
            hierarchy = GetHierarchyHelper.getHierarchy(device)
        }
        Thread.sleep(300)
        repeat(25) {
            val hierarchyAfter = GetHierarchyHelper.getHierarchy(device)
            if (hierarchyAfter == hierarchy) {
                return hierarchyAfter
            }
            Logger.i("waitTillHierarchySettles check if hierarchy is the same not same try again")
            Thread.sleep(200)
            hierarchy = GetHierarchyHelper.getHierarchy(device)
            Thread.sleep(200)
        }
        throw IllegalStateException("Timeout hit while waiting for hierarchy to settle")
    }
}
