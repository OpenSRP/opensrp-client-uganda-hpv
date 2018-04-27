package org.smartregister.ug.hpv.sync;

import android.content.Context;
import android.content.Intent;

import org.smartregister.configurableviews.service.PullConfigurableViewsIntentService;

import static org.smartregister.util.Log.logInfo;

/**
 * Created by ndegwamartin on 15/03/2018.
 */

public class UserConfigurableViewsSyncTask {

    private final Context context;

    public UserConfigurableViewsSyncTask(Context context) {
        this.context = context;
    }

    public void syncFromServer() {
        logInfo("starting syncing From Server");
        startPullConfigurableViewsIntentService();
    }

    private void startPullConfigurableViewsIntentService() {
        Intent intent = new Intent(context, PullConfigurableViewsIntentService.class);
        context.startService(intent);
    }
}