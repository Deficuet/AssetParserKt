package io.github.deficuet.assetparserkt.decoder

import io.github.deficuet.assetparserkt.TypeTreeNode
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import java.nio.ByteBuffer

class UnorderedClassCompositeDecoder(
    rootNode: TypeTreeNode,
    dataReader: ByteBuffer
): AbstractUnityObjectCompositeDecoder(rootNode, dataReader) {
    private val nodeIter = rootNode.children.iterator()
    private lateinit var currentNode: TypeTreeNode

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (!nodeIter.hasNext()) {
            return CompositeDecoder.DECODE_DONE
        }
        var index: Int
        do {
            currentNode = nodeIter.next()
            index = descriptor.getElementIndex(currentNode.name)
            if (index == CompositeDecoder.UNKNOWN_NAME) {
                // TODO: skip the current node in data buffer
            }
        } while (index == CompositeDecoder.UNKNOWN_NAME)
        return index
    }

    override fun getCurrentNode(
        descriptor: SerialDescriptor,
        index: Int
    ): TypeTreeNode {
        return currentNode
    }

    override fun decodeSequentially() = false
}
