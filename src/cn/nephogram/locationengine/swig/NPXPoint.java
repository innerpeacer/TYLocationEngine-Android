/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package cn.nephogram.locationengine.swig;

public class NPXPoint {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected NPXPoint(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(NPXPoint obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        BLELocationEngineJNI.delete_NPXPoint(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public NPXPoint() {
    this(BLELocationEngineJNI.new_NPXPoint__SWIG_0(), true);
  }

  public NPXPoint(double px, double py) {
    this(BLELocationEngineJNI.new_NPXPoint__SWIG_1(px, py), true);
  }

  public NPXPoint(double px, double py, int pf) {
    this(BLELocationEngineJNI.new_NPXPoint__SWIG_2(px, py, pf), true);
  }

  public NPXPoint(NPXPoint p) {
    this(BLELocationEngineJNI.new_NPXPoint__SWIG_3(NPXPoint.getCPtr(p), p), true);
  }

  public NPXPoint assignment_point(NPXPoint p) {
    return new NPXPoint(BLELocationEngineJNI.NPXPoint_assignment_point(swigCPtr, this, NPXPoint.getCPtr(p), p), true);
  }

  public int getFloor() {
    return BLELocationEngineJNI.NPXPoint_getFloor(swigCPtr, this);
  }

  public void setFloor(int floor) {
    BLELocationEngineJNI.NPXPoint_setFloor(swigCPtr, this, floor);
  }

  public double getX() {
    return BLELocationEngineJNI.NPXPoint_getX(swigCPtr, this);
  }

  public double getY() {
    return BLELocationEngineJNI.NPXPoint_getY(swigCPtr, this);
  }

  public static double DistanceBetween(NPXPoint p1, NPXPoint p2) {
    return BLELocationEngineJNI.NPXPoint_DistanceBetween(NPXPoint.getCPtr(p1), p1, NPXPoint.getCPtr(p2), p2);
  }

  public double distanceBetween(NPXPoint p2) {
    return BLELocationEngineJNI.NPXPoint_distanceBetween(swigCPtr, this, NPXPoint.getCPtr(p2), p2);
  }

  public boolean equal_point(NPXPoint aPoint) {
    return BLELocationEngineJNI.NPXPoint_equal_point(swigCPtr, this, NPXPoint.getCPtr(aPoint), aPoint);
  }

  public boolean not_equal_point(NPXPoint aPoint) {
    return BLELocationEngineJNI.NPXPoint_not_equal_point(swigCPtr, this, NPXPoint.getCPtr(aPoint), aPoint);
  }

}
