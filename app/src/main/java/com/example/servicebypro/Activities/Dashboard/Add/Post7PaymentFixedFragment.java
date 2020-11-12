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

public class Post7PaymentFixedFragment extends Fragment implements View.OnClickListener {
    private MyToast myToast;
    private Util util;
    private View view;
    protected int current_step =7;
    IDashboard iDashboard;

    public Post7PaymentFixedFragment() {
        // Required empty public constructor
    }

    private Post7PaymentFixedFragment.FragmentSelectPriceListener listener;

    public interface FragmentSelectPriceListener {
        void onInputSelectPriceSent(Double min_price, Double max_price);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // iPost.setToastToTest("this is the final toast fixed");
        iDashboard.setAddPostTitleText("Fixed",iDashboard.getTextView());
        myToast = new MyToast(getContext());
        util = new Util(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_post_payment_fixed, container, false);
        iDashboard.getButtonNext().setVisibility(View.GONE);
        //Setting listeners
        view.findViewById(R.id.hour10_30).setOnClickListener(this);
        view.findViewById(R.id.hour30_250).setOnClickListener(this);
        view.findViewById(R.id.hour250_750).setOnClickListener(this);
        view.findViewById(R.id.hour750_1500).setOnClickListener(this);
        view.findViewById(R.id.hour1500_3000).setOnClickListener(this);
        view.findViewById(R.id.hour3000_5000).setOnClickListener(this);
        //view.findViewById(R.id.add_post_next_btn).setOnClickListener(this);
       // myToast.showSuccess(iPost.getCurrent_step()+" fixed");
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
        iDashboard.setCurrent_step(current_step);
        if (context instanceof Post7PaymentFixedFragment.FragmentSelectPriceListener) {
            listener = (Post7PaymentFixedFragment.FragmentSelectPriceListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentAListener");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

/*            case R.id.add_post_next_btn:
                break;*/
            case R.id.hour10_30:
                listener.onInputSelectPriceSent(10.0,30.0);
                break;
            case R.id.hour30_250:
                listener.onInputSelectPriceSent(30.0,250.0);
                break;
            case R.id.hour250_750:
                listener.onInputSelectPriceSent(250.0,750.0);
                break;
            case R.id.hour750_1500:
                listener.onInputSelectPriceSent(750.0,1500.0);
                break;
            case R.id.hour1500_3000:
                listener.onInputSelectPriceSent(1500.0,3000.0);
                break;
            case R.id.hour3000_5000:
                listener.onInputSelectPriceSent(3000.0,5000.0);
                break;
        }
        iDashboard.nextStep(iDashboard.getCurrent_step());
        iDashboard.inflateFragment(getString(R.string.fragment_type));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}