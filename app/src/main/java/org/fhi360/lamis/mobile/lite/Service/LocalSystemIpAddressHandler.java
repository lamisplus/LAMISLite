package org.fhi360.lamis.mobile.lite.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import static org.fhi360.lamis.mobile.lite.Utils.Constant.ACTION_UPDATE;

public class LocalSystemIpAddressHandler {
    private Context context;
    private PendingIntent pendingIntentSync;
    private AlarmManager alarmManagerSync;
    public LocalSystemIpAddressHandler(Context context) {
        this.context = context;
    }
    public void start() {
        updateService();
    }


    private void updateService() {
        //Create a pending intent for the SyncNotificationService
        Intent intent = new Intent(context, LocalSystemIpAddressServices.class);
        intent.setAction(ACTION_UPDATE);
        pendingIntentSync = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long currentTime = System.currentTimeMillis();                  // Long time = new GregorianCalendar().getTimeInMillis()+24*60*60*1000;
        long triggerFirst = currentTime + 1;
        long repeatInterval = 1000 * 60 * 60 * 24 * 1;                  // Interval in milliseconds (3 hours) between subsequent repeats of the alarm
        alarmManagerSync = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManagerSync.setRepeating(AlarmManager.RTC_WAKEUP, triggerFirst, repeatInterval, pendingIntentSync);
    }


}