/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.ty.locationengine.swig;

public class TYLocationEngineJNI {
  public final static native long new_IPXBeacon__SWIG_0();
  public final static native long new_IPXBeacon__SWIG_1(String jarg1, int jarg2, int jarg3);
  public final static native void delete_IPXBeacon(long jarg1);
  public final static native int IPXBeacon_getMajor(long jarg1, IPXBeacon jarg1_);
  public final static native int IPXBeacon_getMinor(long jarg1, IPXBeacon jarg1_);
  public final static native String IPXBeacon_getUuid(long jarg1, IPXBeacon jarg1_);
  public final static native long hash_beacon_key_HashBeaconKey(long jarg1, hash_beacon_key jarg1_, long jarg2, IPXBeacon jarg2_);
  public final static native long new_hash_beacon_key();
  public final static native void delete_hash_beacon_key(long jarg1);
  public final static native boolean equal_beacon_key_EqualBeaconKey(long jarg1, equal_beacon_key jarg1_, long jarg2, IPXBeacon jarg2_, long jarg3, IPXBeacon jarg3_);
  public final static native long new_equal_beacon_key();
  public final static native void delete_equal_beacon_key(long jarg1);
  public final static native long new_IPXPublicBeacon__SWIG_0();
  public final static native long new_IPXPublicBeacon__SWIG_1(String jarg1, int jarg2, int jarg3, long jarg4, IPXPoint jarg4_);
  public final static native void delete_IPXPublicBeacon(long jarg1);
  public final static native long IPXPublicBeacon_getLocation(long jarg1, IPXPublicBeacon jarg1_);
  public final static native void IPXPublicBeacon_setLocation(long jarg1, IPXPublicBeacon jarg1_, long jarg2, IPXPoint jarg2_);
  public final static native long new_IPXPoint__SWIG_0();
  public final static native long new_IPXPoint__SWIG_1(double jarg1, double jarg2);
  public final static native long new_IPXPoint__SWIG_2(double jarg1, double jarg2, int jarg3);
  public final static native long new_IPXPoint__SWIG_3(long jarg1, IPXPoint jarg1_);
  public final static native long IPXPoint_assignment_point(long jarg1, IPXPoint jarg1_, long jarg2, IPXPoint jarg2_);
  public final static native void delete_IPXPoint(long jarg1);
  public final static native int IPXPoint_getFloor(long jarg1, IPXPoint jarg1_);
  public final static native void IPXPoint_setFloor(long jarg1, IPXPoint jarg1_, int jarg2);
  public final static native double IPXPoint_getX(long jarg1, IPXPoint jarg1_);
  public final static native double IPXPoint_getY(long jarg1, IPXPoint jarg1_);
  public final static native double IPXPoint_DistanceBetween(long jarg1, IPXPoint jarg1_, long jarg2, IPXPoint jarg2_);
  public final static native double IPXPoint_distanceBetween(long jarg1, IPXPoint jarg1_, long jarg2, IPXPoint jarg2_);
  public final static native boolean IPXPoint_equal_point(long jarg1, IPXPoint jarg1_, long jarg2, IPXPoint jarg2_);
  public final static native boolean IPXPoint_not_equal_point(long jarg1, IPXPoint jarg1_, long jarg2, IPXPoint jarg2_);
  public final static native void INVALID_POINT_set(long jarg1, IPXPoint jarg1_);
  public final static native long INVALID_POINT_get();
  public final static native int IPXProximityUnknwon_get();
  public final static native long new_IPXScannedBeacon(String jarg1, int jarg2, int jarg3, int jarg4, double jarg5, int jarg6);
  public final static native void delete_IPXScannedBeacon(long jarg1);
  public final static native int IPXScannedBeacon_getRssi(long jarg1, IPXScannedBeacon jarg1_);
  public final static native double IPXScannedBeacon_getAccuracy(long jarg1, IPXScannedBeacon jarg1_);
  public final static native int IPXScannedBeacon_getProximity(long jarg1, IPXScannedBeacon jarg1_);
  public final static native long new_VectorOfPublicBeacon__SWIG_0();
  public final static native long new_VectorOfPublicBeacon__SWIG_1(long jarg1);
  public final static native long VectorOfPublicBeacon_size(long jarg1, VectorOfPublicBeacon jarg1_);
  public final static native long VectorOfPublicBeacon_capacity(long jarg1, VectorOfPublicBeacon jarg1_);
  public final static native void VectorOfPublicBeacon_reserve(long jarg1, VectorOfPublicBeacon jarg1_, long jarg2);
  public final static native boolean VectorOfPublicBeacon_isEmpty(long jarg1, VectorOfPublicBeacon jarg1_);
  public final static native void VectorOfPublicBeacon_clear(long jarg1, VectorOfPublicBeacon jarg1_);
  public final static native void VectorOfPublicBeacon_add(long jarg1, VectorOfPublicBeacon jarg1_, long jarg2, IPXPublicBeacon jarg2_);
  public final static native long VectorOfPublicBeacon_get(long jarg1, VectorOfPublicBeacon jarg1_, int jarg2);
  public final static native void VectorOfPublicBeacon_set(long jarg1, VectorOfPublicBeacon jarg1_, int jarg2, long jarg3, IPXPublicBeacon jarg3_);
  public final static native void delete_VectorOfPublicBeacon(long jarg1);
  public final static native long new_VectorOfScannedBeaconPointer__SWIG_0();
  public final static native long new_VectorOfScannedBeaconPointer__SWIG_1(long jarg1);
  public final static native long VectorOfScannedBeaconPointer_size(long jarg1, VectorOfScannedBeaconPointer jarg1_);
  public final static native long VectorOfScannedBeaconPointer_capacity(long jarg1, VectorOfScannedBeaconPointer jarg1_);
  public final static native void VectorOfScannedBeaconPointer_reserve(long jarg1, VectorOfScannedBeaconPointer jarg1_, long jarg2);
  public final static native boolean VectorOfScannedBeaconPointer_isEmpty(long jarg1, VectorOfScannedBeaconPointer jarg1_);
  public final static native void VectorOfScannedBeaconPointer_clear(long jarg1, VectorOfScannedBeaconPointer jarg1_);
  public final static native void VectorOfScannedBeaconPointer_add(long jarg1, VectorOfScannedBeaconPointer jarg1_, long jarg2, IPXScannedBeacon jarg2_);
  public final static native long VectorOfScannedBeaconPointer_get(long jarg1, VectorOfScannedBeaconPointer jarg1_, int jarg2);
  public final static native void VectorOfScannedBeaconPointer_set(long jarg1, VectorOfScannedBeaconPointer jarg1_, int jarg2, long jarg3, IPXScannedBeacon jarg3_);
  public final static native void delete_VectorOfScannedBeaconPointer(long jarg1);
  public final static native void ILocationEngine_Initilize(long jarg1, ILocationEngine jarg1_, long jarg2, VectorOfPublicBeacon jarg2_, String jarg3);
  public final static native void ILocationEngine_processBeacons(long jarg1, ILocationEngine jarg1_, long jarg2, VectorOfScannedBeaconPointer jarg2_);
  public final static native void ILocationEngine_addStepEvent(long jarg1, ILocationEngine jarg1_);
  public final static native void ILocationEngine_reset(long jarg1, ILocationEngine jarg1_);
  public final static native long ILocationEngine_getLocation(long jarg1, ILocationEngine jarg1_);
  public final static native long ILocationEngine_getImmediateLocation(long jarg1, ILocationEngine jarg1_);
  public final static native void delete_ILocationEngine(long jarg1);
  public final static native long CreateIPXStepBaseTriangulationEngine(int jarg1);
  public final static native long IPXPublicBeacon_SWIGUpcast(long jarg1);
  public final static native long IPXScannedBeacon_SWIGUpcast(long jarg1);
}
