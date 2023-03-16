package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice

internal object AssertionHelper {

    fun assert(assertion: Assertion, optional: Boolean, hierarchy: TreeNode, uiDevice: UiDevice): Boolean {
        return when (assertion) {
            is Assertion.NotVisible -> {
                FindUiElementHelper.getUiElement(
                    assertion.uiElement,
                    hierarchy,
                    optional,
                    uiDevice
                ) == null
            }

            is Assertion.Visible -> {
                FindUiElementHelper.getUiElement(
                    assertion.uiElement,
                    hierarchy,
                    optional,
                    uiDevice
                ) != null
            }
        }
    }
}
