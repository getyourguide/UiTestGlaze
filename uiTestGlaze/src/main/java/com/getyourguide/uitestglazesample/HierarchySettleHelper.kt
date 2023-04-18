package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice
import kotlin.time.Duration

internal object HierarchySettleHelper {

    fun waitTillHierarchySettles(
        loadingResourceIds: List<UiTestGlaze.Config.IdResource>,
        device: UiDevice,
        waitTillLoadingViewsGoneTimeout: Duration,
        waitTillHierarchySettlesTimeout: Duration,
        timeoutToGetAnUiElement: Duration
    ): TreeNode {
        Thread.sleep(200)
        var currentTry = 0
        var hierarchy: TreeNode = GetHierarchyHelper.getHierarchy(device)
        val startTimeLoadingViews = System.currentTimeMillis()
        do {
            val isLoadingViewShown = loadingResourceIds.asSequence()
                .mapNotNull {
                    FindUiElementHelper.getUiElement(
                        UiElementIdentifier.Id(it.id),
                        hierarchy,
                        true,
                        device,
                        timeoutToGetAnUiElement
                    )
                }.any()
            if (isLoadingViewShown) {
                Logger.i("waitTillHierarchySettles loading view is shown")
                currentTry++
                Thread.sleep(200)
                hierarchy = GetHierarchyHelper.getHierarchy(device)
            }
        } while (isLoadingViewShown && (System.currentTimeMillis() - startTimeLoadingViews) < waitTillLoadingViewsGoneTimeout.inWholeMilliseconds)

        if ((System.currentTimeMillis() - startTimeLoadingViews) >= waitTillLoadingViewsGoneTimeout.inWholeMilliseconds) {
            throw IllegalStateException("Timeout hit while waiting for loading view to disappear")
        }

        Thread.sleep(200)
        val startTimeHierarchySettle = System.currentTimeMillis()
        do {
            val hierarchyAfter = GetHierarchyHelper.getHierarchy(device)
            if (hierarchyAfter == hierarchy) {
                return hierarchyAfter
            }
            Logger.i("waitTillHierarchySettles check if hierarchy is the same not same try again")
            Thread.sleep(200)
            hierarchy = GetHierarchyHelper.getHierarchy(device)
            Thread.sleep(200)
        } while ((System.currentTimeMillis() - startTimeHierarchySettle) < waitTillHierarchySettlesTimeout.inWholeMilliseconds)
        throw IllegalStateException("Timeout hit while waiting for hierarchy to settle")
    }
}
