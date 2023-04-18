package com.getyourguide.uitestglazesample

import androidx.test.rule.ActivityTestRule
import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test

class FindUiElementTest {

    @get:Rule
    var testRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun testFindUiElement() {
        testRule.launchActivity(null)
        with(UiTestGlaze()) {
            val find = find(UiElementIdentifier.Text("List", true))
            assertNotNull(find)
        }
    }

}
