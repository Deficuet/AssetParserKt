@file:Suppress("unused")

package io.github.deficuet.assetparserkt.classes

import io.github.deficuet.assetparserkt.SequentiallyDeclared
import io.github.deficuet.assetparserkt.enums.ClassIDType
import io.github.deficuet.assetparserkt.math.Quaternionf
import io.github.deficuet.assetparserkt.math.Vector3f
import io.github.deficuet.assetparserkt.math.Vector4f
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PPtr(
    @SerialName("m_FileID") val mFileID: Int,
    @SerialName("m_PathID") val mPathID: Long
)

@Serializable
class Keyframe<T>(
    @SerialName("time")             val time: Float,
    @SerialName("value")            val value: T,
    @SerialName("inSlope")          val inSlope: T,
    @SerialName("outSlope")         val outSlope: T,
    @SerialName("weightedMode")     val weightedMode: Int,
    @SerialName("inWeight")         val inWeight: T,
    @SerialName("outWeight")        val outWeight: T
)

@Serializable
class AnimationCurve<T>(
    @SerialName("m_Curve")          val mCurve: Array<Keyframe<T>>,
    @SerialName("m_PreInfinity")    val mPreInfinity: Int,
    @SerialName("m_PostInfinity")   val mPostInfinity: Int,
    @SerialName("m_RotationOrder")  val mRotationOrder: Int
)

@Serializable
class QuaternionCurve(
    @SerialName("curve")    val curve: AnimationCurve<Quaternionf>,
    @SerialName("path")     val path: String
)

@Serializable
class PackedIntVector(
    @SerialName("m_NumItems") val mNumItems: UInt,
    @SerialName("m_Data") val mData: UByteArray,
    @SerialName("m_BitSize") val mBitSize: UByte
)

@Serializable
class PackedQuatVector(
    @SerialName("m_NumItems") val mNumItems: UInt,
    @SerialName("m_Data") val mData: UByteArray
)

@Serializable
class PackedFloatVector(
    @SerialName("m_NumItems") val mNumItems: UInt,
    @SerialName("m_Range") val mRange: Float,
    @SerialName("m_Start") val mStart: Float,
    @SerialName("m_Data") val mData: UByteArray,
    @SerialName("m_BitSize") val mBitSize: UByte
)

@Serializable
class CompressedAnimationCurve(
    @SerialName("m_Path") val mPath: String,
    @SerialName("m_Times") val mTimes: PackedIntVector,
    @SerialName("m_Values") val mValues: PackedQuatVector,
    @SerialName("m_Slopes") val mSlopes: PackedFloatVector,
    @SerialName("m_PreInfinity") val mPreInfinity: Int,
    @SerialName("m_PostInfinity") val mPostInfinity: Int
)

@Serializable
class Vector3Curve(
    @SerialName("curve") val curve: AnimationCurve<Vector3f>,
    @SerialName("path") val path: String
)

@Serializable
class FloatCurve(
    @SerialName("curve") val curve: AnimationCurve<Float>,
    @SerialName("attribute") val attribute: String,
    @SerialName("path") val path: String,
    @SerialName("classID") val classID: ClassIDType,
    @SerialName("script") val script: PPtr,
    @SerialName("flags") val flags: Int
)

@Serializable
class PPtrKeyframe(
    @SerialName("time") val time: Float,
    @SerialName("value") val value: PPtr
)

@Serializable
class PPtrCurve(
    @SerialName("curve") val curve: Array<PPtrKeyframe>,
    @SerialName("attribute") val attribute: String,
    @SerialName("path") val path: String,
    @SerialName("classID") val classID: ClassIDType,
    @SerialName("script") val script: PPtr,
    @SerialName("flags") val flags: Int
)

@Serializable
class AABB(
    @SerialName("m_Center") val mCenter: Vector3f,
    @SerialName("m_Extent") val mExtent: Vector3f
)

@Serializable
class XForm(
    @SerialName("t") val t: Vector3f,
    @SerialName("q") val q: Vector4f,
    @SerialName("s") val s: Vector3f
)

@Serializable
class HumanGoal(
    @SerialName("m_X") val mX: XForm,
    @SerialName("m_WeightT") val mWeightT: Float,
    @SerialName("m_WeightR") val mWeightR: Float,
    @SerialName("m_HintT") val mHintT: Vector3f,
    @SerialName("m_HintWeightT") val mHintWeightT: Float
)

@Serializable
class HandPose(
    @SerialName("m_GrabX") val mGrabX: XForm,
    @SerialName("m_DoFArray") val mDoFArray: FloatArray,
    @SerialName("m_Override") val mOverride: Float,
    @SerialName("m_CloseOpen") val mCloseOpen: Float,
    @SerialName("m_InOut") val mInOut: Float,
    @SerialName("m_Grab") val mGrab: Float,
)

@Serializable
class HumanPose(
    @SerialName("m_RootX") val mRootX: XForm,
    @SerialName("m_LookAtPosition") val mLookAtPosition: Vector3f,
    @SerialName("m_LookAtWeight") val mLookAtWeight: Vector4f,
    @SerialName("m_GoalArray") val mGoalArray: Array<HumanGoal>,
    @SerialName("m_LeftHandPose") val mLeftHandPose: HandPose,
    @SerialName("m_RightHandPose") val mRightHandPose: HandPose,
    @SerialName("m_DoFArray") val mDoFArray: FloatArray,
    @SerialName("m_TDoFArray") val mTDoFArray: Array<Vector3f>
)

@Serializable
class StreamedClip(
    @SerialName("data") val data: UIntArray,
    @SerialName("curveCount") val curveCount: UShort,
    @SerialName("discreteCurveCount") val discreteCurveCount: UShort
)

@Serializable
class DenseClip(
    @SerialName("m_FrameCount") val mFrameCount: Int,
    @SerialName("m_CurveCount") val mCurveCount: UInt,
    @SerialName("m_SampleRate") val mSampleRate: Float,
    @SerialName("m_BeginTime") val mBeginTime: Float,
    @SerialName("m_SampleArray") val mSampleArray: FloatArray
)

@Serializable
class ConstantClip(
    @SerialName("data") val data: FloatArray
)

@Serializable
class Clip(
    @SerialName("m_StreamedClip") val mStreamedClip: StreamedClip,
    @SerialName("m_DenseClip") val mDenseClip: DenseClip,
    @SerialName("m_ConstantClip") val mConstantClip: ConstantClip
)

@Serializable
class OffsetPtr(
    @SerialName("data") val data: Clip
)

@Serializable
class ValueDelta(
    @SerialName("m_Start") val mStart: Float,
    @SerialName("m_Stop") val mStop: Float
)

@Serializable
class ClipMuscleConstant(
    @SerialName("m_DeltaPose") val mDeltaPose: HumanPose,
    @SerialName("m_StartX") val mStartX: XForm,
    @SerialName("m_StopX") val mStopX: XForm,
    @SerialName("m_LeftFootStartX") val mLeftFootStartX: XForm,
    @SerialName("m_RightFootStartX") val mRightFootStartX: XForm,
    @SerialName("m_AverageSpeed") val mAverageSpeed: Vector3f,
    @SerialName("m_Clip") val mClip: OffsetPtr,
    @SerialName("m_StartTime") val mStartTime: Float,
    @SerialName("m_StopTime") val mStopTime: Float,
    @SerialName("m_OrientationOffsetY") val mOrientationOffsetY: Float,
    @SerialName("m_Level") val mLevel: Float,
    @SerialName("m_CycleOffset") val mCycleOffset: Float,
    @SerialName("m_AverageAngularSpeed") val mAverageAngularSpeed: Float,
    @SerialName("m_IndexArray") val mIndexArray: IntArray,
    @SerialName("m_ValueArrayDelta") val mValueArrayDelta: Array<ValueDelta>,
    @SerialName("m_ValueArrayReferencePose") val mValueArrayReferencePose: FloatArray,
    @SerialName("m_Mirror") val mMirror: Boolean,
    @SerialName("m_LoopTime") val mLoopTime: Boolean,
    @SerialName("m_LoopBlend") val mLoopBlend: Boolean,
    @SerialName("m_LoopBlendOrientation") val mLoopBlendOrientation: Boolean,
    @SerialName("m_LoopBlendPositionY") val mLoopBlendPositionY: Boolean,
    @SerialName("m_LoopBlendPositionXZ") val mLoopBlendPositionXZ: Boolean,
    @SerialName("m_StartAtOrigin") val mStartAtOrigin: Boolean,
    @SerialName("m_KeepOriginalOrientation") val mKeepOriginalOrientation: Boolean,
    @SerialName("m_KeepOriginalPositionY") val mKeepOriginalPositionY: Boolean,
    @SerialName("m_KeepOriginalPositionXZ") val mKeepOriginalPositionXZ: Boolean,
    @SerialName("m_HeightFromFeet") val mHeightFromFeet: Boolean,
)

@Serializable
class GenericBinding(
    @SerialName("path") val path: UInt,
    @SerialName("attribute") val attribute: UInt,
    @SerialName("script") val script: PPtr,
    @SerialName("typeID") val typeID: Int,
    @SerialName("customType") val customType: UByte,
    @SerialName("isPPtrCurve") val isPPtrCurve: UByte,
    @SerialName("isIntCurve") val isIntCurve: UByte,
    @SerialName("isSerializeReferenceCurve") val isSerializeReferenceCurve: UByte,
)

@Serializable
class AnimationClipBindingConstant(
    @SerialName("genericBindings") val genericBindings: Array<GenericBinding>,
    @SerialName("pptrCurveMapping") val pptrCurveMapping: Array<PPtr>
)

@Serializable
class AnimationEvent(
    @SerialName("time") val time: Float,
    @SerialName("functionName") val functionName: String,
    @SerialName("data") val data: String,
    @SerialName("objectReferenceParameter") val objectReferenceParameter: PPtr,
    @SerialName("floatParameter") val floatParameter: Float,
    @SerialName("intParameter") val intParameter: Int,
    @SerialName("messageOptions") val messageOptions: Int
)

interface UnityObjectCompanion<T>

@Serializable
@SequentiallyDeclared
class AnimationClip(
    @SerialName("m_Name")                   val mName: String,
    @SerialName("m_Legacy")                 val mLegacy: Boolean,
    @SerialName("m_Compressed")             val mCompressed: Boolean,
    @SerialName("m_UseHighQualityCurve")    val mUseHighQualityCurve: Boolean,
    @SerialName("m_RotationCurves")         val mRotationCurves: Array<QuaternionCurve>,
    @SerialName("m_CompressedRotationCurves") val mCompressedRotationCurves: Array<CompressedAnimationCurve>,
    @SerialName("m_EulerCurves")    val mEulerCurves: Array<Vector3Curve>,
    @SerialName("m_PositionCurves") val mPositionCurves: Array<Vector3Curve>,
    @SerialName("m_ScaleCurves") val mScaleCurves: Array<Vector3Curve>,
    @SerialName("m_FloatCurves") val mFloatCurves: Array<FloatCurve>,
    @SerialName("m_PPtrCurves") val mPPtrCurves: Array<PPtrCurve>,
    @SerialName("m_SampleRate") val mSampleRate: Float,
    @SerialName("m_WrapMode") val mWrapMode: Int,
    @SerialName("m_Bounds") val mBounds: AABB,
    @SerialName("m_MuscleClipSize") val mMuscleClipSize: UInt,
    @SerialName("m_MuscleClip") val mMuscleClip: ClipMuscleConstant,
    @SerialName("m_ClipBindingConstant") val mClipBindingConstant: AnimationClipBindingConstant,
    @SerialName("m_HasGenericRootTransform") val mHasGenericRootTransform: Boolean,
    @SerialName("m_HasMotionFloatCurves") val mHasMotionFloatCurves: Boolean,
    @SerialName("m_Events") val mEvents: Array<AnimationEvent>
) {
    companion object: UnityObjectCompanion<AnimationClip>
}
