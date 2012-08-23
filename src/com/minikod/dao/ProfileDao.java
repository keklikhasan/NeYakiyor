package com.minikod.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.minikod.exception.DaoServiceException;
import com.minikod.modals.Profile;

public class ProfileDao extends AbstractDao {

	public ProfileDao(Context ctx) {
		TBL_NAME = "profile";
		PACKAGE_NAME = "com.minikod.otoyakit";
		DB_NAME = "yakit.sqlite";
		String[] attrs1 = { "id", "code", "active","defType" };
		attrs = attrs1;
		sql = createDB(ctx);
	}

	public void save(Profile pro) throws DaoServiceException {
		ContentValues initialValues = new ContentValues();
		initialValues.put("id", getMaxId() + 1);
		for (int i = 1; i < attrs.length; i++) {
			switch (i) {
			case 1:
				initialValues.put(attrs[i], pro.getCode());
				break;
			case 2:
				initialValues.put(attrs[i], pro.isActive());
				break;
			case 3:
				initialValues.put(attrs[i], pro.defType);
				break;
			default:
				break;
			}
		}
		try {
			sql.insert(TBL_NAME, null, initialValues);
		} catch (Exception e) {
			throw new DaoServiceException(e);
		}
	}

	public List<Profile> getProfileList() {
		try {
			List<Profile> profiles = new ArrayList<Profile>();
			Cursor cur = getAllRows(TBL_NAME, attrs);
			cur.moveToFirst();
			do {
				try {
					if (cur != null) {
						Profile pro = new Profile();
						pro.setId(cur.getInt(0));
						pro.setCode(cur.getString(1));
						if (cur.getInt(2) == 0) {
							pro.setActive(false);
						} else {
							pro.setActive(true);
						}
						pro.setDefType(cur.getInt(3));
						profiles.add(pro);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} while (cur.moveToNext());
			return profiles;
		} catch (Exception e) {
			e.toString();
		}
		return new ArrayList<Profile>();
	}

	public Profile getActiveProfile() throws DaoServiceException {

		try {
			String query = "SELECT id,code, active,deftType  FROM " + TBL_NAME
					+ " where active = " + 1;
			Cursor cur = sql.rawQuery(query, null);
			cur.moveToFirst();
			do {
				try {
					if (cur != null) {
						Profile pro = new Profile();
						pro.setId(cur.getInt(0));
						pro.setCode(cur.getString(1));
						if (cur.getInt(2) == 0) {
							pro.setActive(false);
						} else {
							pro.setActive(true);
						}
						pro.setDefType(cur.getInt(3));
						return pro;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} while (cur.moveToNext());

		} catch (Exception e) {
			throw new DaoServiceException(e);

		}
		return null;
	}

}
