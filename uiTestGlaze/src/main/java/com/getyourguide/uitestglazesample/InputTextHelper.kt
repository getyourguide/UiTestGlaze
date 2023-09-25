package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import kotlin.time.Duration

internal class InputTextHelper(
    private val getHierarchyHelper: GetHierarchyHelper,
    private val findUiElementHelper: FindUiElementHelper,
) {

    fun inputText(
        text: String,
        uiElementIdentifier: UiElementIdentifier,
        device: UiDevice,
        inputShouldBeRecognizedTimeout: Duration,
    ) {
        val hierarchy = getHierarchyHelper.getHierarchy(device)
        val foundUiElement =
            findUiElementHelper.getUiElement(
                uiElementIdentifier,
                hierarchy,
                false,
                device,
            )
                ?: throw IllegalStateException("Can not find UiElement to enter text")

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
                        .instance(uiElementIdentifier.index)
                ).text = text
            }

            is UiElementIdentifier.Text,
            is UiElementIdentifier.TextResource,
            is UiElementIdentifier.TextRegex,
            -> {
                device.findObject(
                    UiSelector().text(foundUiElement.text).instance(uiElementIdentifier.index)
                ).text = text
            }
        }
        val startTime = System.currentTimeMillis()
        var hierarchyChanged = false
        do {
            val hierarchyAfterEnteringText = getHierarchyHelper.getHierarchy(device)
            if (hierarchy != hierarchyAfterEnteringText) {
                hierarchyChanged = true
                break
            }
        } while ((System.currentTimeMillis() - startTime) < inputShouldBeRecognizedTimeout.inWholeMilliseconds)
        if (!hierarchyChanged) {
            throw IllegalStateException("Timeout hit while waiting for hierarchy to settle")
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
