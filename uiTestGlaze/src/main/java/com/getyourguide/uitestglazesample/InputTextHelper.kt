package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector

internal class InputTextHelper(
    private val getHierarchyHelper: GetHierarchyHelper,
    private val findUiElementHelper: FindUiElementHelper,
) {

    fun inputText(
        text: String,
        uiElementIdentifier: UiElementIdentifier,
        device: UiDevice,
        hierarchy: TreeNode,
        numberOfRetries: Int,
    ) {
        var isEnteringTextSuccessfully: Boolean
        var currentHierarchy = hierarchy
        var currentTry = 0

        do {
            val foundUiElement =
                findUiElementHelper.getUiElement(
                    uiElementIdentifier,
                    currentHierarchy,
                    false,
                    device,
                )
                    ?: throw IllegalStateException("Can not find UiElement to enter text")

            if (foundUiElement.text == text) {
                return
            }
            isEnteringTextSuccessfully = when (uiElementIdentifier) {
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
                    if (foundUiElement.resourceId == null) {
                        throw IllegalStateException("ResourceId not available to enter text")
                    }
                    device.findObject(
                        UiSelector().resourceId(foundUiElement.resourceId)
                            .instance(uiElementIdentifier.index),
                    ).setText(text)
                }

                is UiElementIdentifier.Text,
                is UiElementIdentifier.TextResource,
                is UiElementIdentifier.TextRegex,
                -> {
                    if (foundUiElement.text == null) {
                        throw IllegalStateException("Text not available to enter text")
                    }
                    device.findObject(
                        UiSelector().text(foundUiElement.text).instance(uiElementIdentifier.index),
                    ).setText(text)
                }
            }
            if (!isEnteringTextSuccessfully) {
                currentHierarchy = getHierarchyHelper.getHierarchy(device)
                currentTry++
            }
        } while (!isEnteringTextSuccessfully && currentTry < numberOfRetries)

        if (!isEnteringTextSuccessfully) {
            throw IllegalStateException("Can not enter text")
        }
    }

    private fun enterText(
        inputIndicatorText: Boolean,
        uiElementText: String?,
        uiElementResId: String?,
        device: UiDevice,
        text: String,
    ): Boolean {
        return if (inputIndicatorText) {
            if (uiElementText == null) {
                throw IllegalStateException("Can not find text to enter text")
            }
            device.findObject(UiSelector().text(uiElementText)).setText(text)
        } else {
            if (uiElementResId == null) {
                throw IllegalStateException("Can not find resourceId to enter text")
            }
            device.findObject(UiSelector().resourceId(uiElementResId)).setText(text)
        }
    }
}
