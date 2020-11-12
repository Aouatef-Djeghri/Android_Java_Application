package com.example.servicebypro.Activities.Dashboard.Search;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.servicebypro.Activities.Adapters.BrowseWorkAdapter;
import com.example.servicebypro.Activities.Adapters.CategoryAdapter;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Categorie;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.AddFragmentViewModel;
import com.example.servicebypro.ViewModels.SearchFragmentViewModel;

public class SearchFragment extends Fragment implements
        CategoryAdapter.OnCategoryListener,
        BrowseWorkAdapter.OnWorkListener,
        android.view.View.OnClickListener {

    SearchView searchView;
    ListView listView;
    ArrayList<String> list;
    BrowseWorkAdapter recommendedWorkAdapter;
    View view;
    TextView view_all_recommended, view_all_by_category;
    private CategoryAdapter categoryAdapter;
    private RecyclerView categoryRecyclerView;
    private RecyclerView recommended_jobs_recycler;
    private static final int NUM_COLUMNS = 2;
    private Util util;
    private MyToast myToast;
    private SearchFragmentViewModel searchFragmentViewModel;
    private AddFragmentViewModel addViewModel;
    ArrayList<Categorie> categoriesList = new ArrayList<>();
    ArrayList<Work> recommendedWorkList = new ArrayList<>();
    ArrayList<Work> availableWorkList = new ArrayList<>();
    ArrayList<Work> availableWorkListByCategory = new ArrayList<>();
    ArrayList<Work> availableWorkListByService = new ArrayList<>();
    ArrayList<Service> allServicesList = new ArrayList<>();
    ArrayAdapter<Service> adapter;
    AutoCompleteTextView a1;
    IDashboard iDashboard;
    SessionManager sessionManager;
    RelativeLayout no_work_layout;

    public SearchFragment() {
        // Required empty public constructor
    }

    private SearchFragment.FragmentSelectSearchedWork listener;


    public interface FragmentSelectSearchedWork {
        void onInputSelectSearchedWorkSent(ArrayList<Work> works);
    }

    private SearchFragment.FragmentSelectWorkForDetails listenerSelectWork;


    public interface FragmentSelectWorkForDetails {
        void onInputSelectWorkForDetailsSent(Work works);
    }

    private CheckSearchedWorkListFragment.FragmentHasUserAlreadyApplied hasAppliedListener;


    public interface FragmentHasUserAlreadyApplied {
        void onInputAlreadyAppliedSent(Boolean applied);
    }


    private void initCategoryRecyclerView(ArrayList<Categorie> categories) {

        categoryRecyclerView = view.findViewById(R.id.brows_by_category_recycler);
        categoryRecyclerView.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapter(categories, getContext(), this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void initReommendedWorkRecyclerView(ArrayList<Work> works) {
        ArrayList<Work> miniWorkList = new ArrayList<>();
        if (works.size() >= 2) {
            miniWorkList.add(works.get(0));
            miniWorkList.add(works.get(1));
        } else {
            miniWorkList = works;
        }
        recommended_jobs_recycler = (RecyclerView) view.findViewById(R.id.recommended_jobs_recycler);
        recommended_jobs_recycler.setHasFixedSize(true);
        recommended_jobs_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recommendedWorkAdapter = new BrowseWorkAdapter(miniWorkList, getContext(), this);
        recommended_jobs_recycler.setAdapter(recommendedWorkAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getContext());
        //Utilities class contains reusable methods

        util = new Util(getActivity());
        myToast = new MyToast(getContext());
        addViewModel = new ViewModelProvider(requireActivity()).get(AddFragmentViewModel.class);
        searchFragmentViewModel = new ViewModelProvider(requireActivity()).get(SearchFragmentViewModel.class);
        if (!util.isInternetConnected()) {
            //Show a dialog
            util.ShowCustomDialog();
        }else{
            fetchAllServices();
            fetchCategoryList();
            fetchRecommendedWorkList(sessionManager.getUser().getIdUser());
            fetchAllAvailableWorkList(sessionManager.getUser().getIdUser());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        iDashboard.setCurrentFragTag(getActivity().getResources().getString(R.string.fragment_search));

        //hooks
        view_all_recommended = view.findViewById(R.id.view_all_recommended);
        view_all_by_category = view.findViewById(R.id.view_all_by_category);
        recommended_jobs_recycler = view.findViewById(R.id.recommended_jobs_recycler);
        no_work_layout = view.findViewById(R.id.no_work_layout);


        //Setting listeners
        view.findViewById(R.id.view_all_recommended).setOnClickListener(this);
        view.findViewById(R.id.view_all_by_category).setOnClickListener(this);

        a1 = view.findViewById(R.id.filled_exposed_dropdown);
        a1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof Service) {
                    Service service = (Service) item;
                    //TODO will be redirected to available work with the selected service
                    fetchAllAvailableWorkListByService(service.getIdService(), sessionManager.getUser().getIdUser());

                    //to clear search field
                    a1.getText().clear();
                }
            }
        });
        return view;
    }

    private void fetchRecommendedWorkList(int idUser) {

        searchFragmentViewModel.getRecommendedWorkList(idUser).observe(this, new Observer<ArrayList<Work>>() {
            @Override
            public void onChanged(ArrayList<Work> works) {
                recommendedWorkList = works;

                if (!recommendedWorkList.isEmpty()) {
                    recommended_jobs_recycler.setVisibility(View.VISIBLE);
                    no_work_layout.setVisibility(View.GONE);
                    initReommendedWorkRecyclerView(works);
                    recommendedWorkAdapter.notifyDataSetChanged();
                    //TODO for now this has no use case ,but later find a solution in the case where a category has no services
                } else {
                    recommended_jobs_recycler.setVisibility(View.GONE);
                    no_work_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        searchFragmentViewModel.getToast().observe(this, new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }

    private void fetchAllAvailableWorkList(int idUser) {

        searchFragmentViewModel.getAllAvailableWorkList(idUser).observe(this, new Observer<ArrayList<Work>>() {
            @Override
            public void onChanged(ArrayList<Work> works) {
                availableWorkList = works;
                //listener.onInputSelectSearchedWorkSent(availableWorkList);
            }
        });
        searchFragmentViewModel.getToast().observe(this, new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }

    private void fetchAllAvailableWorkListByCategory(int idCategory, int idUser) {

        searchFragmentViewModel.getAllAvailableWorkListByCategory(idCategory, idUser).observe(getViewLifecycleOwner(), new Observer<ArrayList<Work>>() {
            @Override
            public void onChanged(ArrayList<Work> works) {
                availableWorkListByCategory = works;
                initWorkList(availableWorkListByCategory);
            }
        });
        searchFragmentViewModel.getToast().observe(getViewLifecycleOwner(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                //Toast.makeText(getActivity(), errorResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }

    public void initWorkList(ArrayList<Work> works) {
        listener.onInputSelectSearchedWorkSent(works);
        DialogFragment dialogFragment = new CheckSearchedWorkListFragment();
        dialogFragment.show(getActivity().getFragmentManager(), "dialog");
    }

    public void initWorkDetails(Work works) {
        listenerSelectWork.onInputSelectWorkForDetailsSent(works);
        DialogFragment dialogFragment = new CheckSearchedWorkDetailsFragment();
        dialogFragment.show(getActivity().getFragmentManager(), "dialog");
    }

    public boolean isUserAlreadyApplied(Work work) {

        for (int i = 0; i < work.getApplications().size(); i++) {
            if (work.getApplications().get(i).getUser().getIdUser().equals(sessionManager.getUser().getIdUser()) ) {
                return true;
            }
        }
        return false;
    }

    private void fetchCategoryList() {

        addViewModel.getCategoryList().observe(this, new Observer<ArrayList<Categorie>>() {
            @Override
            public void onChanged(ArrayList<Categorie> categories) {
                categoriesList = categories;
                initCategoryRecyclerView(categories);
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


    private void fetchAllServices() {

        searchFragmentViewModel.getAllServicesList().observe(this, new Observer<ArrayList<Service>>() {
            @Override
            public void onChanged(ArrayList<Service> services) {

                allServicesList = services;
                adapter = new ArrayAdapter<Service>(getContext(),
                        R.layout.dropdown_menu_popup_item, R.id.text, allServicesList);
                a1.setAdapter(adapter);
            }
        });
        searchFragmentViewModel.getToastService().observe(this, new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                //Toast.makeText(getActivity(), errorResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }

    private void fetchAllAvailableWorkListByService(int idService, int idUser) {

        searchFragmentViewModel.getAllAvailableWorkListByService(idService, idUser).observe(getViewLifecycleOwner(), new Observer<ArrayList<Work>>() {
            @Override
            public void onChanged(ArrayList<Work> works) {
                availableWorkListByService = works;
                initWorkList(availableWorkListByService);
            }
        });
        searchFragmentViewModel.getToast().observe(getViewLifecycleOwner(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }


    @Override
    public void onCategoryClick(int position) {
        fetchAllAvailableWorkListByCategory(categoriesList.get(position).getIdCategorie(), sessionManager.getUser().getIdUser());
    }

    @Override
    public void onWorkClick(int position) {
        hasAppliedListener.onInputAlreadyAppliedSent(isUserAlreadyApplied(recommendedWorkList.get(position)));
        initWorkDetails(recommendedWorkList.get(position));
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
        if (context instanceof SearchFragment.FragmentSelectSearchedWork) {
            listener = (SearchFragment.FragmentSelectSearchedWork) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentAListener");
        }

        if (context instanceof SearchFragment.FragmentSelectWorkForDetails) {
            listenerSelectWork = (SearchFragment.FragmentSelectWorkForDetails) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentAListener");
        }

        if (context instanceof CheckSearchedWorkListFragment.FragmentHasUserAlreadyApplied) {
            hasAppliedListener = (CheckSearchedWorkListFragment.FragmentHasUserAlreadyApplied) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        listenerSelectWork = null;
        hasAppliedListener = null;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.view_all_recommended:
                initWorkList(recommendedWorkList);
                break;
            case R.id.view_all_by_category:
                initWorkList(availableWorkList);
                break;

        }
    }
}