package com.example.arsalansiddiq.beem.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.activities.NavigationDrawerActivity;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.interfaces.LoginInterface;
import com.example.arsalansiddiq.beem.interfaces.MeetingCallBack;
import com.example.arsalansiddiq.beem.interfaces.SampleInterface;
import com.example.arsalansiddiq.beem.models.HolderListModel;
import com.example.arsalansiddiq.beem.models.databasemodels.SaleApiResponseTableRealm;
import com.example.arsalansiddiq.beem.models.databasemodels.meetingsup.MeetingSUPEndTable;
import com.example.arsalansiddiq.beem.models.databasemodels.meetingsup.MeetingSUPStartTable;
import com.example.arsalansiddiq.beem.models.databasemodels.meetingsup.MeetingSUPUpdatesTable;
import com.example.arsalansiddiq.beem.models.requestmodels.StartMeetingRequest;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.MeetingResponseModel;

import java.io.File;
import java.util.List;

import retrofit2.Response;

/**
 * Created by jellani on 9/2/2018.
 */

public class SyncDataToServerClass {

    private final String LOG_TAG = SyncDataToServerClass.class.getName();


    private NavigationDrawerActivity navigationDrawerActivity;
    ProgressBar progressBar;
    private Context context;
    private Activity activity;
    private NetworkUtils networkUtils;
    private Toast toast;
    //    private Realm realm;
    private RealmCRUD realmCRUD;
    private AppUtils appUtils;
//    private List<MeetingSUPUpdatesTable> meetingSUPUpdatesTableListRealmUpdate = new ArrayList<>();
    MeetingSUPUpdatesTable meetingSUPUpdatesTable;
    int id;

    int syncSalesPending = 0, syncSalesSuccess = 0, syncOrderPending = 0, syncOrderSuccess = 0,
            salesRecordsExecuted = 0, orderRecordsExecuted = 0, salesRecordsTotal = 0, orderRecordsTotal = 0;
    int count;

    public SyncDataToServerClass(Context context, Activity activity) {
        navigationDrawerActivity = new NavigationDrawerActivity();
        this.context = context;
        this.activity = activity;
        networkUtils = new NetworkUtils(context);
//        realm = Realm.getDefaultInstance();
        realmCRUD = new RealmCRUD(context);
        appUtils = new AppUtils(context);
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

        count = realmCRUD.getPendingOrdersForRequestNumber();
//        initProgressBar();
    }

//    public void initProgressBar() {
//
////        progressBar = new ProgressBar(context);
////
////        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
////        progressBar.setIndeterminate(true);
////
////        RelativeLayout relativeLayout = new RelativeLayout(context);
////        relativeLayout.setGravity(Gravity.CENTER);
////        relativeLayout.addView(progressBar);
////
////        RelativeLayout.LayoutParams params = new
////                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
////        progressBar.setVisibility(View.INVISIBLE);
//
////        .addContentView(relativeLayout, params);
//    }

    //    public void updateOrderStatus(List<HolderListModel> holderListModelList) {
    public void updateOrderStatus(int customSaleId, int sales_id) {
        if (networkUtils.isNetworkConnected()) {

            List<HolderListModel> holderListModelList = realmCRUD.getPendingOrder(customSaleId);

            if (holderListModelList == null) {

            } else {

                if (orderRecordsTotal == 0) {
                    orderRecordsTotal = holderListModelList.size();
                } else {
                    orderRecordsTotal += holderListModelList.size();
                }

                for (int i = 0; i < holderListModelList.size(); i++) {

                    final HolderListModel holderListModel = holderListModelList.get(i);

//                if (isConnected) {
                    networkUtils.sendOrderDetail(holderListModel.getStoreId(), sales_id, holderListModel.getoDate(),
                            holderListModel.getBrand(), holderListModel.getSkuCategory(), holderListModel.getSKU(),
                            holderListModel.getSaleType(), holderListModel.getNoItem(), holderListModel.getPrice(),
                            holderListModel.getsAmount(), new SampleInterface() {

                                @Override
                                public void success(LoginResponse loginResponse) {

                                    orderRecordsExecuted++;

                                    if (loginResponse.getStatus() == 1) {
                                        realmCRUD.updateOrderStatus(holderListModel.getId(), Integer.parseInt(loginResponse.getOrder_id()), loginResponse.getStatus(), 1);
                                        syncOrderSuccess++;

                                        showToastWithCount(1);

                                    } else {
                                        syncOrderPending++;
                                    }

                                    if (count == orderRecordsExecuted) {
                                        alerts(false);
                                    }

                                }

                                @Override
                                public void failed(String error) {
                                    orderRecordsExecuted++;
                                    syncOrderPending++;

                                    showToastWithCount(0);
                                    if (count == orderRecordsExecuted) {
                                        alerts(false);
                                    }
                                }
                            });
                }

            }
        } else {
            alerts(true);
        }

    }

    void alerts(boolean isNetwork) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        String alertTitle = null;
        String alertMessage = null;
        String syncRecordsFailed = null;
        String syncFailedReason = null;
        String syncResolve = null;

        if (isNetwork) {
            alertTitle = " Network";
            alertMessage = "Please Check your internet connection";
        } else {

//            ((NavigationDrawerActivity) context).hideProgress();
            alertTitle = "Sync Status!";

            String syncRecordsSuccess = "Dear user " + syncSalesSuccess + " sales and " + syncOrderSuccess + " orders synced successfully";

            if (syncSalesPending > 0 || syncOrderPending > 0) {
                syncRecordsFailed = "But, " + syncSalesPending + " sales  and " + syncOrderPending + " orders have failed sync";
                syncFailedReason = "Note: The reason of this failure is because your server " +
                        "is unable to handle that much traffic!";
                syncResolve = "Steps to Resolve this issue: " + "Kindly increase your server traffic capacity and number of request handling" +
                        "+ response time";
                syncRecordsSuccess = syncRecordsSuccess + "\n" + "\n" + syncRecordsFailed +"\n"+ "\n" +syncFailedReason+"\n" + "\n" +syncResolve;

            }

            toast.cancel();

            alertMessage = syncRecordsSuccess;
        }
        alertBuilder.setTitle(alertTitle)
                .setMessage(alertMessage)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                            insertorAddORder(sales_id, isConnected);
                    }
                });

        alertBuilder.show();

        enableNav_Sync();
    }

    public void updateSalesData() {

        List<SaleApiResponseTableRealm> saleApiResponseTableRealmList = realmCRUD.getUnsavedSalesDetailsonServer();

        showToastWithCount(0);
        if (saleApiResponseTableRealmList == null) {

        } else {
            salesRecordsTotal = saleApiResponseTableRealmList.size();
            if (networkUtils.isNetworkConnected()) {
//                ((NavigationDrawerActivity) context).showProgress();
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

                                        salesRecordsExecuted++;

                                        showToastWithCount(1);

                                        realmCRUD.updateUnsavedSalesDetailsStatusAndId(saleApiResponseTableRealm.getId(),
                                                loginResponseBody.body().getStatus(), loginResponseBody.body().getSales_id(), 1);

                                        updateOrderStatus(saleApiResponseTableRealm.getId(), saleApiResponseTableRealm.getSales_id());

                                        syncSalesSuccess++;
                                    } else {
                                        syncSalesPending++;
                                    }
//                                    if (salesRecordsExecuted == salesRecordsTotal) {
//                                        alerts(false);
//                                    }
                                }

                                @Override
                                public void failed(String error) {

//                                    if (salesRecordsExecuted == salesRecordsTotal) {
//                                        alerts(false);
//                                    }
                                        syncSalesPending++;

                                    showToastWithCount(0);

                                    Log.e(LOG_TAG, "FailedtogetSalesDetailsUpdate " + error);

                                }
                            });
                }

//                Toast.makeText(context, saleApiResponseTableRealmList.size() + " Pending records sync successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "please check you internet connection", Toast.LENGTH_SHORT).show();
                enableNav_Sync();
            }
        }

    }

    public void startMeetingUpdate() {

        List<MeetingSUPStartTable> meetingSUPStartTableList = realmCRUD.getUnSynycedStartMeetingDataOfSUP();

        if (meetingSUPStartTableList == null) {

        } else {
            for (int i = 0; i < meetingSUPStartTableList.size(); i++) {

                MeetingSUPStartTable meetingSUPStartTable = meetingSUPStartTableList.get(i);

                File file = appUtils.imageByteToFileConversion(meetingSUPStartTable.getImg1());

                StartMeetingRequest startMeetingRequest = new StartMeetingRequest(meetingSUPStartTable.getTask_id(),
                        meetingSUPStartTable.getStartTime(),  file, meetingSUPStartTable.getLat(),
                        meetingSUPStartTable.getLng(), meetingSUPStartTable.getEmp_id());

                if (networkUtils.isNetworkConnected()) {

                    networkUtils.startMeeting(startMeetingRequest, new MeetingCallBack() {
                        @Override
                        public void success(Response<MeetingResponseModel> responseModelResponse) {

                            if (responseModelResponse.isSuccessful()) {
                                if (responseModelResponse.isSuccessful()) {
                                    realmCRUD.updateStartMeetingTable(meetingSUPStartTable.getId(), Integer.parseInt(responseModelResponse.body().getTaskId()),
                                            1);

                                    id = Integer.parseInt(responseModelResponse.body().getTaskId());

                                    updateMeetingUpdate(meetingSUPStartTable.getId(), Integer.valueOf(responseModelResponse.body().getTaskId()));
                                    endMeetingUpdate(meetingSUPStartTable.getId(), Integer.parseInt(responseModelResponse.body().getTaskId()));
                                }

                            } else {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void error(String error) {
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context, "please check you internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void updateMeetingUpdate(int rowId, Integer startMeetingId) {

        List<MeetingSUPUpdatesTable> meetingSUPUpdatesTableList = realmCRUD.getUnSynycedUpdateMeetingDataOfSUP(rowId);
        StartMeetingRequest startMeetingRequest = null;

        if (meetingSUPUpdatesTableList != null) {
            for (int i = 0; i < meetingSUPUpdatesTableList.size(); i++) {
                meetingSUPUpdatesTable = meetingSUPUpdatesTableList.get(i);
                if (meetingSUPUpdatesTableList.get(i).getIsImage() == 1) {
                    startMeetingRequest = new StartMeetingRequest(startMeetingId,
                            appUtils.imageByteToFileConversion(meetingSUPUpdatesTableList.get(i).getImg2()), null);
                } else if (meetingSUPUpdatesTableList.get(i).getIsNote() == 1) {
                    startMeetingRequest = new StartMeetingRequest(startMeetingId,
                            meetingSUPUpdatesTableList.get(i).getNotes());
                }

                if (networkUtils.isNetworkConnected()) {

                    networkUtils.updateMeeting(startMeetingRequest, new MeetingCallBack() {
                        @Override
                        public void success(Response<MeetingResponseModel> responseModelResponse) {

                            if (responseModelResponse.isSuccessful()) {
                                if (responseModelResponse.isSuccessful()) {
                                    realmCRUD.saveUpdateTablesUpdates(meetingSUPUpdatesTable.getPrimaryKey(), startMeetingId, 1);
                                }

                            } else {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void error(String error) {
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context, "please check you internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void endMeetingUpdate(int rowId, int startMeetingId) {
        MeetingSUPEndTable meetingSUPEndTable = realmCRUD.getUnSynycedEndMeetingDataOfSUP(rowId);

        if (meetingSUPEndTable == null) {

        } else {
            StartMeetingRequest startMeetingRequest = new StartMeetingRequest(startMeetingId,
                    meetingSUPEndTable.getEndTime(),
                    appUtils.imageByteToFileConversion(meetingSUPEndTable.getImg4()));

            if (networkUtils.isNetworkConnected()) {

                networkUtils.endMeeting(startMeetingRequest, new MeetingCallBack() {
                    @Override
                    public void success(Response<MeetingResponseModel> responseModelResponse) {

                        if (responseModelResponse.isSuccessful()) {
                            if (responseModelResponse.isSuccessful()) {
                                realmCRUD.updateEndMeetingTable(meetingSUPEndTable.getCustomRelationMeetingId(), startMeetingId,
                                        responseModelResponse.body().getStatus());
                            }

                        } else {
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void error(String error) {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "please check you internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void showToastWithCount(int status) {
        toast.setText("Total Sales Executed: " + salesRecordsExecuted + ", Status: " +status+ "\n" +
                "Total Orders Executed with respect to Sales: " + orderRecordsExecuted + ", Status: " + status);
        toast.show();
    }

    void enableNav_Sync() {
        Log.i("nav_sync", "ClickedEnables");
        NavigationView navigationView = (NavigationView) this.activity.findViewById(R.id.nav_view);
        Menu menuNavigation = navigationView.getMenu();
        MenuItem menuItemNavigation = menuNavigation.findItem(R.id.nav_sync);
        menuItemNavigation.setEnabled(true);
    }
}
