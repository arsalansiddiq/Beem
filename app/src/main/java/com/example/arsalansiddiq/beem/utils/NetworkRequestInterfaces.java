package com.example.arsalansiddiq.beem.utils;

import com.example.arsalansiddiq.beem.models.requestmodels.LoginRequest;
import com.example.arsalansiddiq.beem.models.requestmodels.merchant.MeetingRequestMerchant;
import com.example.arsalansiddiq.beem.models.requestmodels.merchant.SKUMerchantRequestModel;
import com.example.arsalansiddiq.beem.models.responsemodels.AttandanceResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.MeetingResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.ResponseSUP;
import com.example.arsalansiddiq.beem.models.responsemodels.babreak.BreakTypeResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.competitionsku.MerchantSKU;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.compulsory.CompulsoryStepsResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.merchanttask.MerchantTaskResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.storesurveyquestions.StoreSurveyQuestionsResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.surveyquestions.SurveyQuestionsResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.viewinstruction.ViewInstructionResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.salesresponsemodels.SalesObjectResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.targetsandachievementsmodel.TargetsandAchievementsModel;
import com.example.arsalansiddiq.beem.models.responsemodels.tasksresponsemodels.TaskResponse;
import com.jakewharton.retrofit2.adapter.rxjava2.Result;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by arsalansiddiq on 7/16/18.
 */

public interface NetworkRequestInterfaces {

//    @Headers("Content-Type: application/json")
//    @POST("insert.php")
//    Call<SendLcoationResponseModel> insertCoordinates(@Body SendLocationRequestModel sendLocationRequestModel);

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

//    @Multipart
//    @POST("attendance")
//    Call<AttandanceResponse> attandanceBA();

    @Multipart
    @POST("attendance")
    Call<AttandanceResponse> attandanceBA(@Part("date") String date,
                                                   @Part("empid") int userId,
                                                   @Part("name") String name,
                                                   @Part("startTime") String startTime,
                                                   @Part("latitude") double latitude,
                                                   @Part("longitude") double longitude,
                                                   @Part("status") int status,
                                                           @Part MultipartBody.Part file);


    @Multipart
    @POST("attendance")
    Observable<Result<AttandanceResponse>> attandanceBAObservable(@Part("date") String date,
                                                                  @Part("empid") int userId,
                                                                  @Part("name") String name,
                                                                  @Part("startTime") String startTime,
                                                                  @Part("latitude") double latitude,
                                                                  @Part("longitude") double longitude,
                                                                  @Part("status") int status,
                                                                  @Part MultipartBody.Part file);



    @Multipart
    @POST("endattendance")
    Call<AttandanceResponse> endAttandanceBA(@Part("id") int meetingId,
                                          @Part("endTime") String endTime,
                                          @Part("elat") double eLatitude,
                                          @Part("elng") double eLongitude,
                                          @Part MultipartBody.Part endImage);

    @GET("sku/{SKUCaategory}")
    Call<SalesObjectResponse> getBrands(@Path("SKUCaategory") String SKUCaategory);

    @GET("sku/{SKUCaategory}")
    Observable<SalesObjectResponse> getBrand(@Path("SKUCaategory") String SKUCaategory);

    @Multipart
    @POST("sales")
    Call<LoginResponse> sendSalesDetails(@Part("cusName") String cusName, @Part("contact") String contact,
                                               @Part("email") String email, @Part("gender") String gender,
                                               @Part("age") Integer age, @Part("cBrand") String cBrand,
                                               @Part("pBrand") String pBrand, @Part("saleStatus") Integer saleStatus,
                                               @Part("empId") Integer empId, @Part("empName") String empName,
                                               @Part("designation") String designation, @Part("City") String city,
                                               @Part("Location") Integer location);


    @Multipart
    @POST("order")
    Observable<LoginResponse> sendOrderDetails(@Part("storeId") Integer storeId, @Part("salesId") Integer salesId,
                                               @Part("oDate") String oDate, @Part("brand") String brand,
                                               @Part("skuCategory") Integer skuCategory, @Part("SKU") Integer SKU,
                                               @Part("saleType") Integer saleType, @Part("noItem") Integer noItem,
                                               @Part("price") Integer price, @Part("sAmount") Integer sAmount);

    @GET("storeAchieved/{storeId}")
    Call<TargetsandAchievementsModel> getTargetsAndAchievements(@Path("storeId") Integer storeId);


    @Multipart
    @POST("supattendance")
    Call<AttandanceResponse> attandanceSUP(@Part("empid") int userId,
                                          @Part("name") String name,
                                          @Part("startTime") String startTime,
                                          @Part("latitude") double latitude,
                                          @Part("longitude") double longitude,
                                          @Part("status") int status);


    @Multipart
    @POST("supendattendance")
    Call<AttandanceResponse> endAttandanceSUP(@Part("id") int meetingId,
                                             @Part("endTime") String endTime,
                                             @Part("elat") double eLatitude,
                                             @Part("elng") double eLongitude,
                                              @Part("status") int status);

    @Multipart
    @POST("startmeeting")
    Call<MeetingResponseModel> startMeeting(@Part("task_id") int task_id,
                                            @Part("StartTime") String StartTime,
                                            @Part MultipartBody.Part file,
                                            @Part("lat") double lat, @Part("lng") double lng,
                                            @Part("emp_id") int emp_id);

    @Multipart
    @POST("updatemeeting")
    Call<MeetingResponseModel> updateMeeting(@Part("task_id") int task_id,
                                   @Part("notes") String notes,
                                   @Part MultipartBody.Part file);

    @Multipart
    @POST("endmeeting")
    Call<MeetingResponseModel> endMeeting(@Part("task_id") int task_id,
                                   @Part("EndTime") String EndTime,
                                   @Part MultipartBody.Part file);

    @Multipart
    @POST("shopapp")
    Call<ResponseSUP> addShop(@Part("emp_id") Integer emp_id, @Part("shopname") String shopname,
                              @Part("owner") String owner, @Part("contactperson") String contactperson,
                              @Part("contactnumber") String contactnumber, @Part("lat") float lat,
                              @Part("lng") float lng);

    @GET("gettasks/{emp_id}")
    Call<TaskResponse> getTasks(@Path("emp_id") String emp_id);

    @GET("breaktype")
    Call<BreakTypeResponseModel> getBreakTypes();

    @Multipart
    @POST("startbreak")
    Call<ResponseSUP> startBreak(@Part("emp_id") Integer emp_id, @Part("break_id") Integer break_id,
                                 @Part("Date") String Date, @Part("StartTime") String StartTime);

    @Multipart
    @POST("endbreak")
    Call<ResponseSUP> endBreak(@Part("id") Integer id, @Part("EndTime") String EndTime, @Part("Status") Integer Status);

    @Multipart
    @POST("starttracking")
    Call<MeetingResponseModel> startTracking(@Part("emp_id") Integer id, @Part("StartTime") String StartTime,
                                    @Part("lat") Double lat, @Part("lng") Double lng);

    @Multipart
    @POST("updatetracking")
    Call<MeetingResponseModel> updateTracking(@Part("emp_track_id") Integer id, @Part("lat") Double lat,
                                     @Part("lng") Double lng);

    @Multipart
    @POST("endtracking")
    Call<MeetingResponseModel> endTracking(@Part("emp_track_id") Integer id, @Part("lat") Double lat,
                                           @Part("lng") Double lng, @Part("status") Integer status,
                                           @Part("EndTime") String EndTime);

    @GET("brandconfig/{brand_id}")
    Call<CompulsoryStepsResponseModel> getComplusorySteps(@Path("brand_id") int brand_id);

    @GET("survayquestionconfig/{brand_id}")
    Call<SurveyQuestionsResponseModel> getSurveyQA(@Path("brand_id") int brand_id);

    @GET("store-survey-response")
    Call<StoreSurveyQuestionsResponseModel> storeSurveyQuestions(@Query("user_id") int user_id,
                                                                 @Query("brand_id") int brand_id,
                                                                 @Query("questions") String questions,
                                                                 @Query("answers") String answers);

    @Multipart
    @POST("merchant/task/response")
    Call<MerchantTaskResponse> storeMerchantTaskResponse(@Part("user_id") int user_id, @Part("task_id") int task_id,
                                                         @Part("shop_id") int shop_id,
                                                         @Part("brand_id") int brand_id,
                                                         @Part MultipartBody.Part Take_Store_Picture_1,
                                                         @Part MultipartBody.Part Take_Store_Picture_2,
                                                         @Part MultipartBody.Part Before_Chillers_Pic_Front_2,
                                                         @Part("Feedback") String Feedback);

    @Multipart
    @POST("merchant/task/response")
    Call<MerchantTaskResponse> storeMerchantTaskResponseDynamicKeyValues(@Part("user_id") int user_id, @Part("task_id") int task_id,
                                                                         @Part("shop_id") int shop_id,
                                                                         @Part("brand_id") int brand_id,
                                                                         @PartMap Map<String, String> params);
    @Multipart
    @POST("merchant/task/response")
    Call<MerchantTaskResponse> storeMerchantTaskResponseDynamicKeyValuesQA(@Part("user_id") int user_id, @Part("task_id") int task_id,
                                                                         @Part("shop_id") int shop_id,
                                                                         @Part("brand_id") int brand_id,
                                                                         @PartMap Map<String, String> params);

    @Multipart
    @POST("merchant/task/response")
    Call<MerchantTaskResponse> storeMerchantTaskResponseDynamicKeyFiles(@Part("user_id") int user_id, @Part("task_id") int task_id,
                                                                         @Part("shop_id") int shop_id,
                                                                         @Part("brand_id") int brand_id,
                                                                        @Part MultipartBody.Part file);

    @POST("merchant/task/response")
    Call<MerchantTaskResponse> storeMerchantTaskResponse(@Body MeetingRequestMerchant meetingRequestMerchant);

    @Multipart
    @POST("viewinstruction")
    Call<ViewInstructionResponseModel> getViewInstruction(@Part("brand_id") int brand_id,
                                                          @Part("shop_id") int shop_id);

    @Multipart
    @POST("merchant/tasks")
    Call<MerchantTaskResponse> getMerchantTasks(@Part("emp_id") int emp_id);


    @POST("updateshoplatlong")
    Call<ResponseSUP> updateShopLatLong(@Body MeetingRequestMerchant meetingRequestMerchant);

    @POST("getskucompetition")
    Call<MerchantSKU> getCompetitionSKU (@Query("brand_id") int brand_id);

    @POST("getsku")
    Call<MerchantSKU> getMerchantSKU (@Query("brand_id") int brand_id);

    @POST("storeskuresponse")
    Call<ResponseSUP> storeSKUPrice (@Body SKUMerchantRequestModel skuMerchantRequestModel);

}
