package io.github.deficuet.assetparserkt.decoder

import io.github.deficuet.assetparserkt.FieldTypeMismatchException
import io.github.deficuet.assetparserkt.SequentiallyDeclared
import io.github.deficuet.assetparserkt.TypeTreeNode
import io.github.deficuet.assetparserkt.enums.NodeDataType
import io.github.deficuet.assetparserkt.enums.NumericalEnum
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.EmptySerializersModule
import java.nio.ByteBuffer

open class UnityObjectDecoder(
    protected val rootNode: TypeTreeNode,
    protected val dataReader: ByteBuffer
): Decoder {
    override val serializersModule = EmptySerializersModule()

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        when (descriptor.kind) {
            StructureKind.LIST -> {
                return when (rootNode.dataType) {
                    NodeDataType.NonPrimitive.ARRAY -> {
                        ArrayCompositeDecoder(rootNode.children[0], dataReader)
                    }
                    NodeDataType.NonPrimitive.TYPELESS -> {
                        ArrayCompositeDecoder(rootNode, dataReader)
                    }
                    else -> throw FieldTypeMismatchException(rootNode, descriptor.serialName)
                }
            }
            StructureKind.MAP -> {
                if (rootNode.dataType != NodeDataType.NonPrimitive.MAP) {
                    throw FieldTypeMismatchException(rootNode, descriptor.serialName)
                }
                return MapCompositeDecoder(rootNode.children[0], dataReader)
            }
            StructureKind.CLASS -> {
                if (rootNode.dataType != NodeDataType.NonPrimitive.COMPOSITE) {
                    throw FieldTypeMismatchException(rootNode, descriptor.serialName)
                }
                if (descriptor.annotations.any { it.annotationClass == SequentiallyDeclared::class }) {
                    return SequentialClassCompositeDecoder(rootNode, dataReader)
                }
                return UnorderedClassCompositeDecoder(rootNode, dataReader)
            }
            else -> throw FieldTypeMismatchException(rootNode, descriptor.serialName)
        }
    }

    @ExperimentalSerializationApi
    override fun decodeNotNullMark() = true

    @ExperimentalSerializationApi
    override fun decodeNull(): Nothing {
        throw IllegalStateException("Should never reach here.")
    }

    override fun decodeBoolean(): Boolean {
        if (rootNode.dataType != NodeDataType.NonPrimitive.BOOL) {
            throw FieldTypeMismatchException(rootNode, "Boolean")
        }
        return rootNode.readBoolean(dataReader)
    }

    override fun decodeByte(): Byte {
        if (
            rootNode.dataType != NodeDataType.SIntType.INT8 &&
            rootNode.dataType != NodeDataType.UIntType.UINT8
        ) {
            throw FieldTypeMismatchException(rootNode, "Byte/UByte")
        }
        return rootNode.readByte(dataReader)
    }

    override fun decodeShort(): Short {
        if (
            rootNode.dataType != NodeDataType.SIntType.INT16 &&
            rootNode.dataType != NodeDataType.UIntType.UINT16
        ) {
            throw FieldTypeMismatchException(rootNode, "Short/UShort")
        }
        return rootNode.readShort(dataReader)
    }

    override fun decodeChar(): Char {
        if (rootNode.dataType !is NodeDataType.CharType) {
            throw FieldTypeMismatchException(rootNode, "Char")
        }
        return rootNode.readChar(dataReader)
    }

    override fun decodeInt(): Int {
        if (
            rootNode.dataType != NodeDataType.SIntType.INT32 &&
            rootNode.dataType != NodeDataType.UIntType.UINT32
        ) {
            throw FieldTypeMismatchException(rootNode, "Int/UInt")
        }
        return rootNode.readInt(dataReader)
    }

    override fun decodeLong(): Long {
        if (
            rootNode.dataType != NodeDataType.SIntType.INT64 &&
            rootNode.dataType != NodeDataType.UIntType.UINT64
        ) {
            throw FieldTypeMismatchException(rootNode, "Long/ULong")
        }
        return rootNode.readLong(dataReader)
    }

    override fun decodeFloat(): Float {
        if (rootNode.dataType != NodeDataType.DecimalType.FLOAT) {
            throw FieldTypeMismatchException(rootNode, "Float")
        }
        return rootNode.readFloat(dataReader)
    }

    override fun decodeDouble(): Double {
        if (rootNode.dataType != NodeDataType.DecimalType.DOUBLE) {
            throw FieldTypeMismatchException(rootNode, "Double")
        }
        return rootNode.readDouble(dataReader)
    }

    override fun decodeString(): String {
        if (rootNode.dataType != NodeDataType.NonPrimitive.STRING) {
            throw FieldTypeMismatchException(rootNode, "String")
        }
        return rootNode.children[0].readString(dataReader)
    }

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        if (rootNode.dataType != NodeDataType.SIntType.INT32) {
            throw FieldTypeMismatchException(rootNode, "${enumDescriptor.serialName} (Int)")
        }
        val enumId = rootNode.readInt(dataReader)
        return NumericalEnum.getCompanion(enumDescriptor.serialName).of(enumId).ordinal
    }

    override fun decodeInline(descriptor: SerialDescriptor): Decoder {
        return this
    }
}
