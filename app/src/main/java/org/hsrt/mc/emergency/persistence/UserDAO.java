package org.hsrt.mc.emergency.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.hsrt.mc.emergency.user.Contact;
import org.hsrt.mc.emergency.user.Medication;
import org.hsrt.mc.emergency.utils.Verifier;

import java.util.Date;
import java.util.List;

/**
 * Created by KA on 11.06.2016.
 */
public class UserDAO {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper sqlLiteHelper;
    private Context context;
    private String userId;

    public UserDAO(Context context) {
        sqlLiteHelper = new MySQLiteHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = sqlLiteHelper.getWritableDatabase();
        this.setUpUser();
    }

    public void close() {
        sqlLiteHelper.close();
    }

    public void initUser() {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_FIRST_NAME, "Test");
        long insertId = database.insert(MySQLiteHelper.TABLE_USER, null,
                values);
        this.setUserId(String.valueOf(insertId));
    }

    public void updateUser(String firstName) {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUpUser() {
        int count = 0;

        String[]columndsId = new String[] {MySQLiteHelper.COLUMN_ID};
        Cursor cursor = database.query(MySQLiteHelper.TABLE_USER,
                columndsId, MySQLiteHelper.COLUMN_ID, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            count ++;
            cursor.moveToNext();
        }

        if(count == 0) {
            this.initUser();
        } else if (count == 1) {
            cursor.moveToFirst();
            this.setUserId(cursor.getString(0));
        }
        cursor.close();
    }

    public void updateUserFirstName(String firstName) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_FIRST_NAME, firstName);
        database.update(MySQLiteHelper.TABLE_USER, values, null, null);
    }

    public void updateUserLastName(String lastName) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_LAST_NAME, lastName);
        database.update(MySQLiteHelper.TABLE_USER, values, null, null);
    }

    public void updateUserBloodType(String bloodType) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_BLOOD_TYPE, bloodType);
        database.update(MySQLiteHelper.TABLE_USER, values, null, null);
    }

    public void updateUserDob(Date dateOfBirth) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DATE_OF_BIRTH, String.valueOf(dateOfBirth));
        database.update(MySQLiteHelper.TABLE_USER, values, null, null);
    }

    public void addContact(Contact contact) {

    }

    public void addMedication(Medication med) {

    }

    public void addSpecialNeed(String specialNeed) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_SPECIAL_NEEDS, specialNeed);
        long insertId = database.insert(MySQLiteHelper.TABLE_SPECIAL_NEEDS, null, values);
    }

    public void addDiseases(String disease) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DISEASES, disease);
        long insertId = database.insert(MySQLiteHelper.TABLE_DISEASES, null, values);
    }
}
