package com.example.servicebypro.Activities.Dashboard.Add;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;

public class Post6PaymentHourlyFragment extends Fragment implements View.OnClickListener {

    private MyToast myToast;
    private Util util;
    private View view;
    protected int current_step = 6;
    IDashboard iDashboard;

    public Post6PaymentHourlyFragment() {
        // Required empty public constructor
    }

    private Post6PaymentHourlyFragment.FragmentSelectPriceListener listener;

    public interface FragmentSelectPriceListener {
        void onInputSelectPriceSent(Double min_price, Double max_price);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // iPost.setToastToTest("this is the final toast hourly");
        iDashboard.setAddPostTitleText("Hourly",iDashboard.getTextView());
        myToast = new MyToast(getContext());
        util = new Util(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_payment_hourly, container, false);
        iDashboard.getButtonNext().setVisibility(View.GONE);
        //Setting listeners
        view.findViewById(R.id.hour2_8).setOnClickListener(this);
        view.findViewById(R.id.hour8_15).setOnClickListener(this);
        view.findViewById(R.id.hour15_25).setOnClickListener(this);
        view.findViewById(R.id.hour25_50).setOnClickListener(this);
        view.findViewById(R.id.hour50).setOnClickListener(this);

        // view.findViewById(R.id.add_post_next_btn).setOnClickListener(this);
        //myToast.showSuccess(iPost.getCurrent_step() + " hourly");
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
        iDashboard.setCurrent_step(current_step);
        if (context instanceof Post6PaymentHourlyFragment.FragmentSelectPriceListener) {
            listener = (Post6PaymentHourlyFragment.FragmentSelectPriceListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentAListener");
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

/*            case R.id.add_post_next_btn:

                break;*/
            case R.id.hour2_8:
                listener.onInputSelectPriceSent(2.0,8.0);
                break;
            case R.id.hour8_15:
                listener.onInputSelectPriceSent(8.0,15.0);
                break;
            case R.id.hour15_25:
                listener.onInputSelectPriceSent(15.0,25.0);
                break;
            case R.id.hour25_50:
                listener.onInputSelectPriceSent(25.0,50.0);
                break;
            case R.id.hour50:
                listener.onInputSelectPriceSent(50.0,50.0);
                break;
        }
        iDashboard.nextStep(iDashboard.getCurrent_step() + 1);
        // myToast.showSuccess(((TextView)view.findViewById(view.getId())).getText().toString());
        iDashboard.inflateFragment(getString(R.string.fragment_type));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}