/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.ty.locationengine.swig;

public class IPXBeacon {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected IPXBeacon(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(IPXBeacon obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        TYLocationEngineJNI.delete_IPXBeacon(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public IPXBeacon() {
    this(TYLocationEngineJNI.new_IPXBeacon__SWIG_0(), true);
  }

  public IPXBeacon(String uuid, int major, int minor) {
    this(TYLocationEngineJNI.new_IPXBeacon__SWIG_1(uuid, major, minor), true);
  }

  public int getMajor() {
    return TYLocationEngineJNI.IPXBeacon_getMajor(swigCPtr, this);
  }

  public int getMinor() {
    return TYLocationEngineJNI.IPXBeacon_getMinor(swigCPtr, this);
  }

  public String getUuid() {
    return TYLocationEngineJNI.IPXBeacon_getUuid(swigCPtr, this);
  }

}
