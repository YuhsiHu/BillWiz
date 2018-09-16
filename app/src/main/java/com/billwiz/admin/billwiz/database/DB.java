package com.billwiz.admin.billwiz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.billwiz.admin.billwiz.BuildConfig;
import com.billwiz.admin.billwiz.model.BillWizRecord;
import com.billwiz.admin.billwiz.model.RecordManager;
import com.billwiz.admin.billwiz.model.Tag;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;



public class DB {

    public static final String DB_NAME_STRING = "CoCoin Database.db";
    public static final String RECORD_DB_NAME_STRING = "Record";
    public static final String TAG_DB_NAME_STRING = "Tag";

    public static final int VERSION = 1;

    private static DB db;
    private SQLiteDatabase sqliteDatabase;
    private DBHelper dbHelper;

    private DB(Context context) throws IOException {
        dbHelper = new DBHelper(context, DB_NAME_STRING, null, VERSION);
        sqliteDatabase = dbHelper.getWritableDatabase();
    }

    public synchronized static DB getInstance(Context context)
            throws IOException {
        if (db == null) db = new DB(context);
        return db;
    }

    public void getData() {
        RecordManager.RECORDS = new LinkedList<>();
        RecordManager.TAGS = new LinkedList<>();

        Cursor cursor = sqliteDatabase
                .query(TAG_DB_NAME_STRING, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Tag tag = new Tag();
                tag.setId(cursor.getInt(cursor.getColumnIndex("ID")) - 1);
                tag.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                tag.setWeight(cursor.getInt(cursor.getColumnIndex("WEIGHT")));
                RecordManager.TAGS.add(tag);
            } while (cursor.moveToNext());
            if (cursor != null) cursor.close();
        }

        cursor = sqliteDatabase
                .query(RECORD_DB_NAME_STRING, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                BillWizRecord billWizRecord = new BillWizRecord();
                billWizRecord.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                billWizRecord.setMoney(cursor.getFloat(cursor.getColumnIndex("MONEY")));
                billWizRecord.setCurrency(cursor.getString(cursor.getColumnIndex("CURRENCY")));
                billWizRecord.setTag(cursor.getInt(cursor.getColumnIndex("TAG")));
                billWizRecord.setCalendar(cursor.getString(cursor.getColumnIndex("TIME")));
                billWizRecord.setRemark(cursor.getString(cursor.getColumnIndex("REMARK")));
                billWizRecord.setUserId(cursor.getString(cursor.getColumnIndex("USER_ID")));
                billWizRecord.setLocalObjectId(cursor.getString(cursor.getColumnIndex("OBJECT_ID")));
                billWizRecord.setIsUploaded(
                        cursor.getInt(cursor.getColumnIndex("IS_UPLOADED")) == 0 ? false : true);

                if (BuildConfig.DEBUG) Log.d("CoCoin Debugger", "Load " + billWizRecord.toString() + " S");

                RecordManager.RECORDS.add(billWizRecord);
                RecordManager.SUM += (int) billWizRecord.getMoney();
            } while (cursor.moveToNext());
            if (cursor != null) cursor.close();
        }
    }

    // return the row ID of the newly inserted row, or -1 if an error occurred
    public long saveRecord(BillWizRecord billWizRecord) {
        ContentValues values = new ContentValues();
        values.put("MONEY", billWizRecord.getMoney());
        values.put("CURRENCY", billWizRecord.getCurrency());
        values.put("TAG", billWizRecord.getTag());
        values.put("TIME", new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(billWizRecord.getCalendar().getTime()));
        values.put("REMARK", billWizRecord.getRemark());
        values.put("USER_ID", billWizRecord.getUserId());
        values.put("OBJECT_ID", billWizRecord.getLocalObjectId());
        values.put("IS_UPLOADED", billWizRecord.getIsUploaded().equals(Boolean.FALSE) ? 0 : 1);
        long insertId = sqliteDatabase.insert(RECORD_DB_NAME_STRING, null, values);
        billWizRecord.setId(insertId);
        if (BuildConfig.DEBUG)
            Log.d("BillWiz Debugger", "db.saveRecord " + billWizRecord.toString() + " S");
        return insertId;
    }

    // return the row ID of the newly inserted row, or -1 if an error occurred
    public int saveTag(Tag tag) {
        ContentValues values = new ContentValues();
        values.put("NAME", tag.getName());
        values.put("WEIGHT", tag.getWeight());
        int insertId = (int)sqliteDatabase.insert(TAG_DB_NAME_STRING, null, values);
        tag.setId(insertId);
        if (BuildConfig.DEBUG) Log.d("CoCoin Debugger", "db.saveTag " + tag.toString() + " S");
        return insertId - 1;
    }

    // return the id of the record deleted
    public long deleteRecord(long id) {
        long deletedNumber = sqliteDatabase.delete(RECORD_DB_NAME_STRING,
                "ID = ?",
                new String[]{id + ""});
        if (BuildConfig.DEBUG)
            Log.d("CoCoin Debugger", "db.deleteRecord id = " + id + " S");
        if (BuildConfig.DEBUG)
            Log.d("CoCoin Debugger", "db.deleteRecord number = " + deletedNumber + " S");
        return id;
    }

    // return the id of the tag deleted
    public int deleteTag(int id) {
        int deletedNumber = sqliteDatabase.delete(TAG_DB_NAME_STRING,
                "ID = ?",
                new String[]{(id + 1) + ""});
        if (BuildConfig.DEBUG)
            Log.d("CoCoin Debugger", "db.deleteTag id = " + id + " S");
        if (BuildConfig.DEBUG)
            Log.d("CoCoin Debugger", "db.deleteTag number = " + deletedNumber + " S");
        return id;
    }

    // return the id of the coCoinRecord update
    public long updateRecord(BillWizRecord billWizRecord) {
        ContentValues values = new ContentValues();
        values.put("ID", billWizRecord.getId());
        values.put("MONEY", billWizRecord.getMoney());
        values.put("CURRENCY", billWizRecord.getCurrency());
        values.put("TAG", billWizRecord.getTag());
        values.put("TIME", new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(billWizRecord.getCalendar().getTime()));
        values.put("REMARK", billWizRecord.getRemark());
        values.put("USER_ID", billWizRecord.getUserId());
        values.put("OBJECT_ID", billWizRecord.getLocalObjectId());
        values.put("IS_UPLOADED", billWizRecord.getIsUploaded().equals(Boolean.FALSE) ? 0 : 1);
        sqliteDatabase.update(RECORD_DB_NAME_STRING, values,
                "ID = ?",
                new String[]{billWizRecord.getId() + ""});
        if (BuildConfig.DEBUG)
            Log.d("CoCoin Debugger", "db.updateRecord " + billWizRecord.toString() + " S");
        return billWizRecord.getId();
    }

    // return the id of the tag update
    public int updateTag(Tag tag) {
        ContentValues values = new ContentValues();
        values.put("NAME", tag.getName());
        values.put("WEIGHT", tag.getWeight());
        sqliteDatabase.update(TAG_DB_NAME_STRING, values,
                "ID = ?",
                new String[]{(tag.getId() + 1) + ""});
        if (BuildConfig.DEBUG)
            Log.d("CoCoin Debugger", "db.updateTag " + tag.toString() + " S");
        return tag.getId();
    }

    // delete all the records
    public int deleteAllRecords() {
        int deleteNum = sqliteDatabase.delete(RECORD_DB_NAME_STRING, null, null);
        Log.d("CoCoin Debugger", "db.deleteAllRecords " + deleteNum + " S");
        return deleteNum;
    }
}
