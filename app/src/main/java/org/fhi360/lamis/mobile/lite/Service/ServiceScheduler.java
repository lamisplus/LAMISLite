package org.fhi360.lamis.mobile.lite.Service;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import static org.fhi360.lamis.mobile.lite.Utils.Constant.ACTION_UPDATE;

/**
 * Created by aalozie on 10/22/2017.
 */

public class ServiceScheduler {
    private Context context;
    private PendingIntent pendingIntentDefaulter;
    private AlarmManager alarmManagerGps;
    private PendingIntent pendingIntentAppointment;
    private AlarmManager alarmManagerAppointment;
    private PendingIntent pendingIntentSync;
    private AlarmManager alarmManagerSync;

    public ServiceScheduler(Context context) {
        this.context = context;
    }

    public void start() {
        updateService();
      //  gpsAlarm();
    }


    private void updateService() {
        //Create a pending intent for the SyncNotificationService
        Intent intent = new Intent(context, IntentServiceHandler.class);
        intent.setAction(ACTION_UPDATE);
        pendingIntentSync = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Create an Alarm Manager to trigger every 3 hours
        long currentTime = System.currentTimeMillis();                  // Long time = new GregorianCalendar().getTimeInMillis()+24*60*60*1000;
        long triggerFirst = currentTime + 1;
        long repeatInterval = 1 * 60 * 60;                  // Interval in milliseconds (3 hours) between subsequent repeats of the alarm
        //long triggerFirst = currentTime + 60 * 1000;
        //long repeatInterval = 60 * 1000;
        alarmManagerSync = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManagerSync.setRepeating(AlarmManager.RTC_WAKEUP, triggerFirst, repeatInterval, pendingIntentSync);
    }




}
