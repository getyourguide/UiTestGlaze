package com.getyourguide.uitestglaze

import androidx.test.uiautomator.UiDevice

internal object PressKeyHelper {

    fun pressKey(pressKey: PressKey, device: UiDevice) {
        val keyCode = when (pressKey) {
            PressKey.Back -> 4
            PressKey.Backspace -> 67
            PressKey.Enter -> 66
            PressKey.Home -> 3
            PressKey.Lock -> 276
            PressKey.VolumeDown -> 24
            PressKey.VolumeUp -> 25
        }
        device.executeShellCommand("input keyevent $keyCode")
    }
}
