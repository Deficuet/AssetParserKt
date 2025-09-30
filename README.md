# AssetParserKt

**This is a sample repository, may not be used as a library.**

A Unity type tree parser based on [Kotlin serialization plugin](https://github.com/Kotlin/kotlinx.serialization). The parser only relies on the compiler-generated default serializers. 

All things to do is to declare the Unity Object class with annotation `@Serializable` (and optional `@SerialName` on properties). Zero code for reading the Unity Object.
