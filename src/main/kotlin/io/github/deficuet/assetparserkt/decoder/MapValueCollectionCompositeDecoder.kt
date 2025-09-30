package io.github.deficuet.assetparserkt.decoder

import io.github.deficuet.assetparserkt.TypeTreeNode
import kotlinx.serialization.descriptors.SerialDescriptor
import java.nio.ByteBuffer

class MapValueCollectionCompositeDecoder(
    rootNode: TypeTreeNode,
    dataReader: ByteBuffer
): ArrayCompositeDecoder(rootNode, dataReader) {
    override fun decodeCollectionSize(descriptor: SerialDescriptor) = 1
}
