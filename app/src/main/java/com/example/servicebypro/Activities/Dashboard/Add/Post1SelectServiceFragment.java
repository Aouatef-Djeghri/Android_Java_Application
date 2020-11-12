package com.example.servicebypro.Activities.Dashboard.Add;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.servicebypro.Activities.Adapters.ServicesAdapter;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Categorie;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.ViewModels.AddFragmentViewModel;
import java.util.ArrayList;


public class Post1SelectServiceFragment extends Fragment implements ServicesAdapter.OnServiceListener  {
    private MyToast myToast;
    private Util util;
    private View view;
    private RecyclerView recyclerView;
    private ServicesAdapter adapter;
    AddFragmentViewModel addFragmentViewModel ;
    ArrayList<Service> servicesList = new ArrayList<>();
    IDashboard iDashboard;
    protected int current_step = 1;

    private FragmentSelectServiceListener listener;

    public interface FragmentSelectServiceListener{
        void onInputSelectServiceSent(Service service);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_post_select_service, container, false);
        iDashboard.getButtonNext().setVisibility(View.GONE);
        // to get the value in the next activity use this
/*        if (getActivity().getIntent().hasExtra("category")) {
            Categorie categorie = getActivity().getIntent().getParcelableExtra("category");
            //myToast.showInfo("" + categorie.getName());
            fetchData(categorie.getIdCategorie());
        }*/
        if(iDashboard.getWorkCategory() != null){
            Categorie categorie = iDashboard.getWorkCategory();
            fetchData(categorie.getIdCategorie());
        }

        return view;
    }
    private void initRecyclerView(ArrayList<Service> services) {
        recyclerView = (RecyclerView) view.findViewById(R.id.select_service_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ServicesAdapter(services, getContext(),this);
        recyclerView.setAdapter(adapter);

    }

    private void fetchData(int idCategory) {

        addFragmentViewModel.getCategorieServicesList(idCategory).observe(getViewLifecycleOwner(), new Observer<ArrayList<Service>>() {
            @Override
            public void onChanged(ArrayList<Service> services) {
                servicesList = services;
                initRecyclerView(services);
                adapter.notifyDataSetChanged();
                if(services.isEmpty()){
                    //TODO for now this has no use case ,but later find a solution in the case where a category has no services
                }
            }
        });
        addFragmentViewModel.getToastService().observe(getViewLifecycleOwner(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }


    @Override
    public void onServiceClick(int position) {

        //getActivity().getIntent().putExtra("service", servicesList.get(position));
       // ((Post0Job)getActivity()).nextStep(((Post0Job)getActivity()).getCurrent_step());
       // ((Post0Job)getActivity()).showFragment(new Post2DetailsFragment());
        iDashboard.nextStep(iDashboard.getCurrent_step());
        iDashboard.inflateFragment(getString(R.string.fragment_details));
        Service service = servicesList.get(position);
        listener.onInputSelectServiceSent(service);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilities class contains reusable methods
        util = new Util(getContext());
        myToast = new MyToast(getContext());
        addFragmentViewModel = new ViewModelProvider(requireActivity()).get(AddFragmentViewModel.class);
       // iPost.setToastToTest("this is the final toast services");
        iDashboard.setAddPostTitleText("Services", iDashboard.getTextView());

    }

    public Post1SelectServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
        iDashboard.setCurrent_step(current_step);
        iDashboard.getProgressBar().setProgress(1);
        if (context instanceof FragmentSelectServiceListener) {
            listener = (FragmentSelectServiceListener) context;
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