package io.github.deficuet.assetparserkt

import kotlinx.serialization.descriptors.SerialDescriptor

class FieldTypeMismatchException(msg: String): Exception(msg) {
    constructor(
        node: TypeTreeNode,
        descriptor: SerialDescriptor,
        index: Int,
        typeDesc: String
    ): this(
        StringBuffer()
            .append("In class ${descriptor.serialName} ")
            .append("field ${descriptor.getElementName(index)} ")
            .append("requires \"$typeDesc\"(kind ${descriptor.getElementDescriptor(index).kind}) ")
            .append("but the correspond node ${node.name}(${node.type}) ")
            .append("is identified as ${node.dataType} type.")
            .toString()
    )

    constructor(node: TypeTreeNode, typeDesc: String): this(
        StringBuffer()
            .append("UnityObjectDecoder: Field ${node.name} requires \"$typeDesc\", ")
            .append("but the node is identified as ${node.dataType} type.")
            .toString()
    )
}
