LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := prebuilt/$(TARGET_ARCH_ABI)/libruntimecore_java.so
LOCAL_MODULE    := arcgis

include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := prebuilt/$(TARGET_ARCH_ABI)/libTYMapSDK.so
LOCAL_MODULE    := TYMapSDK

include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_CPPFLAGS += -std=gnu++11 -fexceptions -frtti
LOCAL_LDLIBS+= -L$(SYSROOT)/usr/lib -llog

LOCAL_MODULE    := TYLocationEngine
LOCAL_SRC_FILES := TYLocationEngine_wrap.cxx \
					LocationEngine/src/Entity/IPXBeacon.cpp \
					LocationEngine/src/Entity/IPXPoint.cpp \
					LocationEngine/src/Entity/IPXPublicBeacon.cpp \
					LocationEngine/src/Entity/IPXScannedBeacon.cpp \
					LocationEngine/src/Algorithm/IPXGeometryCalculator.cpp \
					LocationEngine/src/Algorithm/IPXLocationAlgorithm.cpp \
					LocationEngine/src/Algorithm/IPXTriangulationAlgorithm.cpp \
					LocationEngine/src/Algorithm/IPXWeightingAlgorithm.cpp \
					LocationEngine/src/Filter/IPXMovingAverage.cpp \
					LocationEngine/src/LocationEngine/IPXStepBasedEngine.cpp \
					LocationEngine/src/Utils/BLEMD5.cpp \
					LocationEngine/src/Utils/IPXBeaconDBChecker.cpp \
					
										

include $(BUILD_SHARED_LIBRARY)
