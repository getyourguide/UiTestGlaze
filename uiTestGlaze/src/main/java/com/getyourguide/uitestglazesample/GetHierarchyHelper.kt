/*
 *
 *  Copyright (c) 2022 mobile.dev inc.
 *  Copyright (c) 2023 GetYourGuide GmbH
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package com.getyourguide.uitestglazesample

import androidx.test.uiautomator.UiDevice
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.ByteArrayOutputStream
import javax.xml.parsers.DocumentBuilderFactory

internal object GetHierarchyHelper {
    private const val MAX_TRIES = 25

    private val documentBuilderFactory = DocumentBuilderFactory.newInstance()

    fun getHierarchy(device: UiDevice): TreeNode {
        var currentTry = 0
        do {
            try {
                val hierarchyFromDevice = getHierarchyFromDevice(device)
                val document = documentBuilderFactory.newDocumentBuilder()
                    .parse(hierarchyFromDevice.byteInputStream())
                return mapHierarchy(document)
                    ?: throw Exception("UiTestGlaze GetHierarchyHelper getHierarchy mapHierarchy returned null")
            } catch (e: Exception) {
                Logger.i("GetHierarchyHelper getHierarchy exception try again")
                Thread.sleep(500)
                currentTry++
                continue
            }
        } while (currentTry < MAX_TRIES)
        throw IllegalStateException("Timeout hit while waiting for hierarchy to settle")
    }

    private fun getHierarchyFromDevice(device: UiDevice): String {
        val outputStream = ByteArrayOutputStream()
        try {
            device.dumpWindowHierarchy(outputStream)
        } catch (e: Exception) {
            Logger.i("GetHierarchyHelper getHierarchyFromDevice exception try again")
            Thread.sleep(500)
            device.dumpWindowHierarchy(outputStream)
        }
        return outputStream.toString(Charsets.UTF_8.name())
    }

    private fun isNotSystembar(node: Element): Boolean {
        if (node.hasAttribute("resource-id")) {
            return node.getAttribute("resource-id") != "com.android.systemui:id/status_bar_container" &&
                    node.getAttribute("resource-id") != "com.android.systemui:id/status_bar_launch_animation_container"
        }
        return true
    }

    //Copied and adapted from mobile-dev-inc/maestro (https://github.com/mobile-dev-inc/maestro)
    private fun mapHierarchy(node: Node): TreeNode? {
        val attributes = if (node is Element && isNotSystembar(node)) {
            val attributesBuilder = mutableMapOf<String, String>()

            if (node.hasAttribute("text")) {
                val text = node.getAttribute("text")

                if (text.isNotBlank()) {
                    attributesBuilder["text"] = text
                } else if (node.hasAttribute("content-desc")) {
                    // Using content-desc as fallback for text
                    attributesBuilder["text"] = node.getAttribute("content-desc")
                } else {
                    attributesBuilder["text"] = text
                }
            }

            if (node.hasAttribute("resource-id")) {
                attributesBuilder["resource-id"] = node.getAttribute("resource-id")
            }

            if (node.hasAttribute("clickable")) {
                attributesBuilder["clickable"] = node.getAttribute("clickable")
            }

            if (node.hasAttribute("bounds")) {
                attributesBuilder["bounds"] = node.getAttribute("bounds")
            }

            if (node.hasAttribute("checked")) {
                attributesBuilder["checked"] = node.getAttribute("checked")
            }

            if (node.hasAttribute("enabled")) {
                attributesBuilder["enabled"] = node.getAttribute("enabled")
            }

            attributesBuilder
        } else if (node is Element && !isNotSystembar(node)) {
            return null
        } else {
            emptyMap()
        }

        val children = mutableListOf<TreeNode?>()
        val childNodes = node.childNodes
        (0 until childNodes.length).forEach { i ->
            children += mapHierarchy(childNodes.item(i))
        }

        if (children.filterNotNull().isEmpty() && attributes.isEmpty()) {
            return null
        }
        return TreeNode(
            attributes = attributes,
            children = children.filterNotNull(),
        )
    }
}
