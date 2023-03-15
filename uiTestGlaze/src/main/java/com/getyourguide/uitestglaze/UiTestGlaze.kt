package com.getyourguide.uitestglaze

import androidx.annotation.IdRes
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
        uiElement: UiElement,
        optional: Boolean = false,
        retryCount: Int = 3,
        longPress: Boolean = false
    ) {
        val hierarchy =
            HierarchySettleHelper.waitTillHierarchySettles(config.loadingResourceIds, device)
        tapHelper.tap(uiElement, optional, retryCount, longPress, hierarchy, device)
    }

    fun scroll(scrollOption: ScrollOption) {
        ScrollHelper.scroll(scrollOption, device)
        HierarchySettleHelper.waitTillHierarchySettles(config.loadingResourceIds, device)
    }

    fun assert(assertion: Assertion, optional: Boolean): Boolean {
        val hierarchy =
            HierarchySettleHelper.waitTillHierarchySettles(config.loadingResourceIds, device)
        return AssertionHelper.assert(assertion, optional, hierarchy, device)
    }

    fun inputText(text: String, uiElement: UiElement) {
        InputTextHelper.inputText(text, uiElement, device)
        HierarchySettleHelper.waitTillHierarchySettles(config.loadingResourceIds, device)
    }

    fun pressKey(pressKey: PressKey) {
        PressKeyHelper.pressKey(pressKey, device)
        HierarchySettleHelper.waitTillHierarchySettles(config.loadingResourceIds, device)
    }

    fun dumpViewHierarchy() {
        PrintHierarchyHelper.print(GetHierarchyHelper.getHierarchy(device))
    }

}

sealed class UiElement(open val index: Int = 0) {
    data class Id(@IdRes val id: Int, override val index: Int = 0) : UiElement(index)
    data class Text(
        val text: String,
        val caseSensitive: Boolean = true,
        override val index: Int = 0
    ) : UiElement(index)

    data class TextRegex(val textRegex: String, override val index: Int = 0) : UiElement(index)
    data class ChildFrom(val uiElementParent: UiElement, val uiElementChild: UiElement) :
        UiElement()
}

sealed interface ScrollOption {
    data class Vertical(val inUiElementId: UiElement.Id) : ScrollOption
    data class Horizontal(val inUiElementId: UiElement.Id) : ScrollOption
    data class Manual(val startX: Int, val startY: Int, val endX: Int, val endY: Int) : ScrollOption
    data class VerticalToElement(
        val toUiElement: UiElement,
        val inUiElementId: UiElement.Id
    ) : ScrollOption

    data class HorizontalToElement(
        val toUiElement: UiElement,
        val inUiElementId: UiElement.Id
    ) : ScrollOption
}

sealed interface Assertion {
    data class Visible(val uiElement: UiElement) : Assertion
    data class NotVisible(val uiElement: UiElement) : Assertion
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
