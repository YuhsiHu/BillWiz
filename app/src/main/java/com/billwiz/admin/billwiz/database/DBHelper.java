package com.billwiz.admin.billwiz.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {

    public static final String CREATE_RECORD_STRING =
            "create table Record (" +
            "ID integer primary key autoincrement," +
            "MONEY float," +
            "CURRENCY text," +
            "TAG integer," +
            "TIME text," +
            "REMARK text," +
            "USER_ID text," +
            "OBJECT_ID text," +
            "IS_UPLOADED integer" + ")";

    public static final String CREATE_TAG_STRING =
            "create table Tag (" +
            "ID integer primary key autoincrement," +
            "NAME text," +
            "WEIGHT integer)";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_RECORD_STRING);
        db.execSQL(CREATE_TAG_STRING);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
