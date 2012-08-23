package com.minikod.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.minikod.exception.DaoServiceException;
import com.minikod.modals.Record;

public class RecordDao extends AbstractDao {

	public RecordDao(Context ctx) {
		TBL_NAME = "log";
		PACKAGE_NAME = "com.minikod.otoyakit";
		DB_NAME = "yakit.sqlite";
		String[] attrs1 = { "id", "type", "unitPrice", "fuelByPrice", "fuel",
				"km", "date", "profile" };
		attrs = attrs1;
		sql = createDB(ctx);
	}

	public void save(Record rec) throws DaoServiceException {
		ContentValues initialValues = new ContentValues();
		initialValues.put("id", getMaxId() + 1);
		for (int i = 1; i < attrs.length; i++) {
			switch (i) {
			case 1:
				initialValues.put(attrs[i], rec.getType());
				break;
			case 2:
				initialValues.put(attrs[i], rec.getUnitPrice());
				break;
			case 3:
				initialValues.put(attrs[i], rec.getFuelByPrice());
				break;
			case 4:
				initialValues.put(attrs[i], rec.getFuel());
				break;
			case 5:
				initialValues.put(attrs[i], rec.getKm());
				break;
			case 6:
				initialValues.put(attrs[i], rec.getDate().getTime());
				break;
			case 7:
				initialValues.put(attrs[i], rec.getProfile());
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

	public List<Record> getRecordList() {
		try {
			List<Record> recs = new ArrayList<Record>();
			Cursor cur = getAllRows(TBL_NAME, attrs);
			cur.moveToFirst();
			do {
				try {
					if (cur != null) {
						Record rec = new Record();
						rec.setId(cur.getInt(0));
						rec.setType(cur.getInt(1));
						rec.setUnitPrice(cur.getDouble(2));
						rec.setFuelByPrice(cur.getDouble(3));
						rec.setFuel(cur.getDouble(4));
						rec.setKm(cur.getDouble(5));
						rec.setDate(new Date(cur.getLong(6)));
						rec.setProfile(cur.getInt(7));
						recs.add(rec);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} while (cur.moveToNext());
			return recs;
		} catch (Exception e) {
			e.toString();
		}
		return new ArrayList<Record>();
	}

	public void clearTable() throws DaoServiceException {
		try {
			sql.execSQL("DELETE FROM " + TBL_NAME);
		} catch (Exception e) {
			throw new DaoServiceException(e);
		}

	}

	public List<Record> getRecordListbyProfile(int profileId)
			throws DaoServiceException {
		List<Record> recs = new ArrayList<Record>();
		try {
			String query = "SELECT id,type, unitPrice, fuelByPrice, fuel,km, date,profile   FROM "
					+ TBL_NAME + " where profile = " + profileId;
			Cursor cur = sql.rawQuery(query, null);
			cur.moveToFirst();
			do {
				try {
					if (cur != null) {
						Record rec = new Record();
						rec.setId(cur.getInt(0));
						rec.setType(cur.getInt(1));
						rec.setUnitPrice(cur.getDouble(2));
						rec.setFuelByPrice(cur.getDouble(3));
						rec.setFuel(cur.getDouble(4));
						rec.setKm(cur.getDouble(5));
						rec.setDate(new Date(cur.getLong(6)));
						rec.setProfile(cur.getInt(7));
						recs.add(rec);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} while (cur.moveToNext());

		} catch (Exception e) {
			throw new DaoServiceException(e);
		}
		return recs;
	}
}
