package nju.com.piece;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by shen on 15/6/30.
 */
//public static class TimePickerFragment extends DialogFragment
//        implements TimePickerDialog.OnTimeSetListener {
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        final Calendar c = Calendar.getInstance();
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//
//        return new TimePickerDialog(getActivity(), this, hour, minute,
//                DateFormat.is24HourFormat(getActivity()));
//    }
//
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//    }
//}