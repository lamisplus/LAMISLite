package org.fhi360.lamis.mobile.lite.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.lite.DAO.FacilityDAO;
import org.fhi360.lamis.mobile.lite.DAO.LgaDAO;
import org.fhi360.lamis.mobile.lite.DAO.RegimensDAO;
import org.fhi360.lamis.mobile.lite.DAO.StateDAO;
import org.fhi360.lamis.mobile.lite.Db.LAMISLiteDb;
import org.fhi360.lamis.mobile.lite.Model.Data;
import org.fhi360.lamis.mobile.lite.Model.Facility;
import org.fhi360.lamis.mobile.lite.Model.Lga;
import org.fhi360.lamis.mobile.lite.Model.Regimens;
import org.fhi360.lamis.mobile.lite.Model.State;
import org.fhi360.lamis.mobile.lite.R;
import org.fhi360.lamis.mobile.lite.Service.APIService;
import org.fhi360.lamis.mobile.lite.Service.ClientAPI;
import org.fhi360.lamis.mobile.lite.Utils.PrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activation extends AppCompatActivity {
    Button activate;
    EditText edit_text_password, edit_text_username;
    Long faclityId;
    String faciltyName;
    Long stateId;
    String stateName;
    Long lgaId;
    String lgaName;
    int SPLASH_TIME = 2500;
    PrefManager prefManager;

    String androidId;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));
        activate = findViewById(R.id.activate);

        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        edit_text_username = findViewById(R.id.edit_text_username);
        edit_text_password = findViewById(R.id.edit_text_password);
        prefManager = new PrefManager(getApplicationContext());
        ArrayList<Facility> states = new FacilityDAO(getApplicationContext()).getFacility();
        if (states.isEmpty()) {
            activate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateInput(edit_text_username.getText().toString(), edit_text_password.getText().toString())) {
                        progressdialog = new ProgressDialog(Activation.this);
                        progressdialog.setMessage("Initializing App please wait...");
                        progressdialog.setCancelable(false);
                        progressdialog.setIndeterminate(false);
                        progressdialog.setMax(100);
                        progressdialog.show();
                        getRecordsFromLamisApi(edit_text_username.getText().toString(), Long.parseLong(edit_text_password.getText().toString()), androidId);

                    }
                }
            });
        } else {
            Intent streamPlayerHome = new Intent(getApplicationContext(), Home.class);
            startActivity(streamPlayerHome);
            finish();
        }

    }

    private void getRecordsFromLamisApi(String userName, long pin, String diviceId) {
        APIService APIService = new APIService();
        ClientAPI clientAPI = APIService.createService(ClientAPI.class);
        HashMap<String, String> user = prefManager.checkIfUserExist();
        String accountuserName = user.get("userName");
        String passwords = user.get("password");
        Call<Data> objectCall = clientAPI.getFacilityCode(userName, pin, diviceId, accountuserName, passwords);
        objectCall.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.code() == 200) {
                    Data dataObject = response.body();
                    Set<Facility> facilities = dataObject.getFacilities();
                    for (Facility facility : facilities) {
                        faclityId = facility.getFacility_id();
                        faciltyName = facility.getName();
                        stateId = facility.getState_id();
                        lgaId = facility.getLga_id();
                        Facility facility1 = new Facility();
                        facility1.setState_id(facility.getState_id());
                        facility1.setName(facility.getName());
                        facility1.setLga_id(facility.getLga_id());
                        facility1.setFacility_id(facility.getFacility_id());
                        facility1.setDeviceconfig_id(facility.getDeviceconfig_id());
                        prefManager.saveDetails("", facility.getName(), "", facility.getLga_id(), facility.getState_id(), facility.getFacility_id(), facility.getDeviceconfig_id(), androidId);
                        new FacilityDAO(getApplicationContext()).saveFacility(facility1);
                    }
                    Set<State> states = dataObject.getStates();
                    for (State state : states) {
                        State state1 = new State();
                        state1.setName(state.getName());
                        state1.setState_id(state.getState_id());
                        new StateDAO(getApplicationContext()).saveState(state1);
                    }
                    Set<Lga> lgas = dataObject.getLgas();
                    for (Lga lga : lgas) {
                        Lga lga1 = new Lga();
                        lga1.setLga_id(lga.getLga_id());
                        lga1.setName(lga.getName());
                        lga1.setState_id(lga.getState_id());
                        new LgaDAO(getApplicationContext()).saveLga(lga1);
                    }

                    Set<Regimens> regimen = dataObject.getRegimens();
                    for (Regimens regimen1 : regimen) {
                        Regimens regimen2 = new Regimens();
                        regimen2.setRegimentype(regimen1.getRegimentype());
                        regimen2.setRegimentype_id(regimen1.getRegimentype_id());
                        regimen2.setRegimen_id(regimen1.getRegimen_id());
                        regimen2.setRegimen(regimen1.getRegimen());
                        new RegimensDAO(getApplicationContext()).save(regimen2);
                    }
                    prefManager.update();
                    Intent streamPlayerHome = new Intent(getApplicationContext(), Home.class);
                    startActivity(streamPlayerHome);
                    finish();
                    progressdialog.dismiss();
                } else if (response.code() == 500) {
                    FancyToast.makeText(getApplicationContext(), "No Server response, contact System Admin", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    progressdialog.dismiss();
                } else if (response.code() == 400) {
                    FancyToast.makeText(getApplicationContext(), "No Server response, contact System Admin", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    progressdialog.dismiss();
                } else if (response.code() == 404) {
                    FancyToast.makeText(getApplicationContext(), "No Server response, contact System Admin", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    progressdialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                t.printStackTrace();
                FancyToast.makeText(getApplicationContext(), "No Internet Connection", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                progressdialog.dismiss();
            }


        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressdialog != null && progressdialog.isShowing()) {
            progressdialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private boolean validateInput(String username1, String password) {
        if (username1.isEmpty()) {
            edit_text_username.setError("username can not be empty");
            return false;
        } else if (password.isEmpty()) {
            edit_text_password.setError("Facility pin code can not be empty");
            return false;
        }
        return true;


    }


}
