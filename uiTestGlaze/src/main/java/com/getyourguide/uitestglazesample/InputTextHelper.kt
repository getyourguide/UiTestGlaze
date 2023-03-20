package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector

internal object InputTextHelper {

    fun inputText(text: String, uiElement: UiElement, device: UiDevice) {
        val hierarchy = GetHierarchyHelper.getHierarchy(device)
        val foundUiElement = FindUiElementHelper.getUiElement(uiElement, hierarchy, false, device)
            ?: throw IllegalStateException("Can not find UiElement to enter text")

        // TODO: Implement the inputText logic for "ChildFrom", "TextRegex" and index from UiElement
        when (uiElement) {
            is UiElement.ChildFrom -> TODO()
            is UiElement.Id -> device.findObject(UiSelector().text(foundUiElement.resourceId)).text =
                text

            is UiElement.Text -> device.findObject(UiSelector().text(foundUiElement.text)).text =
                text

            is UiElement.TextRegex -> TODO()
        }
    }
}
