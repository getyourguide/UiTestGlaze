package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice
import kotlin.time.Duration

internal object AssertionHelper {

    fun assert(
        assertion: Assertion,
        optional: Boolean,
        hierarchy: TreeNode,
        uiDevice: UiDevice,
        timeoutToGetAnUiElement: Duration
    ): Boolean {
        return when (assertion) {
            is Assertion.NotVisible -> {
                FindUiElementHelper.getUiElement(
                    assertion.uiElementIdentifier,
                    hierarchy,
                    optional,
                    uiDevice,
                    timeoutToGetAnUiElement
                ) == null
            }

            is Assertion.Visible -> {
                FindUiElementHelper.getUiElement(
                    assertion.uiElementIdentifier,
                    hierarchy,
                    optional,
                    uiDevice,
                    timeoutToGetAnUiElement
                ) != null
            }
        }
    }
}
