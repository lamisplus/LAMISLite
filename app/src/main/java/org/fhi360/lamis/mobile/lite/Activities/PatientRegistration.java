package org.fhi360.lamis.mobile.lite.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.lite.DAO.HtsDAO;
import org.fhi360.lamis.mobile.lite.DAO.PatientDAO;
import org.fhi360.lamis.mobile.lite.Db.LAMISLiteDb;
import org.fhi360.lamis.mobile.lite.Model.Facility2;
import org.fhi360.lamis.mobile.lite.Model.Patient;
import org.fhi360.lamis.mobile.lite.R;
import org.fhi360.lamis.mobile.lite.Utils.PrefManager;
import org.fhi360.lamis.mobile.lite.Utils.SettingConfig;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class PatientRegistration extends AppCompatActivity {
    private EditText phoneKin, addressKin, dateRegistration, nextKinName, hospitalNum, ageUnit, maritalStatus, gender, state, lga, uniqueId, surname, otherNames, dateBirth, age, dateConfirmedHiv, address, phone;
    private AppCompatSpinner relationKin, pregnancyStatus, tbStatus, occupation, education, entryPoint, statusRegistration;
    private Button save;
    private String lgaName;
    private String facilityName;
    private String stateName;
    private String lgaId;
    private String steteId;
    private String facilityId;
    private String htsId;
    private TextView pregnancyStatus2;
    private PrefManager session;
    private String surname2, otherName2, dateBirth2, phone2, dateVist2, address1, age2, gender2, state2, lga2, maritalStatus2, ageUnits;
    private SettingConfig settingConfig = new SettingConfig();
    private HashMap<String, String> user = null;
    private HashMap<String, String> user1 = null;
    private Calendar myCalendar = Calendar.getInstance();
    private String deviceconfigId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patients);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));
        session = new PrefManager(getApplicationContext());
        phoneKin = findViewById(R.id.phoneKin);
        addressKin = findViewById(R.id.addressKin);
        statusRegistration = findViewById(R.id.statusRegistration);
        dateRegistration = findViewById(R.id.dateRegistrationss);
        pregnancyStatus2 = findViewById(R.id.pregnancyStatus2);
        nextKinName = findViewById(R.id.nextKinName);
        hospitalNum = findViewById(R.id.hospitalNum);
        uniqueId = findViewById(R.id.uniqueId);
        surname = findViewById(R.id.surnames);
        otherNames = findViewById(R.id.otherNamess);
        dateBirth = findViewById(R.id.dateBirthEnrollemt);
        age = findViewById(R.id.ageEnrollemt);
        dateConfirmedHiv = findViewById(R.id.dateConfirmedHiv);
        address = findViewById(R.id.addresss);
        relationKin = findViewById(R.id.relationKin);
        pregnancyStatus = findViewById(R.id.pregnancyStatus1);
        tbStatus = findViewById(R.id.tbStatus1s);
        ageUnit = findViewById(R.id.ageUnits);
        gender = findViewById(R.id.genders);

        maritalStatus = findViewById(R.id.maritalStatus1);
        occupation = findViewById(R.id.occupations);
        education = findViewById(R.id.educations);
        state = findViewById(R.id.states);
        lga = findViewById(R.id.lgas);
        entryPoint = findViewById(R.id.spinner_entry_point);
        phone = findViewById(R.id.phones);
        save = findViewById(R.id.finishButton);


        HashMap<String, String> user1 = session.getProfileDetails();
        surname2 = user1.get("surname");
        otherName2 = user1.get("othernames");
        age2 = user1.get("age");
        address1 = user1.get("address");
        phone2 = user1.get("phone");
        gender2 = user1.get("gender");
        dateVist2 = user1.get("dateVisit");
        dateBirth2 = user1.get("dateBirth");
        lga2 = user1.get("lga");
        state2 = user1.get("state");
        maritalStatus2 = user1.get("maritalStatus");
        ageUnits = user1.get("ageUnit");
        htsId = user1.get("htsId");

        Patient patient = new PatientDAO(this).findPatientHtsId(Long.parseLong(htsId));
        if (patient != null) {
            hospitalNum.setText(patient.getHospitalNum());
            uniqueId.setText(patient.getUniqueId());
            settingConfig.setSpinText(occupation, patient.getOccupation());
            settingConfig.setSpinText(education, patient.getEducation());
            settingConfig.setSpinText(entryPoint, patient.getEntryPoint());
            settingConfig.setSpinText(tbStatus, patient.getTbStatus());
            nextKinName.setText(patient.getNextKin());
            addressKin.setText(patient.getAddressKin());
            settingConfig.setSpinText(relationKin, patient.getRelationKin());
            dateRegistration.setText(patient.getDateRegistration());
            phoneKin.setText(patient.getPhoneKin());
            if (gender2.equals("Male")) {
                pregnancyStatus.setVisibility(View.INVISIBLE);
                pregnancyStatus2.setVisibility(View.INVISIBLE);
            } else {
                settingConfig.setSpinText(pregnancyStatus, patient.getPregnant());
                pregnancyStatus2.setVisibility(View.VISIBLE);
                pregnancyStatus.setVisibility(View.VISIBLE);
            }
        }
        surname.setEnabled(false);
        surname.setText(surname2);

        otherNames.setEnabled(false);
        otherNames.setText(otherName2);
        age.setEnabled(false);
        age.setText(age2);

        maritalStatus.setText(maritalStatus2);
        maritalStatus.setEnabled(false);

        lga.setText(lga2);
        lga.setEnabled(false);


        state.setText(state2);
        state.setEnabled(false);


        address.setText(address1);
        address.setEnabled(false);

        gender.setText(gender2);
        gender.setEnabled(false);

        phone.setText(phone2);
        // phone.setEnabled(false);

        dateBirth.setText(dateBirth2);
        dateBirth.setEnabled(false);

        ageUnit.setEnabled(false);
        ageUnit.setText(ageUnits);

        dateConfirmedHiv.setText(dateVist2);


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


        dateRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final DatePickerDialog mDatePicker = new DatePickerDialog(PatientRegistration.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();


            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    user = session.getHtsDetails();
                    if (user != null) {
                        facilityName = user.get("faciltyName");
                        lgaId = user.get("lgaId");
                        steteId = user.get("stateId");
                        facilityId = user.get("facilityId");
                        stateName = user.get("steteName");
                        lgaName = user.get("lgaNam");
                        deviceconfigId = user.get("deviceconfig_id");
                    }


                    if (validateInput1(hospitalNum.getText().toString(), uniqueId.getText().toString(), dateRegistration.getText().toString())) {
                        if (new PatientDAO(getApplicationContext()).findCheckIfPatientExistByHtsId(Long.parseLong(htsId))) {
                            FancyToast.makeText(getApplicationContext(), "Patient already exist", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        } else {
                            Patient patient = new Patient();
                            Facility2 facility2 = new Facility2();
                            facility2.setFacilityId(Long.parseLong(facilityId));
                            patient.setFacility(facility2);
                            patient.setHospitalNum(hospitalNum.getText().toString());
                            session.createHospitalNumber(hospitalNum.getText().toString(), surname.getText().toString() + "   " + otherNames.getText().toString(), uniqueId.getText().toString());
                            patient.setUniqueId(uniqueId.getText().toString());
                            patient.setSurname(surname.getText().toString());
                            patient.setOtherNames(otherNames.getText().toString());
                            patient.setGender(gender.getText().toString());
                            patient.setDateBirth(dateBirth.getText().toString());
                            patient.setAge(Integer.parseInt(age.getText().toString()));
                            patient.setMaritalStatus(maritalStatus.getText().toString());
                            patient.setEducation(education.getSelectedItem().toString());
                            patient.setOccupation(occupation.getSelectedItem().toString());
                            patient.setAddress(address.getText().toString());
                            patient.setPhone(phone.getText().toString());
                            patient.setHtsId(Long.parseLong(htsId));
                            patient.setState(state.getText().toString());
                            patient.setLga(lga.getText().toString());
                            patient.setNextKin(nextKinName.getText().toString());
                            patient.setAddressKin(addressKin.getText().toString());
                            patient.setEducation(education.getSelectedItem().toString());
                            patient.setOccupation(occupation.getSelectedItem().toString());
                            patient.setPhoneKin(phoneKin.getText().toString());
                            patient.setRelationKin(relationKin.getSelectedItem().toString());
                            patient.setEntryPoint(entryPoint.getSelectedItem().toString());
                            patient.setStatusRegistration(statusRegistration.getSelectedItem().toString());
                            patient.setTargetGroup("");
                            patient.setDeviceconfigId(Long.parseLong(deviceconfigId));
                            patient.setDateConfirmedHiv(dateConfirmedHiv.getText().toString());
                            new HtsDAO(getApplicationContext()).updateDateRegistration(dateRegistration.getText().toString(), htsId);
                            patient.setDateRegistration(dateRegistration.getText().toString());
                            patient.setTbStatus(tbStatus.getSelectedItem().toString());
                            if (pregnancyStatus.getSelectedItem().toString().equals("Pregnant")) {
                                patient.setPregnant(1);
                                patient.setBreastfeeding(0);
                            } else if (pregnancyStatus.getSelectedItem().toString().equals("Breastfeeding")) {
                                patient.setPregnant(0);
                                patient.setBreastfeeding(1);
                            } else if (pregnancyStatus.getSelectedItem().toString().equals("Not Pregnant")) {
                                patient.setPregnant(0);
                                patient.setBreastfeeding(0);
                            } else {
                                patient.setPregnant(0);
                                patient.setBreastfeeding(0);
                            }
                            patient.setUserId(0);//see oga
                            new PatientDAO(getApplicationContext()).insertPatient(patient);
                            FancyToast.makeText(getApplicationContext(), "Patient saved successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        }
                    }

                } catch (Exception E) {

                }
            }


        });

    }

    private boolean validateInput1(String hospitalNum2, String uniqueId1, String dateVist2reg) {
        if (hospitalNum2.isEmpty()) {
            hospitalNum.setError("Enter Hospital number");
            FancyToast.makeText(getApplicationContext(), "Enter Hospital number", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            return false;

        }

        if (uniqueId1.isEmpty()) {
            uniqueId.setError("Enter UniqueId");
            FancyToast.makeText(getApplicationContext(), "Enter UniqueId", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            return false;

        }
        if (dateVist2reg.isEmpty()) {
            uniqueId.setError("Enter Date");
            FancyToast.makeText(getApplicationContext(), "Enter Date", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            return false;

        }

        return true;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_commencemt2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.commencement) {
            Intent intent2 = new Intent(getApplicationContext(), ArtCommencement.class);
            startActivity(intent2);
        }

        return super.onOptionsItemSelected(item);
    }


    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateRegistration.setText(sdf.format(myCalendar.getTime()));

    }

}
