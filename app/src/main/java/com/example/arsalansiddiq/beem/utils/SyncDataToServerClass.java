package com.example.arsalansiddiq.beem.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.interfaces.AttandanceInterface;
import com.example.arsalansiddiq.beem.interfaces.LoginInterface;
import com.example.arsalansiddiq.beem.interfaces.SampleInterface;
import com.example.arsalansiddiq.beem.models.HolderListModel;
import com.example.arsalansiddiq.beem.models.databasemodels.MarkAttendance;
import com.example.arsalansiddiq.beem.models.databasemodels.SaleApiResponseTableRealm;
import com.example.arsalansiddiq.beem.models.responsemodels.AttandanceResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;

import java.io.File;
import java.util.List;

import io.realm.Realm;
import retrofit2.Response;

/**
 * Created by jellani on 9/2/2018.
 */

public class SyncDataToServerClass {

    private final String LOG_TAG = SyncDataToServerClass.class.getName();


    ProgressBar progressBar;
    private Context context;
    private NetworkUtils networkUtils;
//    private Realm realm;
    private RealmCRUD realmCRUD;
    private AppUtils appUtils;


    public SyncDataToServerClass(Context context) {
        this.context = context;
        networkUtils = new NetworkUtils(context);
//        realm = Realm.getDefaultInstance();
        realmCRUD = new RealmCRUD(context);
        appUtils = new AppUtils(context);

        initProgressBar();
    }

    public void initProgressBar() {

        progressBar = new ProgressBar(context);

        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

//        .addContentView(relativeLayout, params);
    }

//    public void updateOrderStatus(List<HolderListModel> holderListModelList) {
    public void updateOrderStatus(int customSaleId, int sales_id) {
        if (networkUtils.isNetworkConnected()) {

            List<HolderListModel> holderListModelList = realmCRUD.getPendingOrder(customSaleId);

            if (holderListModelList == null) {

            } else {
                for (int i = 0; i < holderListModelList.size(); i++) {

                    final HolderListModel holderListModel = holderListModelList.get(i);

//                if (isConnected) {
                    networkUtils.sendOrderDetail(holderListModel.getStoreId(), sales_id, holderListModel.getoDate(),
                            holderListModel.getBrand(), holderListModel.getSkuCategory(), holderListModel.getSKU(),
                            holderListModel.getSaleType(), holderListModel.getNoItem(), holderListModel.getPrice(),
                            holderListModel.getsAmount(), new SampleInterface() {

                                @Override
                                public void success(LoginResponse loginResponse) {
                                    if (loginResponse.getStatus() == 1) {
                                        realmCRUD.updateOrderStatus(holderListModel.getId(), Integer.parseInt(loginResponse.getOrder_id()), loginResponse.getStatus(), 1);
                                    }
                                }

                                @Override
                                public void failed(String error) {
                                    alerts();
                                }
                            });
                }

            }
        }else{
            alerts();
        }

    }

    public void updateMarkAttendanceOnServer(){
        if (networkUtils.isNetworkConnected()) {

            List<MarkAttendance> markAttendanceList = realmCRUD.getUnsavedMarkAttendanceData();

            if (markAttendanceList.size() > 0) {
                for (int i = 0; i < markAttendanceList.size(); i++) {

                    final MarkAttendance markAttendance = markAttendanceList.get(i);
                    File userImageFile = appUtils.imageByteToFileConversion(markAttendance.getStartImage());

//                if (isConnected) {
                    networkUtils.attandanceBA(markAttendance.getDate(), markAttendance.getId(), markAttendance.getName(), userImageFile,
                            markAttendance.getStartTime(), markAttendance.getLatitude(), markAttendance.getLongitude(), 1, new AttandanceInterface() {
                                @Override
                                public void success(Response<AttandanceResponse> attandanceResponseResponse) {
                                    if (attandanceResponseResponse.body().getStatus() == 1) {
//                                        markAttendanceList.get(i) =
                                        Log.i(LOG_TAG, String.valueOf(attandanceResponseResponse.body().getStatus()));
                                    } else {

                                    }
                                }

                                @Override
                                public void failed(String error) {

                                }
                            });
                }
            }

        } else {
            alerts();
        }
    }

    void alerts() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle("Network")
                .setMessage("Please Check your internet connection")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                            insertorAddORder(sales_id, isConnected);
                    }
                });

        alertBuilder.show();
    }

    public void updateEndMarkAttendanceOnServer(){
        if (networkUtils.isNetworkConnected()) {

            List<MarkAttendance> markAttendanceList = realmCRUD.getUnsavedMarkAttendanceData();

            if (markAttendanceList.size() > 0) {
                for (int i = 0; i < markAttendanceList.size(); i++) {

                    final MarkAttendance markAttendance = markAttendanceList.get(i);
                    File userImageFile = appUtils.imageByteToFileConversion(markAttendance.getStartImage());

//                if (isConnected) {
                    networkUtils.attandanceBA(markAttendance.getDate(), markAttendance.getId(), markAttendance.getName(), userImageFile,
                            markAttendance.getStartTime(), markAttendance.getLatitude(), markAttendance.getLongitude(), 1, new AttandanceInterface() {
                                @Override
                                public void success(Response<AttandanceResponse> attandanceResponseResponse) {
                                    if (attandanceResponseResponse.body().getStatus() == 1) {
                                        Log.i(LOG_TAG, String.valueOf(attandanceResponseResponse.body().getStatus()));
                                    } else {

                                    }
                                }

                                @Override
                                public void failed(String error) {

                                }
                            });
                }
            }

        } else {
            alerts();
        }
    }

    public void updateSalesData () {

        List<SaleApiResponseTableRealm> saleApiResponseTableRealmList = realmCRUD.getUnsavedSalesDetailsonServer();

        if (saleApiResponseTableRealmList == null) {

        } else {
            if (networkUtils.isNetworkConnected()) {
                for (int i = 0; i < saleApiResponseTableRealmList.size(); i++) {

                    SaleApiResponseTableRealm saleApiResponseTableRealm = saleApiResponseTableRealmList.get(i);

                    networkUtils.sendSaleDetail(saleApiResponseTableRealm.getCusName(), saleApiResponseTableRealm.getContact(), saleApiResponseTableRealm.getEmail(),
                            saleApiResponseTableRealm.getGender(), saleApiResponseTableRealm.getAge(), saleApiResponseTableRealm.getcBrand(),
                            saleApiResponseTableRealm.getpBrand(), saleApiResponseTableRealm.getSaleStatus(), saleApiResponseTableRealm.getEmpId(),
                            saleApiResponseTableRealm.getEmpName(), saleApiResponseTableRealm.getDesignation(), saleApiResponseTableRealm.getCity(),
                            saleApiResponseTableRealm.getLocation(), new LoginInterface() {
                                @Override
                                public void success(Response<LoginResponse> loginResponseBody) {
                                    Log.i(LOG_TAG, "getSalesId Status" + loginResponseBody.body().getStatus());
                                    if (loginResponseBody.body().getStatus() == 1) {


                                        realmCRUD.updateUnsavedSalesDetailsStatusAndId(saleApiResponseTableRealm.getId(),
                                                loginResponseBody.body().getStatus(), loginResponseBody.body().getSales_id(), 1);

                                        updateOrderStatus(saleApiResponseTableRealm.getId(), saleApiResponseTableRealm.getSales_id());

                                    }
                                }

                                @Override
                                public void failed(String error) {

                                    Log.e(LOG_TAG, "FailedtogetSalesDetailsUpdate " + error);

                                }
                            });
                }

                Toast.makeText(context, saleApiResponseTableRealmList.size() + " Pending records sync successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "please check you internet connection", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
