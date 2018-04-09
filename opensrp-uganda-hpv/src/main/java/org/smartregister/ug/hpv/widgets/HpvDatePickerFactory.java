package org.smartregister.ug.hpv.widgets;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.widgets.DatePickerFactory;

import org.json.JSONObject;
import org.smartregister.ug.hpv.watchers.LookUpTextWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ndegwamartin on 19/03/2018.
 */
public class HpvDatePickerFactory extends DatePickerFactory {

    @Override
    public void attachJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, MaterialEditText editText, TextView duration) {
        super.attachJson(stepName, context, formFragment, jsonObject, editText, duration);

        try {
            if (jsonObject.has("look_up") && jsonObject.get("look_up").toString().equalsIgnoreCase(Boolean.TRUE.toString())) {

                String entityId = jsonObject.getString("entity_id");

                Map<String, List<View>> lookupMap = formFragment.getLookUpMap();
                List<View> lookUpViews = new ArrayList<>();
                if (lookupMap.containsKey(entityId)) {
                    lookUpViews = lookupMap.get(entityId);
                }

                if (!lookUpViews.contains(editText)) {
                    lookUpViews.add(editText);
                }

                lookupMap.put(entityId, lookUpViews);

                editText.addTextChangedListener(new LookUpTextWatcher(formFragment, editText, entityId));
                editText.setTag(com.vijay.jsonwizard.R.id.after_look_up, false);
            }


        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString(), e);
        }
    }
}
