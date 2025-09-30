package io.github.deficuet.assetparserkt

import io.github.deficuet.assetparserkt.enums.NodeDataType
import io.github.deficuet.assetparserkt.math.Matrix4x4f
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.nio.ByteBuffer

@Serializable
data class TypeTreeNode(
    val type: String,
    val name: String,
    val byteSize: Int,
    val index: Int,
    val typeFlags: Int,
    val version: Int,
    val metaFlag: Int,
    val level: Int
) {
    @Transient
    val children = mutableListOf<TypeTreeNode>()

    @Transient
    var dataType: NodeDataType = NodeDataType.NonPrimitive.UNKNOWN
        private set
        get() {
            return if (field == NodeDataType.NonPrimitive.UNKNOWN) {
                field = NodeDataType.of(this)
                field
            } else field
        }

    private inline fun <T> withReader(
        dataReader: ByteBuffer,
        crossinline readerBlock: ByteBuffer.() -> T
    ): T {
        val ret = dataReader.readerBlock()
        if (metaFlag.and(0x4000) != 0) {
            dataReader.alignStream()
        }
        return ret
    }

    fun readBoolean(dataReader: ByteBuffer) = withReader(dataReader) {
        val value = get().toUByte()
        value > 0u
    }

    fun readByte(dataReader: ByteBuffer) = withReader(dataReader) {
        get()
    }

    fun readChar(dataReader: ByteBuffer) = withReader(dataReader) {
        if (dataType == NodeDataType.CharType.WIDE_CHAR) {
            val data = getShort()
            Char(data.toUShort())
        } else {
            val data = get()
            data.toInt().toChar()
        }
    }

    fun readShort(dataReader: ByteBuffer) = withReader(dataReader) {
        getShort()
    }

    fun readInt(dataReader: ByteBuffer) = withReader(dataReader) {
        getInt()
    }

    fun readLong(dataReader: ByteBuffer) = withReader(dataReader) {
        getLong()
    }

    fun readFloat(dataReader: ByteBuffer) = withReader(dataReader) {
        getFloat()
    }

    fun readDouble(dataReader: ByteBuffer) = withReader(dataReader) {
        getDouble()
    }

    /**
     * The node invoking this method must be the Array node inside a string node
     */
    fun readString(dataReader: ByteBuffer) = withReader(dataReader) {
        val sizeNode = children[0]
        val charNode = children[1]
        val size = sizeNode.readInt(this)
        val byteSize = size * charNode.byteSize
        val strBuf = slice(position(), byteSize)
            .order(order())
        val ret = if (charNode.dataType == NodeDataType.CharType.WIDE_CHAR) {
            Charsets.UTF_16LE.decode(strBuf).toString()
        } else {
            Charsets.UTF_8.decode(strBuf).toString()
        }
        position(position() + byteSize)
        ret
    }

    fun readMatrix4x4f(dataReader: ByteBuffer) = withReader(dataReader) {
        val fb = asFloatBuffer()
        val fa = FloatArray(16)
        fb.get(fa)
        val matrix = Matrix4x4f(fa)
        position(position() + 64)
        matrix
    }
}
