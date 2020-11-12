package com.example.servicebypro.Activities.Dashboard.Add;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.servicebypro.Activities.Adapters.CategoryAdapter;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Categorie;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.ViewModels.AddFragmentViewModel;

import java.util.ArrayList;

public class AddFragment extends Fragment implements CategoryAdapter.OnCategoryListener {
    private static final String TAG = "AddFragment";
    private static final int NUM_COLUMNS = 2;
    private CategoryAdapter categoryAdapter;
    private AddFragmentViewModel addViewModel;
    private Util util;
    private MyToast myToast;
    private RecyclerView recyclerView;
    private View view;

    private IDashboard iDashboard;

     ArrayList<Categorie> categoriesList = new ArrayList<>();

    private AddFragment.FragmentSelectCategoryListener listener;

    public interface FragmentSelectCategoryListener{
        void onInputSelectCategorySent(Categorie categorie);
    }

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add, container, false);

        return view;
    }


    private void initRecyclerView(ArrayList<Categorie> categories) {

        recyclerView = view.findViewById(R.id.search_recycler_view);
        recyclerView.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapter(categories, getContext(),this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 3 == 0 ? 2 : 1);
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(categoryAdapter);

    }


    private void fetchData() {

        addViewModel.getCategoryList().observe(this, new Observer<ArrayList<Categorie>>() {
            @Override
            public void onChanged(ArrayList<Categorie> categories) {
                categoriesList = categories;
                initRecyclerView(categories);
                categoryAdapter.notifyDataSetChanged();
            }
        });
        addViewModel.getToastService().observe(this, new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
               // Toast.makeText(getActivity(), errorResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util = new Util(getContext());
        myToast = new MyToast(getContext());
        addViewModel = new ViewModelProvider(requireActivity()).get(AddFragmentViewModel.class);

        //myToast.showInfo("Welcome to add fragment, you are a hero");
        fetchData();
    }

    @Override
    public void onCategoryClick(int position) {

        listener.onInputSelectCategorySent(categoriesList.get(position));
        iDashboard.inflateFragment(getString(R.string.fragment_post_container));

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
        iDashboard.setCurrentFragTag(getString(R.string.fragment_add));

        if (context instanceof AddFragment.FragmentSelectCategoryListener) {
            listener = (AddFragment.FragmentSelectCategoryListener) context;
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