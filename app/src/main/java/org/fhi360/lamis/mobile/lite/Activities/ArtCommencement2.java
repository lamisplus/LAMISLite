package org.fhi360.lamis.mobile.lite.Activities;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.lite.DAO.ClinicDAO;
import org.fhi360.lamis.mobile.lite.DAO.RegimensDAO;
import org.fhi360.lamis.mobile.lite.Db.LAMISLiteDb;
import org.fhi360.lamis.mobile.lite.Model.Clinic;
import org.fhi360.lamis.mobile.lite.Model.Patient2;
import org.fhi360.lamis.mobile.lite.Model.Regimens;
import org.fhi360.lamis.mobile.lite.R;
import org.fhi360.lamis.mobile.lite.Utils.PrefManager;
import org.fhi360.lamis.mobile.lite.Utils.SettingConfig;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ArtCommencement2 extends AppCompatActivity {
    private EditText nextAppointment,
            systolic,
            height,
            bodyWeight,
            dateVisit,
            hospitalNumArt;
    private Spinner tbStatusSpinner,
            funcStatus,
            regimen,
            regimenline;
    private Button save;
    private TextView textViewfullName;
    private Calendar myCalendar = Calendar.getInstance();
    private String lgaName;
    private String facilityName;
    private String stateName;
    private String lgaId;
    private  String steteId;
    private String facilityId;
    private String unigueId;
    private PrefManager session;
    private HashMap<String, String> user = null;
    private String hospitalNumber;
    private SettingConfig setttings = new SettingConfig();
    private String regimenIds;
    private String names;
    private String patientId ;
    private String deviceconfigId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.art_commence);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));
        nextAppointment = findViewById(R.id.nextAppointment);

        systolic = findViewById(R.id.bp);
        height = findViewById(R.id.height);
        textViewfullName = findViewById(R.id.fullName);
        bodyWeight = findViewById(R.id.bodyWeight);
        dateVisit = findViewById(R.id.dateVisit);
        hospitalNumArt = findViewById(R.id.hospitalNum);
        tbStatusSpinner = findViewById(R.id.tbStatus);
        funcStatus = findViewById(R.id.funcStatus);
        hospitalNumArt.setEnabled(false);
        regimen = findViewById(R.id.clinicStage);
        regimenline = findViewById(R.id.regimentype);
        save = findViewById(R.id.finishButton);
        session = new PrefManager(getApplicationContext());
        final HashMap<String, String> patientId1 = session.getPatientId();
        user = session.getHtsDetails();

        if (user != null) {
            facilityName = user.get("faciltyName");
            lgaId = user.get("lgaId");
            steteId = user.get("stateId");
            facilityId = user.get("facilityId");
            stateName = user.get("steteName");
            lgaName = user.get("lgaNam");
            deviceconfigId=user.get("deviceconfig_id");
        }
        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        dateVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final DatePickerDialog mDatePicker = new DatePickerDialog(ArtCommencement2.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.show();


            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabe2();
            }

        };


        nextAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final DatePickerDialog mDatePicker = new DatePickerDialog(ArtCommencement2.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.show();


            }
        });
        final ArrayList regimensId = new ArrayList<>();
        ArrayList regimenstype = new ArrayList<>();
        List<Regimens> regimens = new RegimensDAO(this).getRegimentType();
        for (Regimens regimens1 : regimens) {
            regimensId.add(regimens1.getRegimentype_id());
            regimenstype.add(regimens1.getRegimentype());
        }
        final ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.spinner_items, regimenstype);
        districtAdapter.setDropDownViewResource(R.layout.color_spinner_layout);
        districtAdapter.notifyDataSetChanged();
        regimenline.setAdapter(districtAdapter);
        regimenline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                regimenIds = (String) regimensId.get(position);
                ArrayList<Regimens> regimens1 = new ArrayList<>();
                ArrayList regimens2 = new ArrayList<>();
                regimens1 = new RegimensDAO(getApplicationContext()).getByRegimen(regimenIds);
                for (Regimens regimens4 : regimens1) {
                    regimens2.add(regimens4.getRegimen());
                }
                final ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(getApplicationContext(),
                        R.layout.spinner_items, regimens2);
                districtAdapter.setDropDownViewResource(R.layout.color_spinner_layout);
                districtAdapter.notifyDataSetChanged();
                regimen.setAdapter(districtAdapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
    });
        if (patientId1 != null) {
            patientId = patientId1.get("patientId");
            hospitalNumber = patientId1.get("hospitalNumber");
            unigueId = patientId1.get("uniqueId");
            String surname2 = patientId1.get("surname");
            String othename1 = patientId1.get("othername");
            String firstLetter = String.valueOf(othename1.charAt(0)).toUpperCase()+ othename1.toLowerCase().substring(1);
            names = surname2.toUpperCase() + "   " + firstLetter;
            hospitalNumArt.setText(hospitalNumber);
            Clinic clinic = new ClinicDAO(this).getAllClinicByPatientId(Long.parseLong(patientId));
            if (clinic != null) {
                nextAppointment.setText(clinic.getNextAppointment());
                systolic.setText(clinic.getBp());
                height.setText(String.valueOf(clinic.getHeight()));
                bodyWeight.setText(String.valueOf(clinic.getBodyWeight()));
                dateVisit.setText(clinic.getDateVisit());
                setttings.setSpinText(tbStatusSpinner, clinic.getTbStatus());
                setttings.setSpinText(funcStatus, clinic.getFuncStatus());
                setttings.setSpinText(regimenline, clinic.getRegimentype());
                setttings.setSpinText(regimen, clinic.getRegimen());
            }
            textViewfullName.setText(Html.fromHtml("<font color=\"#000000\">" + "Name:  " + "</font>" + "<font color=\"#CD853F\">" + names + "</font>"));
        } else {
            textViewfullName.setText("ART commencement can not be completed please enroll client");
            textViewfullName.setTextColor(Color.RED);
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput(hospitalNumArt.getText().toString(),dateVisit.getText().toString(), nextAppointment.getText().toString())) {
                    if (patientId != null) {
                        Clinic clinic = new Clinic();
                        if (new ClinicDAO(getApplicationContext()).checkIfClinicExist2(Long.parseLong(patientId))) {
                            FancyToast.makeText(getApplicationContext(), "ART commencement already exist", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        } else {
                            Patient2 patient1 = new Patient2();
                            patient1.setPatientId(Long.parseLong(patientId));
                            clinic.setPatient(patient1);
                            clinic.setFacilityId(Long.parseLong(facilityId));
                            clinic.setDateVisit(dateVisit.getText().toString());
                            clinic.setFuncStatus(funcStatus.getSelectedItem().toString());
                            clinic.setTbStatus(tbStatusSpinner.getSelectedItem().toString());
                            clinic.setViralLoad("");
                            clinic.setDeviceconfigId(Long.parseLong(deviceconfigId));
                            clinic.setRegimentype(regimenline.getSelectedItem().toString());
                            if (regimenline.getSelectedItem().toString().equals("")) {
                                clinic.setRegimen("");
                            } else {
                                clinic.setRegimen(regimen.getSelectedItem().toString());
                            }
                            //regimen table
                            if (bodyWeight.getText().toString().equals("")) {
                                clinic.setBodyWeight(0.0);
                            } else {
                                clinic.setBodyWeight(Double.parseDouble(bodyWeight.getText().toString()));
                            }
                            if (height.getText().toString().equals("")) {
                                clinic.setHeight(0.0);
                            } else {
                                clinic.setHeight(Double.parseDouble(height.getText().toString()));
                            }
                            clinic.setWaist(0);
                            clinic.setBp(systolic.getText().toString());
                            clinic.setPregnant("0");
                            clinic.setLmp("");
                            clinic.setBreastfeeding(0);
                            clinic.setNextAppointment(nextAppointment.getText().toString());
                            clinic.setNotes("");
                            new ClinicDAO(getApplicationContext()).insertClinic(clinic);
                            FancyToast.makeText(getApplicationContext(), "ART saved successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        }
                    } else {
                        FancyToast.makeText(getApplicationContext(), "ART can not be completed please enroll client", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                    }
                }
            }
        });
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateVisit.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabe2() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        nextAppointment.setText(sdf.format(myCalendar.getTime()));
    }

        private boolean validateInput(String hospitalNumber1,String dateVisit1, String dateNextAppointment) {
            if (dateVisit1.isEmpty()) {
                dateVisit.setError("Enter Date visit");
                FancyToast.makeText(getApplicationContext(), "Enter date visit", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                return false;

            } else if (dateNextAppointment.isEmpty()) {
                nextAppointment.setError("Enter date next appointment");
                FancyToast.makeText(getApplicationContext(), "Enter date next appointment", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                return false;
            } else if (hospitalNumber1.isEmpty()) {
                hospitalNumArt.setError("Enter Hospital number");
                FancyToast.makeText(getApplicationContext(), "Enter Hospital number", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                return false;
            }

            return true;


        }

}
