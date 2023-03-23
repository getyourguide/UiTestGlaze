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

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice

internal object FindUiElementHelper {

    fun getUiElement(
        uiElement: UiElement,
        hierarchy: TreeNode,
        optional: Boolean,
        device: UiDevice
    ): FoundUiElement? {
        var newHierarchy = hierarchy
        var foundUiElement: FoundUiElement? = null
        run repeatBlock@{
            // TODO move to a time out based approach which should also be configurable
            repeat(20) {
                foundUiElement = when (uiElement) {
                    is UiElement.ChildFrom -> findUiChildElement(uiElement, newHierarchy)
                    is UiElement.Id -> findUiElementId(uiElement, newHierarchy)
                    is UiElement.Text -> findUiElementText(uiElement, newHierarchy)
                    is UiElement.TextRegex -> findUiElementRegex(uiElement, newHierarchy)
                    is UiElement.TextResource -> findUiElementTextResource(uiElement, newHierarchy)
                    is UiElement.TestTag -> findUIElementTestTag(uiElement, newHierarchy)
                }
                if (foundUiElement != null || optional) {
                    Logger.i("FindUiElementHelper getUiElement $uiElement found so return")
                    return@repeatBlock
                }
                Logger.i("FindUiElementHelper getUiElement $uiElement not found so try again")
                Thread.sleep(200)
                newHierarchy = GetHierarchyHelper.getHierarchy(device)
            }
        }

        if (foundUiElement == null) {
            if (!optional) {
                throw IllegalStateException("Couldn't find uiElement: $uiElement")
            }
        }
        return foundUiElement
    }

    private fun findUIElementTestTag(
        uiElement: UiElement.TestTag,
        hierarchy: TreeNode
    ): FoundUiElement? {
        val treeNode =
            hierarchy.aggregate()
                .filter {
                    (it.attributes[Attribute.RESOURCE_ID] ?: "") == uiElement.testTag
                }
                .getOrNull(uiElement.index)
        return if (treeNode == null) {
            null
        } else {
            helper(treeNode.attributes)
        }
    }

    private fun findUiChildElement(
        uiElement: UiElement.ChildFrom,
        hierarchy: TreeNode
    ): FoundUiElement? {
        val foundParentUiElement = when (uiElement.uiElementParent) {
            is UiElement.ChildFrom -> throw IllegalStateException("Can not have childFrom option as parent")
            is UiElement.Id -> findUiElementId(uiElement.uiElementParent, hierarchy)
            is UiElement.Text -> findUiElementText(uiElement.uiElementParent, hierarchy)
            is UiElement.TextRegex -> findUiElementRegex(uiElement.uiElementParent, hierarchy)
            is UiElement.TextResource -> findUiElementTextResource(
                uiElement.uiElementParent,
                hierarchy
            )

            is UiElement.TestTag -> findUIElementTestTag(uiElement.uiElementParent, hierarchy)
        } ?: return null

        if (foundParentUiElement.treeNode == null) {
            return null
        }

        return when (uiElement.uiElementChild) {
            is UiElement.ChildFrom -> findUiChildElement(
                uiElement.uiElementChild,
                foundParentUiElement.treeNode
            )

            is UiElement.Id -> findUiElementId(
                uiElement.uiElementChild,
                foundParentUiElement.treeNode
            )

            is UiElement.Text -> findUiElementText(
                uiElement.uiElementChild,
                foundParentUiElement.treeNode
            )

            is UiElement.TextRegex -> findUiElementRegex(
                uiElement.uiElementChild,
                foundParentUiElement.treeNode
            )

            is UiElement.TextResource -> findUiElementTextResource(
                uiElement.uiElementChild,
                foundParentUiElement.treeNode
            )

            is UiElement.TestTag -> findUIElementTestTag(
                uiElement.uiElementChild,
                foundParentUiElement.treeNode
            )
        }
    }

    private fun findUiElementRegex(
        uiElement: UiElement.TextRegex,
        hierarchy: TreeNode
    ): FoundUiElement? {
        val treeNode = hierarchy.aggregate()
            .filter {
                uiElement.textRegex.matches(it.attributes[Attribute.TEXT] ?: "")
            }
            .getOrNull(uiElement.index)
        return if (treeNode == null) {
            null
        } else {
            helper(treeNode.attributes)
        }
    }

    private fun findUiElementId(uiElement: UiElement.Id, hierarchy: TreeNode): FoundUiElement? {
        val resourceName =
            InstrumentationRegistry.getInstrumentation().targetContext.resources.getResourceName(
                uiElement.id
            )
        val treeNode =
            hierarchy.aggregate()
                .filter {
                    (it.attributes[Attribute.RESOURCE_ID] ?: "") == resourceName
                }
                .getOrNull(uiElement.index)
        return if (treeNode == null) {
            null
        } else {
            helper(treeNode.attributes)
        }
    }

    private fun findUiElementText(uiElement: UiElement.Text, hierarchy: TreeNode): FoundUiElement? {
        val treeNode = hierarchy.aggregate()
            .filter {
                it.attributes[Attribute.TEXT].equals(
                    uiElement.text,
                    uiElement.ignoreCase
                )
            }
            .getOrNull(uiElement.index)
        return if (treeNode == null) {
            null
        } else {
            helper(treeNode.attributes)
        }
    }

    private fun findUiElementTextResource(
        uiElement: UiElement.TextResource,
        hierarchy: TreeNode
    ): FoundUiElement? {
        val treeNode = hierarchy.aggregate()
            .filter {
                it.attributes[Attribute.TEXT].equals(
                    InstrumentationRegistry.getInstrumentation().targetContext.getString(
                        uiElement.stringResourceId
                    )
                )
            }
            .getOrNull(uiElement.index)
        return if (treeNode == null) {
            null
        } else {
            helper(treeNode.attributes)
        }
    }

    //Copied and adapted from mobile-dev-inc/maestro (https://github.com/mobile-dev-inc/maestro)
    private fun helper(attributes: Map<Attribute, String>): FoundUiElement? {
        val boundsStr = attributes[Attribute.BOUNDS]
            ?: return null

        val boundsArr = boundsStr
            .replace("][", ",")
            .removePrefix("[")
            .removeSuffix("]")
            .split(",")
            .map { it.toInt() }

        return FoundUiElement(
            x = boundsArr[0],
            y = boundsArr[1],
            width = boundsArr[2] - boundsArr[0],
            height = boundsArr[3] - boundsArr[1],
            resourceId = attributes[Attribute.RESOURCE_ID],
            text = attributes[Attribute.TEXT],
            clickable = attributes[Attribute.CLICKABLE]?.toBoolean(),
            checked = attributes[Attribute.CHECKED]?.toBoolean(),
            enabled = attributes[Attribute.ENABLED]?.toBoolean(),
        )
    }
}
