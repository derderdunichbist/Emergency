package org.hsrt.mc.emergency.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KA on 11.06.2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper{

    public static final String TABLE_BLOOD_TYPE="bloodtypes";
    public static final String TABLE_CONTACTS = "contacts";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BLOOD_TYPE = "bloodtype";
    public static final String COLUMN_CONTACT = "contact";

    private static final String DATABASE_NAME = "user";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CONTACTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CONTACT
            + " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
