package com.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;

public class SQLiteDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "records.db"; //database name
    public static final String PROFILE = "profile"; //database table
    public final String PROFILE_ID = "id"; //database column
    public final String PROFILE_FNAME = "fname"; //database column
    public final String PROFILE_MNAME = "mname"; //database column
    public final String PROFILE_LNAME = "lname"; //database column

    public ArrayList<String> Records;
    public ArrayList<Integer> RecordsId;

    public String sql = null;
    public Cursor rs;

    public SQLiteDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase conn) {
        // TODO Auto-generated method stub
        conn.execSQL("CREATE TABLE " + PROFILE + " (" + PROFILE_ID + " Integer PRIMARY KEY AUTOINCREMENT, " + PROFILE_FNAME + " text, " + PROFILE_MNAME + " text, " + PROFILE_LNAME + " text)");
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase conn, int OldVersion, int NewVersion) {
        conn.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(conn);
    }

    public boolean AddRecords(String fname, String mname, String lname) {
        android.database.sqlite.SQLiteDatabase conn = this.getWritableDatabase();

        String query = "SELECT * FROM " + PROFILE + " WHERE "
                        + PROFILE_FNAME + " = ? AND "
                        + PROFILE_MNAME + " = ? AND "
                        + PROFILE_LNAME + " = ?";
        String[] selectionArgs = {fname, mname,lname};
        Cursor cursor = conn.rawQuery(query, selectionArgs);
        if (cursor.getCount() > 0){
            cursor.close();
            return false;
        }

        ContentValues Values = new ContentValues();

        Values.put(PROFILE_FNAME, fname);
        Values.put(PROFILE_MNAME, mname);
        Values.put(PROFILE_LNAME, lname);
        conn.insert(PROFILE, null, Values);

        cursor.close();
        return true;
    }

    public boolean UpdateRecords(int id, String fname, String mname, String lname) {
        android.database.sqlite.SQLiteDatabase conn = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PROFILE_FNAME, fname);
        values.put(PROFILE_MNAME, mname);
        values.put(PROFILE_LNAME, lname);

        String query = "SELECT * FROM " + PROFILE + " WHERE "
                + PROFILE_FNAME + " = ? AND "
                + PROFILE_MNAME + " = ? AND "
                + PROFILE_LNAME + " = ?";

        String[] selectionArgs = {fname, mname,lname};
        Cursor cursor = conn.rawQuery(query, selectionArgs);
        if (cursor.moveToFirst()){
            cursor.close();
            return false;
        }

        int rowsAffected = conn.update(PROFILE, values, PROFILE_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    public Cursor getData(Integer id) {
        android.database.sqlite.SQLiteDatabase conn = this.getReadableDatabase();
        sql = "SELECT * FROM " + PROFILE + " WHERE " + PROFILE_ID + "=" + id;
        rs = conn.rawQuery(sql, null);
        return rs;
    }

    public boolean ClearRecord() {
        android.database.sqlite.SQLiteDatabase conn = this.getReadableDatabase();
        conn.execSQL("DELETE FROM " + PROFILE);
        return true;
    }

    public boolean DeleteRecord(Integer id) {
        android.database.sqlite.SQLiteDatabase conn = this.getReadableDatabase();
        conn.execSQL("DELETE FROM " + PROFILE + " WHERE " + PROFILE_ID + "=" + id);
        return true;
    }

    public ArrayList<String> GetAllData() {
        android.database.sqlite.SQLiteDatabase conn = this.getReadableDatabase();
        Records = new ArrayList<String>();
        RecordsId = new ArrayList<Integer>();

        sql = "SELECT * FROM " + PROFILE;
        rs = conn.rawQuery(sql, null);
        rs.moveToFirst();

        while (rs.isAfterLast() == false) {
            RecordsId.add(rs.getInt(rs.getColumnIndex(PROFILE_ID)));
            Records.add(rs.getString(rs.getColumnIndex(PROFILE_LNAME)) + ", " + rs.getString(rs.getColumnIndex(PROFILE_FNAME)) + ", " + rs.getString(rs.getColumnIndex(PROFILE_MNAME)));
            rs.moveToNext();
        }
        return Records;
    }
}