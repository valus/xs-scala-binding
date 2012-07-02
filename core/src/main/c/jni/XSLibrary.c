
#include "io_crossroads_jni_XSLibrary__.h"
#include <xs/xs.h>
#include <stdint.h>


/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_bind
 * Signature: (JLjava/lang/String;)I
 */
 
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1bind
  (JNIEnv *env, jobject obj, jlong socket, jstring address) {
    void* socket_a = 0;
    const char* address_a = 0;
    int result = 0;
      
    socket_a = (void*) socket;
    address_a = (*env)->GetStringUTFChars(env, address, NULL);
    result = xs_bind(socket_a, address_a);
    
    return result;
  }

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_close
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1close
  (JNIEnv *env, jobject obj, jlong socket) {
  
    void* socket_a = 0;
    int result = 0;
    
    socket_a = (void*) socket;
    result = xs_close(socket_a);
    
    return result;
}

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_connect
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1connect
  (JNIEnv *env, jobject obj, jlong socket, jstring address) {

    void* socket_a = 0;
    const char* address_a = 0;
    int result = 0;
    
    socket_a = (void*) socket;
    address_a = (*env)->GetStringUTFChars(env, address, NULL);
    result = xs_connect(socket_a, address_a);

    return result;
}

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_errno
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1errno
  (JNIEnv *env, jobject obj) {
  return xs_errno();
}

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_getsockoptInt
 * Signature: (JII)I
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1getsockoptInt
  (JNIEnv *env, jobject obj, jlong socket, jint option, jint value) {
      void* socket_a = 0;
      socket_a = (void*) socket;
      size_t value_size = sizeof(value);
      
      int result = -1;
      result = xs_getsockopt(socket_a, option, &value, &value_size);
      if(result < 0)
          return result;
      return value;
}

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_getsockoptLong
 * Signature: (JIJ)I
 */
JNIEXPORT jlong JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1getsockoptLong
(JNIEnv *env, jobject obj, jlong socket, jint option, jlong value) {
    
    void* socket_a = 0;
    socket_a = (void*) socket;
    size_t value_size = sizeof(value);
    
    int result = -1;
    result = xs_getsockopt(socket_a, option, &value, &value_size);
    if(result < 0)
        return result;
    return value;
}

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_getsockoptByte
 * Signature: (JI[B)I
 */
JNIEXPORT jbyteArray JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1getsockoptByte
(JNIEnv *env, jobject obj, jlong socket, jint option, jbyteArray value) {
    void* socket_a = 0;
    socket_a = (void*) socket;
    size_t value_size = sizeof(value);
    
    int result = -1;
    result = xs_getsockopt(socket_a, option, &value, &value_size);
    if(result < 0)
        return result;
    return value;
}

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_init
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1init
  (JNIEnv *env, jobject obj) {
  
    void* context = 0;
    
    context = xs_init();
    
    return context;
}

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_recv
 * Signature: (J[BII)I
 */
JNIEXPORT jbyteArray JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1recv
  (JNIEnv *env, jobject obj, jlong socket, jbyteArray buffer, jint length, jint flags) {
  
    void* socket_a = 0;
    jbyte* buffer_a = 0;
    int result = 0;
    
    socket_a = (void*) socket;
    //buffer_a = (jbyte*) (*env)->GetDirectBufferAddress(env, buffer);
    result = xs_recv(socket_a, buffer, length, flags);
    
    return buffer;
}

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_send
 * Signature: (J[BII)I
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1send
  (JNIEnv *env, jobject obj, jlong socket, jbyteArray buffer, jint length, jint flags) {
  
  	void* socket_a = 0;
    jbyte* buffer_a = 0;
    int result = 0;
    
    socket_a = (void*) socket;
    buffer_a = (jbyte*) (*env)->GetDirectBufferAddress(env, buffer);
    result = xs_send(socket_a, buffer, length, flags);

    return result;
  }

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_setctxoptInt
 * Signature: (JII)I
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1setctxoptInt
(JNIEnv *env, jobject obj, jlong context, jint option, jint value) {
    
    void* context_a = 0;
    context_a = (void*) context;
    size_t value_size = sizeof(value);
    
    int result = -1;
    result = xs_setctxopt(context_a, option, &value, value_size);
    return result;
    
}

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_setctxoptLong
 * Signature: (JIJ)I
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1setctxoptLong
(JNIEnv *env, jobject obj, jlong context, jint option, jlong value) {
    void* context_a = 0;
    context_a = (void*) context;
    size_t value_size = sizeof(value);
    
    int result = -1;
    result = xs_setctxopt(context_a, option, &value, value_size);
    return result;
}

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_setctxoptByte
 * Signature: (JI[B)I
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1setctxoptByte
(JNIEnv *env, jobject obj, jlong context, jint option, jbyteArray value) {
    void* context_a = 0;
    context_a = (void*) context;
    size_t value_size = sizeof(value);
    
    int result = -1;
    result = xs_setctxopt(context_a, option, &value, &value_size);
    return result;
}

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_setsockoptInt
 * Signature: (JII)I
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1setsockoptInt
(JNIEnv *env, jobject obj, jlong socket, jint option, jint value) {
    void* socket_a = 0;
    socket_a = (void*) socket;
    size_t value_size = sizeof(value);
    
    int result = -1;
    result = xs_setsockopt(socket_a, option, &value, value_size);
    return result;
   
}

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_setsockoptLong
 * Signature: (JIJ)I
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1setsockoptLong
(JNIEnv *env, jobject obj, jlong socket, jint option, jlong value) {
    void* socket_a = 0;
    socket_a = (void*) socket;
    size_t value_size = sizeof(value);
    
    int result = -1;
    result = xs_setsockopt(socket_a, option, &value, value_size);
    return result;
}

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_setsockoptByte
 * Signature: (JI[B)I
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1setsockoptByte
(JNIEnv *env, jobject obj, jlong socket, jint option, jbyteArray value) {
    void* socket_a = 0;
    socket_a = (void*) socket;
    //size_t value_size = sizeof(value);
    int value_size = (*env)->GetArrayLength(env, value);
    
    int result = -1;
    result = xs_setsockopt(socket_a, option, &value, value_size);
    return result;
}

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_shutdown
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1shutdown
  (JNIEnv *env, jobject obj, jlong socket, jint how) {
  
    void* socket_a = 0;
    int result = 0;
    
    socket_a = (void*) socket;
    result = xs_shutdown(socket_a, how);

    return result;
  }

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_socket
 * Signature: (JI)J
 */
JNIEXPORT jlong JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1socket
  (JNIEnv *env, jobject obj, jlong context, jint type) {
  
    void* context_a = 0;
    void* socket_a =  0;
    
    context_a = (void*) context;
    socket_a = xs_socket(context_a, type);
    
    return socket_a;
  }

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_strerror
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1strerror
  (JNIEnv *env, jobject obj, jint errnum) {
  
    const char* error = 0;
    jstring answer =  0;
    
    error = xs_strerror(errnum);
    answer = (*env)->NewStringUTF(env, error);
    return answer;
  }

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_term
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1term
  (JNIEnv *env, jobject obj, jlong context) {
  
    void* context_a = 0;
    int result = 0;
    
    context_a = (void*) context;
    result = xs_term(context_a);
    
    return result;
  }

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_version
 * Signature: ([I[I[I)V
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1version
  (JNIEnv *env, jobject obj, jintArray major, jintArray minor, jintArray patch) {
  
    jclass cmaj = 0;
    jclass cmin = 0;
    jclass cpat = 0;
    jfieldID fmaj = 0;
    jfieldID fmin = 0;
    jfieldID fpat = 0;
    int imaj = 0;
    int imin = 0;
    int ipat = 0;
    
    cmaj = (*env)->GetObjectClass(env, major);
    fmaj = (*env)->GetFieldID(env, cmaj, "value", "I");
    cmin = (*env)->GetObjectClass(env, minor);
    fmin = (*env)->GetFieldID(env, cmin, "value", "I");
    cpat = (*env)->GetObjectClass(env, patch);   
    fpat = (*env)->GetFieldID(env, cpat, "value", "I");
    xs_version(&imaj, &imin, &ipat);
    (*env)->SetIntField(env, major, fmaj, imaj);
    (*env)->SetIntField(env, minor, fmin, imin);
    (*env)->SetIntField(env, patch, fpat, ipat);
    
    return (imaj*100+imin)*100+ipat;
  }

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_stopwatch_start
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1stopwatch_1start
  (JNIEnv *env, jobject obj) {
  
    void* watch = 0;
    watch = xs_stopwatch_start();
    
    return (jlong) watch;
  }

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_stopwatch_stop
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1stopwatch_1stop
  (JNIEnv *env, jobject obj, jlong watch) {
  
    void* watch_a = 0;
    long result = 0;
    
    watch_a = (void*) watch;
    result = xs_stopwatch_stop(watch_a);
    
    return result;
  }

/*
 * Class:     io_crossroads_jni_XSLibrary__
 * Method:    xs_poll
 * Signature: ([Lio/crossroads/jni/xs_pollitem_t;II)I
 */
JNIEXPORT jint JNICALL Java_io_crossroads_jni_XSLibrary_00024_xs_1poll
(JNIEnv * env, jobject obj, jobjectArray items, jint nitems, jint timeout) {
    
    xs_pollitem_t* poll_items = malloc(sizeof(xs_pollitem_t) * nitems);
    
    int i = 0;
    
    for (i = 0; i < nitems; i++) {
        jobject item_temp = (*env)->GetObjectArrayElement(env, items, i);
        
        jclass class = (*env)->GetObjectClass(env, item_temp);
        jfieldID socketId = (*env)->GetFieldID(env, class, "socket", "J");
        jfieldID fdId = (*env)->GetFieldID(env, class, "fd", "I");
        jfieldID eventsId = (*env)->GetFieldID(env, class, "events", "S");
        jfieldID reventsId = (*env)->GetFieldID(env, class, "revents", "S");
        
        poll_items [i].socket = (*env)->GetLongField(env, item_temp, socketId);
        poll_items [i].fd = (*env)->GetIntField(env, item_temp, fdId);
        poll_items [i].events = (*env)->GetShortField(env, item_temp, eventsId);
        poll_items [i].revents = (*env)->GetShortField(env, item_temp, reventsId);
    }
    
    int result = 0;
    
    result = xs_poll(poll_items, nitems, timeout);
    for (i = 0; i < nitems; i++) {
        jobject item_temp = (*env)->GetObjectArrayElement(env, items, i);
        
        jclass class = (*env)->GetObjectClass(env, item_temp);
        jfieldID socketId = (*env)->GetFieldID(env, class, "socket", "J");
        jfieldID fdId = (*env)->GetFieldID(env, class, "fd", "I");
        jfieldID eventsId = (*env)->GetFieldID(env, class, "events", "S");
        jfieldID reventsId = (*env)->GetFieldID(env, class, "revents", "S");
        (*env)->SetShortField(env, item_temp, reventsId, poll_items[i].revents);
        // (*env)->SetShortField(env, item_temp, socketId, poll_items[i].socket);
        // (*env)->SetShortField(env, item_temp, fdId, poll_items[i].fd);
        // (*env)->SetShortField(env, item_temp, eventsId, poll_items[i].events);
    }
    
    free(poll_items);
    return result;
}

#ifdef __cplusplus
}
#endif
