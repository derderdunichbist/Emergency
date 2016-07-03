package org.hsrt.mc.emergency.activities;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.hsrt.mc.emergency.R;
import org.hsrt.mc.emergency.persistence.UserDAO;
import org.hsrt.mc.emergency.user.Contact;
import org.hsrt.mc.emergency.user.Medication;
import org.hsrt.mc.emergency.user.User;
import org.hsrt.mc.emergency.user.UserImplementation;
import org.hsrt.mc.emergency.utils.DatePickerFrag;
import org.hsrt.mc.emergency.utils.Verifier;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        private EditText firstName, lastName;
        private static ListView medications, diseaseses,specialNeedlist;
        private Spinner bloodTypeSp;
        private Button addMedication;
        private RadioGroup gender;
        private RadioButton male,female;


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

        /*
        * All elements in the settings menu are declared here an focus change listener will be set to recognize if there is any new content.
        * The switch class is for the given page numbers in the viewpager.
         */

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
                    lastName = (EditText) rootView.findViewById(R.id.lastNameTf);
                    bloodTypeSp = (Spinner) rootView.findViewById(R.id.bloodTypeList);
                    gender = (RadioGroup) rootView.findViewById(R.id.genderRadio);
                    male = (RadioButton) rootView.findViewById(R.id.maleRb);
                    female = (RadioButton) rootView.findViewById(R.id.femaleRb);

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
                    gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if(male.isSelected()){

                            }else if(female.isSelected()){

                            }
                        }
                    });

                    break;

                case 2: rootView = inflater.inflate(R.layout.fragment2_user_needs, container, false);

                    medications = (ListView) rootView.findViewById(R.id.medicationView);

                    addMedication = (Button) rootView.findViewById(R.id.addButton);

                    addMedication.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog();
                        }
                    });



                    break;
            }

            return rootView;
        }

        void showDialog() {
            DialogFragment newFragment = new MedicationFrag();
            newFragment.show(getFragmentManager(), "dialog");
        }

    }

    public static class MedicationFrag extends DialogFragment {
        private EditText medicationName, manufac, amount,dosis, diseases,specialNeeds;
        private Button saveButton;
        final User user = UserImplementation.getUserObject();


        public MedicationFrag(){

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment3_medication, container, false);
            getDialog().setTitle("Simple Dialog");
            medicationName = (EditText) rootView.findViewById(R.id.medicationName);
            dosis = (EditText) rootView.findViewById(R.id.dosis);

            manufac= (EditText) rootView.findViewById(R.id.manufacEdit);
            amount= (EditText) rootView.findViewById(R.id.amountEdit);

            diseases = (EditText) rootView.findViewById(R.id.diseases);
            specialNeeds = (EditText) rootView.findViewById(R.id.specialNeeds);
            saveButton = (Button) rootView.findViewById(R.id.saveButton);





            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* user.addMedication(new Medication(medicationName.getText().toString(),null,null,0));*/
                    ArrayList<String> medication = new ArrayList<>();
                    medication.add("Name: "+medicationName.getText().toString());
                    medication.add("Dosis: "+dosis.getText().toString());
                    medication.add("Manufacturer: "+manufac.getText().toString());
                    medication.add("Amount: "+amount.getText().toString());

                    ArrayList<String> diseasesList = new ArrayList<>();
                    diseasesList.add(diseases.getText().toString());

                    ArrayList<String> specialNeedsList = new ArrayList<>();
                    specialNeedsList.add(specialNeeds.getText().toString());

                    final  ListView medicationView = (ListView) getActivity().findViewById(R.id.medicationView);
                    final  ListView diseasesView = (ListView) getActivity().findViewById(R.id.DiseasesView);
                    final  ListView specialNeedsView= (ListView) getActivity().findViewById(R.id.specialNeedsView);

                    final StableArrayAdapter madapter = new StableArrayAdapter(getContext(),
                            android.R.layout.simple_list_item_1, medication);
                    medicationView.setAdapter(madapter);

                    final StableArrayAdapter dadapter = new StableArrayAdapter(getContext(),
                            android.R.layout.simple_list_item_1, diseasesList);
                    diseasesView.setAdapter(dadapter);

                    final StableArrayAdapter sadapter = new StableArrayAdapter(getContext(),
                            android.R.layout.simple_list_item_1, specialNeedsList);
                    specialNeedsView.setAdapter(sadapter);
              /*      user.addDisease(diseases.getText().toString());
                    user.addSpecialNeed(specialNeeds.getText().toString());*/
                    dismiss();
                }
            });
            return rootView;
        }

       /* public static MedicationFrag newInstance(int title) {
            MedicationFrag frag = new MedicationFrag();
            Bundle args = new Bundle();
            args.putInt("title", title);
            frag.setArguments(args);
            return frag;
        }*/


/*    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.alert_dialog_dart_icon)
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                ((FragmentDialogAlarmActivity) getActivity())
                                        .doPositiveClick();
                            }
                        })
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                ((FragmentDialogAlarmActivity) getActivity())
                                        .doNegativeClick();
                            }
                        }).create();
    }*/
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
            return 2;
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

    private static class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }
    }


}
