package io.github.deficuet.assetparserkt

import io.github.deficuet.assetparserkt.classes.AnimationClip
import io.github.deficuet.assetparserkt.classes.AssetBundle
import io.github.deficuet.assetparserkt.classes.Mesh
import io.github.deficuet.assetparserkt.classes.Texture2D
import io.github.deficuet.assetparserkt.decoder.UnityObjectDecoder
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun main() {
    val testClassName = "Mesh"
    val typeTreeRoot = createNodeTree(
        Json.decodeFromString(
            ListSerializer(TypeTreeNode.serializer()),
            File("./sample/${testClassName}/tree.json")
                .readText()
        )
    )
    val objectData = File("./sample/${testClassName}/data.dat").readBytes()
    val objBuffer = ByteBuffer.wrap(objectData).order(ByteOrder.LITTLE_ENDIAN)
    val mark = System.currentTimeMillis()
    val obj = Mesh.serializer()
        .deserialize(
            UnityObjectDecoder(
                typeTreeRoot,
                objBuffer
            )
        )
    println("Deserialize spent ${System.currentTimeMillis() - mark} ms")
    val json = Json {
        prettyPrint = true
        prettyPrintIndent = "    "
    }
    File("./sample/${testClassName}/export.json")
        .writeText(json.encodeToString(obj))
}

fun createNodeTree(nodeList: List<TypeTreeNode>): TypeTreeNode {
    if (nodeList.isEmpty()) {
        throw IllegalStateException("Type tree must not be empty")
    }
    val baseLevel = nodeList[0].level
    val rootList = mutableListOf<TypeTreeNode>()
    val nodeStack = ArrayDeque<TypeTreeNode>(nodeList.size)
    for (node in nodeList) {
        if (node.level == baseLevel) {
            rootList.add(node)
            nodeStack.addLast(node)
            continue
        }
        val lastNode = nodeStack.last()
        if (node.level > lastNode.level) {
            lastNode.children.add(node)
        } else {
            var top: TypeTreeNode
            do {
                nodeStack.removeLast()
                top = nodeStack.last()
            } while (node.level <= top.level)
            top.children.add(node)
        }
        nodeStack.addLast(node)
    }
    nodeStack.clear()
    if (rootList.size != 1) {
        throw IllegalStateException("Type tree should have only one root node")
    }
    return rootList[0]
}
