package io.github.deficuet.assetparserkt.decoder

import io.github.deficuet.assetparserkt.FieldTypeMismatchException
import io.github.deficuet.assetparserkt.TypeTreeNode
import io.github.deficuet.assetparserkt.alignStream
import io.github.deficuet.assetparserkt.enums.NodeDataType
import io.github.deficuet.assetparserkt.math.Matrix4x4f
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.EmptySerializersModule
import java.nio.ByteBuffer

abstract class AbstractUnityObjectCompositeDecoder(
    protected val rootNode: TypeTreeNode,
    protected val dataReader: ByteBuffer
): CompositeDecoder {
    protected abstract fun getCurrentNode(
        descriptor: SerialDescriptor,
        index: Int
    ): TypeTreeNode

    private inline fun checkType(
        node: TypeTreeNode,
        descriptor: SerialDescriptor,
        index: Int,
        typeDesc: String,
        predicate: TypeTreeNode.() -> Boolean
    ) {
        if (!node.predicate()) {
            throw FieldTypeMismatchException(
                node, descriptor, index, typeDesc
            )
        }
    }

    override val serializersModule = EmptySerializersModule()

    override fun endStructure(descriptor: SerialDescriptor) {
        if (rootNode.metaFlag.and(0x4000) != 0) {
            dataReader.alignStream()
        }
    }

    override fun decodeBooleanElement(
        descriptor: SerialDescriptor,
        index: Int
    ): Boolean {
        val currentNode = getCurrentNode(descriptor, index)
        checkType(currentNode, descriptor, index, "Boolean") {
            dataType == NodeDataType.NonPrimitive.BOOL
        }
        return currentNode.readBoolean(dataReader)
    }

    override fun decodeByteElement(
        descriptor: SerialDescriptor,
        index: Int
    ): Byte {
        val currentNode = getCurrentNode(descriptor, index)
        checkType(currentNode, descriptor, index, "Byte") {
            dataType == NodeDataType.SIntType.INT8
        }
        return currentNode.readByte(dataReader)
    }

    override fun decodeCharElement(
        descriptor: SerialDescriptor,
        index: Int
    ): Char {
        val currentNode = getCurrentNode(descriptor, index)
        checkType(currentNode, descriptor, index, "Char") {
            dataType is NodeDataType.CharType
        }
        return currentNode.readChar(dataReader)
    }

    override fun decodeShortElement(
        descriptor: SerialDescriptor,
        index: Int
    ): Short {
        val currentNode = getCurrentNode(descriptor, index)
        checkType(currentNode, descriptor, index, "Short") {
            dataType == NodeDataType.SIntType.INT16
        }
        return currentNode.readShort(dataReader)
    }

    override fun decodeIntElement(
        descriptor: SerialDescriptor,
        index: Int
    ): Int {
        val currentNode = getCurrentNode(descriptor, index)
        checkType(currentNode, descriptor, index, "Int") {
            dataType == NodeDataType.SIntType.INT32
        }
        return currentNode.readInt(dataReader)
    }

    override fun decodeLongElement(
        descriptor: SerialDescriptor,
        index: Int
    ): Long {
        val currentNode = getCurrentNode(descriptor, index)
        checkType(currentNode, descriptor, index, "Long") {
            dataType == NodeDataType.SIntType.INT64
        }
        return currentNode.readLong(dataReader)
    }

    override fun decodeFloatElement(
        descriptor: SerialDescriptor,
        index: Int
    ): Float {
        val currentNode = getCurrentNode(descriptor, index)
        checkType(currentNode, descriptor, index, "Float") {
            dataType == NodeDataType.DecimalType.FLOAT
        }
        return currentNode.readFloat(dataReader)
    }

    override fun decodeDoubleElement(
        descriptor: SerialDescriptor,
        index: Int
    ): Double {
        val currentNode = getCurrentNode(descriptor, index)
        checkType(currentNode, descriptor, index, "Double") {
            dataType == NodeDataType.DecimalType.DOUBLE
        }
        return currentNode.readDouble(dataReader)
    }

    override fun decodeStringElement(
        descriptor: SerialDescriptor,
        index: Int
    ): String {
        val currentNode = getCurrentNode(descriptor, index)
        checkType(currentNode, descriptor, index, "String") {
            dataType == NodeDataType.NonPrimitive.STRING
        }
        return currentNode.children[0].readString(dataReader)
    }

    override fun decodeInlineElement(
        descriptor: SerialDescriptor,
        index: Int
    ): Decoder {
        val currentNode = getCurrentNode(descriptor, index)
        return UnityObjectDecoder(currentNode, dataReader)
    }

    open fun decoderForSerializableElement(
        node: TypeTreeNode,
        descriptor: SerialDescriptor,
        index: Int
    ): UnityObjectDecoder {
        return UnityObjectDecoder(node, dataReader)
    }

    override fun <T> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?
    ): T {
        val currentNode = getCurrentNode(descriptor, index)
        if (deserializer === Matrix4x4f.serializer()) {
            checkType(currentNode, descriptor, index, "Matrix4x4f") {
                dataType == NodeDataType.NonPrimitive.MATRIX4x4
            }
            @Suppress("UNCHECKED_CAST")
            return currentNode.readMatrix4x4f(dataReader) as T
        }
        return deserializer.deserialize(
            decoderForSerializableElement(currentNode, descriptor, index)
        )
    }

    @ExperimentalSerializationApi
    override fun <T : Any> decodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T?>,
        previousValue: T?
    ): T? {
        throw UnsupportedOperationException("Decoding nullable field is not supported.")
    }
}
