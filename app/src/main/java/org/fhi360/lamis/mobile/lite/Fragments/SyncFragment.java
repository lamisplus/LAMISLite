package org.fhi360.lamis.mobile.lite.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import org.fhi360.lamis.mobile.lite.DAO.AssementDAO;
import org.fhi360.lamis.mobile.lite.DAO.ClinicDAO;
import org.fhi360.lamis.mobile.lite.DAO.HtsDAO;
import org.fhi360.lamis.mobile.lite.DAO.IndexContactDAO;
import org.fhi360.lamis.mobile.lite.DAO.PatientDAO;
import org.fhi360.lamis.mobile.lite.Db.LAMISLiteDb;
import org.fhi360.lamis.mobile.lite.Service.APIService;
import org.fhi360.lamis.mobile.lite.Service.ClientAPI;

import com.google.gson.Gson;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.lite.R;
import org.fhi360.lamis.mobile.lite.Model.ClinicList;
import org.fhi360.lamis.mobile.lite.Model.HtsList;
import org.fhi360.lamis.mobile.lite.Model.IndexContactList;
import org.fhi360.lamis.mobile.lite.Model.PatientList;
import org.fhi360.lamis.mobile.lite.Model.AssessmentList;
import org.fhi360.lamis.mobile.lite.Service.LocalSystemIpAddressServices;
import org.fhi360.lamis.mobile.lite.Utils.PrefManager;
import org.fhi360.lamis.mobile.lite.Utils.SettingConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncFragment extends Fragment {
    private Button synchronize;
    private ProgressDialog mPb;
    public static Retrofit retrofit = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sync, container, false);
        synchronize = rootView.findViewById(R.id.synchronize);
        final AppCompatSpinner defualtServer = rootView.findViewById(R.id.serUrl);
        PrefManager prefManager = new PrefManager(getContext());
        HashMap<String, String> ipAddress = prefManager.getIpAddress();
        final String localIpAdress = ipAddress.get("ipAddress");
        ArrayList list = new ArrayList();
        list.add("http://lamis3.sidhas.org:8080");
        if(localIpAdress!=null){
            list.add(localIpAdress);
        }
        System.out.printf("IPddress " + localIpAdress);
        final ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_items, list);
        districtAdapter.setDropDownViewResource(R.layout.color_spinner_layout);
        districtAdapter.notifyDataSetChanged();
        defualtServer.setAdapter(districtAdapter);
        synchronize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String defualtSer = defualtServer.getSelectedItem().toString();
                if (defualtSer.equals("http://lamis3.sidhas.org:8080")) {
                    synchronizeAssessment();
                } else {
                    synchronizeAssessment(localIpAdress);
                }
            }
        });


        return rootView;
    }


    private void synchronizeAssessment() {
        mPb = new ProgressDialog(getContext());
        mPb.setProgress(0);
        mPb.setMessage("UpLoading data please wait...");
        mPb.setCancelable(false);
        mPb.setIndeterminate(false);
        mPb.setProgress(0);
        mPb.setMax(100);
        mPb.show();
        APIService APIService = new APIService();
        ClientAPI clientAPI = APIService.createService(ClientAPI.class);
        AssessmentList assessmentList = new AssessmentList();
        assessmentList.setAssessment(new AssementDAO(getContext()).getAssessment());
        Call<Object> objectCall = clientAPI.syncAssessment(assessmentList);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    synchronizeHts();
                } else {
                    FancyToast.makeText(getContext(), "Syn was not successfull to LAMIS Server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    mPb.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                FancyToast.makeText(getContext(), "Syn was not successfull to LAMIS Server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                mPb.dismiss();
            }

        });

    }


    public void synchronizeHts() {
        APIService APIService = new APIService();
        ClientAPI clientAPI = APIService.createService(ClientAPI.class);
        HtsList hts = new HtsList();
        hts.setHts(new HtsDAO(getContext()).syncData());
        Call<Object> objectCall = clientAPI.syncHts(hts);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    synchronizeIndexContact();
                } else {
                    FancyToast.makeText(getContext(), "Syn was not successfull to LAMIS Server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    mPb.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                FancyToast.makeText(getContext(), "Syn was not successfull to LAMIS Server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                mPb.dismiss();
            }

        });
    }

    private void synchronizeIndexContact() {
        APIService APIService = new APIService();
        ClientAPI clientAPI = APIService.createService(ClientAPI.class);
        IndexContactList listIndexcontact = new IndexContactList();
        listIndexcontact.setIndexcontact(new IndexContactDAO(getContext()).getIndexContact());
        Call<Object> objectCall = clientAPI.syncIndexContact(listIndexcontact);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    synchronizePatients();
                } else {
                    FancyToast.makeText(getContext(), "Syn was not successfull to LAMIS Server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    mPb.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                FancyToast.makeText(getContext(), "Syn was not successfull to LAMIS Server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                mPb.dismiss();
            }

        });

    }

    private void synchronizePatients() {

        APIService APIService = new APIService();
        ClientAPI clientAPI = APIService.createService(ClientAPI.class);
        PatientList listPatient = new PatientList();
        listPatient.setPatient(new PatientDAO(getContext()).getAllPatient());
        Call<Object> objectCall = clientAPI.syncPatient(listPatient);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    synchronizeClinic();
                } else {
                    FancyToast.makeText(getContext(), "Syn was not successfull to LAMIS Server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    mPb.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                FancyToast.makeText(getContext(), "Syn was not successfull to LAMIS Server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                mPb.dismiss();
            }

        });

    }

    private void synchronizeClinic() {
        APIService APIService = new APIService();
        ClientAPI clientAPI = APIService.createService(ClientAPI.class);
        ClinicList listClinic = new ClinicList();
        listClinic.setClinic(new ClinicDAO(getContext()).getAllClinic());
        Call<Object> objectCall = clientAPI.syncClinic(listClinic);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    FancyToast.makeText(getContext(), "Syn  successfull", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    mPb.hide();
                } else {
                    FancyToast.makeText(getContext(), "Syn was not successfull to LAMIS Server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    mPb.hide();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                FancyToast.makeText(getContext(), "No internet connection", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                mPb.hide();
            }

        });

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

    public <S> S createService(String ipAddress, Class<S> serviceClass) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + "/")
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.client(getClient()).build();
        return retrofit.create(serviceClass);
    }

    private void synchronizeAssessment(final String ipAddress) {
        mPb = new ProgressDialog(getContext());
        mPb.setProgress(0);
        mPb.setMessage("UpLoading data please wait...");
        mPb.setCancelable(false);
        mPb.setIndeterminate(false);
        mPb.setProgress(0);
        mPb.setMax(100);
        mPb.show();
        LocalSystemIpAddressServices APIService = new LocalSystemIpAddressServices();
        ClientAPI clientAPI = APIService.createService(ipAddress, ClientAPI.class);
        AssessmentList assessmentList = new AssessmentList();
        assessmentList.setAssessment(new AssementDAO(getContext()).getAssessment());
        Call<Object> objectCall = clientAPI.syncAssessment(assessmentList);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    synchronizeHts(ipAddress);
                } else {
                    FancyToast.makeText(getContext(), "Syn was not successfull to LAMIS Server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    mPb.hide();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                FancyToast.makeText(getContext(), "No internet connection", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                mPb.hide();
            }

        });

    }


    public void synchronizeHts(final String ipAddress) {
        LocalSystemIpAddressServices APIService = new LocalSystemIpAddressServices();
        ClientAPI clientAPI = APIService.createService(ipAddress, ClientAPI.class);
        HtsList hts = new HtsList();
        hts.setHts(new HtsDAO(getContext()).syncData());
        Call<Object> objectCall = clientAPI.syncHts(hts);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    synchronizeIndexContact(ipAddress);
                } else {
                    FancyToast.makeText(getContext(), "Syn was not successfull to LAMIS Server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    mPb.hide();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                FancyToast.makeText(getContext(), "No internet connection", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                mPb.hide();
            }

        });
    }

    private void synchronizeIndexContact(final String ipAddress) {
        LocalSystemIpAddressServices APIService = new LocalSystemIpAddressServices();
        ClientAPI clientAPI = APIService.createService(ipAddress, ClientAPI.class);
        IndexContactList listIndexcontact = new IndexContactList();
        listIndexcontact.setIndexcontact(new IndexContactDAO(getContext()).getIndexContact());
        Call<Object> objectCall = clientAPI.syncIndexContact(listIndexcontact);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    synchronizePatients(ipAddress);
                } else {
                    FancyToast.makeText(getContext(), "Syn was not successfull to LAMIS Server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    mPb.hide();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                FancyToast.makeText(getContext(), "No internet connection", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                mPb.hide();
            }

        });

    }

    private void synchronizePatients(final String ipAddress) {
        LocalSystemIpAddressServices APIService = new LocalSystemIpAddressServices();
        ClientAPI clientAPI = APIService.createService(ipAddress, ClientAPI.class);
        PatientList listPatient = new PatientList();
        listPatient.setPatient(new PatientDAO(getContext()).getAllPatient());
        Call<Object> objectCall = clientAPI.syncPatient(listPatient);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    synchronizeClinic(ipAddress);

                } else {
                    FancyToast.makeText(getContext(), "Syn was not successfull to LAMIS Server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    mPb.hide();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                FancyToast.makeText(getContext(), "No internet connection", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                mPb.hide();
            }

        });

    }

    private void synchronizeClinic(final String ipAddress) {
        LocalSystemIpAddressServices APIService = new LocalSystemIpAddressServices();
        ClientAPI clientAPI = APIService.createService(ipAddress, ClientAPI.class);
        ClinicList listClinic = new ClinicList();
        listClinic.setClinic(new ClinicDAO(getContext()).getAllClinic());
        Call<Object> objectCall = clientAPI.syncClinic(listClinic);

        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                } else {
                    FancyToast.makeText(getContext(), "Syn was not successfull to LAMIS Server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    mPb.hide();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                FancyToast.makeText(getContext(), "No internet connection", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                mPb.hide();
            }

        });


    }
}
