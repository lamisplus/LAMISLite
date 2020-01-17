package org.fhi360.lamis.mobile.lite.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.fhi360.lamis.mobile.lite.Db.LAMISLiteDb;
import org.fhi360.lamis.mobile.lite.Model.Clinic;
import org.fhi360.lamis.mobile.lite.Model.Patient;
import org.fhi360.lamis.mobile.lite.Model.Patient2;
import org.fhi360.lamis.mobile.lite.Utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class ClinicDAO {
    private Context context;
    private SQLiteOpenHelper lAMISLiteDb;
    public ClinicDAO(Context context) {
        this.context = context;
    }



    public void insertClinic(Clinic clinic) {
        try {
            lAMISLiteDb = LAMISLiteDb.getInstance(context);
            SQLiteDatabase db = lAMISLiteDb.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Constant.facilityId, clinic.getFacilityId());
            values.put(Constant.deviceId, clinic.getDeviceconfigId());
            values.put(Constant.patientId, clinic.getPatient().getPatientId());
            values.put(Constant.dateVisit, clinic.getDateVisit());
            values.put(Constant.clinicStage, clinic.getClinicStage());
            values.put(Constant.funcStatus, clinic.getFuncStatus());
            values.put(Constant.tbStatus, clinic.getTbStatus());
            values.put(Constant.viralLoad, clinic.getViralLoad());
            values.put(Constant.bodyWeight, clinic.getBodyWeight());
            values.put(Constant.regimenType, clinic.getRegimentype());
            values.put(Constant.regimen, clinic.getRegimen());
            values.put(Constant.height, clinic.getHeight());
            values.put(Constant.waist, clinic.getWaist());
            values.put(Constant.bp, clinic.getBp());//month/day
            values.put(Constant.nextAppointment, clinic.getNextAppointment());
            values.put(Constant.notes, clinic.getNotes());
            values.put(Constant.userId, clinic.getUserId());
            values.put(Constant.tbStatus, clinic.getTbStatus());
            values.put(Constant.pregnant, clinic.getPregnant());
            values.put(Constant.timeStamp, clinic.getTimeStamp());
            values.put(Constant.uploaded, clinic.getUploaded());
            values.put(Constant.timeUploaded, clinic.getTimeUploaded());
            db.insert(Constant.TABLE_CLINIC, null, values);
            db.close();

        } catch (Exception e) {

        }
    }





    public boolean checkIfClinicExist(Clinic clinic, long patientIdS) {
        boolean bol = false;
        try {
            String query = "Select * FROM " + Constant.TABLE_CLINIC + " WHERE " + Constant.patientId + " = " + patientIdS;
            lAMISLiteDb = LAMISLiteDb.getInstance(context);
            SQLiteDatabase db = lAMISLiteDb.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put(Constant.facilityId, clinic.getFacilityId());
                values.put(Constant.patientId, clinic.getPatient().getPatientId());
                values.put(Constant.dateVisit, clinic.getDateVisit());
                values.put(Constant.clinicStage, clinic.getClinicStage());
                values.put(Constant.funcStatus, clinic.getFuncStatus());
                values.put(Constant.tbStatus, clinic.getTbStatus());
                values.put(Constant.bodyWeight, clinic.getBodyWeight());
                values.put(Constant.regimenType, clinic.getRegimentype());
                values.put(Constant.regimen, clinic.getRegimen());
                values.put(Constant.height, clinic.getHeight());
                values.put(Constant.waist, clinic.getWaist());
                values.put(Constant.bp, clinic.getBp());//month/day
                values.put(Constant.nextAppointment, clinic.getNextAppointment());
                values.put(Constant.tbStatus, clinic.getTbStatus());
                db.update(Constant.TABLE_CLINIC, values, "patient_id = ?", new String[]{String.valueOf(patientIdS)});
                bol = true;
            } else {
                bol = false;
            }
            cursor.close();
        } catch (Exception e) {

        }
        return bol;
    }

    public boolean checkIfClinicExist2(long patientIdS) {
        boolean bol = false;
        try {
            String query = "Select * FROM " + Constant.TABLE_CLINIC + " WHERE " + Constant.patientId + " = " + patientIdS;
            lAMISLiteDb = LAMISLiteDb.getInstance(context);
            SQLiteDatabase db = lAMISLiteDb.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                bol = true;
            }
            cursor.close();
        } catch (Exception e) {

        }

        return bol;
    }


    public Clinic getAllClinicByPatientId(long patientIdS) {
        String query = "Select * FROM " + Constant.TABLE_CLINIC + " WHERE " + Constant.patientId + " = " + patientIdS;
        lAMISLiteDb = LAMISLiteDb.getInstance(context);
        SQLiteDatabase db = lAMISLiteDb.getWritableDatabase();
        Clinic clinic = new Clinic();
        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                Patient2 patient1 = new Patient2();
                patient1 = new PatientDAO(context).findPatientById(cursor.getLong(cursor.getColumnIndex(Constant.patientId)));
                clinic.setPatient(patient1);
                clinic.setDeviceconfigId(cursor.getLong(cursor.getColumnIndex(Constant.deviceId)));
                clinic.setClinicId(cursor.getLong(cursor.getColumnIndex(Constant.clinicId)));
                clinic.setBp(cursor.getString(cursor.getColumnIndex(Constant.bp)));
                clinic.setDateVisit(cursor.getString(cursor.getColumnIndex(Constant.dateVisit)));
                clinic.setHeight(cursor.getDouble(cursor.getColumnIndex(Constant.height)));
                clinic.setBodyWeight(cursor.getDouble(cursor.getColumnIndex(Constant.bodyWeight)));
                clinic.setDateVisit(cursor.getString(cursor.getColumnIndex(Constant.dateVisit)));
                clinic.setTbStatus(cursor.getString(cursor.getColumnIndex(Constant.tbStatus)));
                clinic.setFuncStatus(cursor.getString(cursor.getColumnIndex(Constant.funcStatus)));
                clinic.setRegimen(cursor.getString(cursor.getColumnIndex(Constant.regimen)));
                clinic.setRegimentype(cursor.getString(cursor.getColumnIndex(Constant.regimenType)));
                clinic.setNextAppointment(cursor.getString(cursor.getColumnIndex(Constant.nextAppointment)));
            }
            cursor.close();
        } catch (Exception e) {

        }
        return clinic;
    }


    public List<Clinic> getAllClinic() {
        List<Clinic> clinics = new ArrayList<>();
        try {
            String query = "Select * FROM " + Constant.TABLE_CLINIC;
            lAMISLiteDb = LAMISLiteDb.getInstance(context);
            SQLiteDatabase db = lAMISLiteDb.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Patient2 patient1 = new Patient2();
                    Clinic clinic = new Clinic();
                    patient1 = new PatientDAO(context).findPatientById(cursor.getLong(cursor.getColumnIndex(Constant.patientId)));
                    clinic.setPatient(patient1);
                    clinic.setDeviceconfigId(cursor.getLong(cursor.getColumnIndex(Constant.deviceId)));
                    clinic.setBp(cursor.getString(cursor.getColumnIndex(Constant.bp)));
                    clinic.setFacilityId(cursor.getLong(cursor.getColumnIndex(Constant.facilityId)));
                    clinic.setHeight(cursor.getDouble(cursor.getColumnIndex(Constant.height)));
                    clinic.setBodyWeight(cursor.getDouble(cursor.getColumnIndex(Constant.bodyWeight)));
                    clinic.setDateVisit(cursor.getString(cursor.getColumnIndex(Constant.dateVisit)));
                    clinic.setTbStatus(cursor.getString(cursor.getColumnIndex(Constant.tbStatus)));
                    clinic.setFuncStatus(cursor.getString(cursor.getColumnIndex(Constant.funcStatus)));
                    clinic.setRegimen(cursor.getString(cursor.getColumnIndex(Constant.regimen)));
                    clinic.setRegimentype(cursor.getString(cursor.getColumnIndex(Constant.regimenType)));
                    clinic.setNextAppointment(cursor.getString(cursor.getColumnIndex(Constant.nextAppointment)));
                    clinics.add(clinic);
                } while ((cursor.moveToNext()));
            }
            cursor.close();
            db.close();
        } catch (Exception e) {

        }
        return clinics;
    }





}
