package com.hmlafrugalitas.flavorcontacts.views;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hmlafrugalitas.flavorcontacts.Enums.FlavorContactEnums;
import com.hmlafrugalitas.flavorcontacts.R;
import com.hmlafrugalitas.flavorcontacts.Tools.Utilities;
import com.hmlafrugalitas.flavorcontacts.model.FlavorContact;

import java.util.HashMap;
import java.util.Objects;


/**
 * Created by mlaouhih on 21/03/14.
 */
public class FlavorContactsConfigureActivity extends Activity {

    private ListView lvGroups;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private String groupId = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.flavor_contacts_configure_activity);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        final Context context = FlavorContactsConfigureActivity.this;
        lvGroups = (ListView) findViewById(R.id.lvGroups);
        lvGroups.setAdapter(Utilities.getContactGroupList(getApplicationContext()));
        lvGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,String> selectedGroup = (HashMap<String,String>)(lvGroups.getItemAtPosition(i));
                updateWidget(selectedGroup);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                FlavorContactsWidget.updateWidget(context, appWidgetManager, mAppWidgetId);
            }
        });
    }

    private void updateWidget(HashMap<String, String> selectedGroup) {
        savePref(getApplicationContext(),mAppWidgetId, FlavorContactEnums.GROUP_ID,selectedGroup.get("groupId"));
        // It is the responsibility of the configuration activity to update the app widget
        //AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }


    // Write the prefix to the SharedPreferences object for this widget
    static void savePref(Context context, int appWidgetId, String pref, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(FlavorContactEnums.PREFS_NAME, appWidgetId).edit();
        prefs.putString(pref + "_" + appWidgetId, text);
        prefs.commit();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static Object loadPref(Context context, int appWidgetId, String pref) {
        SharedPreferences prefs = context.getSharedPreferences(FlavorContactEnums.PREFS_NAME, appWidgetId);
        Object titleValue = prefs.getString(pref+"_"+ appWidgetId, null);
        return titleValue;
    }

    static void deletePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(FlavorContactEnums.PREFS_NAME, appWidgetId).edit();
        prefs.remove(FlavorContactEnums.PREFS_NAME + appWidgetId);
        prefs.commit();
    }
}