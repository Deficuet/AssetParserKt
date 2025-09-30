package io.github.deficuet.assetparserkt.decoder

import io.github.deficuet.assetparserkt.TypeTreeNode
import kotlinx.serialization.descriptors.SerialDescriptor
import java.nio.ByteBuffer

open class ArrayCompositeDecoder(
    rootNode: TypeTreeNode,
    dataReader: ByteBuffer
): SequentialClassCompositeDecoder(rootNode, dataReader) {
    protected open val elementDecoder = UnityObjectDecoder(rootNode.children[1], dataReader)

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int {
        return rootNode.children[0].readInt(dataReader)
    }

    override fun getCurrentNode(
        descriptor: SerialDescriptor,
        index: Int
    ): TypeTreeNode {
        return rootNode.children[1]
    }

    override fun decoderForSerializableElement(
        node: TypeTreeNode,
        descriptor: SerialDescriptor,
        index: Int
    ): UnityObjectDecoder {
        return elementDecoder
    }
}
