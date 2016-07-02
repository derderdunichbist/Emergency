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

    /**
     * Initializes the user table with a single entry 'Test'
     * This is needed, for it is not possible to insert, at the beginning of the application, a
     * dataset into the database which has no parameters specified
     */
    public void initUser() {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_FIRST_NAME, "Test");
        long insertId = database.insert(MySQLiteHelper.TABLE_USER, null,
                values);
    }

    /**
     * Checks whether there is a user specified in the database or not and performs adequately
     */
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

    /**
     * Updates the user's first name in the table 'User'
     * @param firstName The user's first name
     */
    public void updateUserFirstName(String firstName) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_FIRST_NAME, firstName);
        database.update(MySQLiteHelper.TABLE_USER, values, null, null);
    }

    /**
     * Updates the user's last name in the table 'User'
     * @param lastName The user's first name
     */
    public void updateUserLastName(String lastName) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_LAST_NAME, lastName);
        database.update(MySQLiteHelper.TABLE_USER, values, null, null);
    }

    /**
     * Updates the user's Blood Type in the table 'User'
     * @param bloodType The user's first name
     */
    public void updateUserBloodType(String bloodType) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_BLOOD_TYPE, bloodType);
        database.update(MySQLiteHelper.TABLE_USER, values, null, null);
    }

    /**
     * Updates the user's Date of Birth in the table 'User'
     * @param dateOfBirth The user's first name
     */
    public void updateUserDob(String dateOfBirth) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DATE_OF_BIRTH, dateOfBirth);
        database.update(MySQLiteHelper.TABLE_USER, values, null, null);
    }

    /**
     * Updates the user's gender in the table 'User'
     * @param gender The user's first name
     */
    public void updateGender(String gender) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_GENDER, gender);
        database.update(MySQLiteHelper.TABLE_USER, values, null, null);
    }

    /**
     * Adds a single special need into the table 'specialNeeds'
     * @param specialNeed The special need to be added
     */
    public void addSpecialNeed(String specialNeed) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_SPECIAL_NEEDS, specialNeed);
        long insertId = database.insert(MySQLiteHelper.TABLE_SPECIAL_NEEDS, null, values);
    }

    /**
     * Adds a single disease into the table 'specialNeeds'
     * @param disease The special need to be added
     */
    public void addDiseases(String disease) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DISEASES, disease);
        long insertId = database.insert(MySQLiteHelper.TABLE_DISEASES, null, values);
    }

    /**
     * Assigns the correct values from the database to the userobject
     * @param user The application's user
     */
    public void buildUserObjectFromDatabase(User user) {
        getUserData(user);
        addAllContactsToUser(user);
        addAllMedicationToUser(user);
        addAllDiseasesToUser(user);
        addAllSpecialNeedsToUser(user);
    }

    private void getUserData(User user) {
        String[] allColumns = new String[] {MySQLiteHelper.COLUMN_FIRST_NAME, MySQLiteHelper.COLUMN_LAST_NAME, MySQLiteHelper.COLUMN_DATE_OF_BIRTH, MySQLiteHelper.COLUMN_BLOOD_TYPE, MySQLiteHelper.COLUMN_GENDER};

        Cursor cursor = database.query(MySQLiteHelper.TABLE_USER,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        user.setFirstName(cursor.getString(0));
        user.setLastName(cursor.getString(1));
        user.setDateOfBirth(cursor.getString(2));
        user.setBloodType(cursor.getString(3));
        user.setGender(cursor.getString(4));

        cursor.close();
    }

    /**
     * Inserts a single contact into the database by also checking if it already exists
     * @param contact The contact to be added
     * @return <code>true</code> or <code>false</code> whether the opertaion was succesful or not
     */
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

    /**
     * Inserts a single medication into the database by also checking if it already exists
     * @param medication The medication to be added
     * @return <code>true</code> or <code>false</code> whether the opertaion was succesful or not
     */
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

    /**
     * Inserts a single disease into the database by also checking if it already exists
     * @param disease The disease to be added
     * @return <code>true</code> or <code>false</code> whether the opertaion was succesful or not
     */
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

    /**
     * Inserts a single Special Need into the database by also checking if it already exists
     * @param specialNeed The Special Need to be added
     * @return <code>true</code> or <code>false</code> whether the opertaion was succesful or not
     */
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

    /**
     * Removes and adds all the user's contacts from and to the database
     * @param user The application's user object to perform the action
     */
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

    /**
     * Removes and adds all the user's medication from and to the database
     * @param user The application's user object to perform the action
     */
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

    /**
     * Removes and adds all the user's special Needs from and to the database
     * @param user The application's user object to perform the action
     */
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

    /**
     * Removes and adds all the user's diseases from and to the database
     * @param user The application's user object to perform the action
     */
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

    /**
     * Removes and adds a single contact from and to the database
     * @param user The application's user object to perform the action
     */
    private void removeAndAddContact(User user, Cursor cursor) {
        Contact contact = cursorToContact(cursor);
        user.removeContact(contact);
        user.addContact(contact);
    }

    /**
     * Removes and adds a single medication from and to the database
     * @param user The application's user object to perform the action
     */
    private void removeAndAddMedication(User user, Cursor cursor) {
        Medication medication = cursorToMedication(cursor);
        user.removeMedication(medication);
        user.addMedication(medication);
    }

    /**
     * Removes and adds a single disease from and to the database
     * @param user The application's user object to perform the action
     */
    private void addDisease(User user, Cursor cursor) {
        String disease = cursor.getString(1);
        user.removeDisease(disease);
        user.addDisease(disease);
    }

    /**
     * Removes and adds a single special need from and to the database
     * @param user The application's user object to perform the action
     */
    private void addSpecialNeed(User user, Cursor cursor) {
        String specialNeed = cursor.getString(1);
        user.removeSpecialNeed(specialNeed);
        user.addSpecialNeed(specialNeed);
    }

    /**
     * Deletes a contact from the database
     * @param contact The contact to be removed
     */
    public void deleteContactFromDatabase(Contact contact) {
        database.delete(MySQLiteHelper.TABLE_CONTACTS, MySQLiteHelper.COLUMN_ID
                + " = " + contact.getId(), null);
    }

    /**
     * Deletes a medication from the database
     * @param medication The medication to be removed
     */
    public void deleteMedicationFromDatabase(Medication medication) {
        database.delete(MySQLiteHelper.TABLE_MEDICATION, MySQLiteHelper.COLUMN_ID
                + " = " + medication.getId(), null);
    }

    /**
     * Deletes a disease from the database
     * @param disease The disease to be removed
     */
    public void deleteDiseaseFromDatabase(String disease) {
        database.delete(MySQLiteHelper.TABLE_DISEASES, MySQLiteHelper.COLUMN_DISEASES
                + " = " + "'" + disease + "'", null);
    }

    /**
     * Deletes a special need from the database
     * @param specialNeed The special need to be removed
     */
    public void deleteSpecialNeedFromDatabase(String specialNeed) {
        database.delete(MySQLiteHelper.TABLE_SPECIAL_NEEDS, MySQLiteHelper.COLUMN_SPECIAL_NEEDS
                + " = " + "'" + specialNeed + "'", null);
    }

    /**
     * Transforms a valid medication object from a given cursor
     * @param cursor The curor to be transformed
     * @return The transformed medication
     */
    private Medication cursorToMedication(Cursor cursor) {
        Medication medication = new Medication(cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));
        medication.setId(Integer.parseInt(cursor.getString(0)));

        return  medication;
    }

    /**
     * Transforms a valid contact object from a given cursor
     * @param cursor The curor to be transformed
     * @return The transformed contact
     */
    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact(cursor.getString(1), cursor.getString(2), cursor.getString(3), Boolean.parseBoolean(cursor.getString(4)));
        contact.setId(Integer.parseInt(cursor.getString(0)));

        return  contact;
    }

}
