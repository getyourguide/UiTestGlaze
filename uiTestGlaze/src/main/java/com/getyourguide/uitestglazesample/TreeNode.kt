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

// Copied and adapted from mobile-dev-inc/maestro (https://github.com/mobile-dev-inc/maestro)
internal data class TreeNode(
    val attributes: Map<Attribute, String> = emptyMap(),
    val children: List<TreeNode> = emptyList(),
) {

    fun aggregate(): List<TreeNode> {
        return listOf(this) + children.flatMap { it.aggregate() }
    }
}
