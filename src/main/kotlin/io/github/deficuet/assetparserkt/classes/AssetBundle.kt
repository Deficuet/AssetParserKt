@file:Suppress("unused")

package io.github.deficuet.assetparserkt.classes

import io.github.deficuet.assetparserkt.SequentiallyDeclared
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AssetInfo(
    val preloadIndex: Int,
    val preloadSize: Int,
    val asset: PPtr
)

@Serializable
@SequentiallyDeclared
class AssetBundle(
    @SerialName("m_Name")                       val mName: String,
    @SerialName("m_PreloadTable")               val mPreloadTable: Array<PPtr>,
    @SerialName("m_Container")                  val mContainer: Map<String, LinkedHashSet<AssetInfo>>,
    @SerialName("m_MainAsset")                  val mMainAsset: AssetInfo,
    @SerialName("m_RuntimeCompatibility")       val mRuntimeCompatibility: UInt,
    @SerialName("m_AssetBundleName")            val mAssetBundleName: String,
    @SerialName("m_Dependencies")               val mDependencies: Array<String>,
    @SerialName("m_IsStreamedSceneAssetBundle") val mIsStreamedSceneAssetBundle: Boolean,
    @SerialName("m_ExplicitDataLayout")         val mExplicitDataLayout: Int,
    @SerialName("m_PathFlags")                  val mPathFlags: Int,
    @SerialName("m_SceneHashes")                val mSceneHashes: Map<String, LinkedHashSet<String>>
) {
    companion object: UnityObjectCompanion<AssetBundle>
}
