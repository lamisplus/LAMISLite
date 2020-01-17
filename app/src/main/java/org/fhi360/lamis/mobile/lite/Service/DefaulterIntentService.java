package org.fhi360.lamis.mobile.lite.Service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.fhi360.lamis.mobile.lite.Activities.ClientTracking;
import org.fhi360.lamis.mobile.lite.Activities.TableViewAdapter;
import org.fhi360.lamis.mobile.lite.DAO.DefaultersDAO;
import org.fhi360.lamis.mobile.lite.Db.LAMISLiteDb;
import org.fhi360.lamis.mobile.lite.Model.Defaulter;
import org.fhi360.lamis.mobile.lite.Model.ListDefaulters;
import org.fhi360.lamis.mobile.lite.Utils.PrefManager;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.fhi360.lamis.mobile.lite.Utils.Constant.ACTION_UPDATE;

public class DefaulterIntentService extends IntentService {
    public DefaulterIntentService() {
        super("IntentServiceHandler");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE.equals(action)) {
                System.out.println("SUCCESS");
                getDefualters();

            }

        }
    }

    public void getDefualters() {
        APIService APIService = new APIService();
        ClientAPI clientAPI = APIService.createService(ClientAPI.class);
        HashMap<String, String> user1 =new PrefManager(this).getHtsDetails();
        String facilityId = user1.get("facilityId");
        Call<ListDefaulters> objectCall = clientAPI.clientTracking(Long.parseLong(facilityId));
        objectCall.enqueue(new Callback<ListDefaulters>() {
            @Override
            public void onResponse(Call<ListDefaulters> call, Response<ListDefaulters> response) {
                if (response.code() == 200) {
                    ListDefaulters dataObject = response.body();
                    List<Defaulter> defaulters = dataObject.getDefaulter();
                    for (Defaulter defaulter : defaulters) {
                        new DefaultersDAO(getApplicationContext()).saveDefaulters(defaulter);
                    }

                }

            }

            @Override
            public void onFailure(Call<ListDefaulters> call, Throwable t) {
                t.printStackTrace();

            }


        });

    }


}