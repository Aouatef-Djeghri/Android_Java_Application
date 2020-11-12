package com.example.servicebypro.Activities.Dashboard.Add;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Service;


public class Post5PaymentTypeFragment extends Fragment implements View.OnClickListener {

    private MyToast myToast;
    private Util util;
    private View view;
    IDashboard iDashboard;
    protected int current_step = 5;

    private Post5PaymentTypeFragment.FragmentSelectPaymentTypeListener listener;

    public interface FragmentSelectPaymentTypeListener {
        void onInputSelectPaymentTypeSent(String payment_method);
    }

    public Post5PaymentTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        util = new Util(getContext());
        myToast = new MyToast(getContext());
        // iPost.setToastToTest("this is the final toast payment");
        iDashboard.setAddPostTitleText("Payment",iDashboard.getTextView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_post_payment_type, container, false);
        iDashboard.getButtonNext().setVisibility(View.GONE);
        //Setting listeners
        view.findViewById(R.id.hourlypay).setOnClickListener(this);
        view.findViewById(R.id.fixedpay).setOnClickListener(this);
        view.findViewById(R.id.payment_type_skip).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.hourlypay:
                iDashboard.nextStep(iDashboard.getCurrent_step());
                iDashboard.getPrevious_step(6);
                iDashboard.inflateFragment(getString(R.string.fragment_payment_hourly));
                listener.onInputSelectPaymentTypeSent("hourly");
                break;
            case R.id.fixedpay:
                iDashboard.nextStep(iDashboard.getCurrent_step());
                iDashboard.getPrevious_step(7);
                iDashboard.inflateFragment(getString(R.string.fragment_payment_fixed));
                listener.onInputSelectPaymentTypeSent("fixed");
                break;

            case R.id.payment_type_skip:
                iDashboard.nextStep(iDashboard.getCurrent_step() + 2);
                iDashboard.getPrevious_step(5);
                iDashboard.inflateFragment(getString(R.string.fragment_type));
                listener.onInputSelectPaymentTypeSent("not_precise");
                break;

        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
        iDashboard.setCurrent_step(current_step);
        if (context instanceof Post5PaymentTypeFragment.FragmentSelectPaymentTypeListener) {
            listener = (Post5PaymentTypeFragment.FragmentSelectPaymentTypeListener) context;
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