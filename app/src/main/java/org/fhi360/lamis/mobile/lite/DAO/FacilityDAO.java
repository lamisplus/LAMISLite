package org.fhi360.lamis.mobile.lite.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.fhi360.lamis.mobile.lite.Db.LAMISLiteDb;
import org.fhi360.lamis.mobile.lite.Model.Facility;
import org.fhi360.lamis.mobile.lite.Utils.Constant;

import java.util.ArrayList;

public class FacilityDAO {
    private Context context;
    private SQLiteOpenHelper lAMISLiteDb;
    public FacilityDAO(Context context) {
        this.context = context;
    }


    public ArrayList getFacilityByLgaId(long lgaIds) {

        ArrayList listFacility = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + Constant.TABLE_FACILITY + " WHERE  lga_id=" + lgaIds + " ORDER BY facility_name ASC";
            lAMISLiteDb = LAMISLiteDb.getInstance(context);
            SQLiteDatabase db = lAMISLiteDb.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Facility facility = new Facility();
                    facility.setFacility_id(cursor.getLong(cursor.getColumnIndex(String.valueOf(Constant.facilityId))));
                    facility.setName(cursor.getString(cursor.getColumnIndex(Constant.facilityName)));
                    facility.setLga_id(cursor.getLong(cursor.getColumnIndex(Constant.lgaId)));
                    facility.setState_id(cursor.getLong(cursor.getColumnIndex(Constant.stateId)));
                    listFacility.add(facility);
                } while (cursor.moveToNext());
            }
            db.close();
            cursor.close();
            // return user list
        } catch (Exception e) {

        }
        return listFacility;
    }


    public Facility getFacilityById(long id) {
        ArrayList listFacility = new ArrayList<>();
        Facility facility = new Facility();
        try {
            String selectQuery = "SELECT * FROM " + Constant.TABLE_FACILITY + " WHERE  facility_id=" + id + " ORDER BY facility_name ASC";
            lAMISLiteDb = LAMISLiteDb.getInstance(context);
            SQLiteDatabase db = lAMISLiteDb.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                facility.setFacility_id(cursor.getLong(cursor.getColumnIndex(String.valueOf(Constant.facilityId))));
                facility.setName(cursor.getString(cursor.getColumnIndex(Constant.facilityName)));
                facility.setLga_id(cursor.getLong(cursor.getColumnIndex(Constant.lgaId)));
                facility.setState_id(cursor.getLong(cursor.getColumnIndex(Constant.stateId)));
            }
            db.close();
            cursor.close();
        } catch (Exception e) {

        }
        return facility;
    }


    public ArrayList getFacility() {
        ArrayList listFacility = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + Constant.TABLE_FACILITY + " ORDER BY facility_name ASC";
            lAMISLiteDb = LAMISLiteDb.getInstance(context);
            SQLiteDatabase db = lAMISLiteDb.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Facility facility = new Facility();
                    facility.setFacility_id(cursor.getLong(cursor.getColumnIndex(String.valueOf(Constant.facilityId))));
                    facility.setName(cursor.getString(cursor.getColumnIndex(Constant.facilityName)));
                    facility.setLga_id(cursor.getLong(cursor.getColumnIndex(Constant.lgaId)));
                    facility.setState_id(cursor.getLong(cursor.getColumnIndex(Constant.stateId)));
                    listFacility.add(facility);
                } while (cursor.moveToNext());
            }
            db.close();
            cursor.close();
        } catch (Exception e) {

        }
        return listFacility;
    }

    public void saveFacility(Facility facility) {
        try {
            lAMISLiteDb = LAMISLiteDb.getInstance(context);
            SQLiteDatabase db = lAMISLiteDb.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Constant.facilityId, String.valueOf(facility.getFacility_id()));
            values.put(Constant.deviceId, String.valueOf(facility.getDeviceconfig_id()));
            values.put(Constant.facilityName, facility.getName());
            values.put(Constant.stateId, String.valueOf(facility.getState_id()));
            values.put(Constant.lgaId, String.valueOf(facility.getLga_id()));
            db.insert(Constant.TABLE_FACILITY, null, values);
            db.close();
        } catch (Exception e) {

        }

    }

}
