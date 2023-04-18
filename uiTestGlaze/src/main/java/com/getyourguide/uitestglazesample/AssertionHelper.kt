package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice
import kotlin.time.Duration

internal class AssertionHelper(private val findUiElementHelper: FindUiElementHelper) {

    fun assert(
        assertion: Assertion,
        optional: Boolean,
        hierarchy: TreeNode,
        uiDevice: UiDevice,
        timeoutToGetAnUiElement: Duration
    ): Boolean {
        return when (assertion) {
            is Assertion.NotVisible -> {
                findUiElementHelper.getUiElement(
                    assertion.uiElementIdentifier,
                    hierarchy,
                    true,
                    uiDevice,
                    timeoutToGetAnUiElement
                ) == null
            }

            is Assertion.Visible -> {
                findUiElementHelper.getUiElement(
                    assertion.uiElementIdentifier,
                    hierarchy,
                    optional,
                    uiDevice,
                    timeoutToGetAnUiElement
                ) != null
            }

            is Assertion.Checked ->
                findUiElementHelper.getUiElement(
                    assertion.uiElementIdentifier,
                    hierarchy,
                    optional,
                    uiDevice,
                    timeoutToGetAnUiElement
                )?.checked == true

            is Assertion.NotChecked ->
                findUiElementHelper.getUiElement(
                    assertion.uiElementIdentifier,
                    hierarchy,
                    optional,
                    uiDevice,
                    timeoutToGetAnUiElement
                )?.checked == false

            is Assertion.Enabled -> findUiElementHelper.getUiElement(
                assertion.uiElementIdentifier,
                hierarchy,
                optional,
                uiDevice,
                timeoutToGetAnUiElement
            )?.enabled == true

            is Assertion.NotEnabled -> findUiElementHelper.getUiElement(
                assertion.uiElementIdentifier,
                hierarchy,
                optional,
                uiDevice,
                timeoutToGetAnUiElement
            )?.enabled == false
        }
    }
}
