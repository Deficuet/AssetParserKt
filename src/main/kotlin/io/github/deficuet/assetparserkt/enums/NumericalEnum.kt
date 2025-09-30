package io.github.deficuet.assetparserkt.enums

import kotlinx.serialization.serializer
import kotlin.enums.EnumEntries

interface NumericalEnum {
    val id: Int

    companion object {
        private val serialNameCompanionMap = mutableMapOf<String, NumericalEnumCompanion<*>>()

        fun cacheCompanion(serialName: String, companion: NumericalEnumCompanion<*>) {
            serialNameCompanionMap[serialName] = companion
        }

        fun getCompanion(serialName: String) = serialNameCompanionMap.getValue(serialName)
    }
}

interface NumericalEnumMap<E: Enum<E>> {
    fun of(value: Int): E
}

abstract class NumericalEnumCompanion<E>(
    enumValues: EnumEntries<E>,
    private val default: E
): NumericalEnumMap<E> where E: Enum<E>, E: NumericalEnum {
    val cacheTable: Map<Int, E> = enumValues.associateBy { it.id }

    override fun of(value: Int): E = cacheTable[value] ?: default
}

inline fun <reified E> NumericalEnumCompanion<E>.cacheCompanion()
where E: Enum<E>, E: NumericalEnum {
    NumericalEnum.cacheCompanion(serializer<E>().descriptor.serialName, this)
}
