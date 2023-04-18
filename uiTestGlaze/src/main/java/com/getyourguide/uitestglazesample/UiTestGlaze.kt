package com.getyourguide.uitestglazesample

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice

data class UiTestGlaze(
    private val config: Config = Config()
) {
    data class Config(
        val logEverything: Boolean = false,
        val loadingResourceIds: List<IdResource> = emptyList(),
        val tapRetryCount: Int = 3
    ) {
        data class IdResource(@IdRes val id: Int)
    }

    private val tapHelper = TapHelper(config)
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    fun tap(
        uiElementIdentifier: UiElementIdentifier,
        optional: Boolean = false,
        retryCount: Int = 3,
        longPress: Boolean = false
    ) {
        val hierarchy =
            HierarchySettleHelper.waitTillHierarchySettles(config.loadingResourceIds, device)
        tapHelper.tap(uiElementIdentifier, optional, retryCount, longPress, hierarchy, device)
    }

    fun scroll(scrollOption: ScrollOption) {
        ScrollHelper.scroll(scrollOption, device)
        HierarchySettleHelper.waitTillHierarchySettles(config.loadingResourceIds, device)
    }

    fun assert(assertion: Assertion, optional: Boolean = false): Boolean {
        val hierarchy =
            HierarchySettleHelper.waitTillHierarchySettles(config.loadingResourceIds, device)
        return AssertionHelper.assert(assertion, optional, hierarchy, device)
    }

    fun find(uiElementIdentifier: UiElementIdentifier): UiElement? {
        val hierarchy =
            HierarchySettleHelper.waitTillHierarchySettles(config.loadingResourceIds, device)
        return FindUiElementHelper.getUiElement(uiElementIdentifier, hierarchy, true, device)
    }

    fun inputText(text: String, uiElementIdentifier: UiElementIdentifier) {
        InputTextHelper.inputText(text, uiElementIdentifier, device)
        HierarchySettleHelper.waitTillHierarchySettles(config.loadingResourceIds, device)
    }

    fun pressKey(pressKey: PressKey) {
        PressKeyHelper.pressKey(pressKey, device)
        HierarchySettleHelper.waitTillHierarchySettles(config.loadingResourceIds, device)
    }

    fun dumpViewHierarchy(waitForHierarchyToSettle: Boolean = false) {
        if (waitForHierarchyToSettle) {
            HierarchySettleHelper.waitTillHierarchySettles(config.loadingResourceIds, device)
        }
        PrintHierarchyHelper.print(GetHierarchyHelper.getHierarchy(device))
    }

}

sealed class UiElementIdentifier(open val index: Int = 0) {
    data class Id(@IdRes val id: Int, override val index: Int = 0) : UiElementIdentifier(index)
    data class TestTag(val testTag: String, override val index: Int = 0) :
        UiElementIdentifier(index)

    data class Text(
        val text: String,
        val ignoreCase: Boolean = false,
        override val index: Int = 0
    ) : UiElementIdentifier(index)

    data class TextResource(
        @StringRes val stringResourceId: Int,
        override val index: Int = 0
    ) : UiElementIdentifier(index)

    data class TextRegex(val textRegex: Regex, override val index: Int = 0) :
        UiElementIdentifier(index)

    data class ChildFrom(
        val uiElementIdentifierParent: UiElementIdentifier,
        val uiElementIdentifierChild: UiElementIdentifier,
        val inputIndicatorText: Boolean = true
    ) : UiElementIdentifier()

    data class PositionInHierarchy(
        override val index: Int = 0,
        val inputIndicatorText: Boolean = true
    ) : UiElementIdentifier(index)
}

sealed interface ScrollOption {
    data class VerticalDown(val inUiElement: UiElementIdentifier) : ScrollOption
    data class HorizontalRight(val inUiElement: UiElementIdentifier) : ScrollOption
    data class Manual(val startX: Int, val startY: Int, val endX: Int, val endY: Int) : ScrollOption
    data class VerticalDownToElement(
        val toUiElement: UiElementIdentifier,
        val inUiElement: UiElementIdentifier
    ) : ScrollOption

    data class HorizontalRightToElement(
        val toUiElement: UiElementIdentifier,
        val inUiElement: UiElementIdentifier
    ) : ScrollOption
}

sealed interface Assertion {
    data class Visible(val uiElementIdentifier: UiElementIdentifier) : Assertion
    data class NotVisible(val uiElementIdentifier: UiElementIdentifier) : Assertion
}

sealed interface PressKey {
    object Enter : PressKey
    object Backspace : PressKey
    object Back : PressKey
    object Home : PressKey
    object Lock : PressKey
    object VolumeUp : PressKey
    object VolumeDown : PressKey
}

data class UiElement(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val resourceId: String? = null,
    val text: String? = null,
    val clickable: Boolean? = null,
    val checked: Boolean? = null,
    val enabled: Boolean? = null,
    val children: List<UiElement>? = null
)
