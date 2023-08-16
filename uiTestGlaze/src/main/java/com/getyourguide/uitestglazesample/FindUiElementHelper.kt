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
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal class FindUiElementHelper(
    private val logger: Logger,
    private val getHierarchyHelper: GetHierarchyHelper,
) {

    fun getUiElement(
        uiElementIdentifier: UiElementIdentifier,
        hierarchy: TreeNode,
        optional: Boolean,
        device: UiDevice,
        timeoutToGetAnUiElement: Duration = 0.seconds,
    ): UiElement? {
        var newHierarchy = hierarchy
        var uiElement: UiElement?

        val startTime = System.currentTimeMillis()
        do {
            uiElement = when (uiElementIdentifier) {
                is UiElementIdentifier.ChildFrom -> findUiChildElement(
                    uiElementIdentifier,
                    newHierarchy,
                )?.first

                is UiElementIdentifier.Id -> findUiElementId(
                    uiElementIdentifier,
                    newHierarchy,
                )?.first

                is UiElementIdentifier.Text -> findUiElementText(
                    uiElementIdentifier,
                    newHierarchy,
                )?.first

                is UiElementIdentifier.TextRegex -> findUiElementRegex(
                    uiElementIdentifier,
                    newHierarchy,
                )?.first

                is UiElementIdentifier.TextResource -> findUiElementTextResource(
                    uiElementIdentifier,
                    newHierarchy,
                )?.first

                is UiElementIdentifier.TestTag -> findUIElementTestTag(
                    uiElementIdentifier,
                    newHierarchy,
                )?.first

                is UiElementIdentifier.PositionInHierarchy -> findUiElementPositionInHierarchy(
                    uiElementIdentifier,
                    newHierarchy,
                )?.first
            }
            if (uiElement != null) {
                logger.i("FindUiElementHelper getUiElement $uiElementIdentifier found return: $uiElement")
                break
            }
            logger.i("FindUiElementHelper getUiElement $uiElementIdentifier not found so try again")
            logger.i("Run time: ${System.currentTimeMillis() - startTime}")
            logger.i("TImeout: ${timeoutToGetAnUiElement.inWholeMilliseconds}")
            Thread.sleep(200)
            newHierarchy = getHierarchyHelper.getHierarchy(device)
        } while ((System.currentTimeMillis() - startTime) < timeoutToGetAnUiElement.inWholeMilliseconds)

        if (uiElement == null && !optional) {
            throw IllegalStateException("Couldn't find uiElement: $uiElementIdentifier")
        }
        return uiElement
    }

    private fun findUiElementPositionInHierarchy(
        uiElementIdentifier: UiElementIdentifier.PositionInHierarchy,
        hierarchy: TreeNode,
    ): Pair<UiElement?, TreeNode>? {
        val treeNode = hierarchy.aggregate().getOrNull(uiElementIdentifier.index)
        return if (treeNode == null) {
            null
        } else {
            Pair(toUiElement(treeNode.attributes, treeNode), treeNode)
        }
    }

    private fun findUIElementTestTag(
        uiTestTagResourceIdentifier: UiElementIdentifier.TestTag,
        hierarchy: TreeNode,
    ): Pair<UiElement?, TreeNode>? {
        val treeNode =
            hierarchy.aggregate()
                .filter {
                    (
                        it.attributes[Attribute.RESOURCE_ID]
                            ?: ""
                        ) == uiTestTagResourceIdentifier.testTag
                }
                .getOrNull(uiTestTagResourceIdentifier.index)
        return if (treeNode == null) {
            null
        } else {
            Pair(toUiElement(treeNode.attributes, treeNode), treeNode)
        }
    }

    private fun findUiChildElement(
        uiChildFromResourceIdentifier: UiElementIdentifier.ChildFrom,
        hierarchy: TreeNode,
    ): Pair<UiElement?, TreeNode>? {
        val foundParentUiElement = when (uiChildFromResourceIdentifier.uiElementIdentifierParent) {
            is UiElementIdentifier.ChildFrom -> throw IllegalStateException("Can not have childFrom option as parent")
            is UiElementIdentifier.Id -> findUiElementId(
                uiChildFromResourceIdentifier.uiElementIdentifierParent,
                hierarchy,
            )

            is UiElementIdentifier.Text -> findUiElementText(
                uiChildFromResourceIdentifier.uiElementIdentifierParent,
                hierarchy,
            )

            is UiElementIdentifier.TextRegex -> findUiElementRegex(
                uiChildFromResourceIdentifier.uiElementIdentifierParent,
                hierarchy,
            )

            is UiElementIdentifier.TextResource -> findUiElementTextResource(
                uiChildFromResourceIdentifier.uiElementIdentifierParent,
                hierarchy,
            )

            is UiElementIdentifier.TestTag -> findUIElementTestTag(
                uiChildFromResourceIdentifier.uiElementIdentifierParent,
                hierarchy,
            )

            is UiElementIdentifier.PositionInHierarchy -> findUiElementPositionInHierarchy(
                uiChildFromResourceIdentifier.uiElementIdentifierParent,
                hierarchy,
            )
        } ?: return null

        if (foundParentUiElement.first == null) {
            return null
        }

        return when (uiChildFromResourceIdentifier.uiElementIdentifierChild) {
            is UiElementIdentifier.ChildFrom -> findUiChildElement(
                uiChildFromResourceIdentifier.uiElementIdentifierChild,
                foundParentUiElement.second,
            )

            is UiElementIdentifier.Id -> findUiElementId(
                uiChildFromResourceIdentifier.uiElementIdentifierChild,
                foundParentUiElement.second,
            )

            is UiElementIdentifier.Text -> findUiElementText(
                uiChildFromResourceIdentifier.uiElementIdentifierChild,
                foundParentUiElement.second,
            )

            is UiElementIdentifier.TextRegex -> findUiElementRegex(
                uiChildFromResourceIdentifier.uiElementIdentifierChild,
                foundParentUiElement.second,
            )

            is UiElementIdentifier.TextResource -> findUiElementTextResource(
                uiChildFromResourceIdentifier.uiElementIdentifierChild,
                foundParentUiElement.second,
            )

            is UiElementIdentifier.TestTag -> findUIElementTestTag(
                uiChildFromResourceIdentifier.uiElementIdentifierChild,
                foundParentUiElement.second,
            )

            is UiElementIdentifier.PositionInHierarchy -> findUiElementPositionInHierarchy(
                uiChildFromResourceIdentifier.uiElementIdentifierChild,
                foundParentUiElement.second,
            )
        }
    }

    private fun findUiElementRegex(
        uiTextRegexResourceIdentifier: UiElementIdentifier.TextRegex,
        hierarchy: TreeNode,
    ): Pair<UiElement?, TreeNode>? {
        val treeNode = hierarchy.aggregate()
            .filter {
                uiTextRegexResourceIdentifier.textRegex.matches(it.attributes[Attribute.TEXT] ?: "")
            }
            .getOrNull(uiTextRegexResourceIdentifier.index)
        return if (treeNode == null) {
            null
        } else {
            Pair(toUiElement(treeNode.attributes, treeNode), treeNode)
        }
    }

    private fun findUiElementId(
        uiIdResourceIdentifier: UiElementIdentifier.Id,
        hierarchy: TreeNode,
    ): Pair<UiElement?, TreeNode>? {
        val resourceName =
            InstrumentationRegistry.getInstrumentation().targetContext.resources.getResourceName(
                uiIdResourceIdentifier.id,
            )
        val treeNode =
            hierarchy.aggregate()
                .filter {
                    (it.attributes[Attribute.RESOURCE_ID] ?: "") == resourceName
                }
                .getOrNull(uiIdResourceIdentifier.index)
        return if (treeNode == null) {
            null
        } else {
            Pair(toUiElement(treeNode.attributes, treeNode), treeNode)
        }
    }

    private fun findUiElementText(
        uiTextIdentifier: UiElementIdentifier.Text,
        hierarchy: TreeNode,
    ): Pair<UiElement?, TreeNode>? {
        val treeNode = hierarchy.aggregate()
            .filter {
                it.attributes[Attribute.TEXT].equals(
                    uiTextIdentifier.text,
                    uiTextIdentifier.ignoreCase,
                )
            }
            .getOrNull(uiTextIdentifier.index)
        return if (treeNode == null) {
            null
        } else {
            Pair(toUiElement(treeNode.attributes, treeNode), treeNode)
        }
    }

    private fun findUiElementTextResource(
        uiTextResourceIdentifier: UiElementIdentifier.TextResource,
        hierarchy: TreeNode,
    ): Pair<UiElement?, TreeNode>? {
        val treeNode = hierarchy.aggregate()
            .filter {
                it.attributes[Attribute.TEXT].equals(
                    InstrumentationRegistry.getInstrumentation().targetContext.getString(
                        uiTextResourceIdentifier.stringResourceId,
                    ),
                )
            }
            .getOrNull(uiTextResourceIdentifier.index)
        return if (treeNode == null) {
            null
        } else {
            Pair(toUiElement(treeNode.attributes, treeNode), treeNode)
        }
    }

    // Copied and adapted from mobile-dev-inc/maestro (https://github.com/mobile-dev-inc/maestro)
    private fun toUiElement(attributes: Map<Attribute, String>, treeNode: TreeNode): UiElement? {
        val boundsStr = attributes[Attribute.BOUNDS]
            ?: return null

        val boundsArr = boundsStr
            .replace("][", ",")
            .removePrefix("[")
            .removeSuffix("]")
            .split(",")
            .map { it.toInt() }

        return UiElement(
            x = boundsArr[0],
            y = boundsArr[1],
            width = boundsArr[2] - boundsArr[0],
            height = boundsArr[3] - boundsArr[1],
            resourceId = attributes[Attribute.RESOURCE_ID],
            text = attributes[Attribute.TEXT],
            clickable = attributes[Attribute.CLICKABLE]?.toBoolean(),
            checked = attributes[Attribute.CHECKED]?.toBoolean(),
            enabled = attributes[Attribute.ENABLED]?.toBoolean(),
            children = treeNode.children.mapNotNull { toUiElement(it.attributes, it) },
        )
    }
}
