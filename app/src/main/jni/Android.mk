LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := keys
LOCAL_SRC_FILES := keys.c pref.c prefdd.c urls.c constants.c apis.c params.c
include $(BUILD_SHARED_LIBRARY)