package com.example.arsalansiddiq.beem.fragments;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.activities.TargetsAndAchievementsActivity;
import com.example.arsalansiddiq.beem.adapters.CustomListAdapterTargets;
import com.example.arsalansiddiq.beem.adapters.RecyclerViewAdapter;
import com.example.arsalansiddiq.beem.interfaces.TargetsAndAchievementResponseInterface;
import com.example.arsalansiddiq.beem.models.responsemodels.targetsandachievementsmodel.Target;
import com.example.arsalansiddiq.beem.models.responsemodels.targetsandachievementsmodel.TargetsandAchievementsModel;
import com.example.arsalansiddiq.beem.rnd.GenericAdapter;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TargetAndAchievFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TargetAndAchievFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TargetAndAchievFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PARAM_STORE_ID = "storeId";
    private static final String ARG_PARAM2 = "param2";

//    private ListView listView_fragmetTarget;
    private RecyclerView recyclerView;
    private int storeId;

//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;


    private OnFragmentInteractionListener mListener;

    public TargetAndAchievFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TargetAndAchievFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public TargetAndAchievFragment newInstance(List<Target> targetsandAchievementsModels) {
    public static TargetAndAchievFragment newInstance(int storeId) {
        TargetAndAchievFragment fragment = new TargetAndAchievFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_STORE_ID, storeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            storeId = getArguments().getInt(PARAM_STORE_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_target_and_achiev, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


//        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(getContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
//        mAdapter = new Rec
//        mRecyclerView.setAdapter(mAdapter);



//        listView_fragmetTarget = (ListView)view.findViewById(R.id.listView_fragmetTarget);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        listView_fragmetTarget.setNestedScrollingEnabled(true);
        getListFromServer();
//        super.onViewCreated(view, savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void getListFromServer() {

        NetworkUtils networkUtils = new NetworkUtils(getActivity());

        if (networkUtils.isNetworkConnected()) {
            networkUtils.getTargetsAndAchievements(storeId, new TargetsAndAchievementResponseInterface() {
                @Override
                public void success(Response<TargetsandAchievementsModel> targetsandAchievementsModelResponse) {
                    if (targetsandAchievementsModelResponse.body().getStatus() == 1) {

//                        CustomListAdapterTargets customListAdapterTargets = new CustomListAdapterTargets(getContext(), 0,
//                                targetsandAchievementsModelResponse.body().getTarget());
//                        listView_fragmetTarget.setAdapter(customListAdapterTargets);
//                        listView_fragmetTarget.setVisibility(View.GONE);



                        // Create adapter passing in the sample user data
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(targetsandAchievementsModelResponse.body().getTarget());

                        RecyclerView.Adapter recyclerAdapter = new GenericAdapter(getContext(), targetsandAchievementsModelResponse.body().getTarget()) {
                            @Override
                            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                                return null;
                            }

                            @Override
                            public void onBindData(RecyclerView.ViewHolder holder, Object val) {

                            }
                        };
                        // Attach the adapter to the recyclerview to populate items
                        recyclerView.setAdapter(adapter);
                        // Set layout manager to position the items
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


                    } else {
                        Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failed(String error) {
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            alertBuilder.setTitle("Network")
                    .setMessage("Please Check your internet connection")
                    .setCancelable(false)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            getListFromServer();
                        }
                    });

            alertBuilder.show();
        }

    }
}
