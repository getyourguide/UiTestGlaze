package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice
import kotlin.time.Duration

internal class HierarchySettleHelper(
    private val getHierarchyHelper: GetHierarchyHelper,
    private val findUiElementHelper: FindUiElementHelper,
    private val logger: Logger,
) {

    fun waitTillHierarchySettles(
        loadingResourceIds: List<UiTestGlaze.Config.LoadingResource>,
        device: UiDevice,
        waitTillLoadingViewsGoneTimeout: Duration,
        waitTillHierarchySettlesTimeout: Duration,
    ): TreeNode {
        Thread.sleep(200)
        var hierarchy: TreeNode = getHierarchyHelper.getHierarchy(device)
        val startTimeLoadingViews = System.currentTimeMillis()
        do {
            val isLoadingViewShown = loadingResourceIds.asSequence()
                .mapNotNull {
                    findUiElementHelper.getUiElement(
                        getUiElementIdentifier(it),
                        hierarchy,
                        true,
                        device,
                    )
                }.any()
            if (isLoadingViewShown) {
                logger.i("waitTillHierarchySettles loading view is shown")
                Thread.sleep(200)
                hierarchy = getHierarchyHelper.getHierarchy(device)
            }
        } while (isLoadingViewShown && (System.currentTimeMillis() - startTimeLoadingViews) < waitTillLoadingViewsGoneTimeout.inWholeMilliseconds)

        if ((System.currentTimeMillis() - startTimeLoadingViews) >= waitTillLoadingViewsGoneTimeout.inWholeMilliseconds) {
            throw IllegalStateException("Timeout hit while waiting for loading view to disappear")
        }

        Thread.sleep(200)
        val startTimeHierarchySettle = System.currentTimeMillis()
        do {
            val hierarchyAfter = getHierarchyHelper.getHierarchy(device)
            if (hierarchyAfter == hierarchy) {
                return hierarchyAfter
            }
            logger.i("waitTillHierarchySettles check if hierarchy is the same not same try again")
            Thread.sleep(200)
            hierarchy = getHierarchyHelper.getHierarchy(device)
            Thread.sleep(200)
        } while ((System.currentTimeMillis() - startTimeHierarchySettle) < waitTillHierarchySettlesTimeout.inWholeMilliseconds)
        throw IllegalStateException("Timeout hit while waiting for hierarchy to settle")
    }

    private fun getUiElementIdentifier(loadingResource: UiTestGlaze.Config.LoadingResource): UiElementIdentifier {
        return when (loadingResource) {
            is UiTestGlaze.Config.LoadingResource.IdResource -> {
                UiElementIdentifier.Id(loadingResource.id)
            }

            is UiTestGlaze.Config.LoadingResource.TestTagResource -> {
                UiElementIdentifier.TestTag(loadingResource.testTag)
            }
        }
    }
}
