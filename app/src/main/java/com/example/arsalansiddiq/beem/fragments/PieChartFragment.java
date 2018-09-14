package com.example.arsalansiddiq.beem.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.adapters.CustomListAdapterTargets;
import com.example.arsalansiddiq.beem.interfaces.TargetsAndAchievementResponseInterface;
import com.example.arsalansiddiq.beem.models.responsemodels.targetsandachievementsmodel.TargetsandAchievementsModel;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
// * {@link PieChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PieChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PARAM_STORE_ID = "storeId";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PieChart pieChart;

    private int achieved = 0, target = 0;

    private int storeId;
//    private OnFragmentInteractionListener mListener;

    public PieChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PieChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PieChartFragment newInstance(int storeId) {
        PieChartFragment fragment = new PieChartFragment();
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

        return inflater.inflate(R.layout.fragment_pie_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        getListFromServer();

        pieChart = view.findViewById(R.id.pieChart);


//        super.onViewCreated(view, savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }



    private void getListFromServer() {

        NetworkUtils networkUtils = new NetworkUtils(getActivity());

        if (networkUtils.isNetworkConnected()) {
            networkUtils.getTargetsAndAchievements(storeId, new TargetsAndAchievementResponseInterface() {
                @Override
                public void success(Response<TargetsandAchievementsModel> targetsandAchievementsModelResponse) {
                    if (targetsandAchievementsModelResponse.body().getStatus() == 1) {

                        for (int i = 0; i < targetsandAchievementsModelResponse.body().getTarget().size(); i++) {
                            achieved += targetsandAchievementsModelResponse.body().getTarget().get(i).getAch();
                            target += Integer.parseInt(targetsandAchievementsModelResponse.body().getTarget().get(i).getSkutargets());
                        }


                        int total = 0, percentage = 100;

                        try {
                            if (target != 0 && achieved != 0) {
                                total = ((achieved * percentage) / target);
                                percentage = percentage - total;
                            }
                        } catch (NumberFormatException e) {

                        }

                        Log.i("stat", total + "  " + percentage + "   " + target + "   " + achieved);

                        String[] arr = {"achieveds", "remainings"};

                        List<PieEntry> arrayList = new ArrayList<>();

                        arrayList.add(new PieEntry(total));
                        arrayList.add(new PieEntry(percentage));

                        PieDataSet pieDataSet = new PieDataSet(arrayList, "OverseasProgress");

                        pieDataSet.setColors(new int[]{Color.parseColor("#FF32DA64"),
                                Color.parseColor("#FFA500")});

                        PieData pieData= new PieData(pieDataSet);
                        pieData.setValueFormatter(new PercentFormatter());
                        pieData.setValueTextSize(18f);

                        List<LegendEntry> legendEntryList = new ArrayList<>();
                        legendEntryList.add(new LegendEntry("% Target Remaining", Legend.LegendForm.SQUARE, 15f,
                                5f, null, Color.parseColor("#FFA500")));
                        legendEntryList.add(new LegendEntry("% Achieved Progress Chart", Legend.LegendForm.SQUARE, 15f,
                                5f, null, Color.parseColor("#FF32DA64")));


                        Legend legend = pieChart.getLegend();
//                        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
                        legend.setCustom(legendEntryList);
                        legend.setEnabled(true);
//                        pieChart.setDrawHoleEnabled(true);
                        pieChart.getDescription().setEnabled(false);
//                        pieChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        pieChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

                        pieChart.setData(pieData);

//                        pieChart.getLegend().getCalculatedLineSizes();



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
                            getListFromServer();
                        }
                    });

            alertBuilder.show();
        }

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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
