package com.uoft.campusplannerapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.IOException;
import java.util.Map;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "124761461971";
    SharedPreferences pref;
    String version;
    
    private static void create_alert(Context ctx, String msg) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
    	builder.setMessage(msg)
    	       .setCancelable(false)
    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                //do things
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = this.getSharedPreferences("RegIds",MODE_PRIVATE);
        String oldRegId = pref.getString("regid",null);
        String oldVersionId = pref.getString("version", null);
        version = getApplicationVersion();
        String msg = "RegId is" + oldRegId + "version is" + oldVersionId + version;
        create_alert(this, msg);
        if ((oldRegId == null) || !(oldVersionId.equals(version))) {
        	register_gcm();
        } else {
        	regid = oldRegId; 
        	TextView tv = (TextView) findViewById(R.id.register);
            tv.setText("Application already registered\n");
        }
        
    }

    private String getApplicationVersion() {
    	try {
            PackageInfo packageInfo = getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
  
    }
    private void register_gcm() {
    	new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);

                	SharedPreferences.Editor edit = pref.edit();
                	edit.putString("regid", regid);
                	edit.putString("version", version);
                	msg = "RegId is" + regid + "version is" + version;
                	edit.commit();
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM",  msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            	TextView tv = (TextView) findViewById(R.id.register);
                tv.setText(msg + "\n");
            }
        }.execute(null, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
