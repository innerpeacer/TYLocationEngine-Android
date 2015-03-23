LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := prebuilt/$(TARGET_ARCH_ABI)/libruntimecore_java.so
LOCAL_MODULE    := arcgis

include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_CPPFLAGS += -std=gnu++11 -fexceptions -frtti

LOCAL_MODULE    := BLELocationEngine
LOCAL_SRC_FILES := BLELocationEngine_wrap.cxx \
					LocationEngine/src/Entity/NPXBeacon.cpp \
					LocationEngine/src/Entity/NPXPoint.cpp \
					LocationEngine/src/Entity/NPXPublicBeacon.cpp \
					LocationEngine/src/Entity/NPXScannedBeacon.cpp \
					LocationEngine/src/Algorithm/NPXGeometryCalculator.cpp \
					LocationEngine/src/Algorithm/NPXLocationAlgorithm.cpp \
					LocationEngine/src/Algorithm/NPXTriangulationAlgorithm.cpp \
					LocationEngine/src/Algorithm/NPXWeightingAlgorithm.cpp \
					LocationEngine/src/Filter/NPXMovingAverage.cpp \
					LocationEngine/src/LocationEngine/NPXStepBasedEngine.cpp \
					
										

include $(BUILD_SHARED_LIBRARY)
