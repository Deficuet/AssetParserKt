package io.github.deficuet.assetparserkt.decoder

import io.github.deficuet.assetparserkt.TypeTreeNode
import kotlinx.serialization.descriptors.SerialDescriptor
import java.nio.ByteBuffer

open class SequentialClassCompositeDecoder(
    rootNode: TypeTreeNode,
    dataReader: ByteBuffer
): AbstractUnityObjectCompositeDecoder(rootNode, dataReader) {
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        throw IllegalStateException("Sequential composite decoder should never call decodeElementIndex")
    }

    override fun decodeSequentially() = true

    override fun getCurrentNode(
        descriptor: SerialDescriptor,
        index: Int
    ): TypeTreeNode {
        if (index > rootNode.children.lastIndex) {
            throw IndexOutOfBoundsException(
                StringBuffer()
                    .append("SequentialCompositeDecoder: In class ${descriptor.serialName} ")
                    .append("field ${descriptor.getElementName(index)} ")
                    .append("is at index $index ")
                    .append("but the class root node has ${rootNode.children.size} child nodes only.")
                    .toString()
            )
        }
        return rootNode.children[index]
    }
}
