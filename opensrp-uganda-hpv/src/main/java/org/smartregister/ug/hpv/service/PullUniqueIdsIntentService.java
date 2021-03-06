package org.smartregister.ug.hpv.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.domain.Response;
import org.smartregister.service.HTTPAgent;
import org.smartregister.ug.hpv.application.HpvApplication;
import org.smartregister.ug.hpv.exception.PullUniqueIdsException;
import org.smartregister.ug.hpv.receiver.AlarmReceiver;
import org.smartregister.ug.hpv.repository.UniqueIdRepository;
import org.smartregister.ug.hpv.util.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by onamacuser on 18/03/2016.
 */
public class PullUniqueIdsIntentService extends IntentService {
    public static final String ID_URL = "/uniqueids/get";
    public static final String IDENTIFIERS = "identifiers";
    private static final String TAG = PullUniqueIdsIntentService.class.getCanonicalName();
    private UniqueIdRepository uniqueIdRepo;


    public PullUniqueIdsIntentService() {
        super("PullUniqueOpenMRSUniqueIdsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            int numberToGenerate;
            if (uniqueIdRepo.countUnUsedIds() == 0) { // first time pull no ids at all
                numberToGenerate = Constants.OPENMRS_UNIQUE_ID_INITIAL_BATCH_SIZE;
            } else if (uniqueIdRepo.countUnUsedIds() <= 250) { //maintain a minimum of 250 else skip this pull
                numberToGenerate = Constants.OPENMRS_UNIQUE_ID_BATCH_SIZE;
            } else {
                return;
            }
            JSONObject ids = fetchOpenMRSIds(Constants.OPENMRS_UNIQUE_ID_SOURCE, numberToGenerate);
            if (ids != null && ids.has(IDENTIFIERS)) {
                parseResponse(ids);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            AlarmReceiver.completeWakefulIntent(intent);
        }
    }

    private JSONObject fetchOpenMRSIds(int source, int numberToGenerate) throws Exception {
        HTTPAgent httpAgent = HpvApplication.getInstance().getContext().getHttpAgent();
        String baseUrl = HpvApplication.getInstance().getContext().
                configuration().dristhiBaseURL();
        String endString = "/";
        if (baseUrl.endsWith(endString)) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(endString));
        }

        String url = baseUrl + ID_URL + "?source=" + source + "&numberToGenerate=" + numberToGenerate;
        Log.i(PullUniqueIdsIntentService.class.getName(), "URL: " + url);

        if (httpAgent == null) {
            throw new PullUniqueIdsException(ID_URL + " http agent is null");
        }

        Response resp = httpAgent.fetch(url);
        if (resp.isFailure()) {
            throw new PullUniqueIdsException(ID_URL + " not returned data");
        }

        return new JSONObject((String) resp.payload());
    }

    private void parseResponse(JSONObject idsFromOMRS) throws Exception {
        JSONArray jsonArray = idsFromOMRS.getJSONArray(IDENTIFIERS);
        if (jsonArray != null && jsonArray.length() > 0) {
            List<String> ids = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                ids.add(jsonArray.getString(i));
            }
            uniqueIdRepo.bulkInserOpenmrsIds(ids);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        uniqueIdRepo = HpvApplication.getInstance().getUniqueIdRepository();
        return super.onStartCommand(intent, flags, startId);
    }
}
