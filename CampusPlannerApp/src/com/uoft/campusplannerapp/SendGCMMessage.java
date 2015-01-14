package com.uoft.campusplannerapp;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class SendGCMMessage {
	AtomicInteger msgId = new AtomicInteger();
	public void SendToCloud(final GoogleCloudMessaging gcm, final String ProjectNumber, final Bundle data) {
		new AsyncTask<Void, Void, String>() {
	        @Override
	        protected String doInBackground(Void... params) {
	            String msg = "";
	            try {
	                String id = Integer.toString(msgId.incrementAndGet());
	                gcm.send(ProjectNumber + "@gcm.googleapis.com", id, data);
	                msg = "Sent message";
	            } catch (IOException ex) {
	                msg = "Error :" + ex.getMessage();
	            }
	            return msg;
	        }
	
	        @Override
	        protected void onPostExecute(String msg) {
	            
	        }
		}.execute(null, null, null);
	}
}
