/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package cn.nephogram.locationengine.swig;

public class ILocationEngine {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected ILocationEngine(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ILocationEngine obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        BLELocationEngineJNI.delete_ILocationEngine(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void Initilize(VectorOfPublicBeacon beacons) {
    BLELocationEngineJNI.ILocationEngine_Initilize(swigCPtr, this, VectorOfPublicBeacon.getCPtr(beacons), beacons);
  }

  public void processBeacons(VectorOfScannedBeaconPointer beacons) {
    BLELocationEngineJNI.ILocationEngine_processBeacons(swigCPtr, this, VectorOfScannedBeaconPointer.getCPtr(beacons), beacons);
  }

  public void addStepEvent() {
    BLELocationEngineJNI.ILocationEngine_addStepEvent(swigCPtr, this);
  }

  public void reset() {
    BLELocationEngineJNI.ILocationEngine_reset(swigCPtr, this);
  }

  public NPXPoint getLocation() {
    return new NPXPoint(BLELocationEngineJNI.ILocationEngine_getLocation(swigCPtr, this), true);
  }

}
