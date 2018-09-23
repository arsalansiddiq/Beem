package com.example.arsalansiddiq.beem.databases;

import android.content.Context;
import android.util.Log;

import com.example.arsalansiddiq.beem.interfaces.RealmCallback;
import com.example.arsalansiddiq.beem.models.HolderListModel;
import com.example.arsalansiddiq.beem.models.databasemodels.EndMarkAttendanceTable;
import com.example.arsalansiddiq.beem.models.databasemodels.MarkAttendance;
import com.example.arsalansiddiq.beem.models.databasemodels.SaleApiResponseTableRealm;
import com.example.arsalansiddiq.beem.models.databasemodels.SalesAndNoSales;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.salesresponsemodels.SalesSKUArrayResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.tasksresponsemodels.Task;
import com.example.arsalansiddiq.beem.utils.ProgressDialogCustom;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jellani on 9/1/2018.
 */

public class RealmCRUD {

    private final String LOG_TAG = RealmCRUD.class.getName();
    private boolean check;
    private Context context;
    private int nextIdSales;

    private Realm realm;
    private ProgressDialogCustom progressDialogCustom;

    public RealmCRUD() {
        realm = Realm.getDefaultInstance();
    }

    public RealmCRUD(Context context) {
        this.context = context;
        realm = Realm.getDefaultInstance();
        progressDialogCustom = new ProgressDialogCustom(context);
    }

    public void addUserLoginInformation(final LoginResponse loginResponse, final int loginStatus) {
//        progressDialogCustom.showProgress();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Number currentIdNumber = realm.where(LoginResponse.class).max("id");
                int nextId;

                if (currentIdNumber == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNumber.intValue() + 1;
                }

                LoginResponse loginResponse1 = realm.createObject(LoginResponse.class, nextId);
                loginResponse1.setUserId(loginResponse.getUserId());
                loginResponse1.setName(loginResponse.getName());
                loginResponse1.setBrand(loginResponse.getBrand());
                loginResponse1.setuT(loginResponse.getuT());
                loginResponse1.setStoreId(loginResponse.getStoreId());
                loginResponse1.setStatus(loginResponse.getStatus());
                loginResponse1.setLoginStatus(loginStatus);
            }
        });
    }

    public void updateLoginStatus(final int userId, final int status) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LoginResponse loginResponse = realm.where(LoginResponse.class).equalTo("userId", userId).findFirst();

                loginResponse.setLoginStatus(status);

                realm.copyToRealmOrUpdate(loginResponse);
            }
        });
    }

    public LoginResponse getLoginInformationDetails() {

        LoginResponse loginResponse1;

        RealmResults<LoginResponse> loginResponse = realm.where(LoginResponse.class).findAll();

        if (loginResponse.size() > 0) {
            loginResponse1 = loginResponse.last();
        } else {
            loginResponse1 = null;
        }

        return loginResponse1;
    }

    public boolean checkLoginIdExist(int id) {

        LoginResponse loginResponse = realm.where(LoginResponse.class).equalTo("userId", id).findFirst();

        if (loginResponse != null) {
            return true;
        } else {
            return false;
        }
    }

     public void addMarkAttendanceDetails(final MarkAttendance markAttendance, final int markAttendanceResponseID) {
        progressDialogCustom.showProgress();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Number currentIdNumber = realm.where(MarkAttendance.class).max("id");
                int nextId;

                if (currentIdNumber == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNumber.intValue() + 1;
                }
                MarkAttendance markAttendanceSet = realm.createObject(MarkAttendance.class, nextId);
                markAttendanceSet.setEmpid(markAttendance.getEmpid());
                markAttendanceSet.setDate(markAttendance.getDate());
                markAttendanceSet.setName(markAttendance.getName());
                markAttendanceSet.setStartImage(markAttendance.getStartImage());
                markAttendanceSet.setStartTime(markAttendance.getStartTime());
                markAttendanceSet.setLatitude(markAttendance.getLatitude());
                markAttendanceSet.setLongitude(markAttendance.getLongitude());
                markAttendanceSet.setStatus(markAttendance.getStatus());
                markAttendanceSet.setMarkAttendanceResponseID(markAttendanceResponseID);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                progressDialogCustom.hideProgress();
                Log.i(LOG_TAG, "addMarkAttendanceDetails   "  + "SuccessCalled" );
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                progressDialogCustom.hideProgress();
                Log.i(LOG_TAG, "addMarkAttendanceDetails   "  + "onErrorCalled" );
            }
        });
    }

    public List<MarkAttendance> getUnsavedMarkAttendanceData() {
        List<MarkAttendance> markAttendanceList = realm.where(MarkAttendance.class).equalTo("markAttendanceResponseID", 0).findAll();

        if (markAttendanceList != null) {
            return markAttendanceList;
        } else {
            return null;
        }
    }

    public void updateUnsavedMarkAttendanceId(final List<MarkAttendance> markAttendance) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                List<MarkAttendance> markAttendanceList = realm.where(MarkAttendance.class).equalTo("markAttendanceResponseID", 0).findAll();

                markAttendanceList = markAttendance;
            }
            });

    }


    public void addEndMarkAttendanceDetails(final EndMarkAttendanceTable endMarkAttendanceTable, final int status, final int markEndAttendanceResponseID) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Number currentIdNumber = realm.where(EndMarkAttendanceTable.class).max("id");
                int nextId;

                if (currentIdNumber == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNumber.intValue() + 1;
                }
                EndMarkAttendanceTable endMarkAttendanceTable1 = realm.createObject(EndMarkAttendanceTable.class, nextId);
                endMarkAttendanceTable1.setEndMarkAttendanceResponseId(endMarkAttendanceTable.getEndMarkAttendanceResponseId());
                endMarkAttendanceTable1.setGetDate(endMarkAttendanceTable.getGetDate());
                endMarkAttendanceTable1.setEndImage(endMarkAttendanceTable.getEndImage());
                endMarkAttendanceTable1.setEndTime(endMarkAttendanceTable.getEndTime());
                endMarkAttendanceTable1.seteLatitude(endMarkAttendanceTable.geteLatitude());
                endMarkAttendanceTable1.seteLongitude(endMarkAttendanceTable.geteLongitude());
                endMarkAttendanceTable1.setEndMarkAttedanceResponseStatus(status);
                endMarkAttendanceTable1.setEndMarkAttendanceResponseId(markEndAttendanceResponseID);
            }
        });
    }


    public List<EndMarkAttendanceTable> getUnsavedEndMarkAttendanceData() {
        List<EndMarkAttendanceTable> endMarkAttendanceTableList = realm.where(EndMarkAttendanceTable.class).equalTo("status", 0).findAll();

        if (endMarkAttendanceTableList != null) {
            return endMarkAttendanceTableList;
        } else {
            return null;
        }
    }


    public int insertSalesDetails(final SaleApiResponseTableRealm saleApiResponseTableRealm, final int salesId, final int salesResponseStatus) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Number currentIdNumber = realm.where(SaleApiResponseTableRealm.class).max("id");
                int nextId;

                if (currentIdNumber == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNumber.intValue() + 1;
                }

                SaleApiResponseTableRealm saleApiResponseTableRealm1 = realm.createObject(SaleApiResponseTableRealm.class, nextId);
                saleApiResponseTableRealm1.setCusName(saleApiResponseTableRealm.getCusName());
                saleApiResponseTableRealm1.setContact(saleApiResponseTableRealm.getContact());
                saleApiResponseTableRealm1.setEmail(saleApiResponseTableRealm.getEmail());
                saleApiResponseTableRealm1.setGender(saleApiResponseTableRealm.getGender());
                saleApiResponseTableRealm1.setAge(saleApiResponseTableRealm.getAge());
                saleApiResponseTableRealm1.setcBrand(saleApiResponseTableRealm.getcBrand());
                saleApiResponseTableRealm1.setpBrand(saleApiResponseTableRealm.getpBrand());
                saleApiResponseTableRealm1.setSaleStatus(saleApiResponseTableRealm.getSaleStatus());
                saleApiResponseTableRealm1.setEmpId(saleApiResponseTableRealm.getEmpId());
                saleApiResponseTableRealm1.setEmpName(saleApiResponseTableRealm.getEmpName());
                saleApiResponseTableRealm1.setDesignation(saleApiResponseTableRealm.getDesignation());
                saleApiResponseTableRealm1.setCity(saleApiResponseTableRealm.getCity());
                saleApiResponseTableRealm1.setLocation(saleApiResponseTableRealm.getLocation());
                saleApiResponseTableRealm1.setSales_id(salesId);
                saleApiResponseTableRealm1.setSales_response_status(salesResponseStatus);
                saleApiResponseTableRealm1.setSyncStatus(saleApiResponseTableRealm.getSyncStatus());



                nextIdSales = saleApiResponseTableRealm1.getId();
            }
        });

        return nextIdSales;

    }

    public List<SaleApiResponseTableRealm> getUnsavedSalesDetailsonServer() {
        List<SaleApiResponseTableRealm> saleApiResponseTableRealmList = realm.where(SaleApiResponseTableRealm.class).
                equalTo("sales_id", 0).findAll();

        if (saleApiResponseTableRealmList.size() > 0) {
            return saleApiResponseTableRealmList;
        } else {
            return null;
        }
    }

    public void updateUnsavedSalesDetailsStatusAndId (final int id, final int salesResponseStatus, final int sales_id, final int syncStatus) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SaleApiResponseTableRealm saleApiResponseTableRealm1 = realm.where(SaleApiResponseTableRealm.class).equalTo("id", id).findFirst();
                saleApiResponseTableRealm1.setSales_response_status(salesResponseStatus);
                saleApiResponseTableRealm1.setSales_id(sales_id);
                saleApiResponseTableRealm1.setSyncStatus(syncStatus);

                realm.copyToRealmOrUpdate(saleApiResponseTableRealm1);
            }
            });

    }

    public int getLastSaleOfflineId() {

        RealmResults<SaleApiResponseTableRealm> saleApiResponseTableRealms = realm.where(SaleApiResponseTableRealm.class).findAll();

        SaleApiResponseTableRealm saleApiResponseTableRealms1;

        if (saleApiResponseTableRealms.size() > 0) {
            saleApiResponseTableRealms1 = saleApiResponseTableRealms.last();
            return saleApiResponseTableRealms1.getId();
        } else {
            return 0;
        }
    }


    public void insertOrderDetails(final HolderListModel holderListModel, final String getDate, final int sales_id,
                                   final int orderId, final int orderStatus, final int syncStatus) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

//                for (int i = 0; i < orderApiResponseTableRealmList.size(); i++) {

                    Number currentIdNumber = realm.where(HolderListModel.class).max("id");
                    int nextId;

                    if (currentIdNumber == null) {
                        nextId = 1;
                    } else {
                        nextId = currentIdNumber.intValue() + 1;
                    }

//                    OrderApiResponseTableRealm holderListModel = orderApiResponseTableRealmList.get(i);
                    HolderListModel holderListModel1 = realm.createObject(HolderListModel.class, nextId);
                    holderListModel1.setStoreId(holderListModel.getStoreId());
                    holderListModel1.setSalesId(sales_id);
                    holderListModel1.setCustomSaleId(holderListModel.getCustomSaleId());
                    holderListModel1.setoDate(getDate);
                    holderListModel1.setBrand(holderListModel.getBrand());
                    holderListModel1.setSkuCategory(holderListModel.getSkuCategory());
                    holderListModel1.setSKU(holderListModel.getSKU());
                    holderListModel1.setSaleType(holderListModel.getSaleType());
                    holderListModel1.setNoItem(holderListModel.getNoItem());
                    holderListModel1.setPrice(holderListModel.getPrice());
                    holderListModel1.setsAmount(holderListModel.getsAmount());
                    holderListModel1.setOrderStatus(orderStatus);
                    holderListModel1.setOrderId(orderId);
                    holderListModel1.setSyncStatus(syncStatus);
//                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.i(LOG_TAG, "Insertion Success");
                check = true;
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.i(LOG_TAG, "Insertion Failed");
                check = false;
            }
        });
    }

    public List<HolderListModel> getPendingOrder(int customSaleId) {
        List<HolderListModel> holderListModelList = realm.where(HolderListModel.class).equalTo("customSaleId", customSaleId).findAll();

        if (holderListModelList.size() > 0) {
            return holderListModelList;
        } else {
            return null;
        }
    }


//    public List<HolderListModel> getPendingOrder() {
//        List<HolderListModel> holderListModelList = realm.where(HolderListModel.class).equalTo("orderStatus", 0).findAll();
//
//        if (holderListModelList.size() > 0) {
//            return holderListModelList;
//        } else {
//            return null;
//        }
//    }

    public void updateOrderStatus (final int id, final int orderId, final int orderStatus, final int syncStatus) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HolderListModel holderListModel = realm.where(HolderListModel.class).equalTo("id", id).findFirst();

                holderListModel.setOrderId(orderId);
                holderListModel.setOrderStatus(orderStatus);
                holderListModel.setSyncStatus(syncStatus);

                realm.copyToRealmOrUpdate(holderListModel);
            }
        });
    }

    public SalesAndNoSales getSaleAndNoSales () {

        SalesAndNoSales salesAndNoSales = null;

        try {
            int sale = (int) realm.where(SaleApiResponseTableRealm.class).equalTo("syncStatus", 1).count();
            int noSale = (int) realm.where(SaleApiResponseTableRealm.class).equalTo("syncStatus", 0).count();

        salesAndNoSales = new SalesAndNoSales(sale, noSale);

        }catch (NumberFormatException e) {

        }

        return salesAndNoSales;


    }

    public int getLastLoggedInUserStoreID() {
        List<LoginResponse> loginResponses = realm.where(LoginResponse.class).equalTo("loginStatus", 1).findAll();

        if (loginResponses.size() != 0) {
            return Integer.parseInt(loginResponses.get(loginResponses.size() - 1).getStoreId());
        } else {
            return 0;
        }
    }

    public void insertMarkAneEndAttendanceRelationIdInEndAttendanceTable(final int relationalIdMarkAndEndAttendance) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Number currentIdNumber = realm.where(EndMarkAttendanceTable.class).max("id");
                int nextId;

                if (currentIdNumber == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNumber.intValue() + 1;
                }
                EndMarkAttendanceTable endMarkAttendanceTable1 = realm.createObject(EndMarkAttendanceTable.class, nextId);
                endMarkAttendanceTable1.setMarkAndEndAttendanceRelationId(relationalIdMarkAndEndAttendance);
            }
        });
    }

    public void insertBrandsCategoryDetails(SalesSKUArrayResponse salesSKUArrayResponse, int loginUserRelationIdWithBrands) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number currentIdNumber = realm.where(SalesSKUArrayResponse.class).max("id");
                int nextId;

                if (currentIdNumber == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNumber.intValue() + 1;
                }

                SalesSKUArrayResponse salesSKUArrayResponse1 = realm.createObject(SalesSKUArrayResponse.class, nextId);
                salesSKUArrayResponse1.setLoginUserRelationIdWithBrands(loginUserRelationIdWithBrands);
                salesSKUArrayResponse1.setId(salesSKUArrayResponse.getId());
                salesSKUArrayResponse1.setBrand(salesSKUArrayResponse.getBrand());
                salesSKUArrayResponse1.setBrandId(salesSKUArrayResponse.getBrandId());
                salesSKUArrayResponse1.setsKUCaategory(salesSKUArrayResponse.getsKUCaategory());
                salesSKUArrayResponse1.setCateId(salesSKUArrayResponse.getCateId());
                salesSKUArrayResponse1.setName(salesSKUArrayResponse.getName());
                salesSKUArrayResponse1.setPrice(salesSKUArrayResponse.getPrice());
                salesSKUArrayResponse1.setItemPerCarton(salesSKUArrayResponse.getItemPerCarton());
                salesSKUArrayResponse1.setSKUImage(salesSKUArrayResponse.getSKUImage());
                salesSKUArrayResponse1.setCreatedAt(salesSKUArrayResponse.getCreatedAt());
                salesSKUArrayResponse1.setUpdatedAt(salesSKUArrayResponse.getUpdatedAt());
//                salesSKUArrayResponse1.setDeletedAt(salesSKUArrayResponse.getDeletedAt());

            }
        });
    }

    public boolean checkCurrentUserHasSavedBrands(int userId) {

        RealmResults<SalesSKUArrayResponse> salesSKUArrayResponses = realm.where(SalesSKUArrayResponse.class).equalTo("loginUserRelationIdWithBrands",
                userId).findAll();

        if (salesSKUArrayResponses.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public List<SalesSKUArrayResponse> getUserBrandsSKUCategory (int loginUserRelationIdWithBrands) {

        RealmResults<SalesSKUArrayResponse> salesSKUArrayResponses = realm.where(SalesSKUArrayResponse.class).equalTo("loginUserRelationIdWithBrands",
                loginUserRelationIdWithBrands).findAll();

        if (salesSKUArrayResponses.size() > 0) {
            return salesSKUArrayResponses;
        } else {
            return salesSKUArrayResponses = null;
        }
    }

    public List<SalesSKUArrayResponse> getUserBrandsSKUCategoryByBrandId (int brandId) {

        RealmResults<SalesSKUArrayResponse> salesSKUArrayResponses = realm.where(SalesSKUArrayResponse.class).equalTo("cateId",
                brandId).findAll();

        if (salesSKUArrayResponses.size() > 0) {
            return salesSKUArrayResponses;
        } else {
            return salesSKUArrayResponses = null;
        }
    }


    public void removeUserBrandsSKUCategories (int loginUserRelationIdWithBrands) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(SalesSKUArrayResponse.class);

                RealmResults<LoginResponse> result = realm.where(LoginResponse.class).equalTo("userId", loginUserRelationIdWithBrands).findAll();
                result.deleteAllFromRealm();
            }
        });

    }

    public void insertTasksDetails(final List<Task> taskList, final RealmCallback realmCallback) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for (int i=0; i < taskList.size(); i++) {
                    Task task = realm.createObject(Task.class);
                    task.setId(taskList.get(i).getId());
                    task.setAssetId(taskList.get(i).getAssetId());
                    task.setAssetId(taskList.get(i).getAssetId());
                    task.setEmpId(taskList.get(i).getEmpId());
                    task.setAssignBy(taskList.get(i).getAssignBy());
                    task.setTasktype(taskList.get(i).getTasktype());
                    task.setStartDate(taskList.get(i).getStartDate());
                    task.setEndDate(taskList.get(i).getEndDate());
                    task.setDescription(taskList.get(i).getDescription());
                    task.setStatus(taskList.get(i).getStatus());
                    task.setCreatedAt(taskList.get(i).getCreatedAt());
                    task.setUpdatedAt(taskList.get(i).getUpdatedAt());
                    task.setDeletedAt(taskList.get(i).getDeletedAt());
                    task.setShopId(taskList.get(i).getShopId());
                    task.setShopName(taskList.get(i).getShopName());
                    task.setShopLat(taskList.get(i).getShopLat());
                    task.setShopLng(taskList.get(i).getShopLng());

                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realmCallback.onSuccess();
                Log.i("OnSuccess for Task", "inserted");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                realmCallback.onError();
                Log.i("OnError for Task", "Not inserted");
            }
        });
    }

    public List<Task> getAllTasks () {

        List<Task> taskRealmResults = realm.where(Task.class).findAll();

        if (taskRealmResults.size() > 0) {
            return taskRealmResults;
        } else {
            return taskRealmResults = null;
        }
    }

}

