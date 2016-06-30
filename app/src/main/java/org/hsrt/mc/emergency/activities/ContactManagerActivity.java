package org.hsrt.mc.emergency.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.hsrt.mc.emergency.R;
import org.hsrt.mc.emergency.persistence.UserDAO;
import org.hsrt.mc.emergency.user.Contact;
import org.hsrt.mc.emergency.user.User;
import org.hsrt.mc.emergency.user.UserImplementation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContactManagerActivity extends AppCompatActivity {
    ListView listView;
    User user;
    Button firstContactButton;
    Button secondContactButton;
    Button thirdContactButton;
    private Map<Integer, Integer> contactIdMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_manager);

        user = UserImplementation.getUserObject();

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.contact_manager_toolbar);
        setSupportActionBar(mActionBarToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        firstContactButton= (Button)findViewById(R.id.first_contact_button);
        secondContactButton= (Button)findViewById(R.id.second_contact_button);
        thirdContactButton= (Button)findViewById(R.id.third_contact_button);

        initButtons();

        firstContactButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 1);
            }
        });

        secondContactButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 2);
            }
        });

        thirdContactButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 3);
            }
        });
    }

    private void initButtons() {
        List<Contact> contacts = user.getContacts();
        Contact importantContact = null;
        boolean secondButtonAssigned = false;
        contactIdMap.clear();
        for(Contact contact : contacts) {
            if(contact.isFavourite()) {
                contactIdMap.put(1, contact.getId());
                firstContactButton.setText(contact.getName());
            } else {
                if(!secondButtonAssigned) {
                    contactIdMap.put(2, contact.getId());
                    secondContactButton.setText(contact.getName());
                    secondButtonAssigned = true;
                } else {
                    contactIdMap.put(3, contact.getId());
                    thirdContactButton.setText(contact.getName());
                }
            }
        }

        if(!contactIdMap.containsKey(1)) {
            firstContactButton.setText("No user specified");
        }

        if(!contactIdMap.containsKey(2)) {
            secondContactButton.setText("No user specified");
        }

        if(!contactIdMap.containsKey(3)) {
            thirdContactButton.setText("No user specified");
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //ListView listView = (ListView)findViewById(R.id.contactsListViewTest);
        if (resultCode == RESULT_OK) {
            boolean isFavourite = false;
            int idToDelete = 0;
            try {
                if(requestCode == 1) {
                    isFavourite = true;
                    idToDelete = contactIdMap.get(1);
                } else if(requestCode == 2) {
                    idToDelete = contactIdMap.get(2);
                } else if(requestCode == 3) {
                    idToDelete = contactIdMap.get(3);
                }

            } catch (Exception e) {
                idToDelete = -1;
            }

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
                    String cNumber = "";

                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                null, null);
                        phones.moveToFirst();
                        cNumber = phones.getString(phones.getColumnIndex("data1")); //User number
                    }


                    String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // String email = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    //String phoneNumber = c.getString(c.getColumnIndex(ContactsContract.Data.DATA1));
                    Contact contact = new Contact(name, "test@test.de", cNumber, isFavourite);

                    List<Contact> contacts = new ArrayList<>();
                    for(Contact contactToAddToList : user.getContacts()) {
                        contacts.add(contactToAddToList.clone());
                    }

                    if(idToDelete != -1) {
                        for(Contact contactToDelete : contacts) {
                            if (contactToDelete.getId() == idToDelete) {
                                user.removeContact(contactToDelete);
                            }
                        }
                    }
                    user.addContact(contact);
            }
            this.initButtons();
        } else {

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

       // ArrayAdapter<Contact> adapter = (ArrayAdapter<Contact>) getListAdapter();
        Contact contact = new Contact();
        contact.setName(name);
    }

}
