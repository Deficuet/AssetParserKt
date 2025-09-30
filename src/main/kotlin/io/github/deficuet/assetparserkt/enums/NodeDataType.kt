package io.github.deficuet.assetparserkt.enums

import io.github.deficuet.assetparserkt.TypeTreeNode

sealed interface NodeDataType {
    enum class SIntType: NodeDataType {
        INT8,
        INT16,
        INT32,
        INT64,
    }

    enum class UIntType: NodeDataType {
        UINT8,
        UINT16,
        UINT32,
        UINT64,
    }

    enum class DecimalType: NodeDataType {
        FLOAT,
        DOUBLE,
    }

    enum class CharType: NodeDataType {
        CHAR,
        WIDE_CHAR,
    }

    enum class NonPrimitive: NodeDataType {
        BOOL,
        STRING,
        MATRIX4x4,
        MAP,
        PAIR,
        ARRAY,
        TYPELESS,
        COMPOSITE,
        UNKNOWN,
    }

    companion object {
        val SIGNED_INT_TYPES = setOf(
            "SInt8",
            "SInt16", "short",
            "SInt32", "int",
            "SInt64", "long long"
        )
        val UNSIGNED_INT_TYPES = setOf(
            "UInt8",
            "UInt16", "unsigned short",
            "UInt32", "unsigned int", "Type*",
            "UInt64", "unsigned long long", "FileSize"
        )
        val FLOAT_TYPES = setOf("float", "double")

        fun isSignedIntType(node: TypeTreeNode): Boolean {
            return node.type in SIGNED_INT_TYPES && node.children.isEmpty()
        }

        fun isUnsignedIntType(node: TypeTreeNode): Boolean {
            return node.type in UNSIGNED_INT_TYPES && node.children.isEmpty()
        }

        fun isDecimal(node: TypeTreeNode): Boolean {
            return node.type in FLOAT_TYPES && node.children.isEmpty()
        }

        fun isBool(node: TypeTreeNode): Boolean {
            return node.type == "bool" && node.byteSize == 1 && node.children.isEmpty()
        }

        fun isChar(node: TypeTreeNode): Boolean {
            return node.type == "char" && node.children.isEmpty()
        }

        fun isArray(node: TypeTreeNode): Boolean {
            if (node.children.size != 1 || node.children[0].type != "Array") return false
            val arrayNode = node.children[0]
            if (arrayNode.children.size != 2) return false
            return arrayNode.children[0].dataType == SIntType.INT32
        }

        fun isString(node: TypeTreeNode): Boolean {
            return node.type == "string" && isArray(node) &&
                    node.children[0].children[1].dataType is CharType
        }

        fun isMatrix4x4f(node: TypeTreeNode): Boolean {
            return node.type == "Matrix4x4f" && node.children.size == 16 &&
                    node.children.all { it.dataType == DecimalType.FLOAT }
        }

        fun isPair(node: TypeTreeNode): Boolean {
            return node.type == "pair" && node.children.size == 2
        }

        fun isMap(node: TypeTreeNode): Boolean {
            return node.type == "map" && isArray(node) &&
                    node.children[0].children[1].dataType == NonPrimitive.PAIR
        }

        fun isTypeless(node: TypeTreeNode): Boolean {
            if (node.type != "TypelessData") return false
            if (node.children.size != 2) return false
            if (node.children[0].dataType != SIntType.INT32) return false
            val elementType = node.children[1].dataType
            return elementType is SIntType || elementType is UIntType
        }

        fun of(node: TypeTreeNode): NodeDataType {
            return when {
                isSignedIntType(node) -> {
                    when (node.byteSize) {
                        1 -> SIntType.INT8
                        2 -> SIntType.INT16
                        4 -> SIntType.INT32
                        8 -> SIntType.INT64
                        else -> NonPrimitive.COMPOSITE
                    }
                }
                isUnsignedIntType(node) -> {
                    when (node.byteSize) {
                        1 -> UIntType.UINT8
                        2 -> UIntType.UINT16
                        4 -> UIntType.UINT32
                        8 -> UIntType.UINT64
                        else -> NonPrimitive.COMPOSITE
                    }
                }
                isDecimal(node) -> {
                    when (node.byteSize) {
                        4 -> DecimalType.FLOAT
                        8 -> DecimalType.DOUBLE
                        else -> NonPrimitive.COMPOSITE
                    }
                }
                isBool(node) -> NonPrimitive.BOOL
                isChar(node) -> {
                    when (node.byteSize) {
                        1 -> CharType.CHAR
                        2 -> CharType.WIDE_CHAR
                        else -> NonPrimitive.COMPOSITE
                    }
                }
                isString(node) -> NonPrimitive.STRING
                isMatrix4x4f(node) -> NonPrimitive.MATRIX4x4
                isPair(node) -> NonPrimitive.PAIR
                isMap(node) -> NonPrimitive.MAP
                isArray(node) -> NonPrimitive.ARRAY
                isTypeless(node) -> NonPrimitive.TYPELESS
                else -> NonPrimitive.COMPOSITE
            }
        }
    }
}
