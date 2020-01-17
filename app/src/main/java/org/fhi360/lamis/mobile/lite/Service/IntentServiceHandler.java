package org.fhi360.lamis.mobile.lite.Service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import org.fhi360.lamis.mobile.lite.DAO.HtsDAO;
import org.fhi360.lamis.mobile.lite.Db.LAMISLiteDb;
import org.fhi360.lamis.mobile.lite.Utils.PrefManager;

import java.util.HashMap;

import static org.fhi360.lamis.mobile.lite.Utils.Constant.*;


public class IntentServiceHandler extends IntentService {
    String gpsStatus = "";

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    HashMap<String, String> status;
    String longitude;
    String latitude;
    String deviceconfigId;

    public IntentServiceHandler() {
        super("IntentServiceHandler");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE.equals(action)) {
                HashMap<String, String> user = new PrefManager(this).getHtsDetails();
                status = new PrefManager(this).getClientCodeStatus();
                if (status.get("status") != null) {
                } else {
                    deviceconfigId = user.get("deviceconfig_id");
                    new HtsDAO(this).updateClientCodeFromHtsByDeviceConfigId(deviceconfigId);
                    new PrefManager(this).setClientCodeStatus();
                    Integer lastNumber = new HtsDAO(this).getAutGeneratedClientCode();
                    new PrefManager(this).setLastHtsSerialDigit(lastNumber);
                }


            }


        }

    }


}
