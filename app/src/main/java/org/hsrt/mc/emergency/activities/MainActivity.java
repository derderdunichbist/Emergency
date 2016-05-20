package org.hsrt.mc.emergency.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.hsrt.mc.emergency.R;
import org.hsrt.mc.emergency.backend.Contact;

import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();

        Button button = (Button) findViewById(R.id.button);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView textView = (TextView)findViewById(R.id.textView2);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1001:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Set keys = extras.keySet();
                        Iterator iterate = keys.iterator();
                        while (iterate.hasNext()) {
                            String key = (String) iterate.next();
                            // Log.v(DEBUG_TAG, key + "[" + extras.get(key) + "]");
                        }
                    }
                    Uri result = data.getData();
                    textView.setText("Got a result: " + result.toString());

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
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            System.out.println("number is:" + cNumber);
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        textView.setText(name);

                        break;
                    }


            }
        } else {
            throw new RuntimeException("Bad result");
        }
    }

}