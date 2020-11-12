package com.example.servicebypro.Activities.Dashboard.Add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Service;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


public class Post4DateFragment extends Fragment implements View.OnClickListener {

    private MyToast myToast;
    private Util util;
    private View view;
    IDashboard iDashboard;
    private TextInputEditText date_time;
    private TextInputLayout dateTimeLayout;
    protected int current_step = 4;
    LinearLayout date_layout;

    String due_date;
    private Post4DateFragment.FragmentSelectDateListener listener;

    public interface FragmentSelectDateListener{
        void onInputSelectDateSent(String Date);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_post_date, container, false);
        iDashboard.getButtonNext().setVisibility(View.VISIBLE);
        dateTimeLayout = view.findViewById(R.id.date_time_layout);
        date_time = view.findViewById(R.id.date_time);
        date_time.setInputType(InputType.TYPE_NULL);
        date_layout = view.findViewById(R.id.date_layout);

        //Setting listeners
         view.findViewById(R.id.date_time).setOnClickListener(this);

        //Setting listeners
        iDashboard.getButtonNext().setOnClickListener(this);

       // preFillingEditTextIfDataExists();
        return view;
    }

/*    public void preFillingEditTextIfDataExists(){
        if(iDashboard.getWorkDate()!=null){
            String input = iDashboard.getWorkDate();
            dateTimeLayout.getEditText().setText(input);
        }
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.date_time:
                //TODO do things
                showDateTimeDialog(date_time);
               // myToast.showInfo("R.id.date_time");
                break;
            case R.id.add_post_next_btn:
                submitDate();
                break;


        }
    }

    public void submitDate() {
        if (!util.validateWorkDate(dateTimeLayout)) {
            return;
        }else{
            iDashboard.nextStep(iDashboard.getCurrent_step());
            iDashboard.inflateFragment(getString(R.string.fragment_payment));
            listener.onInputSelectDateSent(due_date);
        }
    }

    private void showDateTimeDialog(TextInputEditText date_time) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);


                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        due_date = simpleDateFormat.format(calendar.getTime());
                        date_time.setText(due_date);

                    }
                };
                new TimePickerDialog(getActivity(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),false).show();
            }
        };


        DatePickerDialog dialog = new DatePickerDialog(getContext(), dateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        dialog.show();
    }


    public Post4DateFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilities class contains reusable methods
        util = new Util(getContext());
        myToast = new MyToast(getContext());
        iDashboard.setAddPostTitleText("Date",iDashboard.getTextView());
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
        iDashboard.setCurrent_step(current_step);

        if (context instanceof Post4DateFragment.FragmentSelectDateListener) {
            listener = (Post4DateFragment.FragmentSelectDateListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentAListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}