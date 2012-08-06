package com.minikod.bean;

import java.io.Serializable;
import java.util.HashMap;
/**
 * 
 * @author hkeklik
 * this class has a hasp map that can app use a session 
 * in any class we can set object to this hashmap to use later
 * acitvity can pass only primitive variables so i create this.
 */
@SuppressWarnings("serial")
public class SessionBean implements Serializable {

	private static HashMap<String, Object> session = new HashMap<String, Object>();

	public static void setObject(String name, Object object) {
		if (name != null && name.trim().length() > 0) {
			if (object != null) {
				if (session.containsKey(name)) {
					session.remove(name);
					session.put(name, object);
				} else {
					session.put(name, object);
				}
			}else
			{
				session.remove(name);
			}
		}
	}

	public static Object getObject(String name) {
		if (name != null && name.trim().length() > 0) {
			if (session.containsKey(name)) {
				return session.get(name);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static void reset() {
		session = new HashMap<String, Object>();
	}

	public static HashMap<String, Object> getSession() {
		return session;
	}
}
