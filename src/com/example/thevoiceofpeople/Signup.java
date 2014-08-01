package com.example.thevoiceofpeople;


import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
 
import android.widget.Toast;

public class Signup extends Activity{
	
	EditText username,email,password,confirm_password;
	String usernameV ,passwordV ,confirm_passwordV,emailV;
	Button signup,reset;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("point 1");
		setContentView(R.layout.signup);
		System.out.println("point 2");
		username = (EditText) findViewById(R.id.username);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		confirm_password = (EditText) findViewById(R.id.confirm_password);
		
		System.out.println("point 3");
		if(isConnected()){ 
           System.out.println("You are conncted");
		}
		else{
			System.out.println("You are NOT conncted");
        }
	
			
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	public void reset(View v) { 
		System.out.println("RESET");
		username.setText("");
		email.setText("");
		password.setText("");
		confirm_password.setText("");
	}
	public void signup(View v) {
		// TODO Auto-generated method stub
		boolean all_fields_are_ok = true;
		
		emailV= email.getText().toString();
		usernameV=username.getText().toString();
		passwordV=password.getText().toString();
		confirm_passwordV=confirm_password.getText().toString();
		
		
		if(emailV == null || usernameV.equals("") || passwordV.equals("") || confirm_passwordV.equals("") ||  !isValidEmail((CharSequence)emailV) || !passwordV.equals(confirm_passwordV) )
			all_fields_are_ok = false;
		
		if (all_fields_are_ok ) {
			HttpAsyncTask task=new HttpAsyncTask();
        	//task.execute("http://192.168.1.65:8081/TheVoiceOfPeople1/signup");
			task.execute("http://83.212.97.66:8080/TheVoiceOfPeople1/signup");
        }
        else {
        	Toast.makeText(getBaseContext(),"All fields are required",Toast.LENGTH_SHORT).show();
        }
        
	}
	
	
	public class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
 
        	 
            ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("username", usernameV));
            postParameters.add(new BasicNameValuePair("email",emailV));
            postParameters.add(new BasicNameValuePair("password", passwordV));
            postParameters.add(new BasicNameValuePair("country", ""));
 
            return POST(urls[0],postParameters);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	System.out.println(result);
        	if (result.equals("SUCCESSFULLY_REGISTERED")){
        		Toast.makeText(getBaseContext(), "Success signup,please log in!", Toast.LENGTH_LONG).show();
        		
        	}
        	else if(result.equals("ALREADY_REGISTERED")){
        		Toast.makeText(getBaseContext(), "You have already a account!", Toast.LENGTH_LONG).show();
        	}
        	else if(result.equals("ERROR")){
        		Toast.makeText(getBaseContext(), "An error occured ", Toast.LENGTH_LONG).show();
        	}
            
       }
    }
	public static String POST(String url ,ArrayList<NameValuePair> accountParameters){
        //InputStream inputStream = null;
        String result = "";
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

