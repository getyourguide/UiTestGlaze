package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice
import kotlin.time.Duration.Companion.seconds

internal class AssertionHelper(private val findUiElementHelper: FindUiElementHelper) {

    companion object {
        private val TIMEOUT_TO_FIND_ELEMENT = 3.seconds
    }

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
                        timeoutToGetAnUiElement = TIMEOUT_TO_FIND_ELEMENT,
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
                        timeoutToGetAnUiElement = TIMEOUT_TO_FIND_ELEMENT,
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
                        timeoutToGetAnUiElement = TIMEOUT_TO_FIND_ELEMENT,
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
                        timeoutToGetAnUiElement = TIMEOUT_TO_FIND_ELEMENT,
                    )?.checked != false
                ) {
                    throw IllegalStateException("Assertion: $assertion failed. UiElement is checked")
                }

            is Assertion.Enabled -> if (findUiElementHelper.getUiElement(
                    uiElementIdentifier = assertion.uiElementIdentifier,
                    hierarchy = hierarchy,
                    optional = true,
                    device = uiDevice,
                    timeoutToGetAnUiElement = TIMEOUT_TO_FIND_ELEMENT,
                )?.enabled != true
            ) {
                throw IllegalStateException("Assertion: $assertion failed. UiElement is not enabled")
            }

            is Assertion.NotEnabled -> if (findUiElementHelper.getUiElement(
                    uiElementIdentifier = assertion.uiElementIdentifier,
                    hierarchy = hierarchy,
                    optional = true,
                    device = uiDevice,
                    timeoutToGetAnUiElement = TIMEOUT_TO_FIND_ELEMENT,
                )?.enabled != false
            ) {
                throw IllegalStateException("Assertion: $assertion failed. UiElement is enabled")
            }
        }
    }
}
