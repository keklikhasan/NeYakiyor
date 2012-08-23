package com.minikod.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.minikod.exception.DaoServiceException;

/**
 * 
 * @author hkeklik abstract class for using db on device
 */
public abstract class AbstractDao {
	/*
	 * db name and ns must be set app's db name anda package name
	 */
	public static String DB_NAME;
	public static String PACKAGE_NAME ;
	protected String TBL_NAME ;
	protected SQLiteDatabase sql;
	protected String[] attrs;

	public SQLiteDatabase createDB(Context ctx) {
		SQLiteDatabase sql = null;
		try {
			boolean init = true;
			String[] list = ctx.databaseList();
			if (list != null && list.length > 0) {
				for (int i = 0; i < list.length; i++) {
					if (list[i] != null && list[i].equalsIgnoreCase(DB_NAME)) {
						init = false;
					}
				}
			}

			if (init) {
				File path = new File("/data/data/" + PACKAGE_NAME
						+ "/databases/");
				path.mkdirs();
				File dbFile = new File("/data/data/" + PACKAGE_NAME
						+ "/databases/" + DB_NAME);
				if (dbFile.exists()) {
					dbFile.delete();
				}
				dbFile.createNewFile();

				InputStream myInput = ctx.getAssets().open(DB_NAME);
				String outFileName = "/data/data/" + PACKAGE_NAME
						+ "/databases/" + DB_NAME;
				OutputStream myOutput = new FileOutputStream(outFileName);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = myInput.read(buffer)) > 0) {
					myOutput.write(buffer, 0, length);
				}
				myOutput.flush();
				myOutput.close();
				myInput.close();
			}
			sql = ctx.openOrCreateDatabase(DB_NAME,
					SQLiteDatabase.CREATE_IF_NECESSARY, null);
		} catch (Exception e) {
			sql.close();
			sql = ctx.openOrCreateDatabase(DB_NAME,
					SQLiteDatabase.CREATE_IF_NECESSARY, null);
		}
		return sql;
	}

	public void close() {
		if (sql != null) {
			sql.close();
		}
	}

	public void delete(String whereClause) throws Exception {
		sql.delete(TBL_NAME, whereClause, null);
	}

	public Cursor getAllRows(String table, String[] var)
			throws DaoServiceException {
		try {
			return sql.query(table, var, null, null, null, null, null);
		} catch (Exception e) {
			throw new DaoServiceException(e);
		}
	}
	
	public int getMaxId() throws DaoServiceException {
		try {
			String query = "SELECT MAX(id) AS max_id FROM " + TBL_NAME;
			Cursor cursor = sql.rawQuery(query, null);
			int id = 0;
			if (cursor.moveToFirst()) {
				do {
					id = cursor.getInt(0);
				} while (cursor.moveToNext());
			}
			return id;

		} catch (Exception e) {
			throw new DaoServiceException(e);
		}
	}

	public String getTBL_NAME() {
		return TBL_NAME;
	}

	public void setTBL_NAME(String tBL_NAME) {
		TBL_NAME = tBL_NAME;
	}

	public SQLiteDatabase getSql() {
		return sql;
	}

	public void setSql(SQLiteDatabase sql) {
		this.sql = sql;
	}

	public String[] getAttrs() {
		return attrs;
	}

	public void setAttrs(String[] attrs) {
		this.attrs = attrs;
	}

	public String getDB_NAME() {
		return DB_NAME;
	}
}
