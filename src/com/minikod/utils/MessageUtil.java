package com.minikod.utils;

import android.content.Context;
import android.widget.Toast;
/**
 * 
 * @author hkeklik
 * message util we can add custom message box for future
 */
public class MessageUtil {

	public static void makeToast(Context context, String msg, boolean islong) {
		Toast.makeText(context, msg,
				islong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
	}
	
	
}
