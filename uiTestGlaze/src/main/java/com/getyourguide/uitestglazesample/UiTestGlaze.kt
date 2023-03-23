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

    fun find(uiElement: UiElement): FoundUiElement? {
        val hierarchy =
            HierarchySettleHelper.waitTillHierarchySettles(config.loadingResourceIds, device)
        return FindUiElementHelper.getUiElement(uiElement, hierarchy, true, device)
    }

    fun inputText(text: String, uiElement: UiElement) {
        InputTextHelper.inputText(text, uiElement, device)
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

sealed class UiElement(open val index: Int = 0) {
    data class Id(@IdRes val id: Int, override val index: Int = 0) : UiElement(index)
    data class Text(
        val text: String,
        val ignoreCase: Boolean = false,
        override val index: Int = 0
    ) : UiElement(index)

    data class TextResource(
        @StringRes val stringResourceId: Int,
        override val index: Int = 0
    ) : UiElement(index)

    data class TextRegex(val textRegex: Regex, override val index: Int = 0) : UiElement(index)
    data class ChildFrom(
        val uiElementParent: UiElement,
        val uiElementChild: UiElement,
        val inputIndicatorText: Boolean = true
    ) :
        UiElement()
}

sealed interface ScrollOption {
    data class VerticalDown(val inUiElementId: UiElement.Id) : ScrollOption
    data class HorizontalRight(val inUiElementId: UiElement.Id) : ScrollOption
    data class Manual(val startX: Int, val startY: Int, val endX: Int, val endY: Int) : ScrollOption
    data class VerticalDownToElement(
        val toUiElement: UiElement,
        val inUiElementId: UiElement.Id
    ) : ScrollOption

    data class HorizontalRightToElement(
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

data class FoundUiElement(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val resourceId: String? = null,
    val text: String? = null,
    val clickable: Boolean? = null,
    val checked: Boolean? = null,
    val enabled: Boolean? = null,
    val treeNode: TreeNode? = null
)
