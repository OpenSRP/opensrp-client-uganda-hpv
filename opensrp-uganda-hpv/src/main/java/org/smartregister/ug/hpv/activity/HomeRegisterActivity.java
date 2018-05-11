package org.smartregister.ug.hpv.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import org.smartregister.ug.hpv.fragment.HomeRegisterFragment;

import java.util.Arrays;
import java.util.List;

import static org.smartregister.ug.hpv.util.Constants.VIEW_CONFIGS.HOME_REGISTER;

/**
 * Created by ndegwamartin on 14/03/2018.
 */

public class HomeRegisterActivity extends BaseRegisterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    @Override
    public Fragment getRegisterFragment() {
        return new HomeRegisterFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreation() {//Do something on creation

    }

    @Override
    public List<String> getViewIdentifiers() {
        return Arrays.asList(HOME_REGISTER);
    }


}
