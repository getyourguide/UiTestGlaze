package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice

internal class AssertionHelper(private val findUiElementHelper: FindUiElementHelper) {

    fun assert(
        assertion: Assertion,
        hierarchy: TreeNode,
        uiDevice: UiDevice,
    ) {
        when (assertion) {
            is Assertion.NotVisible -> {
                if (findUiElementHelper.getUiElement(
                        uiElementIdentifier = assertion.uiElementIdentifier,
                        hierarchy = hierarchy,
                        optional = true,
                        device = uiDevice,
                    ) != null
                ) {
                    throw IllegalStateException("Assertion: $assertion failed. UiElement is visible")
                }
            }

            is Assertion.Visible -> {
                if (findUiElementHelper.getUiElement(
                        uiElementIdentifier = assertion.uiElementIdentifier,
                        hierarchy = hierarchy,
                        optional = true,
                        device = uiDevice,
                    ) == null
                ) {
                    throw IllegalStateException("Assertion: $assertion failed. UiElement is not visible")
                }
            }

            is Assertion.Checked ->
                if (findUiElementHelper.getUiElement(
                        uiElementIdentifier = assertion.uiElementIdentifier,
                        hierarchy = hierarchy,
                        optional = true,
                        device = uiDevice,
                    )?.checked != true
                ) {
                    throw IllegalStateException("Assertion: $assertion failed. UiElement is not checked")
                }

            is Assertion.NotChecked ->
                if (findUiElementHelper.getUiElement(
                        uiElementIdentifier = assertion.uiElementIdentifier,
                        hierarchy = hierarchy,
                        optional = true,
                        device = uiDevice,
                    )?.checked != false
                ) {
                    throw IllegalStateException("Assertion: $assertion failed. UiElement is checked")
                }

            is Assertion.Enabled -> if (findUiElementHelper.getUiElement(
                    uiElementIdentifier = assertion.uiElementIdentifier,
                    hierarchy = hierarchy,
                    optional = true,
                    device = uiDevice,
                )?.enabled != true
            ) {
                throw IllegalStateException("Assertion: $assertion failed. UiElement is not enabled")
            }

            is Assertion.NotEnabled -> if (findUiElementHelper.getUiElement(
                    uiElementIdentifier = assertion.uiElementIdentifier,
                    hierarchy = hierarchy,
                    optional = true,
                    device = uiDevice,
                )?.enabled != false
            ) {
                throw IllegalStateException("Assertion: $assertion failed. UiElement is enabled")
            }
        }
    }
}
