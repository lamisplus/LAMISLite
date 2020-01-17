package org.fhi360.lamis.mobile.lite.Service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.lite.Activities.ClientTracking;
import org.fhi360.lamis.mobile.lite.Activities.TableViewAdapter;
import org.fhi360.lamis.mobile.lite.DAO.AssementDAO;
import org.fhi360.lamis.mobile.lite.DAO.ClinicDAO;
import org.fhi360.lamis.mobile.lite.DAO.HtsDAO;
import org.fhi360.lamis.mobile.lite.DAO.IndexContactDAO;
import org.fhi360.lamis.mobile.lite.DAO.PatientDAO;
import org.fhi360.lamis.mobile.lite.Db.LAMISLiteDb;
import org.fhi360.lamis.mobile.lite.Model.AssessmentList;
import org.fhi360.lamis.mobile.lite.Model.ClinicList;
import org.fhi360.lamis.mobile.lite.Model.Defaulter;
import org.fhi360.lamis.mobile.lite.Model.HtsList;
import org.fhi360.lamis.mobile.lite.Model.IndexContactList;
import org.fhi360.lamis.mobile.lite.Model.IpAddress;
import org.fhi360.lamis.mobile.lite.Model.ListDefaulters;
import org.fhi360.lamis.mobile.lite.Model.PatientList;
import org.fhi360.lamis.mobile.lite.Utils.PrefManager;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Intent.getIntent;
import static org.fhi360.lamis.mobile.lite.Utils.Constant.ACTION_UPDATE;

public class LocalSystemIpAddressServices extends IntentService {
    public static Retrofit retrofit = null;
    public Context context;

    public LocalSystemIpAddressServices() {
        super("IntentServiceHandler");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE.equals(action)) {
                PrefManager prefManager = new PrefManager(this);
                HashMap<String, String> ipAddress = prefManager.getIpAddress();
                String localIpAdress = ipAddress.get("ipAddress");
                synchronizeAssessment(localIpAdress);
            }

        }

    }



    private static OkHttpClient getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        return client;
    }

    public <S> S createService(String ipAddress,Class<S> serviceClass) {
         Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://" +ipAddress+ "/")
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.client(getClient()).build();
        return retrofit.create(serviceClass);
    }

    private void synchronizeAssessment(final String ipAddress) {
        LocalSystemIpAddressServices APIService = new LocalSystemIpAddressServices();
        ClientAPI clientAPI = APIService.createService(ipAddress,ClientAPI.class);
        AssessmentList assessmentList = new AssessmentList();
        assessmentList.setAssessment(new AssementDAO(getApplicationContext()).getAssessment());
        Call<Object> objectCall = clientAPI.syncAssessment(assessmentList);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    synchronizeHts(ipAddress);
                } else {

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();

            }

        });

    }


    public void synchronizeHts(final String ipAddress) {
        LocalSystemIpAddressServices APIService = new LocalSystemIpAddressServices();
        ClientAPI clientAPI = APIService.createService(ipAddress,ClientAPI.class);
        HtsList hts = new HtsList();
        hts.setHts(new HtsDAO(getApplicationContext()).syncData());
        Call<Object> objectCall = clientAPI.syncHts(hts);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    synchronizeIndexContact(ipAddress);
                } else {

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();

            }

        });
    }

    private void synchronizeIndexContact(final String ipAddress) {
        LocalSystemIpAddressServices APIService = new LocalSystemIpAddressServices();
        ClientAPI clientAPI =APIService.createService(ipAddress,ClientAPI.class);
        IndexContactList listIndexcontact = new IndexContactList();
        listIndexcontact.setIndexcontact(new IndexContactDAO(getApplicationContext()).getIndexContact());
        Call<Object> objectCall = clientAPI.syncIndexContact(listIndexcontact);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    synchronizePatients(ipAddress);
                } else {

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();

            }

        });

    }

    private void synchronizePatients(final String ipAddress) {
        LocalSystemIpAddressServices APIService = new LocalSystemIpAddressServices();
        ClientAPI clientAPI = APIService.createService(ipAddress,ClientAPI.class);
        PatientList listPatient = new PatientList();
        listPatient.setPatient(new PatientDAO(getApplicationContext()).getAllPatient());
        Call<Object> objectCall = clientAPI.syncPatient(listPatient);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    synchronizeClinic(ipAddress);

                } else {

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();

            }

        });

    }

    private void synchronizeClinic(final String ipAddress) {
        LocalSystemIpAddressServices APIService = new LocalSystemIpAddressServices();
        ClientAPI clientAPI = APIService.createService(ipAddress,ClientAPI.class);
        ClinicList listClinic = new ClinicList();
        listClinic.setClinic(new ClinicDAO(getApplicationContext()).getAllClinic());
        Call<Object> objectCall = clientAPI.syncClinic(listClinic);

        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();

            }

        });


    }

}