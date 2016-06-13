package org.hsrt.mc.emergency.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.hsrt.mc.emergency.user.Contact;

/**
 * Created by KA on 11.06.2016.
 */
public class UserDAO {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper sqlLiteHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_CONTACT };

    public UserDAO(Context context) {
        sqlLiteHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = sqlLiteHelper.getWritableDatabase();
    }

    public void close() {
        sqlLiteHelper.close();
    }

    public Contact persistContact(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_CONTACT, contact.getCompleteName());
        long insertId = database.insert(MySQLiteHelper.COLUMN_CONTACT, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.COLUMN_CONTACT,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Contact newContact = cursorToContact(cursor);
        cursor.close();
        return newContact;
    }

    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.setNameFromCompleteName(cursor.getString(0));
        return contact;
    }
}
