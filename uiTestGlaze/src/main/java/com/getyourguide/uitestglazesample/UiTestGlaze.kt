package com.getyourguide.uitestglazesample

import android.util.Log
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * UiTestGlaze is a library to help you write UI tests.
 *
 * It provides a simple API to tap, scroll, wait, assert and more.
 * It's advisable to use UiTestGlaze with the scope function `with()`.
 *
 * ```
 * with(UiTestGlaze()) {
 *   tap(UiElementIdentifier.Text("Hello"))
 *   assert(Assertion.Visible(UiElementIdentifier.Text("World")))
 * }
 * ```
 */
data class UiTestGlaze(
    private val config: Config = Config(),
) {
    /**
     * Configure UiTestGlaze.
     *
     * @property logEverything If true, UiTestGlaze will log everything it does.
     * @property loadingResourceIds List of resource ids of views that are considered as loading views. UiTestGlaze will wait till these views are gone.
     * @property waitTillLoadingViewsGoneTimeout Timeout to wait till loading views are gone.
     * @property waitTillHierarchySettlesTimeout Timeout to wait till hierarchy settles.
     * @property logger Customer way to log messages.
     */
    data class Config(
        val logEverything: Boolean = false,
        val loadingResourceIds: List<LoadingResource> = emptyList(),
        val waitTillLoadingViewsGoneTimeout: Duration = 30.seconds,
        val waitTillHierarchySettlesTimeout: Duration = 30.seconds,
        val logger: (String) -> Unit = {
            if (logEverything) {
                Log.i("UiTestGlaze", it)
            }
        },
    ) {

        /**
         * LoadingResource is a sealed class to wrap a resource id or a test tag of a view.
         */
        sealed interface LoadingResource {
            /**
             * IdResource to wrap a resource id of a view.
             *
             * @param id Resource id of a view.
             */
            data class IdResource(@IdRes val id: Int) : LoadingResource

            /**
             * TestTagResource to wrap a test tag of a view. Mainly used to find Jetpack Compose views.
             *
             * @param testTag Test tag of a view.
             */
            data class TestTagResource(val testTag: String) : LoadingResource
        }
    }

    private val logger = Logger(logger = config.logger)
    private val printHierarchyHelper = PrintHierarchyHelper(logger = logger)
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val getHierarchyHelper = GetHierarchyHelper(logger = logger)
    private val findUiElementHelper = FindUiElementHelper(
        logger = logger,
        getHierarchyHelper = getHierarchyHelper,
    )
    private val assertionHelper = AssertionHelper(findUiElementHelper = findUiElementHelper)
    private val hierarchySettleHelper =
        HierarchySettleHelper(
            getHierarchyHelper = getHierarchyHelper,
            findUiElementHelper = findUiElementHelper,
            logger = logger,
        )
    private val inputTextHelper = InputTextHelper(
        findUiElementHelper = findUiElementHelper,
        getHierarchyHelper = getHierarchyHelper,
    )
    private val scrollHelper =
        ScrollHelper(
            findUiElementHelper = findUiElementHelper,
            getHierarchyHelper = getHierarchyHelper,
            hierarchySettleHelper = hierarchySettleHelper,
        )
    private val tapHelper =
        TapHelper(
            config = config,
            findUiElementHelper = findUiElementHelper,
            hierarchySettleHelper = hierarchySettleHelper,
        )

    /**
     * Tap on an element and expecting the UI to change.
     *
     * @param uiElementIdentifier Identifier of the element to tap.
     * @param optional If true, UiTestGlaze will not throw an exception if the element is not found.
     * @param retryCount Number of times to retry if the Ui does not change after tapping.
     * @param longPress If true, UiTestGlaze will long press on the element.
     */
    fun tap(
        uiElementIdentifier: UiElementIdentifier,
        optional: Boolean = false,
        retryCount: Int = 3,
        longPress: Boolean = false,
        offsetX: Int = 0,
        offsetY: Int = 0,
    ) {
        tapHelper.tap(
            uiElementIdentifier,
            optional,
            retryCount,
            longPress,
            offsetX,
            offsetY,
            device,
        )
    }

    /**
     * Tap on a defined position.
     *
     * @param xPosition X position to tap.
     * @param yPosition Y position to tap.
     * @param longPress If true, UiTestGlaze will long press on the element.
     */
    fun tap(
        xPosition: Int,
        yPosition: Int,
        longPress: Boolean = false,
    ) {
        tapHelper.tap(xPosition, yPosition, longPress, device)
    }

    /**
     * Scroll a view.
     *
     * @param scrollOption ScrollOption to specify the view to scroll and the direction to scroll.
     */
    fun scroll(scrollOption: ScrollOption) {
        hierarchySettleHelper.waitTillHierarchySettles(
            config.loadingResourceIds,
            device,
            config.waitTillLoadingViewsGoneTimeout,
            config.waitTillHierarchySettlesTimeout,
        )
        scrollHelper.scroll(scrollOption, device, config)
    }

    /**
     * Assert an assertion.
     *
     * @param assertion Assertion to assert.
     */
    fun assert(assertion: Assertion) {
        val hierarchy =
            hierarchySettleHelper.waitTillHierarchySettles(
                config.loadingResourceIds,
                device,
                config.waitTillLoadingViewsGoneTimeout,
                config.waitTillHierarchySettlesTimeout,
            )
        assertionHelper.assert(
            assertion,
            hierarchy,
            device,
        )
    }

    /**
     * Make multiple assertions.
     *
     * @param assertions Block of assertions.
     */
    fun assert(assertions: List<Assertion>) {
        val hierarchy =
            hierarchySettleHelper.waitTillHierarchySettles(
                config.loadingResourceIds,
                device,
                config.waitTillLoadingViewsGoneTimeout,
                config.waitTillHierarchySettlesTimeout,
            )
        assertions.asSequence().forEach {
            assertionHelper.assert(
                it,
                hierarchy,
                device,
            )
        }
    }

    /**
     * Find a UiElement in the current hierarchy.
     *
     * @param uiElementIdentifier Identifier of the element to find.
     */
    fun find(uiElementIdentifier: UiElementIdentifier): UiElement? {
        val hierarchy =
            hierarchySettleHelper.waitTillHierarchySettles(
                config.loadingResourceIds,
                device,
                config.waitTillLoadingViewsGoneTimeout,
                config.waitTillHierarchySettlesTimeout,
            )
        return findUiElementHelper.getUiElement(
            uiElementIdentifier,
            hierarchy,
            true,
            device,
        )
    }

    /**
     * Input text to an element.
     *
     * @param text Text to input.
     * @param uiElementIdentifier Identifier of the element to input text.
     * @param numberOfRetries Number of times to retry if the input is not entered.
     * @param inputShouldBeRecognizedTimeout Timeout to wait till the input is recognized.
     */
    fun inputText(
        text: String,
        uiElementIdentifier: UiElementIdentifier,
        numberOfRetries: Int = 3,
        inputShouldBeRecognizedTimeout: Duration = 10.seconds,
    ) {
        val hierarchy = hierarchySettleHelper.waitTillHierarchySettles(
            config.loadingResourceIds,
            device,
            config.waitTillLoadingViewsGoneTimeout,
            config.waitTillHierarchySettlesTimeout,
        )
        inputTextHelper.inputText(
            text = text,
            uiElementIdentifier = uiElementIdentifier,
            device = device,
            hierarchy = hierarchy,
            numberOfRetries = numberOfRetries,
            inputShouldBeRecognizedTimeout = inputShouldBeRecognizedTimeout
        )
    }

    /**
     * Press a key.
     *
     * @param pressKey Key to press.
     */
    fun pressKey(pressKey: PressKey) {
        hierarchySettleHelper.waitTillHierarchySettles(
            config.loadingResourceIds,
            device,
            config.waitTillLoadingViewsGoneTimeout,
            config.waitTillHierarchySettlesTimeout,
        )
        PressKeyHelper.pressKey(pressKey, device)
    }

    /**
     * Dump the current view hierarchy.
     *
     * @param waitForHierarchyToSettle If true, UiTestGlaze will wait till the hierarchy settles before dumping.
     */
    fun dumpViewHierarchy(waitForHierarchyToSettle: Boolean = false) {
        if (waitForHierarchyToSettle) {
            hierarchySettleHelper.waitTillHierarchySettles(
                config.loadingResourceIds,
                device,
                config.waitTillLoadingViewsGoneTimeout,
                config.waitTillHierarchySettlesTimeout,
            )
        }
        printHierarchyHelper.print(getHierarchyHelper.getHierarchy(device))
    }
}

/**
 * UiElementIdentifier is used to identify a UiElement.
 *
 * @param index Index of the element to identify. If there are multiple elements with the same identifier, this index will be used to identify the element.
 */
sealed class UiElementIdentifier(open val index: Int = 0) {
    /**
     * Identifier of an element by its resource id.
     *
     * @param id Resource id of the element.
     * @param index Index of the element to identify. If there are multiple elements with the same identifier, this index will be used to identify the element.
     */
    data class Id(@IdRes val id: Int, override val index: Int = 0) : UiElementIdentifier(index)

    /**
     * Identifier of an element by its set test tag. Used to identify Jetpack Compose views.
     *
     * @param testTag Test tag of the element.
     * @param index Index of the element to identify. If there are multiple elements with the same identifier, this index will be used to identify the element.
     */
    data class TestTag(val testTag: String, override val index: Int = 0) :
        UiElementIdentifier(index)

    /**
     * Identifier of an element by its text.
     *
     * @param text Text of the element.
     * @param ignoreCase If true, the text will be matched ignoring case.
     * @param index Index of the element to identify. If there are multiple elements with the same identifier, this index will be used to identify the element.
     */
    data class Text(
        val text: String,
        val ignoreCase: Boolean = false,
        override val index: Int = 0,
    ) : UiElementIdentifier(index)

    /**
     * Identifier of an element by its text resource id.
     *
     * @param stringResourceId Resource id of the text of the element.
     * @param index Index of the element to identify. If there are multiple elements with the same identifier, this index will be used to identify the element.
     */
    data class TextResource(
        @StringRes val stringResourceId: Int,
        override val index: Int = 0,
    ) : UiElementIdentifier(index)

    /**
     * Identifier of an element by its text regex.
     *
     * @param textRegex Regex of the text of the element.
     * @param index Index of the element to identify. If there are multiple elements with the same identifier, this index will be used to identify the element.
     */
    data class TextRegex(val textRegex: Regex, override val index: Int = 0) :
        UiElementIdentifier(index)

    /**
     * Identifier to find an UiElement inside another UiElement.
     *
     * @param uiElementIdentifierParent Identifier of the parent element.
     * @param uiElementIdentifierChild Identifier of the child element.
     * @param inputIndicatorText If true, UiTestGlaze will use the text as identifier to input a given text into a view.
     */
    data class ChildFrom(
        val uiElementIdentifierParent: UiElementIdentifier,
        val uiElementIdentifierChild: UiElementIdentifier,
        val inputIndicatorText: Boolean = true,
    ) : UiElementIdentifier()

    /**
     * Identifier to find an UiElement by its position in the hierarchy.
     * CAUTION: The position inside the hierarchy is not deterministic!
     *
     * @param index Index of the element to identify in the hierarchy.
     * @param inputIndicatorText If true, UiTestGlaze will use the text as identifier to input a given text into a view.
     */
    data class PositionInHierarchy(
        override val index: Int = 0,
        val inputIndicatorText: Boolean = true,
    ) : UiElementIdentifier(index)
}

/**
 * ScrollOption is used to specify how to scroll.
 */
sealed interface ScrollOption {
    /**
     * Scroll vertical down.
     *
     * @param inUiElement UiElement to scroll in.
     */
    data class VerticalDown(val inUiElement: UiElementIdentifier) : ScrollOption

    /**
     * Scroll vertical up.
     *
     * @param inUiElement UiElement to scroll in.
     */
    data class VerticalUp(val inUiElement: UiElementIdentifier) : ScrollOption

    /**
     * Scroll vertical up.
     *
     * @param inUiElement UiElement to scroll in.
     */
    data class HorizontalRight(val inUiElement: UiElementIdentifier) : ScrollOption

    /**
     * Manual scroll.
     *
     * @param startX Start x position of the scroll.
     * @param startY Start y position of the scroll.
     * @param endX End x position of the scroll.
     * @param endY End y position of the scroll.
     */
    data class Manual(val startX: Int, val startY: Int, val endX: Int, val endY: Int) : ScrollOption

    /**
     * Scroll vertical down to a given UiElement.
     *
     * @param toUiElement UiElement to scroll to.
     * @param inUiElement UiElement to scroll in.
     */
    data class VerticalDownToElement(
        val toUiElement: UiElementIdentifier,
        val inUiElement: UiElementIdentifier,
    ) : ScrollOption

    /**
     * Scroll vertical up to a given UiElement.
     *
     * @param toUiElement UiElement to scroll to.
     * @param inUiElement UiElement to scroll in.
     */
    data class VerticalUpToElement(
        val toUiElement: UiElementIdentifier,
        val inUiElement: UiElementIdentifier,
    ) : ScrollOption

    /**
     * Scroll vertical up to a given UiElement.
     *
     * @param toUiElement UiElement to scroll to.
     * @param inUiElement UiElement to scroll in.
     */
    data class HorizontalRightToElement(
        val toUiElement: UiElementIdentifier,
        val inUiElement: UiElementIdentifier,
    ) : ScrollOption
}

/**
 * Assert a given view.
 */
sealed interface Assertion {
    /**
     * Assert that a given UiElement is visible.
     *
     * @param uiElementIdentifier Identifier of the UiElement to assert.
     */
    data class Visible(val uiElementIdentifier: UiElementIdentifier) : Assertion

    /**
     * Assert that a given UiElement is not visible.
     *
     * @param uiElementIdentifier Identifier of the UiElement to assert.
     */
    data class NotVisible(val uiElementIdentifier: UiElementIdentifier) : Assertion

    /**
     * Assert that a given UiElement is checked.
     *
     * @param uiElementIdentifier Identifier of the UiElement to assert.
     */
    data class Checked(val uiElementIdentifier: UiElementIdentifier) : Assertion

    /**
     * Assert that a given UiElement is not checked.
     *
     * @param uiElementIdentifier Identifier of the UiElement to assert.
     */
    data class NotChecked(val uiElementIdentifier: UiElementIdentifier) : Assertion

    /**
     * Assert that a given UiElement is enabled.
     *
     * @param uiElementIdentifier Identifier of the UiElement to assert.
     */
    data class Enabled(val uiElementIdentifier: UiElementIdentifier) : Assertion

    /**
     * Assert that a given UiElement is not enabled.
     *
     * @param uiElementIdentifier Identifier of the UiElement to assert.
     */
    data class NotEnabled(val uiElementIdentifier: UiElementIdentifier) : Assertion
}

/**
 * Press a hardware key.
 */
sealed interface PressKey {
    /**
     * Press the hardware key "Enter".
     */
    object Enter : PressKey

    /**
     * Press the hardware key "Backspace".
     */
    object Backspace : PressKey

    /**
     * Press the hardware key "Back".
     */
    object Back : PressKey

    /**
     * Press the hardware key "Home".
     */
    object Home : PressKey

    /**
     * Press the hardware key "Lock".
     */
    object Lock : PressKey

    /**
     * Press the hardware key "VolumeUp".
     */
    object VolumeUp : PressKey

    /**
     * Press the hardware key "VolumeDown".
     */
    object VolumeDown : PressKey
}

/**
 * Data class of an UiElement from the hierarchy.
 *
 * @property x X position of the element on the screen.
 * @property y Y position of the element on the screen.
 * @property width Width of the element.
 * @property height Height of the element.
 * @property resourceId Resource id of the element.
 * @property text Text of the element.
 * @property clickable If true, the element is clickable.
 * @property checked If true, the element is checked.
 * @property enabled If true, the element is enabled.
 * @property children List of children of the element.
 */
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
    val children: List<UiElement>? = null,
)
