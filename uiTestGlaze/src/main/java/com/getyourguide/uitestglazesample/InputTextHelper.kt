package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import kotlin.time.Duration

internal object InputTextHelper {

    fun inputText(
        text: String,
        uiElementIdentifier: UiElementIdentifier,
        device: UiDevice,
        timeoutToGetAnUiElement: Duration
    ) {
        val hierarchy = GetHierarchyHelper.getHierarchy(device)
        val foundUiElement =
            FindUiElementHelper.getUiElement(
                uiElementIdentifier,
                hierarchy,
                false,
                device,
                timeoutToGetAnUiElement
            )
                ?: throw IllegalStateException("Can not find UiElement to enter text")

        when (uiElementIdentifier) {
            is UiElementIdentifier.PositionInHierarchy ->
                enterText(
                    uiElementIdentifier.inputIndicatorText,
                    foundUiElement.text,
                    foundUiElement.resourceId,
                    device,
                    text
                )

            is UiElementIdentifier.ChildFrom ->
                enterText(
                    uiElementIdentifier.inputIndicatorText,
                    foundUiElement.text,
                    foundUiElement.resourceId,
                    device,
                    text
                )

            is UiElementIdentifier.Id,
            is UiElementIdentifier.TestTag -> {
                device.findObject(UiSelector().resourceId(foundUiElement.resourceId)).text = text
            }

            is UiElementIdentifier.Text,
            is UiElementIdentifier.TextResource,
            is UiElementIdentifier.TextRegex -> {
                device.findObject(UiSelector().text(foundUiElement.text)).text = text
            }
        }
    }

    private fun enterText(
        inputIndicatorText: Boolean,
        uiElementText: String?,
        uiElementResId: String?,
        device: UiDevice,
        text: String
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
