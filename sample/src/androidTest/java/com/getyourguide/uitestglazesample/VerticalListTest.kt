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
            tap(UiElement.Text("List", false))
            scroll(ScrollOption.Vertical(UiElement.Id(R.id.list)))
        }
    }
}
