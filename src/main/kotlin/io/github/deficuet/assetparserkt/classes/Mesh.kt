@file:Suppress("unused")

package io.github.deficuet.assetparserkt.classes

import io.github.deficuet.assetparserkt.math.Matrix4x4f
import io.github.deficuet.assetparserkt.math.Vector3f
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SubMash(
    val firstByte: UInt,
    val indexCount: UInt,
    val topology: Int,
    val baseVertex: UInt,
    val firstVertex: UInt,
    val vertexCount: UInt,
    val localAABB: AABB,
)

@Serializable
class BlendShapeVertex(
    val vertex: Vector3f,
    val normal: Vector3f,
    val tangent: Vector3f,
    val index: UInt,
)

@Serializable
class MeshBlendShape(
    val firstVertex: UInt,
    val vertexCount: UInt,
    val hasNormals: Boolean,
    val hasTangents: Boolean,
)

@Serializable
class MeshBlendShapeChannel(
    val name: String,
    val nameHash: UInt,
    val frameIndex: Int,
    val frameCount: Int,
)

@Serializable
class BlendShapeData(
    val vertices: Array<BlendShapeVertex>,
    val shapes: Array<MeshBlendShape>,
    val channels: Array<MeshBlendShapeChannel>,
    val fullWeights: FloatArray,
)

@Serializable
class MinMaxAABB(
    @SerialName("m_Min") val mMin: Vector3f,
    @SerialName("m_Max") val mMax: Vector3f,
)

@Serializable
class VariableBoneCountWeights(
    @SerialName("m_Data") val mData: UIntArray,
)

@Serializable
class ChannelInfo(
    val stream: UByte,
    val offset: UByte,
    val format: UByte,
    val dimension: UByte,
)

@Serializable
class VertexData(
    @SerialName("m_VertexCount") val mVertexCount: UInt,
    @SerialName("m_Channels") val mChannels: Array<ChannelInfo>,
    @SerialName("m_DataSize") val mDataSize: UByteArray,
)

@Serializable
class CompressedMesh(
    @SerialName("m_Vertices") val mVertices: PackedFloatVector,
    @SerialName("m_UV") val mUV: PackedFloatVector,
    @SerialName("m_Normals") val mNormals: PackedFloatVector,
    @SerialName("m_Tangents") val mTangents: PackedFloatVector,
    @SerialName("m_Weights") val mWeights: PackedIntVector,
    @SerialName("m_NormalSigns") val mNormalSigns: PackedIntVector,
    @SerialName("m_TangentSigns") val mTangentSigns: PackedIntVector,
    @SerialName("m_FloatColors") val mFloatColors: PackedFloatVector,
    @SerialName("m_BoneIndices") val mBoneIndices: PackedIntVector,
    @SerialName("m_Triangles") val mTriangles: PackedIntVector,
    @SerialName("m_UVInfo") val mUVInfo: UInt,
)

@Serializable
class Mesh(
    @SerialName("m_Name") val mName: String,
    @SerialName("m_SubMeshes") val mSubMeshes: Array<SubMash>,
    @SerialName("m_Shapes") val mShapes: BlendShapeData,
    @SerialName("m_BindPose") val mBindPose: Array<Matrix4x4f>,
    @SerialName("m_BoneNameHashes") val mBoneNameHashes: UIntArray,
    @SerialName("m_RootBoneNameHash") val mRootBoneNameHash: UInt,
    @SerialName("m_BonesAABB") val mBonesAABB: Array<MinMaxAABB>,
    @SerialName("m_VariableBoneCountWeights") val mVariableBoneCountWeights: VariableBoneCountWeights,
    @SerialName("m_MeshCompression") val mMeshCompression: UByte,
    @SerialName("m_IsReadable") val mIsReadable: Boolean,
    @SerialName("m_KeepVertices") val mKeepVertices: Boolean,
    @SerialName("m_KeepIndices") val mKeepIndices: Boolean,
    @SerialName("m_IndexFormat") val mIndexFormat: Int,
    @SerialName("m_IndexBuffer") val mIndexBuffer: UByteArray,
    @SerialName("m_VertexData") val mVertexData: VertexData,
    @SerialName("m_CompressedMesh") val mCompressedMesh: CompressedMesh,
    @SerialName("m_LocalAABB") val mLocalAABB: AABB,
    @SerialName("m_MeshUsageFlags") val mMeshUsageFlags: Int,
    @SerialName("m_CookingOptions") val mCookingOptions: Int,
    @SerialName("m_BakedConvexCollisionMesh") val mBakedConvexCollisionMesh: UByteArray,
    @SerialName("m_BakedTriangleCollisionMesh") val mBakedTriangleCollisionMesh: UByteArray,
    @SerialName("m_MeshMetrics[0]") val mMeshMetrics0: Float,
    @SerialName("m_MeshMetrics[1]") val mMeshMetrics1: Float,
    @SerialName("m_StreamData") val mStreamData: StreamingInfo,
)
