package com.example.thevoiceofpeople;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	Button login,signup;
	EditText email,password;
	String emailV,passwordV;
	public static final String PREFS_NAME = "SessionID";		// for storing the cooking value retreiving from server
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		login=(Button) findViewById(R.id.login_button);
		signup=(Button) findViewById(R.id.signup_button);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		
		if(isConnected()){ 
            System.out.println("You are conncted");
        }
        else{
        	System.out.println("You are NOT conncted");
        }
		
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean all_fields_are_ok = true;
				
				emailV= email.getText().toString();
				passwordV=password.getText().toString();
				if (emailV == null || passwordV.equals("") || !isValidEmail((CharSequence)emailV)) {
					all_fields_are_ok=false;
				}
				if (all_fields_are_ok ) {
		        	//new HttpAsyncTask().execute("http://192.168.1.65:8081/TheVoiceOfPeople1/login");
					new HttpAsyncTask().execute("http://83.212.97.66:8080/TheVoiceOfPeople1/login");
		        }
				else {
		        	Toast.makeText(getBaseContext(),"All fields are required",Toast.LENGTH_SHORT).show();
		        }
				
			}
		});
        signup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent signup=new Intent("com.example.thevoiceofpeople.SIGNUP");
				startActivity(signup);
			}
		});
        
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//finish();
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
 
        	 
            ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("email",emailV));
            postParameters.add(new BasicNameValuePair("password", passwordV));
 
            return POST(urls[0],postParameters);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        	System.out.println(result);
            if (result.equals("ACCOUNT_DOESNT_EXIST")){
            	Toast.makeText(getBaseContext(), "You must sign up", Toast.LENGTH_LONG).show();
            	
            }
            else if(result.equals("WRONG_PASSWORD"))
            {
            	Toast.makeText(getBaseContext(), "Retype your password", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("SUCCESSFULLY_LOGIN")){
            	
            	
            	Intent profile=new Intent("com.example.thevoiceofpeople.PROFILE");
				startActivity(profile);
            }
            
       }
    }
	public String POST(String url ,ArrayList<NameValuePair> accountParameters){
        //InputStream inputStream = null;
        String result = "3";
        

        try {
 
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            
            httpPost.setEntity(new UrlEncodedFormEntity(accountParameters));
 
            HttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity httpentity=httpResponse.getEntity();
            InputStream is=httpentity.getContent();
            result=convertInputStreamToString(is);
            
            List<Cookie> cookies = ((AbstractHttpClient) httpclient).getCookieStore().getCookies(); 	//  Storing cookies
            
    		//System.out.println("- " + cookies.get(0).getValue()); 	// Cookie found 
    		String cookie = cookies.get(0).getValue();
    		
    		
    		//SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
    		//editor.putString("sessionID",cookie);
    		//editor.commit();
    		
    		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
    		SharedPreferences.Editor editor = settings.edit(); 
    		editor.putString("sessionID", cookie);
    		editor.commit();
    		
    		
    		System.out.println("********");
    		//SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    		String restoredText = prefs.getString("sessionID", "");
    		System.out.println(restoredText);
    		System.out.println("********");
    		
 
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
 
        // 11. return result
        return result;	
    }
	
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
	        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
	        String line = "";
	        String result = "";
	        while((line = bufferedReader.readLine()) != null)
	            result += line;
	 
	        inputStream.close();
	        return result;
	   
	 }   
	

	public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                return true;
            else
                return false;    
    }
	
	

	
	public final static boolean isValidEmail(CharSequence target) {
	   
		return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	   
	}
	
       
		
	
 
}

