package com.example.servicebypro.Activities.Dashboard.Add;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.servicebypro.Activities.Dashboard.Assignments.AssignmentsFragment;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.SessionManager;
import static com.example.servicebypro.Activities.HelperClasses.Constants.MAX_STEP;


public class Post0JobFragment extends Fragment implements View.OnClickListener{

    private MyToast myToast;
    private Util util;
    private View view;
    private SessionManager sessionManager;
    private IDashboard iDashboard;

    // private TextView status;
    TextView add_post_title_text;
    Work work;
    Button add_post_next_btn;

    private ProgressBar progressBar;

    private IOnBackPressedListener listener;

    public Post0JobFragment() {
        // Required empty public constructor
    }
    public interface IOnBackPressedListener {
        public void doBack();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util = new Util(getContext());
        myToast = new MyToast(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_post_job, container, false);
        sessionManager = new SessionManager(getContext());

        //hooks
        add_post_title_text = view.findViewById(R.id.add_post_title_text);
        add_post_next_btn =  view.findViewById(R.id.add_post_next_btn);


        iDashboard.setTextView(add_post_title_text);
        iDashboard.setButtonNext(add_post_next_btn);
        //Setting up the progress bar
        setProgressBar();

        //Setting listeners
        view.findViewById(R.id.back).setOnClickListener(this);

        init();

         return view;
    }

    private void init() {
        doFragmentTransaction(new Post1SelectServiceFragment(), getString(R.string.fragment_service), false);
    }

    private void doFragmentTransaction(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();


        transaction.replace(R.id.post_job_fragment_container, fragment, tag);

        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
       // iDashboard.setCurrentFragTag(getString(R.string.fragment_post_container));
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.back:

              if(iDashboard.getCurrent_step() == 1){
                  iDashboard.backStep(iDashboard.getCurrent_step());
                }else{
                  iDashboard.backStep(iDashboard.getCurrent_step());
                  iDashboard.inflateFragment(iDashboard.getCurrentFragmentTag(iDashboard.getCurrent_step()));
              }

                break;
        }
    }


    public void setProgressBar() {
        //hooks
        progressBar = view.findViewById(R.id.progress);
        progressBar.setMax(MAX_STEP);
        progressBar.setProgress(iDashboard.getCurrent_step());
        iDashboard.setProgressBar(progressBar);
    }

}