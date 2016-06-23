package org.hsrt.mc.emergency.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.hsrt.mc.emergency.R;
import org.hsrt.mc.emergency.persistence.UserDAO;
import org.hsrt.mc.emergency.user.Contact;

import java.util.Iterator;
import java.util.Set;

public class ContactManagerActivity extends ListActivity {

    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_manager);

        this.userDAO = new UserDAO(this);
        this.userDAO.open();

        Button button = (Button)findViewById(R.id.buttonAddContact);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 1001);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //ListView listView = (ListView)findViewById(R.id.contactsListViewTest);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1001:
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        Set keys = extras.keySet();
                        Iterator iterate = keys.iterator();
                        while (iterate.hasNext()) {
                            String key = (String) iterate.next();
                            // Log.v(DEBUG_TAG, key + "[" + extras.get(key) + "]");
                        }
                    }
                    Uri result = intent.getData();

                    Cursor c = managedQuery(result, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            /*
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            for(String s: phones.getColumnNames()){
                                System.out.println(s + ": " + phones.getString(phones.getColumnIndex(s)));

                            }
                            phones.close();
                           // System.out.println("number is:" + cNumber);
                            */
                        }


                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_ALTERNATIVE));
                       // String email = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        //String phoneNumber = c.getString(c.getColumnIndex(ContactsContract.Data.DATA1));
                        putIntoDatabase(name, "test@test.de", "+4971156471321");

                        //this.displayAndPersist(name);

                        break;
                    }

            }
        } else {
            throw new RuntimeException("Bad result");
        }
    }

    private void putIntoDatabase(String name, String email, String phoneNumber) {

        Contact contact = new Contact(name, email, phoneNumber, true);
        //this.userDAO.insertContact(contact);
    }

  /*  @Override
    protected void onResume()
    {
        this.userDAO.open();
        super.onResume();
    }


    @Override
    protected void onPause() {
        this.userDAO.close();
        super.onPause();
    }
    */
    private void displayAndPersist(String name) {

        ArrayAdapter<Contact> adapter = (ArrayAdapter<Contact>) getListAdapter();
        Contact contact = new Contact();
        contact.setName(name);
    }

}
