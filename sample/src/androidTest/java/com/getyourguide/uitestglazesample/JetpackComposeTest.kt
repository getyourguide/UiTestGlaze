package com.getyourguide.uitestglazesample

import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class JetpackComposeTest {

    @get:Rule
    var testRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun testJetpackCompose() {
        testRule.launchActivity(null)
        with(UiTestGlaze(config = UiTestGlaze.Config(logEverything = true))) {
            tap(UiElementIdentifier.Text("Jetpack Compose", true))
            assert(Assertion.Visible(UiElementIdentifier.Text("Hello World", true)))
            assert(Assertion.Visible(UiElementIdentifier.TestTag("hello_world_tag")))

            inputText("UiTestGlaze rocks!", UiElementIdentifier.TestTag("text_field_tag"))
            assert(Assertion.Visible(UiElementIdentifier.Text("UiTestGlaze rocks!")))
        }
    }
}
