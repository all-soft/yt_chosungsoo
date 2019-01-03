//
// Created by 조남두 on 2017. 9. 25..
//

#include <jni.h>
#include <string.h>
#include "libkey.h"

extern "C" {
JNIEXPORT jstring JNICALL Java_com_chsapps_yt_1hongjinyoung_utils_SecureUtils_aesKey(JNIEnv *env,jobject obj);
JNIEXPORT jstring JNICALL Java_com_chsapps_yt_1hongjinyoung_utils_SecureUtils_appKey(JNIEnv *env,jobject obj);
JNIEXPORT jstring JNICALL Java_com_chsapps_yt_1hongjinyoung_utils_SecureUtils_appValue(JNIEnv *env,jobject obj);
}

extern "C"
JNIEXPORT jstring
JNICALL
Java_com_chsapps_yt_1hongjinyoung_utils_SecureUtils_aesKey(JNIEnv *env, jobject obj){
      char* EncText;
      try
      {
            EncText = aes_key();
      }
      catch (char *exception)
      {
            return (env)->NewStringUTF("");
      }
      return env->NewStringUTF(EncText);
}

JNIEXPORT jstring
JNICALL
Java_com_chsapps_yt_1hongjinyoung_utils_SecureUtils_appKey(JNIEnv *env, jobject obj){
      char* EncText;
      try
      {
            EncText = app_key();
      }
      catch (char *exception)
      {
            return (env)->NewStringUTF("");
      }
      return env->NewStringUTF(EncText);
}

JNIEXPORT jstring
JNICALL Java_com_chsapps_yt_1hongjinyoung_utils_SecureUtils_appValue(JNIEnv *env, jobject obj){
    char* EncText;
    try
    {
        EncText = app_value();
    }
        catch (char *exception)
    {
        return (env)->NewStringUTF("");
    }
    return env->NewStringUTF(EncText);
}

