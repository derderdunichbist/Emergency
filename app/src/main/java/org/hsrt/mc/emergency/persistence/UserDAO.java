package org.hsrt.mc.emergency.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.hsrt.mc.emergency.user.Contact;
import org.hsrt.mc.emergency.user.Medication;
import org.hsrt.mc.emergency.user.User;
import org.hsrt.mc.emergency.utils.Verifier;

import java.util.Date;

/**
 * Created by KA on 11.06.2016.
 */
public class UserDAO {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper sqlLiteHelper;
    private Context context;
    private static UserDAO userDAO;


    public UserDAO(Context context) {
        if(userDAO == null) {
            sqlLiteHelper = new MySQLiteHelper(context);
            this.context = context;
            userDAO = this;
        }
    }

    public Context getContext() {
        return this.context;
    }

    public void open() throws SQLException {
        database = sqlLiteHelper.getWritableDatabase();
        this.setUpUser();

    }

    public void close() {
        sqlLiteHelper.close();
    }

    public static UserDAO getUserDAO() {
        return userDAO;
    }

    public void initUser() {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_FIRST_NAME, "Test");
        long insertId = database.insert(MySQLiteHelper.TABLE_USER, null,
                values);
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

    public void buildUserObjectFromDatabase(User user) {
        getUserData(user);
        addAllContactsToUser(user);
        addAllMedicationToUser(user);
        addAllDiseasesToUser(user);
        addAllSpecialNeedsToUser(user);
    }

    private void getUserData(User user) {
        String[] allColumns = new String[] {MySQLiteHelper.COLUMN_FIRST_NAME, MySQLiteHelper.COLUMN_LAST_NAME, MySQLiteHelper.COLUMN_DATE_OF_BIRTH, MySQLiteHelper.COLUMN_BLOOD_TYPE};

        Cursor cursor = database.query(MySQLiteHelper.TABLE_USER,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        user.setFirstName(cursor.getString(0));
        user.setLastName(cursor.getString(1));
        //user.setDateOfBirth(cursor.getString(2));
        user.setBloodType(cursor.getString(3));

        // make sure to close the cursor
        cursor.close();
    }

    public boolean insertContactIntoDatabase(Contact contact) {
        boolean contactAdded = false;
        //Check if already in DB
        String[] allColumns = new String[] {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_EMAIL, MySQLiteHelper.COLUMN_PHONE_NUMBER, MySQLiteHelper.COLUMN_IS_FAVOURITE};
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        boolean isInDb = false;
        while (!cursor.isAfterLast()) {
            Contact con = cursorToContact(cursor);
            if(con.equals(contact)){
                isInDb = true;
            }
            cursor.moveToNext();
        }
        cursor.close();

        if(!isInDb) {
            ContentValues values = new ContentValues();

            if(!Verifier.isStringEmptyOrNull(contact.getName())) {
                values.put(MySQLiteHelper.COLUMN_NAME, contact.getName());
            }

            if(!Verifier.isStringEmptyOrNull(contact.getEmail())) {
                values.put(MySQLiteHelper.COLUMN_EMAIL, contact.getEmail());
            }

            if(!Verifier.isStringEmptyOrNull(contact.getPhoneNumber())) {
                values.put(MySQLiteHelper.COLUMN_PHONE_NUMBER, contact.getPhoneNumber());
            }
            values.put(MySQLiteHelper.COLUMN_IS_FAVOURITE, String.valueOf(contact.isFavourite()));

            if(contact.getId() == 0) {
                long insertId = database.insert(MySQLiteHelper.TABLE_CONTACTS, null, values);
                contact.setId((int)insertId);
            } else {
                values.put(MySQLiteHelper.COLUMN_ID, contact.getId());
                database.insert(MySQLiteHelper.TABLE_CONTACTS, null, values);
            }

            contactAdded = true;
        }
        return contactAdded;
    }

    public boolean insertMedicationIntoDatabase(Medication medication) {
        boolean medicationAdded = false;
        //Check if already in DB
        String[] allColumns = new String[] {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_DOSIS, MySQLiteHelper.COLUMN_MANUFACTURER, MySQLiteHelper.COLUMN_AMOUNT_PER_DAY};
        Cursor cursor = database.query(MySQLiteHelper.TABLE_MEDICATION,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        boolean isInDb = false;
        while (!cursor.isAfterLast()) {
            Medication med = cursorToMedication(cursor);
            if(med.equals(medication)){
                isInDb = true;
            }
            cursor.moveToNext();
        }
        cursor.close();

        if(!isInDb) {
            ContentValues values = new ContentValues();

            if(!Verifier.isStringEmptyOrNull(medication.getName())) {
                values.put(MySQLiteHelper.COLUMN_NAME, medication.getName());
            }

            if(!Verifier.isStringEmptyOrNull(medication.getDosis())) {
                values.put(MySQLiteHelper.COLUMN_DOSIS, medication.getDosis());
            }

            if(!Verifier.isStringEmptyOrNull(medication.getManufacturer())) {
                values.put(MySQLiteHelper.COLUMN_MANUFACTURER, medication.getManufacturer());
            }
            values.put(MySQLiteHelper.COLUMN_AMOUNT_PER_DAY, String.valueOf(medication.getAmountPerDay()));

            if(medication.getId() == 0) {
                long insertId = database.insert(MySQLiteHelper.TABLE_MEDICATION, null, values);
                medication.setId((int)insertId);
            } else {
                values.put(MySQLiteHelper.COLUMN_ID, medication.getId());
                database.insert(MySQLiteHelper.TABLE_MEDICATION, null, values);
            }

            medicationAdded = true;
        }
        return medicationAdded;
    }

    public boolean insertDiseaseIntoDatabase(String disease) {
        boolean diseaseAdded = false;
        //Check if already in DB
        String[] allColumns = new String[] {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_DISEASES};
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DISEASES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        boolean isInDb = false;
        while (!cursor.isAfterLast()) {
            String dis = cursor.getString(1);
            if(dis.equals(disease)){
                isInDb = true;
            }
            cursor.moveToNext();
        }
        cursor.close();

        if(!isInDb) {
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_DISEASES, disease);
            database.insert(MySQLiteHelper.TABLE_DISEASES, null, values);
            diseaseAdded = true;
        }
        return diseaseAdded;
    }

    public boolean insertSpecialNeedIntoDatabase(String specialNeed) {
        boolean diseaseAdded = false;
        //Check if already in DB
        String[] allColumns = new String[] {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_SPECIAL_NEEDS};
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SPECIAL_NEEDS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        boolean isInDb = false;
        while (!cursor.isAfterLast()) {
            String sN = cursor.getString(1);
            if(sN.equals(specialNeed)){
                isInDb = true;
            }
            cursor.moveToNext();
        }
        cursor.close();

        if(!isInDb) {
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_SPECIAL_NEEDS, specialNeed);
            database.insert(MySQLiteHelper.TABLE_SPECIAL_NEEDS, null, values);
            diseaseAdded = true;
        }
        return diseaseAdded;
    }

    private void addAllContactsToUser(User user) {
        String[] allColumns = new String[] {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_EMAIL, MySQLiteHelper.COLUMN_PHONE_NUMBER, MySQLiteHelper.COLUMN_IS_FAVOURITE};
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            removeAndAddContact(user, cursor);
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void addAllMedicationToUser(User user) {
        String[] allColumns = new String[] {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_DOSIS, MySQLiteHelper.COLUMN_MANUFACTURER, MySQLiteHelper.COLUMN_AMOUNT_PER_DAY};
        Cursor cursor = database.query(MySQLiteHelper.TABLE_MEDICATION,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            removeAndAddMedication(user, cursor);
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void addAllSpecialNeedsToUser(User user) {
        String[] allColumns = new String[] {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_SPECIAL_NEEDS};
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SPECIAL_NEEDS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            addSpecialNeed(user, cursor);
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void addAllDiseasesToUser(User user) {
        String[] allColumns = new String[] {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_DISEASES};
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DISEASES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            addDisease(user, cursor);
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void removeAndAddContact(User user, Cursor cursor) {
        Contact contact = cursorToContact(cursor);
        user.removeContact(contact);
        user.addContact(contact);
    }


    private void removeAndAddMedication(User user, Cursor cursor) {
        Medication medication = cursorToMedication(cursor);
        user.removeMedication(medication);
        user.addMedication(medication);
    }

    private void addDisease(User user, Cursor cursor) {
        String disease = cursor.getString(1);
        user.removeDisease(disease);
        user.addDisease(disease);
    }

    private void addSpecialNeed(User user, Cursor cursor) {
        String specialNeed = cursor.getString(1);
        user.removeSpecialNeed(specialNeed);
        user.addSpecialNeed(specialNeed);
    }

    public void deleteContactFromDatabase(Contact contact) {
        database.delete(MySQLiteHelper.TABLE_CONTACTS, MySQLiteHelper.COLUMN_ID
                + " = " + contact.getId(), null);
    }


    public void deleteMedicationFromDatabase(Medication medication) {
        database.delete(MySQLiteHelper.TABLE_MEDICATION, MySQLiteHelper.COLUMN_ID
                + " = " + medication.getId(), null);
    }

    public void deleteDiseaseFromDatabase(String disease) {
        database.delete(MySQLiteHelper.TABLE_DISEASES, MySQLiteHelper.COLUMN_DISEASES
                + " = " + "'" + disease + "'", null);
    }

    public void deleteSpecialNeedFromDatabase(String specialNeed) {
        database.delete(MySQLiteHelper.TABLE_SPECIAL_NEEDS, MySQLiteHelper.COLUMN_SPECIAL_NEEDS
                + " = " + "'" + specialNeed + "'", null);
    }

    private Medication cursorToMedication(Cursor cursor) {
        Medication medication = new Medication(cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));
        medication.setId(Integer.parseInt(cursor.getString(0)));

        return  medication;
    }


    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact(cursor.getString(1), cursor.getString(2), cursor.getString(3), Boolean.parseBoolean(cursor.getString(4)));
        contact.setId(Integer.parseInt(cursor.getString(0)));

        return  contact;
    }
}
