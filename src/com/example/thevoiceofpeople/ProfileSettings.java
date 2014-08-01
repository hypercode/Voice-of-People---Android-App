package com.example.thevoiceofpeople;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class ProfileSettings extends Activity{
	
	private EditText location;
	private LinearLayout interests;
    private ArrayList<CheckBox> interestsArray;
    public String locationV=null;
    private   SharedPreferences prefs;
    
    private GIFView gifLoader;
    private LinearLayout container;
    
    public static final String PREFS_NAME1 = "world_interest";
	public static final String PREFS_NAME2 = "politics_interest";
	public static final String PREFS_NAME4 = "tech_interest";
	public static final String PREFS_NAME5 = "sports_interest";
	public static final String PREFS_NAME6 = "other_interest";
	
    
    //private CheckBox cb[]=new CheckBox[10]; 
    //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_settings);
		gifLoader=(GIFView) findViewById(R.id.GIFLoader);
		container=(LinearLayout)findViewById(R.id.container);
		container.setVisibility(View.GONE);
	    prefs = PreferenceManager.getDefaultSharedPreferences(this);
	  
//		GIFView gifView;
//		try {
//			gifView = new GIFView(this);
//			setContentView(gifView);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		 
		
		location=(EditText)findViewById(R.id.location);
        interests=(LinearLayout)findViewById(R.id.interests);
		interestsArray = new ArrayList<CheckBox>();
		//interest.setText("sports");
		//interests.addView(interest);
		GetXMLTask task = new GetXMLTask();
		task.execute("http://192.168.1.65:8081/TheVoiceOfPeople1/profile_settings");
		    
		
        
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
	protected void createProfileSettingsUI(String xml) {
    
		
		
		gifLoader.setVisibility(View.GONE);
		container.setVisibility(View.VISIBLE);
    	try {
    		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
    		SharedPreferences.Editor editor = settings.edit();
    		
		    XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();

		    InputStream in_s =  new ByteArrayInputStream(xml.getBytes("UTF-8"));
	        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            
            int eventType = parser.getEventType();
            
            String tag;
            
            int i=0;
            String interestTitle="";
            String country ="";
            boolean interestChecked=false; 
            while (eventType != XmlPullParser.END_DOCUMENT){
            	
            	tag = parser.getName();
                if( eventType == XmlPullParser.START_TAG){
                	    System.out.println(tag);
                        if (tag.equals("location"))
                        {
                        	country=parser.nextText();
                            location.setText(country);
                            System.out.println(country);
                            
                        } 
                        else if (tag.equals("interest"))
                        {
                        	System.out.println("interest");
                        	
                        }
                        else if (tag.equals("title")){   
                        	
                        	interestTitle=parser.nextText();
                        	System.out.println(interestTitle);
                        	//LinearLayout interestsLocal=(LinearLayout)findViewById(R.id.interests);
                        	
                        	
                        } 
                        else if (tag.equals("checked")){
                        	i++;
                        	//System.out.println(parser.nextText());
                        	String value=parser.nextText();
                        	if(value.equals("TRUE"))
                        		interestChecked=true;
                        	else
                        		interestChecked=false;
                        	
                        	CheckBox cb1 = new CheckBox(this);
                            cb1.setText(interestTitle);
                            cb1.setChecked(interestChecked);
                            cb1.setId(i+6);
                            interests.addView(cb1);
                            interestsArray.add(cb1);
                        	
                            if (interestTitle.equals("world")) {
                            	if (interestChecked==true) {
                            		editor.putString("world_interest", "yes");
                            		editor.commit();
                            	}
                        		else {
                        			editor.putString("world_interest", "no");
                        			editor.commit();
                        		}
                            }
                            else if (interestTitle.equals("politics")) {
                            	if (interestChecked==true) {
                            		editor.putString("politics_interest", "yes");
                            	 	editor.commit();
                            	}
                        		else {
                        			editor.putString("politics_interest", "no");
                            		editor.commit();
                        		}
                            }
                            else if (interestTitle.equals("technology")) {
                            	if (interestChecked==true) {
                            		editor.putString("tech_interest", "yes");
                            		editor.commit();
                            	}
                        		else {
                        			editor.putString("tech_interest", "no");
                        			 editor.commit();
                        		}
                            }
                            else if (interestTitle.equals("sports")) {
                            	if (interestChecked==true) {
                            		editor.putString("sports_interest", "yes");
                            		editor.commit();
                            	}
                        		else {
                        			editor.putString("sports_interest", "no");
                        			editor.commit();
                        		}
                            }
                            else if (interestTitle.equals("other")) {
                            	if (interestChecked==true){
                            		editor.putString("other_interest", "yes");
                            		 editor.commit();
                            	}
                        		else {
                        			editor.putString("other_interest", "no");
                        			editor.commit();
                        		}
                            }
                            	
                           
                                      	                       	    
                        }
                        
                }
                eventType = parser.next();
                
            }  
            int size=interestsArray.size();
            System.out.println(size);
            

	} catch (XmlPullParserException e) {

		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}
	public void save_preferences(View v)
	{
		HttpAsyncTask task=new HttpAsyncTask();
    	//task.execute("http://192.168.1.65:8081/TheVoiceOfPeople1/profile_settings");
		task.execute("http://83.212.97.66:8080/TheVoiceOfPeople1/profile_settings");
	}
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
        	String locationV = location.getText().toString();
        	
            ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("location", locationV));
            
            for(CheckBox cb:interestsArray){
            	if(cb.isChecked())
            		postParameters.add(new BasicNameValuePair(cb.getText().toString(), "TRUE"));
    	    	else
    	    		postParameters.add(new BasicNameValuePair(cb.getText().toString(), "FALSE"));
    	    }
       
 
            return POST(urls[0],postParameters);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	System.out.println(result);
        	if (result.equals("SUCCESSFULLY_UPDATE")){
        		Toast.makeText(getBaseContext(), "Data saved!", Toast.LENGTH_LONG).show();
        		
        	}
        	else{
        		Toast.makeText(getBaseContext(), "An error occured ", Toast.LENGTH_LONG).show();
        	}
            
       }
    }
	public  String POST(String url ,ArrayList<NameValuePair> accountParameters){
        //InputStream inputStream = null;
        String result = "";
        try {
 
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            
            httpPost.setEntity(new UrlEncodedFormEntity(accountParameters));
            
            String COOKIE_VALUE=prefs.getString("sessionID", "");
            httpPost.setHeader("Cookie",  "JSESSIONID="+COOKIE_VALUE); //Here i am sending the Cookie session ID
            
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
	
	public class GetXMLTask extends AsyncTask<String, Void, String> {
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
        	
//           InputStream stream = null;
             URI url = new URI(urlString);
//            URLConnection connection = url.openConnection();
//           
//            try {
//            	
//                HttpURLConnection httpConnection = (HttpURLConnection) connection;
//                httpConnection.setRequestMethod("GET");
//                
//               // CookieStore cookieStore = (CookieStore) new BasicCookieStore();
//                //HttpContext httpContext = new BasicHttpContext();
//                //httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
//                
//                
//                System.out.println("----");
//             
//        		String restoredText = prefs.getString("sessionID", "");
//        		System.out.println(restoredText);
//        		System.out.println("----");
//                httpConnection.setRequestProperty("JSESSIONID", restoredText); 
//                httpConnection.connect();
// 
//                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    stream = httpConnection.getInputStream();
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            return stream;

        	HttpClient httpclient = new DefaultHttpClient();
        	HttpGet httpget = new HttpGet(url);
        	HttpResponse response;
        	String COOKIE_VALUE=prefs.getString("sessionID", "");
           
            InputStream instream=null;
            httpget.setHeader("Cookie",  "JSESSIONID="+COOKIE_VALUE); 	//Here i am sending the Cookie session ID 
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
        	//setContentView(R.layout.profile_settings);//::::::::::::::::::::::::::::::
        	System.out.println("get the xml");
        	System.out.println(output);
        	
        	createProfileSettingsUI(output);
    			
        }
    }
	
	public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                return true;
            else
                return false;    
    }
	
	

}

