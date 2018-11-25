package com.example.arsalansiddiq.beem.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.example.arsalansiddiq.beem.interfaces.AttandanceInterface;
import com.example.arsalansiddiq.beem.interfaces.EndAttendanceInterface;
import com.example.arsalansiddiq.beem.interfaces.LoginInterface;
import com.example.arsalansiddiq.beem.interfaces.MeetingCallBack;
import com.example.arsalansiddiq.beem.interfaces.SKUCategoryInterface;
import com.example.arsalansiddiq.beem.interfaces.SampleInterface;
import com.example.arsalansiddiq.beem.interfaces.TargetsAndAchievementResponseInterface;
import com.example.arsalansiddiq.beem.interfaces.merchantcallback.BaseCallbackInterface;
import com.example.arsalansiddiq.beem.models.requestmodels.AddShopRequest;
import com.example.arsalansiddiq.beem.models.requestmodels.LoginRequest;
import com.example.arsalansiddiq.beem.models.requestmodels.StartMeetingRequest;
import com.example.arsalansiddiq.beem.models.responsemodels.AttandanceResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.MeetingResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.ResponseSUP;
import com.example.arsalansiddiq.beem.models.responsemodels.babreak.BreakTypeResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.compulsory.CompulsoryStepsResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.merchanttask.MerchantTaskResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.storesurveyquestions.StoreSurveyQuestionsResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.surveyquestions.SurveyQuestionsResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.salesresponsemodels.SalesObjectResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.targetsandachievementsmodel.TargetsandAchievementsModel;
import com.example.arsalansiddiq.beem.models.responsemodels.tasksresponsemodels.TaskResponse;
import com.example.arsalansiddiq.beem.utils.data.UpdateCallback;
import com.example.arsalansiddiq.beem.utils.data.UpdateRH;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arsalansiddiq on 1/18/18.
 */

public class NetworkUtils {

    private static final String LOG_TAG = "NetworkUtils";

    private Context mcontext;

    private static NetworkRequestInterfaces networkRequestInterfaces = ApiUtils.getConnection();

    private ProgressDialog progressDialog;

    public NetworkUtils(Context context) {
        this.mcontext = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void userLogin(LoginRequest loginRequest, final LoginInterface loginInterface) {
        progressDialog.show();
        networkRequestInterfaces.userLogin(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {
                    Log.i(LOG_TAG, String.valueOf(response.body().getStatus()));
                    loginInterface.success(response);
                } else {
                    loginInterface.failed("invalid credentials");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginInterface.failed("Something went wrong!");
                progressDialog.cancel();
            }
        });
    }

    public void attandanceBA(String date, int userId, String name, File file, String startTime,
                             double latitude, double longitude, int status, final AttandanceInterface attandanceInterface) {

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("StartImage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));


        progressDialog.show();
        networkRequestInterfaces.attandanceBA(date, userId, name, startTime, latitude,
                longitude, status, filePart).enqueue(new Callback<AttandanceResponse>() {
            @Override
            public void onResponse(Call<AttandanceResponse> call, Response<AttandanceResponse> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {
                    attandanceInterface.success(response);
                } else {
                    attandanceInterface.failed("invalid credentials");
                }
            }

            @Override
            public void onFailure(Call<AttandanceResponse> call, Throwable t) {
                attandanceInterface.failed(t.getLocalizedMessage());
                progressDialog.cancel();
            }
        });
    }

    public void endAttandenceBA(int meetingId, String endTime, double eLatitude, double eLongitude,
                                File userImage, final EndAttendanceInterface endAttendanceInterface) {

        MultipartBody.Part endImage = MultipartBody.Part.createFormData("EndImage", userImage.getName(), RequestBody.create(MediaType.parse("image/*"), userImage));

        progressDialog.show();
        networkRequestInterfaces.endAttandanceBA(meetingId, endTime, eLatitude, eLongitude, endImage).enqueue(new Callback<AttandanceResponse>() {
            @Override
            public void onResponse(Call<AttandanceResponse> call, Response<AttandanceResponse> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {
                    endAttendanceInterface.success(response);
                } else {
                    endAttendanceInterface.failed("invalid credentials");
                }
            }

            @Override
            public void onFailure(Call<AttandanceResponse> call, Throwable t) {
                endAttendanceInterface.failed(t.getLocalizedMessage());
                progressDialog.cancel();
            }
        });
    }

    public void getBrandsofUser(String brandName, final SKUCategoryInterface skuCategoryInterface) {

        networkRequestInterfaces.getBrands(brandName).enqueue(new Callback<SalesObjectResponse>() {
            @Override
            public void onResponse(Call<SalesObjectResponse> call, Response<SalesObjectResponse> response) {
                if (response.isSuccessful()) {
                    skuCategoryInterface.success(response);
                } else {
                    skuCategoryInterface.failed("No Products Available");
                }
            }

            @Override
            public void onFailure(Call<SalesObjectResponse> call, Throwable t) {
                skuCategoryInterface.failed(t.getLocalizedMessage());
            }
        });
    }


    public void sendSaleDetail(String cusName, String contact, String email, String gender, Integer age, String  cBrand,
                               String pBrand, Integer saleStatus, Integer empId, String empName, String designation, String city,
                               Integer location, final LoginInterface loginInterface) {

        networkRequestInterfaces.sendSalesDetails(cusName, contact, email, gender, age, cBrand, pBrand, saleStatus,
                empId, empName, designation, city, location).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                progressDialog.cancel();
                if (response.isSuccessful()) {
                    loginInterface.success(response);
                    Log.i("Sale Status", String.valueOf(response.body().getStatus()));
                } else {
                    loginInterface.failed("Something Went Worng");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginInterface.failed(t.getLocalizedMessage());
            }
        });
    }


    public void sendOrderDetail(Integer storeId, Integer salesId, String oDate, String brand, Integer skuCategory, Integer SKU, Integer saleType,
                                Integer noItem, Integer price, Integer sAmount, final SampleInterface loginInterface) {

        networkRequestInterfaces.sendOrderDetails(storeId, salesId, oDate, brand, skuCategory, SKU, saleType, noItem, price,
                sAmount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(LoginResponse value) {
                        loginInterface.success(value);
                        Log.i("val", String.valueOf(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        loginInterface.failed(e.getLocalizedMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getTargetsAndAchievements(int storeId, final TargetsAndAchievementResponseInterface targetsAndAchievementResponseInterface) {

        networkRequestInterfaces.getTargetsAndAchievements(storeId).enqueue(new Callback<TargetsandAchievementsModel>() {
            @Override
            public void onResponse(Call<TargetsandAchievementsModel> call, Response<TargetsandAchievementsModel> response) {
                if (response.isSuccessful()) {
                    targetsAndAchievementResponseInterface.success(response);
                    Log.i("Sale Status", String.valueOf(response.body().getStatus()));
                } else {
                    targetsAndAchievementResponseInterface.failed("Something Went Worng");
                }
            }

            @Override
            public void onFailure(Call<TargetsandAchievementsModel> call, Throwable t) {
                targetsAndAchievementResponseInterface.failed(t.getLocalizedMessage());
            }
        });
    }

    public void attandanceSUP(int userId, String name, String startTime,
                              double latitude, double longitude, int status, final AttandanceInterface attandanceInterface) {

        progressDialog.show();
        networkRequestInterfaces.attandanceSUP(userId, name, startTime, latitude,
                longitude, status).enqueue(new Callback<AttandanceResponse>() {
            @Override
            public void onResponse(Call<AttandanceResponse> call, Response<AttandanceResponse> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {
                    attandanceInterface.success(response);
                } else {
                    attandanceInterface.failed("invalid credentials");
                }
            }

            @Override
            public void onFailure(Call<AttandanceResponse> call, Throwable t) {
                attandanceInterface.failed(t.getLocalizedMessage());
                progressDialog.cancel();
            }
        });
    }

    public void endAttandenceSUP(int meetingId, String endTime, double eLatitude, double eLongitude,
                                 int status, final EndAttendanceInterface endAttendanceInterface) {

        progressDialog.show();
        networkRequestInterfaces.endAttandanceSUP(meetingId, endTime, eLatitude, eLongitude, status).enqueue(new Callback<AttandanceResponse>() {
            @Override
            public void onResponse(Call<AttandanceResponse> call, Response<AttandanceResponse> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {
                    endAttendanceInterface.success(response);
                } else {
                    endAttendanceInterface.failed("invalid credentials");
                }
            }

            @Override
            public void onFailure(Call<AttandanceResponse> call, Throwable t) {
                endAttendanceInterface.failed(t.getLocalizedMessage());
                progressDialog.cancel();
            }
        });
    }

    public void startMeeting (StartMeetingRequest startMeetingRequest, MeetingCallBack meetingCallBack) {

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("img1", startMeetingRequest.getFile().getName(), RequestBody.create(MediaType.parse("image/*"), startMeetingRequest.getFile()));

        networkRequestInterfaces.startMeeting(startMeetingRequest.getTask_id(), startMeetingRequest.getDateTime(), filePart,
                startMeetingRequest.getLat(), startMeetingRequest.getLng(), startMeetingRequest.getEmp_id()).enqueue(new Callback<MeetingResponseModel>() {
            @Override
            public void onResponse(Call<MeetingResponseModel> call, Response<MeetingResponseModel> response) {
                if (response.isSuccessful()) {
                    meetingCallBack.success(response);
                } else {
                    meetingCallBack.error("Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<MeetingResponseModel> call, Throwable t) {
                meetingCallBack.error(t.getLocalizedMessage());
            }
        });

    }


//    public void startMeeting (StartMeetingRequest startMeetingRequest, UpdateCallback updateCallback) {
//
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("img1", startMeetingRequest.getFile().getName(), RequestBody.create(MediaType.parse("image/*"), startMeetingRequest.getFile()));
//
//        networkRequestInterfaces.startMeeting(startMeetingRequest.getTask_id(), startMeetingRequest.getDateTime(), filePart).enqueue(new UpdateRH<MeetingResponseModel>(updateCallback));
//
//    }

    public void getTaskList (int empId, UpdateCallback updateCallback) {
        networkRequestInterfaces.getTasks(String.valueOf(empId)).enqueue(new UpdateRH<TaskResponse>(updateCallback));
    }

    public void updateMeeting (StartMeetingRequest startMeetingRequest, MeetingCallBack meetingCallBack) {
        MultipartBody.Part filePart = null;

        if (!TextUtils.isEmpty(startMeetingRequest.getNotes()) || startMeetingRequest.getNotes() != null) {
            filePart = null;
        } else if (startMeetingRequest.getFile() != null){
            filePart = MultipartBody.Part.createFormData("img2", startMeetingRequest.getFile().getName(), RequestBody.create(MediaType.parse("image/*"), startMeetingRequest.getFile()));
        }

        networkRequestInterfaces.updateMeeting(startMeetingRequest.getTask_id(), startMeetingRequest.getNotes(), filePart).enqueue(new Callback<MeetingResponseModel>() {
            @Override
            public void onResponse(Call<MeetingResponseModel> call, Response<MeetingResponseModel> response) {
                if (response.isSuccessful()) {
                    meetingCallBack.success(response);
                } else {
                    meetingCallBack.error("Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<MeetingResponseModel> call, Throwable t) {
                meetingCallBack.error(t.getLocalizedMessage());
            }
        });
    }

    public void endMeeting(StartMeetingRequest startMeetingRequest, MeetingCallBack meetingCallBack) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("img4", startMeetingRequest.getFile().getName(), RequestBody.create(MediaType.parse("image/*"), startMeetingRequest.getFile()));
        networkRequestInterfaces.endMeeting(startMeetingRequest.getTask_id(), startMeetingRequest.getDateTime(), filePart).enqueue(new Callback<MeetingResponseModel>() {
            @Override
            public void onResponse(Call<MeetingResponseModel> call, Response<MeetingResponseModel> response) {
                if (response.isSuccessful()) {
                    meetingCallBack.success(response);
                } else {
                    meetingCallBack.error("Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<MeetingResponseModel> call, Throwable t) {
                meetingCallBack.error(t.getLocalizedMessage());
            }
        });
    }

    public void addShop (AddShopRequest addShopRequest, UpdateCallback updateCallback) {

        networkRequestInterfaces.addShop(addShopRequest.getEmp_id(), addShopRequest.getShopname(), addShopRequest.getOwner(),
                addShopRequest.getContactperson(), addShopRequest.getContactnumber(), addShopRequest.getLat(), addShopRequest.getLng())
                .enqueue(new UpdateRH<ResponseSUP>(updateCallback));

    }

    public void getBreakTypes(UpdateCallback updateCallback) {
        networkRequestInterfaces.getBreakTypes().enqueue(new UpdateRH<BreakTypeResponseModel>(updateCallback));
    }

    public void startBreak(int emp_id, int break_id, String Date, String StartTime, UpdateCallback updateCallback) {
        networkRequestInterfaces.startBreak(emp_id, break_id, Date, StartTime).enqueue(new UpdateRH<ResponseSUP>(updateCallback));
    }

    public void endBreak(int id, String EndTime, int Status, UpdateCallback updateCallback) {
        networkRequestInterfaces.endBreak(id, EndTime, Status).enqueue(new UpdateRH<ResponseSUP>(updateCallback));
    }

    public void startTracking(int emp_id, String startTime, double lat, double lng, UpdateCallback updateCallback) {
        networkRequestInterfaces.startTracking(emp_id, startTime, lat, lng).enqueue(new UpdateRH<MeetingResponseModel>(updateCallback));
    }

    public void updateTracking(int emp_id, double lat, double lng, UpdateCallback updateCallback) {
        networkRequestInterfaces.updateTracking(emp_id, lat, lng).enqueue(new UpdateRH<MeetingResponseModel>(updateCallback));
    }

    public void updateTrackingOffline(int emp_id, double lat, double lng, MeetingCallBack meetingCallBack) {
        networkRequestInterfaces.updateTracking(emp_id, lat, lng).enqueue(new Callback<MeetingResponseModel>() {
            @Override
            public void onResponse(Call<MeetingResponseModel> call, Response<MeetingResponseModel> response) {
                if (response.isSuccessful()) {
                    meetingCallBack.success(response);
                } else {
                    meetingCallBack.error("something went wrong");
                }
            }

            @Override
            public void onFailure(Call<MeetingResponseModel> call, Throwable t) {
                meetingCallBack.error(t.getLocalizedMessage());
            }
        });
    }

    public void endTracking(int emp_id, double lat, double lng, int status, String endTime, UpdateCallback updateCallback) {
        networkRequestInterfaces.endTracking(emp_id, lat, lng, status, endTime).enqueue(new UpdateRH<MeetingResponseModel>(updateCallback));
    }

    public void getComplusorySteps(int brand_id, BaseCallbackInterface baseCallbackInterface){
        networkRequestInterfaces.getComplusorySteps(brand_id).enqueue(new Callback<CompulsoryStepsResponseModel>() {
            @Override
            public void onResponse(Call<CompulsoryStepsResponseModel> call, Response<CompulsoryStepsResponseModel> response) {
                baseCallbackInterface.success(response);
                Log.i("getComplusorySteps", String.valueOf(response.body().getStatus()));
            }

            @Override
            public void onFailure(Call<CompulsoryStepsResponseModel> call, Throwable t) {
                baseCallbackInterface.failure(t.getLocalizedMessage());
                Log.e("getComplusorySteps", "error");
            }
        });
    }

//    public void getSurveyQA(int brand_id) {
    public void getSurveyQA(BaseCallbackInterface baseCallbackInterface) {
        networkRequestInterfaces.getSurveyQA(39).enqueue(new Callback<SurveyQuestionsResponseModel>() {
            @Override
            public void onResponse(Call<SurveyQuestionsResponseModel> call, Response<SurveyQuestionsResponseModel> response) {
                baseCallbackInterface.success(response);
                Log.i("getSurveyQA", String.valueOf(response.body().getStatus()));
            }

            @Override
            public void onFailure(Call<SurveyQuestionsResponseModel> call, Throwable t) {
                baseCallbackInterface.failure(t.getLocalizedMessage());
                Log.e("getSurveyQA", "error");
            }
        });
    }

//    public void storeSurveyQuestions(int user_id, int brand_id, String questions, String answers) {
    public void storeSurveyQuestions(BaseCallbackInterface baseCallbackInterface) {
        networkRequestInterfaces.storeSurveyQuestions(1, 2, "[\"Question1\",\"Question2\",\"Question3\",\"Question4\"]",
                "[\"Answer1\",\"Answer2\",\"Answer3\",\"\"]").enqueue(new Callback<StoreSurveyQuestionsResponseModel>() {
            @Override
            public void onResponse(Call<StoreSurveyQuestionsResponseModel> call, Response<StoreSurveyQuestionsResponseModel> response) {
                baseCallbackInterface.success(response);
                Log.i("storeSurveyQuestions", String.valueOf(response.body().getStatus()));
            }

            @Override
            public void onFailure(Call<StoreSurveyQuestionsResponseModel> call, Throwable t) {
                baseCallbackInterface.failure(t.getLocalizedMessage());
                Log.e("storeSurveyQuestions", "error");
            }
        });
    }

//    public void storeMerchantTaskResponse(int user_id, int task_id, int shop_id, int brand_id,
//                                          File take_store_picture_1, File take_store_picture_2,
//                                          File before_chillers_pic_front_2, String feedback) {
    public void storeMerchantTaskResponse(BaseCallbackInterface baseCallbackInterface) {
        MultipartBody.Part filePart1 = null;
//                = MultipartBody.Part.createFormData("Take_Store_Picture_1",
//                take_store_picture_1.getName(), RequestBody.create(MediaType.parse("image/*"), take_store_picture_1));
        MultipartBody.Part filePart2 = null;
//                = MultipartBody.Part.createFormData("Take_Store_Picture_2",
//                take_store_picture_1.getName(), RequestBody.create(MediaType.parse("image/*"), take_store_picture_2));
        MultipartBody.Part filePart3 = null;
//        = MultipartBody.Part.createFormData("Before_Chillers_Pic_Front_2",
//                before_chillers_pic_front_2.getName(), RequestBody.create(MediaType.parse("image/*"), before_chillers_pic_front_2));

        networkRequestInterfaces.storeMerchantTaskResponse(10, 7, 7, 7,
                filePart1, filePart2, filePart3, "test")
                .enqueue(new Callback<MerchantTaskResponse>() {
                    @Override
                    public void onResponse(Call<MerchantTaskResponse> call, Response<MerchantTaskResponse> response) {
                        baseCallbackInterface.success(response);
                        Log.i("storeMerchantTa", String.valueOf(response.body().getStatus()));
                    }

                    @Override
                    public void onFailure(Call<MerchantTaskResponse> call, Throwable t) {
                        baseCallbackInterface.failure(t.getLocalizedMessage());
                        Log.e("storeMerchantTa", "error");
                    }
                });
    }

    public void dynamicKeyValue(int user_id, int task_id, int shop_id, int brand_id, String key, String value, BaseCallbackInterface baseCallbackInterface) {

        Map<String, String> mapParams = new HashMap<>();
        mapParams.put(key, value);

        networkRequestInterfaces.storeMerchantTaskResponseDynamicKeyValues(8, 8, 8, 8, mapParams).enqueue(
                new Callback<MerchantTaskResponse>() {
                    @Override
                    public void onResponse(Call<MerchantTaskResponse> call, Response<MerchantTaskResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<MerchantTaskResponse> call, Throwable t) {

                    }
                }
        );

    }

    public void dynamicKeyFiles(int user_id, int task_id, int shop_id, int brand_id, String key, File file, BaseCallbackInterface baseCallbackInterface) {

        MultipartBody.Part filePart = MultipartBody.Part.createFormData(key,
                file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        networkRequestInterfaces.storeMerchantTaskResponseDynamicKeyFiles(8, 8, 8, 8, filePart).enqueue(
                new Callback<MerchantTaskResponse>() {
                    @Override
                    public void onResponse(Call<MerchantTaskResponse> call, Response<MerchantTaskResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<MerchantTaskResponse> call, Throwable t) {

                    }
                }
        );
    }
}
