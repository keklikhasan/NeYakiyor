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
		String[] attrs1 = { "id", "code", "active", "fuel_type", "unit_price" };
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
				initialValues.put(attrs[i], pro.getFuel_type());
				break;
			case 4:
				initialValues.put(attrs[i], pro.getUnit_price());
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
		Cursor cur = null;
		try {
			List<Profile> profiles = new ArrayList<Profile>();
			cur = getAllRows(TBL_NAME, attrs);
			cur.moveToFirst();
			do {
				try {
					if (cur != null) {
						Profile pro = new Profile();
						pro.setId(cur.getInt(0));
						pro.setCode(cur.getString(1));
						pro.setFuel_type(cur.getInt(3));
						if (cur.getInt(2) == 0) {
							pro.setActive(false);
						} else {
							pro.setActive(true);
						}
						pro.setUnit_price(cur.getDouble(4));
						profiles.add(pro);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} while (cur.moveToNext());
			return profiles;
		} catch (Exception e) {
			e.toString();
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
		return new ArrayList<Profile>();
	}

	public Profile getActiveProfile() {
		Profile pro = new Profile();
		Cursor cur = null;
		try {
			String query = "SELECT id,code,active,fuel_type,unit_price FROM "
					+ TBL_NAME + " where active=1";
			cur = sql.rawQuery(query, null);
			if (cur.moveToFirst()) {
				pro.setId(cur.getInt(0));
				pro.setCode(cur.getString(1));
				pro.setFuel_type(cur.getInt(3));
				if (cur.getInt(2) == 0) {
					pro.setActive(false);
				} else {
					pro.setActive(true);
				}
				pro.setUnit_price(cur.getDouble(4));
				return pro;
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
		return null;
	}

	public Profile getProfile(int id) {
		Profile pro = new Profile();
		Cursor cur = null;
		try {
			String query = "SELECT id,code,active,fuel_type,unit_price FROM "
					+ TBL_NAME + " where id=" + id;
			cur = sql.rawQuery(query, null);
			if (cur.moveToFirst()) {
				pro.setId(cur.getInt(0));
				pro.setCode(cur.getString(1));
				pro.setFuel_type(cur.getInt(3));
				if (cur.getInt(2) == 0) {
					pro.setActive(false);
				} else {
					pro.setActive(true);
				}
				pro.setUnit_price(cur.getDouble(4));
				return pro;
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
		return null;
	}

	public Profile getProfile(String name) {
		Profile pro = new Profile();
		Cursor cur = null;
		try {
			String query = "SELECT id,code,active,fuel_type,unit_price FROM "
					+ TBL_NAME + " where code='" + name + "'";
			cur = sql.rawQuery(query, null);
			if (cur.moveToFirst()) {
				pro.setId(cur.getInt(0));
				pro.setCode(cur.getString(1));
				pro.setFuel_type(cur.getInt(3));
				if (cur.getInt(2) == 0) {
					pro.setActive(false);
				} else {
					pro.setActive(true);
				}
				pro.setUnit_price(cur.getDouble(4));
				return pro;
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
		return null;
	}

	public void deleteProfile(int id) throws DaoServiceException {
		try {
			sql.execSQL("DELETE FROM " + TBL_NAME + " where id=" + id);

		} catch (Exception e) {
			throw new DaoServiceException(e);
		}

	}

	public void update(Profile pro) throws Exception {
		Profile tmp = getProfile(pro.getId());
		if (tmp == null) {
			save(pro);
		} else {
			try {
				String query = "UPDATE " + TBL_NAME
						+ " set active=1,fuel_type=" + pro.getFuel_type()
						+ ",unit_price=" + pro.getUnit_price() + " where id="
						+ pro.getId();
				sql.execSQL(query);
			} catch (Exception e) {
				throw e;
			}
		}
	}
}
