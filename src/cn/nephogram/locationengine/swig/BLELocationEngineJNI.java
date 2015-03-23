/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package cn.nephogram.locationengine.swig;

public class BLELocationEngineJNI {
  public final static native long new_NPXBeacon__SWIG_0();
  public final static native long new_NPXBeacon__SWIG_1(String jarg1, int jarg2, int jarg3);
  public final static native void delete_NPXBeacon(long jarg1);
  public final static native int NPXBeacon_getMajor(long jarg1, NPXBeacon jarg1_);
  public final static native int NPXBeacon_getMinor(long jarg1, NPXBeacon jarg1_);
  public final static native String NPXBeacon_getUuid(long jarg1, NPXBeacon jarg1_);
  public final static native long hash_beacon_key_hash_beacon_key(long jarg1, hash_beacon_key jarg1_, long jarg2, NPXBeacon jarg2_);
  public final static native void delete_hash_beacon_key(long jarg1);
  public final static native boolean equal_beacon_key_equal_beacon_key(long jarg1, equal_beacon_key jarg1_, long jarg2, NPXBeacon jarg2_, long jarg3, NPXBeacon jarg3_);
  public final static native void delete_equal_beacon_key(long jarg1);
  public final static native long new_NPXPublicBeacon__SWIG_0();
  public final static native long new_NPXPublicBeacon__SWIG_1(String jarg1, int jarg2, int jarg3, long jarg4, NPXPoint jarg4_);
  public final static native void delete_NPXPublicBeacon(long jarg1);
  public final static native long NPXPublicBeacon_getLocation(long jarg1, NPXPublicBeacon jarg1_);
  public final static native void NPXPublicBeacon_setLocation(long jarg1, NPXPublicBeacon jarg1_, long jarg2, NPXPoint jarg2_);
  public final static native long new_NPXPoint__SWIG_0();
  public final static native long new_NPXPoint__SWIG_1(double jarg1, double jarg2);
  public final static native long new_NPXPoint__SWIG_2(double jarg1, double jarg2, int jarg3);
  public final static native long new_NPXPoint__SWIG_3(long jarg1, NPXPoint jarg1_);
  public final static native long NPXPoint_assignment_point(long jarg1, NPXPoint jarg1_, long jarg2, NPXPoint jarg2_);
  public final static native void delete_NPXPoint(long jarg1);
  public final static native int NPXPoint_getFloor(long jarg1, NPXPoint jarg1_);
  public final static native void NPXPoint_setFloor(long jarg1, NPXPoint jarg1_, int jarg2);
  public final static native double NPXPoint_getX(long jarg1, NPXPoint jarg1_);
  public final static native double NPXPoint_getY(long jarg1, NPXPoint jarg1_);
  public final static native double NPXPoint_DistanceBetween(long jarg1, NPXPoint jarg1_, long jarg2, NPXPoint jarg2_);
  public final static native double NPXPoint_distanceBetween(long jarg1, NPXPoint jarg1_, long jarg2, NPXPoint jarg2_);
  public final static native boolean NPXPoint_equal_point(long jarg1, NPXPoint jarg1_, long jarg2, NPXPoint jarg2_);
  public final static native boolean NPXPoint_not_equal_point(long jarg1, NPXPoint jarg1_, long jarg2, NPXPoint jarg2_);
  public final static native void INVALID_POINT_set(long jarg1, NPXPoint jarg1_);
  public final static native long INVALID_POINT_get();
  public final static native int NPXProximityUnknwon_get();
  public final static native long new_NPXScannedBeacon(String jarg1, int jarg2, int jarg3, int jarg4, double jarg5, int jarg6);
  public final static native void delete_NPXScannedBeacon(long jarg1);
  public final static native int NPXScannedBeacon_getRssi(long jarg1, NPXScannedBeacon jarg1_);
  public final static native double NPXScannedBeacon_getAccuracy(long jarg1, NPXScannedBeacon jarg1_);
  public final static native int NPXScannedBeacon_getProximity(long jarg1, NPXScannedBeacon jarg1_);
  public final static native long new_VectorOfPublicBeacon__SWIG_0();
  public final static native long new_VectorOfPublicBeacon__SWIG_1(long jarg1);
  public final static native long VectorOfPublicBeacon_size(long jarg1, VectorOfPublicBeacon jarg1_);
  public final static native long VectorOfPublicBeacon_capacity(long jarg1, VectorOfPublicBeacon jarg1_);
  public final static native void VectorOfPublicBeacon_reserve(long jarg1, VectorOfPublicBeacon jarg1_, long jarg2);
  public final static native boolean VectorOfPublicBeacon_isEmpty(long jarg1, VectorOfPublicBeacon jarg1_);
  public final static native void VectorOfPublicBeacon_clear(long jarg1, VectorOfPublicBeacon jarg1_);
  public final static native void VectorOfPublicBeacon_add(long jarg1, VectorOfPublicBeacon jarg1_, long jarg2, NPXPublicBeacon jarg2_);
  public final static native long VectorOfPublicBeacon_get(long jarg1, VectorOfPublicBeacon jarg1_, int jarg2);
  public final static native void VectorOfPublicBeacon_set(long jarg1, VectorOfPublicBeacon jarg1_, int jarg2, long jarg3, NPXPublicBeacon jarg3_);
  public final static native void delete_VectorOfPublicBeacon(long jarg1);
  public final static native long new_VectorOfScannedBeaconPointer__SWIG_0();
  public final static native long new_VectorOfScannedBeaconPointer__SWIG_1(long jarg1);
  public final static native long VectorOfScannedBeaconPointer_size(long jarg1, VectorOfScannedBeaconPointer jarg1_);
  public final static native long VectorOfScannedBeaconPointer_capacity(long jarg1, VectorOfScannedBeaconPointer jarg1_);
  public final static native void VectorOfScannedBeaconPointer_reserve(long jarg1, VectorOfScannedBeaconPointer jarg1_, long jarg2);
  public final static native boolean VectorOfScannedBeaconPointer_isEmpty(long jarg1, VectorOfScannedBeaconPointer jarg1_);
  public final static native void VectorOfScannedBeaconPointer_clear(long jarg1, VectorOfScannedBeaconPointer jarg1_);
  public final static native void VectorOfScannedBeaconPointer_add(long jarg1, VectorOfScannedBeaconPointer jarg1_, long jarg2, NPXScannedBeacon jarg2_);
  public final static native long VectorOfScannedBeaconPointer_get(long jarg1, VectorOfScannedBeaconPointer jarg1_, int jarg2);
  public final static native void VectorOfScannedBeaconPointer_set(long jarg1, VectorOfScannedBeaconPointer jarg1_, int jarg2, long jarg3, NPXScannedBeacon jarg3_);
  public final static native void delete_VectorOfScannedBeaconPointer(long jarg1);
  public final static native void ILocationEngine_Initilize(long jarg1, ILocationEngine jarg1_, long jarg2, VectorOfPublicBeacon jarg2_);
  public final static native void ILocationEngine_processBeacons(long jarg1, ILocationEngine jarg1_, long jarg2, VectorOfScannedBeaconPointer jarg2_);
  public final static native void ILocationEngine_addStepEvent(long jarg1, ILocationEngine jarg1_);
  public final static native long ILocationEngine_getLocation(long jarg1, ILocationEngine jarg1_);
  public final static native void delete_ILocationEngine(long jarg1);
  public final static native long CreateNPXStepBaseTriangulationEngine(int jarg1);
  public final static native long NPXPublicBeacon_SWIGUpcast(long jarg1);
  public final static native long NPXScannedBeacon_SWIGUpcast(long jarg1);
}
