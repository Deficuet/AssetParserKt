package io.github.deficuet.assetparserkt

import java.nio.ByteBuffer

fun ByteBuffer.alignStream(alignment: Int = 4) {
    val currPos = position()
    position(currPos + ((alignment - (currPos % alignment)) % alignment))
}
