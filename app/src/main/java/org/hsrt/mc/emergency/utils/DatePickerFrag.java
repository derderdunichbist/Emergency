package org.hsrt.mc.emergency.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;

import org.hsrt.mc.emergency.R;

import java.util.Calendar;

/**
 * Created by Andreas on 21.06.2016.
 */
public class DatePickerFrag extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    Button dobButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Uses the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        dobButton = (Button) getActivity().findViewById(R.id.calenderButton);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // Do something with the date chosen by the user

        dobButton.setText(view.getDayOfMonth()+"."+view.getMonth()+"."+view.getYear());

    }

}