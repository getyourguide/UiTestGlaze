package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector

internal object InputTextHelper {

    fun inputText(text: String, uiElement: UiElement, device: UiDevice) {
        val hierarchy = GetHierarchyHelper.getHierarchy(device)
        val foundUiElement = FindUiElementHelper.getUiElement(uiElement, hierarchy, false, device)
            ?: throw IllegalStateException("Can not find UiElement to enter text")

        when (uiElement) {
            is UiElement.ChildFrom ->
                if (uiElement.inputIndicatorText) {
                    if (foundUiElement.text == null) {
                        throw IllegalStateException("Can not find text to enter text")
                    }
                    UiSelector().text(
                        foundUiElement.text
                    )
                } else {
                    if (foundUiElement.resourceId == null) {
                        throw IllegalStateException("Can not find resourceId to enter text")
                    }
                    device.findObject(UiSelector().text(foundUiElement.resourceId)).text =
                        text
                }

            is UiElement.Id -> {
                device.findObject(UiSelector().text(foundUiElement.resourceId)).text = text
            }

            is UiElement.Text,
            is UiElement.TextResource,
            is UiElement.TextRegex -> {
                device.findObject(UiSelector().text(foundUiElement.text)).text = text
            }
        }
    }
}
