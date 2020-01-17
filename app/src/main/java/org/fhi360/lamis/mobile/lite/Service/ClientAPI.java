package org.fhi360.lamis.mobile.lite.Service;
import org.fhi360.lamis.mobile.lite.Model.Account;
import org.fhi360.lamis.mobile.lite.Model.Data;
import org.fhi360.lamis.mobile.lite.Model.ClinicList;
import org.fhi360.lamis.mobile.lite.Model.Defaulter;
import org.fhi360.lamis.mobile.lite.Model.HtsList;
import org.fhi360.lamis.mobile.lite.Model.IndexContactList;
import org.fhi360.lamis.mobile.lite.Model.ListDefaulters;
import org.fhi360.lamis.mobile.lite.Model.PatientList;
import org.fhi360.lamis.mobile.lite.Model.AssessmentList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface ClientAPI {
    @Headers("Content-Type: application/json")
    @GET("resources/webservice/mobile/initialize")
    Call<Data> getData();

    @Headers("Content-Type: application/json")
    @GET("resources/webservice/mobile/initialize/{userName}/{facilityId}/{deviceId}/{accountUserName}/{accountPassword}")
    Call<Data> getFacilityCode(@Path("userName") String username,@Path("facilityId") long pin,@Path("deviceId") String deviceId,@Path("accountUserName") String accountUserName,@Path("accountPassword") String accountPassword);


    @Headers("Content-Type: application/json")
    @GET("resources/webservice/mobile/initialize/{deviceId}/{facilityId}")
    Call<Account> getUsernamePasswordFromLamis(@Path("deviceId") String deviceId,@Path("facilityId") long facilityId);


    @Headers("Content-Type: application/json")
    @GET("resources/webservice/mobile/defaulter/{facilityId}")
    Call<ListDefaulters> clientTracking(@Path("facilityId") Long facilityId);



    @Headers("Content-Type: application/json")
    @POST("resources/webservice/mobile/sync/hts")
    Call<Object> syncHts(@Body HtsList hts);


    @Headers("Content-Type: application/json")
    @POST("resources/webservice/mobile/sync/patient")
    Call<Object> syncPatient(@Body PatientList patients);


    @Headers("Content-Type: application/json")
    @POST("resources/webservice/mobile/sync/assessment")
    Call<Object> syncAssessment(@Body AssessmentList patients);

    @Headers("Content-Type: application/json")
    @POST("resources/webservice/mobile/sync/indexcontact")
    Call<Object> syncIndexContact(@Body IndexContactList indexContacts);


    @Headers("Content-Type: application/json")
    @POST("resources/webservice/mobile/sync/clinic")
    Call<Object> syncClinic(@Body ClinicList clinics);

}
