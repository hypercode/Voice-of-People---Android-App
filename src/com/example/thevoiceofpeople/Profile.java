package com.example.thevoiceofpeople;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.thevoiceofpeople.MyLocationListener.LocationResult;
import com.example.thevoiceofpeople.ProfileSettings.GetXMLTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wallet.Address;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.PopupWindow;

public class Profile extends Activity {
	
	private TextView location;
	private TextView username;
	private MapFragment mf;
	private GoogleMap map;
	private LinearLayout upperBar;
	private TextView number_of_posts;
	private   SharedPreferences prefs;
	
	private String title,summary,category;
	private String title1,summary1,category1;
	private String latitude1 = "";
	private String  longitude1 = "";
	
	
	private String country ="";
	private String user_name="";
	
	private String longitude ="";
	private String latitude="";
	private String time_date="";
	//private String post_location=null;
	private String likes="";
	private String deslikes="";
	
	Spinner spinner;
	private LinearLayout textview;
	ViewFlipper flipper;
	ImageButton showPostView;
	Button showTextView,showMapView;	
	Button btnClosePopup;
	Button btnCreatePopup;
	final Context context = this;
	EditText post_title,post_summary,post_location;
	Spinner post_category;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		
		
		LocationResult locationResult = new LocationResult(){
		    @Override
		    public void gotLocation(Location LocationResult ){
		    
		       longitude1 = String.valueOf(LocationResult.getLongitude());
		       latitude1 = String.valueOf(LocationResult.getLatitude()); 
		       
		       
		   }
		};
		MyLocationListener myLocation = new MyLocationListener();
		myLocation.getLocation(this, locationResult);
		
		
		
		mf = (MapFragment) getFragmentManager().findFragmentById(R.id.profile_map);
		map = mf.getMap();
		flipper = (ViewFlipper)findViewById(R.id.contentVF);
		showTextView = (Button) findViewById(R.id.textviewBTN);
		showMapView = (Button) findViewById(R.id.mapviewBTN);
		showPostView =(ImageButton) findViewById(R.id.postviewBTN);
		textview=(LinearLayout)findViewById(R.id.textview);
		number_of_posts = (TextView)findViewById(R.id.numberOfPosts);
		username=(TextView)findViewById(R.id.username);
		location=(TextView)findViewById(R.id.location);
		upperBar=(LinearLayout)findViewById(R.id.upperbar);
		
		post_title = (EditText)findViewById(R.id.post_title);
		post_summary = (EditText)findViewById(R.id.post_summary);
		post_category = (Spinner)findViewById(R.id.category);
		
		post_location = (EditText)findViewById(R.id.post_location);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		spinner = (Spinner) findViewById(R.id.category);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.categories, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		
		GetXMLTask task = new GetXMLTask();
		//task.execute("http://192.168.1.65:8081/TheVoiceOfPeople1/profile");
		task.execute("http://83.212.97.66:8080/TheVoiceOfPeople1/profile");
		
		
		
	}
	
	public void post(View v) {
		
		title1 = post_title.getText().toString();
		summary1 = post_summary.getText().toString();
		category1 = post_category.getSelectedItem().toString().toLowerCase();

	
		HttpAsyncTask task=new HttpAsyncTask();
    	//task.execute("http://192.168.1.65:8081/TheVoiceOfPeople1/post_to_map");
		task.execute("http://83.212.97.66:8080/TheVoiceOfPeople1/post_to_map");
		
	}
	


	public void changeView1(View v)
	{	
		  
		   flipper.setDisplayedChild(0);
		   
	    
	}
	public void changeView2(View v)
	{	
		  
		   flipper.setDisplayedChild(1);
		   
	    
	}
	public void changeView3(View v)
	{	
		  
		   flipper.setDisplayedChild(2);
		   
	    
	}
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

	
	protected void createProfile(String xml)
    {
		System.out.println("create the profile");
    	try {
    		
    		
		    XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();

		    InputStream in_s =  new ByteArrayInputStream(xml.getBytes("UTF-8"));
	        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            
            int eventType = parser.getEventType();
            
            String tag;
            
            
            
            
            boolean f_post=true;
            boolean f_username=true;
            boolean f_location = true;
            int i=0;
            while (eventType != XmlPullParser.END_DOCUMENT){
            	
            	tag = parser.getName();
            	//System.out.println(tag);
                if( eventType == XmlPullParser.START_TAG){
//                		LinearLayout post = new LinearLayout(this);
//                		post.setLayoutParams(new LayoutParams(155,
//                                LayoutParams.WRAP_CONTENT));
//                		post.setBackgroundColor(Color.RED);
//                		post.setOrientation(LinearLayout.VERTICAL);
//                		post.setId(i);
                		
                		
	                	if (tag.equals("posts")) {
	                    
	                    	number_of_posts.setText("Posts:"+parser.nextText());
	                        
	                        
	                    
	                	}
	                	else if (tag.equals("username"))
                        {
                        	user_name=parser.nextText();
                        	username.setText("Username:"+user_name);
                           
                            
                        }
	                	else if (tag.equals("location"))
                        {
                        	country=parser.nextText();
                            location.setText("Country:"+country);
                            
                            
                        }
                        else if (tag.equals("title")){
                        	
                        	TextView post_title = new TextView(this);
                        	title=parser.nextText();
                        	post_title.setText(title);
                        	post_title.setTextColor(Color.parseColor("#F5DC49"));
                        	textview.addView(post_title);
                        	
                        	
                        	
                        	
                        }
                        else if (tag.equals("summary")){
                        	TextView post_summary = new TextView(this);
                        	summary=parser.nextText();
                        	post_summary.setText(summary);
                        	textview.addView(post_summary);
                        	
                        	
                        }
                        else if (tag.equals("longitude")){
                        	longitude=parser.nextText();
                        	
                        }
                        else if (tag.equals("latitude")){
                        	latitude=parser.nextText();
                        	
                        	
                        }
                        else if (tag.equals("time_date")){
                        	time_date=parser.nextText();
                        	
                        }
                        
                        else if (tag.equals("likes")){
                        	likes=parser.nextText();
                        	
                        }
                        else if (tag.equals("deslikes")){
                        	deslikes=parser.nextText();
                        	
                        	// post_location = PostLocation(longitude,latitude);
                            TextView post_info = new TextView(this);
                            post_info.setText(likes+" likes,"+deslikes+" deslikes,posted at:"+time_date);
                           //post_info.setText(post_location+","+likes+","+deslikes+"at:"+time_date);
                            textview.addView(post_info);
                            
                            //post the maker on the map  
                            LatLng position = new LatLng(Double.parseDouble(longitude),Double.parseDouble(latitude));

                             map.setMyLocationEnabled(true);
                            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 23));
                             
                             map.addMarker(new MarkerOptions()
                             .title(title)
                             .snippet(summary+","+likes+"likes,"+deslikes+"deslikes")
                             .position(position));
                            
                            
                        }
                     
                           
                }
                eventType = parser.next();
                
            }  
            
            

	} catch (XmlPullParserException e) {

		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}
	
	private TextView setText(String title) {
		// TODO Auto-generated method stub
		return null;
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
        	

            URI url = new URI(urlString);
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
        			System.out.println("OKKKKK");
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
        	
        	createProfile(output);
    			
        }
    }
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
        	try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}      
        	
        	
            ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("post_title",title1));
            postParameters.add(new BasicNameValuePair("post_category",category1));
            postParameters.add(new BasicNameValuePair("post_location1",latitude1));
            postParameters.add(new BasicNameValuePair("post_location2",longitude1));
            
            postParameters.add(new BasicNameValuePair("summary",summary1 ));
            postParameters.add(new BasicNameValuePair("post_url", ""));
 
 
            return POST(urls[0],postParameters);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	
        	Toast.makeText(getBaseContext(), "Your post was saved!!!", Toast.LENGTH_LONG).show();

            
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

	
	
	
	
	
}
