package com.colorworld.manbocash.retrofit;

import com.colorworld.manbocash.model.Duplication;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {


    //@Path --> @GET ("/{user_mp}/")

    @GET("checkemail")
    Call<Duplication> duplicationEmailCheck(@Query("email") String checkingEmail);

    @GET("checkreferee")
    Call<Duplication> duplicationReferCodeCheck(@Query("referee") String checkingReferCode);


    @GET("checkuid")
    Call<Duplication> duplicationUidCheck(@Query("uid") String checkingUid);
//
//
//    @GET("ars_kiosk_save_payment/")
//    Call<Data> getNetStat(@Query("user_mp") String phoneNum,
//                          @Query("kiosk_tel") String kiosk,
//                          @Query("point") String point,
//                          @Query("m_cnt") String mCnt,
//                          @Query("w_cnt") String wCnt);

    @FormUrlEncoded
    @POST("checkemail")
    Call<Duplication> duplicationEmailCheck(@FieldMap HashMap<String, String> param);

    @FormUrlEncoded
    @POST("referee")
    Call<Duplication> duplicationReferCodeCheck(@FieldMap HashMap<String, String> param);

//    @FormUrlEncoded
//    @POST("LogIn/app_req_qr_login")
//    Call<QrCodeData> getQRCodeData(@FieldMap HashMap<String, String> param);

//    @FormUrlEncoded
//    @POST("Attend/admin_logout")
//    Call<LoginData> logoutProcess(@FieldMap HashMap<String, String> param);
}
