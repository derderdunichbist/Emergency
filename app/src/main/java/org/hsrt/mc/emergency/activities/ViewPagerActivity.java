package org.hsrt.mc.emergency.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.hsrt.mc.emergency.R;
import org.hsrt.mc.emergency.persistence.UserDAO;
import org.hsrt.mc.emergency.user.Contact;
import org.hsrt.mc.emergency.user.User;
import org.hsrt.mc.emergency.user.UserImplementation;
import org.hsrt.mc.emergency.utils.DatePickerFrag;
import org.hsrt.mc.emergency.utils.Verifier;

public class ViewPagerActivity extends AppCompatActivity{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    /**
     * A placeholder fragment containing a simple view.
     */
    public static class UserDataFragment1 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private EditText phoneNumber1, phoneNumber2, phoneNumber3, firstName, lastName, medicationName, dosis,
        diseases,specialNeeds;
        private Contact contact1, contact2, contact3;
        private Spinner bloodTypeSp;
        private RadioGroup gender;


/*        private Boolean isFirstTime;

        SharedPreferences app_preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

        SharedPreferences.Editor editor = app_preferences.edit();*/


        public UserDataFragment1() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static UserDataFragment1 newInstance(int sectionNumber) {
            UserDataFragment1 fragment = new UserDataFragment1();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {

                final User user = UserImplementation.getUserObject();
                if(container == null){
                    return null;
                }
                View rootView = null;
                switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                    case 1: rootView  =inflater.inflate(R.layout.fragment_user_data, container, false);
                        firstName = (EditText) rootView.findViewById(R.id.firstNameTf);
                        //firstName.setText(user.getFirstName());
                        lastName = (EditText) rootView.findViewById(R.id.lastNameTf);
                        //lastName.setText(user.getLastName());
                        gender = (RadioGroup) rootView.findViewById(R.id.genderRadio);

                        firstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if(hasFocus == false) {
                                    if(!Verifier.isStringEmptyOrNull(firstName.getText().toString())) {
                                        user.setFirstName(firstName.getText().toString());
                                    }
                                }
                            }
                        });

                        lastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if(hasFocus == false) {
                                    if(!Verifier.isStringEmptyOrNull(lastName.getText().toString())) {
                                        user.setLastName(lastName.getText().toString());
                                    }
                                }
                            }
                        });

                        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                int checkedButtonId = group.getCheckedRadioButtonId();
                                View radioButton = group.findViewById(checkedButtonId);
                                int idx = group.indexOfChild(radioButton);
                                RadioButton r = (RadioButton) group.getChildAt(idx);
                                String selectedtext = r.getText().toString();
                                user.setGender(selectedtext);
                            }
                        });

                        break;

                    case 2: rootView = inflater.inflate(R.layout.fragment2_user_needs, container, false);

                        bloodTypeSp = (Spinner) rootView.findViewById(R.id.bloodTypeList);
                        medicationName = (EditText) rootView.findViewById(R.id.medicationName);
                        dosis = (EditText) rootView.findViewById(R.id.dosis);
                        diseases = (EditText) rootView.findViewById(R.id.diseases);
                        specialNeeds = (EditText) rootView.findViewById(R.id.specialNeeds);

                        bloodTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String bloodType = bloodTypeSp.getItemAtPosition(position).toString();
                                if (!Verifier.isStringEmptyOrNull(bloodType)) {
                                    user.setBloodType(bloodType);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        break;

                    case 3:
                        rootView = inflater.inflate(R.layout.fragment3_user_contacts, container, false);

                        phoneNumber1 = (EditText) rootView.findViewById(R.id.phoneNumber1);
                        phoneNumber2 = (EditText) rootView.findViewById(R.id.phoneNumber2);
                        phoneNumber3 = (EditText) rootView.findViewById(R.id.phoneNumber3);

                        Button saveData = (Button) rootView.findViewById(R.id.confirmUserData);
                        saveData.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                                        ContactsContract.Contacts.CONTENT_URI);
                                startActivityForResult(contactPickerIntent, 1001);

                                Contact contact1 = new Contact(null,null,phoneNumber1.getText().toString(),true);



                                //TODO Add contact here!
                                //user.addContact(contact1);

                               /* isFirstTime = app_preferences.getBoolean("isFirstTime", true);

                                if (!isFirstTime) {

                                    Intent main = new Intent(getActivity(), MainActivity.class);
                                    startActivity(main);
                                    getActivity().finish();

                                }else{
                                    editor.putBoolean("isFirstTime", false);
                                    editor.commit();
                                    Log.d("PhoneNumberTest",phoneNumber1.getText().toString());
                                }*/

                            }

                        });


                        break;
                }

                return rootView;
            }

    }




    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return UserDataFragment1.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFrag();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


}
