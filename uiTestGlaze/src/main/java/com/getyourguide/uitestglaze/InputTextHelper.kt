package com.getyourguide.uitestglaze

import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector

internal object InputTextHelper {

    fun inputText(text: String, uiElement: UiElement, device: UiDevice) {
        val hierarchy = GetHierarchyHelper.getHierarchy(device)
        val foundUiElement = FindUiElementHelper.getUiElement(uiElement, hierarchy, false, device)
        if (foundUiElement?.resourceId != null) {
            device.findObject(UiSelector().resourceId(foundUiElement.resourceId)).text = text
        } else if (foundUiElement?.text != null) {
            device.findObject(UiSelector().text(foundUiElement.text)).text = text
        } else {
            throw IllegalStateException("Can not enter text for UiElement $uiElement because resourceId and text was null")
        }
    }
}
