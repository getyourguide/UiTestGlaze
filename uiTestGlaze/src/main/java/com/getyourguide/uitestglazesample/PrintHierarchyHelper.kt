package com.getyourguide.uitestglazesample

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

internal object PrintHierarchyHelper {

    fun print(treeNode: TreeNode, prefix: String = "") {
        Logger.i(
            prefix + jacksonObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(treeNode)
        )
    }
}
