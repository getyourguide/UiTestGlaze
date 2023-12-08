package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import kotlin.time.Duration

internal class InputTextHelper(
    private val config: UiTestGlaze.Config,
    private val getHierarchyHelper: GetHierarchyHelper,
    private val findUiElementHelper: FindUiElementHelper,
    private val hierarchySettleHelper: HierarchySettleHelper,
    private val printHierarchyHelper: PrintHierarchyHelper
) {

    fun inputText(
        text: String,
        uiElementIdentifier: UiElementIdentifier,
        device: UiDevice,
        inputShouldBeRecognizedTimeout: Duration,
        numberOfRetries: Int,
    ) {
        var currentTry = 0
        while (numberOfRetries >= currentTry) {
            val hierarchy = getHierarchyHelper.getHierarchy(device)
            val foundUiElement =
                findUiElementHelper.getUiElement(
                    uiElementIdentifier,
                    hierarchy,
                    false,
                    device,
                )
                    ?: throw IllegalStateException("Can not find UiElement to enter text")

            if (foundUiElement.text == text) {
                return
            }

            when (uiElementIdentifier) {
                is UiElementIdentifier.PositionInHierarchy ->
                    enterText(
                        uiElementIdentifier.inputIndicatorText,
                        foundUiElement.text,
                        foundUiElement.resourceId,
                        device,
                        text,
                    )

                is UiElementIdentifier.ChildFrom ->
                    enterText(
                        uiElementIdentifier.inputIndicatorText,
                        foundUiElement.text,
                        foundUiElement.resourceId,
                        device,
                        text,
                    )

                is UiElementIdentifier.Id,
                is UiElementIdentifier.TestTag,
                -> {
                    device.findObject(
                        UiSelector().resourceId(foundUiElement.resourceId)
                            .instance(uiElementIdentifier.index),
                    ).text = text
                }

                is UiElementIdentifier.Text,
                is UiElementIdentifier.TextResource,
                is UiElementIdentifier.TextRegex,
                -> {
                    device.findObject(
                        UiSelector().text(foundUiElement.text).instance(uiElementIdentifier.index),
                    ).text = text
                }
            }
            val startTime = System.currentTimeMillis()
            var hierarchyChanged = false
            do {
                val hierarchyAfterEnteringText = hierarchySettleHelper.waitTillHierarchySettles(
                    config.loadingResourceIds,
                    device,
                    config.waitTillLoadingViewsGoneTimeout,
                    config.waitTillHierarchySettlesTimeout,
                )
                printHierarchyHelper.print(hierarchyAfterEnteringText, "After entering text ")
                if (hierarchy != hierarchyAfterEnteringText) {
                    hierarchyChanged = true
                    break
                }
            } while ((System.currentTimeMillis() - startTime) < inputShouldBeRecognizedTimeout.inWholeMilliseconds)

            if (!hierarchyChanged) {
                if (currentTry == numberOfRetries) {
                    throw IllegalStateException("Timeout hit while waiting for text to appear")
                }
                currentTry++
            } else {
                return
            }
        }
    }

    private fun enterText(
        inputIndicatorText: Boolean,
        uiElementText: String?,
        uiElementResId: String?,
        device: UiDevice,
        text: String,
    ) {
        if (inputIndicatorText) {
            if (uiElementText == null) {
                throw IllegalStateException("Can not find text to enter text")
            }
            device.findObject(UiSelector().text(uiElementText)).text = text
        } else {
            if (uiElementResId == null) {
                throw IllegalStateException("Can not find resourceId to enter text")
            }
            device.findObject(UiSelector().resourceId(uiElementResId)).text = text
        }
    }
}
