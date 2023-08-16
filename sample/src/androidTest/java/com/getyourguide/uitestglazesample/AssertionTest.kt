package com.getyourguide.uitestglazesample

import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class AssertionTest {

    @get:Rule
    var testRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun testFindUiElement() {
        testRule.launchActivity(null)
        with(UiTestGlaze()) {
            assert(
                listOf(
                    Assertion.Visible(UiElementIdentifier.Text("INPUT")),
                    Assertion.Visible(UiElementIdentifier.Text("LOADING VIEWS")),
                    Assertion.Visible(UiElementIdentifier.Text("LIST")),
                    Assertion.Visible(UiElementIdentifier.Text("JETPACK COMPOSE")),

                )
            )
        }
    }
}
