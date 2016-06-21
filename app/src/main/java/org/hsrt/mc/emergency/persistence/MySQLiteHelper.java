package org.hsrt.mc.emergency.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.hsrt.mc.emergency.user.BloodType;
import org.hsrt.mc.emergency.user.ePriority;

/**
 * Created by KA on 11.06.2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper{

    //Database
    private static final String DATABASE_NAME = "emergency";
    private static final int DATABASE_VERSION = 1;

    //Tables
    public static final String TABLE_BLOOD_TYPE="bloodtypes";
    public static final String TABLE_CONTACTS = "contacts";
    public static final String TABLE_PRIORITY = "priorities";
    public static final String TABLE_MEDICATION = "medication";
    public static final String TABLE_USER = "users";
    public static final String TABLE_DISEASES = "diseases";
    public static final String TABLE_SPECIAL_NEEDS = "special_needs";

    //Columns for USER-Table
    public static final String COLUMN_BLOOD_TYPE = "bloodtype";
    public static final String COLUMN_CONTACT = "contact";
    public static final String COLUMN_MEDICATION = "medication";
    private static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";

    //Columns for MEDIACTION-Table
    public static final String COLUMN_DOSIS = "dosis";
    public static final String COLUMN_MANUFACTURER = "manufacturer";
    public static final String COLUMN_AMOUNT_PER_DAY = "amount_per_day";

    //Columns for CONTACT-Table
    public static final String COLUMN_FIRST_NAME= "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_PRIORITY = "priority";

    //Columns for DISEASES-Table
    public static final String COLUMN_DISEASES = "diseases";

    //Columns for SPECIAL_NEEDS-Table
    public static final String COLUMN_SPECIAL_NEEDS = "special_needs";

    //Columns SHARED
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String CREATE_TABLE = "CREATE TABLE ";
    public static final String CREATE_ID_STRING = "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
    public static final String INTEGER = " INTEGER";
    public static final String REAL = " REAL";
    public static final String TEXT = " TEXT";
    public static final String NOT_NULL = " NOT NULL";
    public static final String FOREIGN_KEY = "FOREIGN KEY";
    public static final String REFERENCES = " REFERENCES ";

    /**
     * SQL-Statement to create MEDICATION-Table
     */
    private static final String CREATE_TABLE_MEDICATION = CREATE_TABLE
            + TABLE_MEDICATION + CREATE_ID_STRING
            + COLUMN_NAME + TEXT + ", "
            + COLUMN_DOSIS + TEXT + ", "
            + COLUMN_MANUFACTURER + TEXT + ", "
            + COLUMN_AMOUNT_PER_DAY + INTEGER + ");";

    /**
     * SQL-Statement to create DISEASES-Table
     */
    private static final String CREATE_TABLE_DISEASES = CREATE_TABLE
            + TABLE_DISEASES+ CREATE_ID_STRING
            + COLUMN_DISEASES+ TEXT + NOT_NULL + ");";

    /**
     * SQL-Statement to create SPECIAL_NEEDS-Table
     */
    private static final String CREATE_TABLE_SPECIAL_NEEDS = CREATE_TABLE
            + TABLE_SPECIAL_NEEDS + CREATE_ID_STRING
            + COLUMN_SPECIAL_NEEDS + TEXT + NOT_NULL + ");";

    /**
     * SQL-Statement to create CONTACT-Table
     */
    private static final String CREATE_TABLE_CONTACT = CREATE_TABLE
            + TABLE_CONTACTS + CREATE_ID_STRING
            + COLUMN_FIRST_NAME+ TEXT + ", "
            + COLUMN_LAST_NAME+ TEXT +  ", "
            + COLUMN_EMAIL+ TEXT +  ", "
            + COLUMN_PHONE_NUMBER + TEXT + ", "
            + COLUMN_PRIORITY + TEXT + ");";

    /**
     * SQL-Statement to create USER-Table
     */
    private static final String CREATE_TABLE_USER = CREATE_TABLE
            + TABLE_USER + CREATE_ID_STRING
            + COLUMN_FIRST_NAME + TEXT + ", "
            + COLUMN_LAST_NAME + TEXT +  ", "
            + COLUMN_DATE_OF_BIRTH + TEXT +  ", "
            + COLUMN_BLOOD_TYPE + TEXT +  ", "
            + COLUMN_MEDICATION + INTEGER + REFERENCES + TABLE_MEDICATION + "(" + COLUMN_ID + ")" + ", "
            + COLUMN_DISEASES + INTEGER + REFERENCES + TABLE_DISEASES + "(" + COLUMN_ID + ")" + ", "
            + COLUMN_SPECIAL_NEEDS + INTEGER + REFERENCES + TABLE_SPECIAL_NEEDS + "(" + COLUMN_ID + ")"
            + ");";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.createDatabaseTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECIAL_NEEDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISEASES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    private void createDatabaseTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SPECIAL_NEEDS);
        db.execSQL(CREATE_TABLE_DISEASES);
        db.execSQL(CREATE_TABLE_MEDICATION);
        db.execSQL(CREATE_TABLE_CONTACT);
        db.execSQL(CREATE_TABLE_USER);
    }

}
