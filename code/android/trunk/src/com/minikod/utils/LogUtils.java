package com.minikod.utils;

import android.util.Log;
/**
 * 
 * @author hkeklik
 * log utils  app use this log util 
 * so we can do what ever we want extra write file etc...
 */
public class LogUtils {

	public static void addError(String TAG, String msg) {
		Log.e(TAG, msg);
	}

	public static void addInfo(String TAG, String msg) {
		Log.i(TAG, msg);
	}

	public static void addWarning(String TAG, String msg) {
		Log.w(TAG, msg);
	}

	public static void addError(String TAG, Exception e) {
		Log.e(TAG, "", e);
	}
}
