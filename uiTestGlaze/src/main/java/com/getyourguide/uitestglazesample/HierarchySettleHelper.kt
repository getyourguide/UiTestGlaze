package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice

internal object HierarchySettleHelper {

    private const val MAX_RETRIES = 30

    fun waitTillHierarchySettles(
        loadingResourceIds: List<UiTestGlaze.Config.IdResource>,
        device: UiDevice
    ): TreeNode {
        Thread.sleep(300)
        var currentTry = 0
        var hierarchy: TreeNode = GetHierarchyHelper.getHierarchy(device)

        do {
            val isLoadingViewShown = loadingResourceIds.mapNotNull {
                FindUiElementHelper.getUiElement(
                    UiElement.Id(it.id),
                    hierarchy,
                    true,
                    device
                )
            }.isNotEmpty()
            if (isLoadingViewShown) {
                Logger.i("waitTillHierarchySettles loading view is shown")
                currentTry++
                Thread.sleep(200)
                hierarchy = GetHierarchyHelper.getHierarchy(device)
            }
        } while (isLoadingViewShown && currentTry < MAX_RETRIES)

        if (currentTry == MAX_RETRIES) {
            throw IllegalStateException("Timeout hit while waiting for loading view to disappear")
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
