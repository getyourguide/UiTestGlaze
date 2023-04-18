package com.getyourguide.uitestglazesample

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

internal class PrintHierarchyHelper(private val logger: Logger) {

    fun print(treeNode: TreeNode, prefix: String = "") {
        logger.i(
            prefix + jacksonObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(treeNode)
        )
    }
}
