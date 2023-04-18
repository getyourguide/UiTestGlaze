package com.getyourguide.uitestglazesample

import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class VerticalListTest {

    @get:Rule
    var testRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun testVerticalListScrolling() {
        testRule.launchActivity(null)
        with(UiTestGlaze()) {
            tap(UiElementIdentifier.Text("List", true))
            scroll(ScrollOption.VerticalDown(UiElementIdentifier.Id(R.id.list)))
            assert(Assertion.Visible(UiElementIdentifier.Text("17")), false)
        }
    }

    @Test
    fun testVerticalListScrollToElement() {
        testRule.launchActivity(null)
        with(UiTestGlaze()) {
            tap(UiElementIdentifier.Text("List", true))
            scroll(
                ScrollOption.VerticalDownToElement(
                    toUiElement = UiElementIdentifier.Text("42"),
                    inUiElement = UiElementIdentifier.Id(R.id.list)
                )
            )
            assert(Assertion.Visible(UiElementIdentifier.Text("42")), false)
        }
    }
}
