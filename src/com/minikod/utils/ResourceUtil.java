package com.minikod.utils;

import android.content.res.Resources;
/**
 * 
 * @author hkeklik
 * for reacing string xml with code
 * res must be set befoure using
 * on the first acitivty 
 */
public class ResourceUtil {

	public static Resources res;
	public static String TAG = ResourceUtil.class.getSimpleName();

	public static String getText(int bundle) {
		String result = null;
		try {
			result = res.getString(bundle);
		} catch (Exception e) {
			LogUtils.addError(TAG, e);
			result = "";
		}
		return result;
	}
}
