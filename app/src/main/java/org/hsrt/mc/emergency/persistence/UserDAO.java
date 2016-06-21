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
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_CONTACT };
    private Context context;

    public UserDAO(Context context) {
        sqlLiteHelper = new MySQLiteHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = sqlLiteHelper.getWritableDatabase();
    }

    public void close() {
        sqlLiteHelper.close();
    }


    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.setNameFromCompleteName(cursor.getString(0));
        return contact;
    }

    public void insertContact(String [] fields, String email, String phoneNumber){

        String lastName = fields[0].trim();
        String firstNames = fields[1].trim();

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_FIRST_NAME, firstNames);
        values.put(MySQLiteHelper.COLUMN_LAST_NAME, lastName);
        values.put(MySQLiteHelper.COLUMN_EMAIL, email);
        values.put(MySQLiteHelper.COLUMN_PHONE_NUMBER, phoneNumber);

        long insertId = database.insert(MySQLiteHelper.TABLE_CONTACTS, null,
                values);

//        String [] columns = new String[] {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_FIRST_NAME, MySQLiteHelper.COLUMN_LAST_NAME};
//        Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACTS,
//        columns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
//                null, null, null);
//       cursor.moveToFirst();
//        Toast.makeText(context, "Inserted contact: " + cursor.getString(1), Toast.LENGTH_SHORT).show();
//        cursor.close();
    }

    public void insertUser(String firstName, String lastName, String bloodType, Date dateOfBirth, List<Medication> medication, List<String> diseases, List<String> specialNeeds, List<Contact> contacts){

        ContentValues values = new ContentValues();
        if(!Verifier.isStringEmptyOrNull(firstName)) {
            values.put(MySQLiteHelper.COLUMN_FIRST_NAME, firstName);
        }

        if(!Verifier.isStringEmptyOrNull(lastName)) {
            values.put(MySQLiteHelper.COLUMN_LAST_NAME, lastName);
        }

        if(!Verifier.isStringEmptyOrNull(bloodType)) {
            values.put(MySQLiteHelper.COLUMN_BLOOD_TYPE, bloodType);
        }

        if(!Verifier.isStringEmptyOrNull(bloodType)) {
            values.put(MySQLiteHelper.COLUMN_BLOOD_TYPE, bloodType);
        }

        this.insertMedication(medication);



        //values.put(MySQLiteHelper.COLUMN_PHONE_NUMBER, phoneNumber);

        long insertId = database.insert(MySQLiteHelper.TABLE_CONTACTS, null,
                values);
    }

    public void insertMedication(List<Medication> medications) {

        for(Medication medication : medications) {
            ContentValues values = new ContentValues();
            if(!Verifier.isStringEmptyOrNull(medication.getName())){
                values.put(MySQLiteHelper.COLUMN_NAME, medication.getName());
            }

            if(!Verifier.isStringEmptyOrNull(medication.getDosis())){
                values.put(MySQLiteHelper.COLUMN_DOSIS, medication.getDosis());
            }

            if(!Verifier.isStringEmptyOrNull(medication.getManufacturer())){
                values.put(MySQLiteHelper.COLUMN_MANUFACTURER, medication.getManufacturer());
            }

            if(!Verifier.isIntZero(medication.getAmountPerDay())){
                //TODO Display something adequate
                values.put(MySQLiteHelper.COLUMN_AMOUNT_PER_DAY, medication.getAmountPerDay());
            }


            long insertId = database.insert(MySQLiteHelper.TABLE_MEDICATION, null,
                    values);
        }

    }

    public void insertDisease(String disease) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DISEASES, disease);

        long insertId = database.insert(MySQLiteHelper.TABLE_DISEASES, null,
                values);
    }

    public void insertSpecialNeeds(String specialNeed) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_SPECIAL_NEEDS, specialNeed);

        long insertId = database.insert(MySQLiteHelper.TABLE_SPECIAL_NEEDS, null,
                values);
    }

}
