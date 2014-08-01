package com.example.thevoiceofpeople;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;


public class Settings extends Activity {
	
	private   SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	public void profile_settings(View v){
			Intent profile_settings=new Intent("com.example.thevoiceofpeople.PROFILE_SETTINGS");
			startActivity(profile_settings);
	}
	public void logout(View v){
		GetXMLTask task = new GetXMLTask();
		
		//task.execute("http://192.168.1.65:8081/TheVoiceOfPeople1/log_out");
		task.execute("http://83.212.97.66:8080/TheVoiceOfPeople1/log_out");
		
	}
	//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	public void worldActivity(View v){
		
		Intent world=new Intent("com.example.thevoiceofpeople.WORLD");
		startActivity(world);
	}
	public void profileActivity(View v){
		
		Intent profile=new Intent("com.example.thevoiceofpeople.PROFILE");
		startActivity(profile);
	}
	public void settingsActivity(View v){
		
		Intent settings=new Intent("com.example.thevoiceofpeople.SETTINGS");
		startActivity(settings);
	}
	//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	private class GetXMLTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String output = null;
            
            for (String url : urls) {
                output = getOutputFromUrl(url);
            }
            return output;
        }
 
        private String getOutputFromUrl(String url) {
            StringBuffer output = new StringBuffer("");
            try {
                InputStream stream = getHttpConnection(url);
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(stream));
                String s = "";
                while ((s = buffer.readLine()) != null)
                    output.append(s);
            } catch (Exception e1) {
            	 e1.printStackTrace();
            }
            return output.toString();
        }
 
        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)throws IOException, URISyntaxException {
        	URI url = new URI(urlString);
           	HttpClient httpclient = new DefaultHttpClient();
        	HttpGet httpget = new HttpGet(url);
        	HttpResponse response;
        	String COOKIE_VALUE=prefs.getString("sessionID", "");
           
            InputStream instream=null;
            httpget.setHeader("Cookie",  "JSESSIONID="+COOKIE_VALUE); //Here i am sending the Cookie session ID 
        	try { 
        		response = httpclient.execute(httpget); 
        		HttpEntity entity = response.getEntity(); 
        		if (entity != null) { 
        			instream = entity.getContent();
      
        		} 
        	} catch (ClientProtocolException e) { 
        		e.printStackTrace(); 
        	} catch (IOException e) { 
        		e.printStackTrace(); 
        	}

         return instream;
        }
 
        @Override
        protected void onPostExecute(String output) {
        	if (output.equals("SUCCESSFULLY_LOGOUT")) {
        		Intent intent = new Intent(Settings.this, Login.class);
        	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
        	    startActivity(intent);
        	}
        	else
        		Toast.makeText(getBaseContext(), "There was an error", Toast.LENGTH_LONG).show();
        	
        	
        	
    			
        }
    }

	
	
	
}
