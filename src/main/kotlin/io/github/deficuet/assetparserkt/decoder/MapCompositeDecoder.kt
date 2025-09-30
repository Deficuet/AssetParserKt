package io.github.deficuet.assetparserkt.decoder

import io.github.deficuet.assetparserkt.FieldTypeMismatchException
import io.github.deficuet.assetparserkt.TypeTreeNode
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.internal.AbstractCollectionSerializer
import java.nio.ByteBuffer

class MapCompositeDecoder(
    rootNode: TypeTreeNode,
    dataReader: ByteBuffer
): ArrayCompositeDecoder(rootNode, dataReader) {
    private val pairNode = rootNode.children[1]
    private val keyDecoder = UnityObjectDecoder(pairNode.children[0], dataReader)
    private val valueCollectionDecoder = object : UnityObjectDecoder(pairNode, dataReader) {
        override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
            return MapValueCollectionCompositeDecoder(this.rootNode, this.dataReader)
        }
    }

    override fun getCurrentNode(
        descriptor: SerialDescriptor,
        index: Int
    ): TypeTreeNode {
        return pairNode.children[index % 2]
    }

    override fun decoderForSerializableElement(
        node: TypeTreeNode,
        descriptor: SerialDescriptor,
        index: Int
    ): UnityObjectDecoder {
        if (index % 2 == 0) {
            return keyDecoder
        }
        throw IllegalStateException("Decoding map value should never call decoderForSerializableElement")
    }

    override fun <T> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?
    ): T {
        @OptIn(InternalSerializationApi::class)
        if (index % 2 == 1) {
            val collectionSerializer = deserializer as? AbstractCollectionSerializer<*, T, *>
                ?: throw FieldTypeMismatchException(
                    "In Unity maps are multi-map. " +
                            "The value type of the map must be a collection which has " +
                            "a compiler generated serializer that extends " +
                            "kotlinx.serialization.internal.AbstractCollectionSerializer"
                )
            return collectionSerializer.merge(valueCollectionDecoder, previousValue)
        }
        return super.decodeSerializableElement(descriptor, index, deserializer, previousValue)
    }
}
