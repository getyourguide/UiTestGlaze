package com.getyourguide.uitestglazesample

import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class InputTest {

    @get:Rule
    var testRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun testInputText() {
        testRule.launchActivity(null)
        with(UiTestGlaze()) {
            tap(UiElementIdentifier.Text("Input", true))
            inputText("UiTestGlaze rocks!", UiElementIdentifier.Id(R.id.first_edit_text))
            assert(Assertion.Visible(UiElementIdentifier.Text("UiTestGlaze rocks!")))

            inputText("", UiElementIdentifier.Id(R.id.first_edit_text))
            assert(Assertion.NotVisible(UiElementIdentifier.Text("UiTestGlaze rocks!")))

            inputText("What about using the hint?!", UiElementIdentifier.Text("First Input"))
            assert(
                Assertion.Visible(UiElementIdentifier.Text("What about using the hint?!"))
            )
        }
    }

    @Test
    fun testSwitchIsChecked() {
        testRule.launchActivity(null)
        with(UiTestGlaze()) {
            tap(UiElementIdentifier.Text("Input", true))
            assert(Assertion.NotChecked(UiElementIdentifier.Text("Really nice switch")))

            tap(UiElementIdentifier.Text("Really nice switch"))
            assert(Assertion.Checked(UiElementIdentifier.Text("Really nice switch")))
        }
    }

    @Test
    fun testCheckboxIsChecked() {
        testRule.launchActivity(null)
        with(UiTestGlaze()) {
            tap(UiElementIdentifier.Text("Input", true))
            assert(Assertion.NotChecked(UiElementIdentifier.Id(R.id.checkbox)))

            tap(UiElementIdentifier.Id(R.id.checkbox))
            assert(Assertion.Checked(UiElementIdentifier.Id(R.id.checkbox)))
        }
    }
}
