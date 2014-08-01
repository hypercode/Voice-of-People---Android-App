package com.example.thevoiceofpeople;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

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

import com.example.thevoiceofpeople.Profile.GetXMLTask;
import com.example.thevoiceofpeople.Signup.HttpAsyncTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class World  extends Activity implements OnInfoWindowClickListener{

	private   SharedPreferences prefs;
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private MapFragment mf;
	private GoogleMap map;
	private CheckBox worlD,sportS,tecH,otheR,politicS;
	private TextView post_likes ;
	private TextView post_deslikes ;
	final Context context = this;
	private Marker marker_world = null;
	private String world_interest,politics_interest,sports_interest,tech_interest,other_interest;
	
	private Transparent popup;
	private Transparent popup1;
	private String title="";
	private String summary="";
	private String username="";
	private String likes="";
    private String deslikes="";
    private String time_date="";
	private int action;
    private String post_id;  		//the id of marker which 'll sent to server
    private String clicked_marker_id;					//the current marker which is clicked
    private String[] markersID = new String[150];		//marker id from the server
   
    private Marker[] world = new Marker[30];
	private int i1=0;
	
	private Marker[] politics = new Marker[30];
	private int i2=0;
	
	private Marker[] sports = new Marker[30];
	private int i3=0;

	private Marker[] tech = new Marker[30];
	private int i4=0;
	
	private Marker[] other = new Marker[30];
	private int i5=0;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	// TODO Auto-generated method stub
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.world);
		
		popup = (Transparent) findViewById(R.id.popup_window);		// interests
	    popup.setVisibility(View.GONE);
	    
	    popup1 = (Transparent) findViewById(R.id.popup_window1);	// like-unlike
	    popup1.setVisibility(View.GONE);
	    
	    post_likes = (TextView) findViewById(R.id.likes);
	    post_deslikes = (TextView) findViewById(R.id.deslikes);
	    
	    worlD = (CheckBox) findViewById(R.id.world);
		politicS = (CheckBox) findViewById(R.id.politics);
		sportS = (CheckBox) findViewById(R.id.sports);
		tecH = (CheckBox) findViewById(R.id.tech);
		otheR = (CheckBox) findViewById(R.id.other);
		
		mf = (MapFragment) getFragmentManager().findFragmentById(R.id.world_map);
		map = mf.getMap();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		world_interest = prefs.getString("world_interest", "");
		politics_interest = prefs.getString("politics_interest", "");
		sports_interest = prefs.getString("sports_interest", "");
		tech_interest = prefs.getString("tech_interest", "");
		other_interest = prefs.getString("other_interest", "");
		
		GetXMLTask task = new GetXMLTask();
		//task.execute("http://192.168.1.65:8081/TheVoiceOfPeople1/return_markers");
		task.execute("http://83.212.97.66:8080/TheVoiceOfPeople1/return_markers");
		
		
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
        	
        	
        	System.out.println(output);
        	showMarkers(output);
    			
        }
    }
	
	protected void showMarkers(String xml){
    
		
    	try {
    		
    		XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();

		    InputStream in_s =  new ByteArrayInputStream(xml.getBytes("UTF-8"));
	        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            
            int eventType = parser.getEventType();
            
            String tag;
            
            LatLng position=null;
            
            String country ="";
            String user_name="";
            
            String longitude="";
            String latitude="";
          
            String url="";
            String category_id="";
            String user_id="";
            
            String post_id="";
            String post_location=null;
            
            String category_icon="";
            String postinfo="";
            
            int i=0;
            while (eventType != XmlPullParser.END_DOCUMENT){
            	
            	tag = parser.getName();
            	//System.out.println(tag);
                if( eventType == XmlPullParser.START_TAG){
                	if (tag.equals("title")) 
                		title=parser.nextText();
                        
                    else if (tag.equals("summary"))
                		summary=parser.nextText();
                    	
                    else if (tag.equals("time_date"))
                		time_date=parser.nextText();
                       
                    else if (tag.equals("longitude"))
                    	longitude=parser.nextText();
                    	
                    else if (tag.equals("latitude"))
                    	latitude=parser.nextText();
                    	
                    else if (tag.equals("url"))
                    	url=parser.nextText();
                    	
                    else if (tag.equals("id_categories"))
                    	category_id=parser.nextText();
                    	
                    else if (tag.equals("id_user"))
                    	user_id=parser.nextText();
                    	
                    else if (tag.equals("username"))
                    	username=parser.nextText();
                    	
                    else if (tag.equals("ID")) {
                    	
                    	post_id = parser.nextText();
                    	markersID[i] = post_id;
                    	i=i+1;
                    	
                    }
                    else if (tag.equals("likes"))
                    	likes=parser.nextText();
                	
                	else if (tag.equals("deslikes")) {
                    	deslikes=parser.nextText();
                    	
                    	position = new LatLng(Double.parseDouble(longitude),Double.parseDouble(latitude));
                       
                        map.setMyLocationEnabled(true);
                        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 23));
                        //category_icon = getIcon(ctaegory_id);
                        
                      if (category_id.equals("1")){				//world
                    	 world[i1] = map.addMarker(new MarkerOptions()
	                          .title(title)
	                          .snippet(summary + "(" + likes + "," + deslikes +")" )
	                          .position(position)
	                          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    	 
                    	 i1=i1+1;
                      }
                      else if (category_id.equals("2")){		//politics
                    	  politics[i2] = map.addMarker(new MarkerOptions()
	                          .title(title)
	                          
                    	  	  .snippet(summary + "(" + likes + "," + deslikes +")")
	                          .position(position)
	                          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    	 
                    	  i2=i2+1;
                      }
                      else if (category_id.equals("3")){		//sports
                    	 sports[i3] = map.addMarker(new MarkerOptions()
	                          .title(title)
	                          .snippet(summary + "(" + likes + "," + deslikes +")")
	                          .position(position)
	                          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    	 
                    	 i3=i3+1;	
                      }
                      else if (category_id.equals("4")){		//technology
                    	  tech[i4] = map.addMarker(new MarkerOptions()
	                          .title(title)
	                          
	                          .snippet(summary + "(" + likes + "," + deslikes +")")
	                          .position(position)
	                          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    	 
                    	  i4=i4+1;
                      }
                      else {									//other
                    	  other[i5] = map.addMarker(new MarkerOptions()
	                          .title(title)
	                         
	                          .snippet(summary + "(" + likes + "," + deslikes +")")
	                          .position(position)
	                          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    	 
                    	  i5=i5+1;
                      }
                      
                      map.setOnInfoWindowClickListener((OnInfoWindowClickListener) this);
                        
                   // Setting a custom info window adapter for the google map
                      map.setInfoWindowAdapter(new InfoWindowAdapter() {
               
                          // Use default InfoWindow frame
                          @Override
                          public View getInfoWindow(Marker arg0) {
                              return null;
                          }
               
                          // Defines the contents of the InfoWindow
                          @Override
                          public View getInfoContents(Marker marker) {
               
                              // Getting view from the layout file info_window_layout
                              View v = getLayoutInflater().inflate(R.layout.marker_content, null);
               
                              // Getting the position from the marker
                              LatLng latLng = marker.getPosition();
               
                              
                              TextView post_title = (TextView) v.findViewById(R.id.title);
                              TextView post_summary = (TextView) v.findViewById(R.id.summary);
                              TextView post_likes_deslikes = (TextView) v.findViewById(R.id.likes_deslikes);
                              TextView post_deslikes = (TextView) v.findViewById(R.id.deslikes);
                            //  TextView post_user = (TextView) v.findViewById(R.id.user);
                             // TextView post_date = (TextView) v.findViewById(R.id.date);
               
                              post_title.setText(marker.getTitle());
                              //post_likes_deslikes.setText(likes+","+deslikes);
                             // post_deslikes.setText(deslikes);
                             // post_user.setText(username);
                              //post_date.setText(time_date);
                              // Setting the longitude
                              post_summary.setText(marker.getSnippet());
               
                              // Returning the view containing InfoWindow contents
                              return v;
               
                          }
                      });  
                        
                       
                        
                    	
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
	public void showInterests(View v) {
		

		if (world_interest.equals("yes"))
			worlD.setChecked(true);
		else
			worlD.setChecked(false);
		if (politics_interest.equals("yes"))
			politicS.setChecked(true);
		else
			politicS.setChecked(false);
		if (sports_interest.equals("yes"))
			sportS.setChecked(true);
		else
			sportS.setChecked(false);
		if (tech_interest.equals("yes"))
			tecH.setChecked(true);
		else
			tecH.setChecked(false);
		if (other_interest.equals("yes"))
			otheR.setChecked(true);
		else
			otheR.setChecked(false);
			
		popup.setVisibility(View.VISIBLE);
		

		
	}
	
	public void saveInterests(View v) {
		
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		editor = settings.edit();
		
		popup.setVisibility(View.GONE);
		
		if (!worlD.isChecked()) {
			for (int i=0;i<i1;i++){
				world[i].setVisible(false);
			}
			editor.putString("world_interest", "no");
    		editor.commit();
			
		}
		else {
			for (int i=0;i<i1;i++){
				world[i].setVisible(true);
			}
			
		}
		if (!politicS.isChecked()) {
			for (int i=0;i<i2;i++){
				politics[i].setVisible(false);
			}
			editor.putString("politics_interest", "no");
    		editor.commit();
		}
		else {
			for (int i=0;i<i2;i=i+1){
				politics[i].setVisible(true);
			}
		}
		if (!sportS.isChecked()) {
			for (int i=0;i<i3;i=i+1){
				sports[i].setVisible(false);
			}
			editor.putString("sports_interest", "no");
    		editor.commit();
		}
		else {
			for (int i=0;i<i3;i=i+1){
				sports[i].setVisible(true);
			}
		}
		if (!tecH.isChecked()) {
			for (int i=0;i<i4;i=i+1){
				tech[i].setVisible(false);
			}
			editor.putString("tech_interest", "no");
    		editor.commit();
		}
		else {
			for (int i=0;i<i4;i=i+1){
				tech[i].setVisible(true);
			}
		}
		if (!otheR.isChecked()) {
			for (int i=0;i<i5;i=i+1){
				other[i].setVisible(false);
			}
			editor.putString("other_interest", "no");
    		editor.commit();
		}
		else {
			for (int i=0;i<i5;i=i+1){
				other[i].setVisible(true);
			}
		}
		
		
		
		
		
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
	public void like(View v){
		
		post_id = String.valueOf(clicked_marker_id.charAt(1)); 		//+2
		action = 1;
		
		HttpAsyncTask task=new HttpAsyncTask();
    	//task.execute("http://192.168.1.65:8081/TheVoiceOfPeople1/like_deslike");
		task.execute("http://83.212.97.66:8080/TheVoiceOfPeople1/like_deslike");
		
		popup1.setVisibility(View.GONE);
	}
	public void deslike(View v){
		
		post_id = String.valueOf(clicked_marker_id.charAt(1));		//+2
		action = 0;
		
		HttpAsyncTask task=new HttpAsyncTask();
    	//task.execute("http://192.168.1.65:8081/TheVoiceOfPeople1/like_deslike");
		task.execute("http://83.212.97.66:8080/TheVoiceOfPeople1/like_deslike");
		popup1.setVisibility(View.GONE);
	}
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
        

            ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("ID",post_id));
            
            if (action == 1)
            	postParameters.add(new BasicNameValuePair("action","like"));
            else
            	postParameters.add(new BasicNameValuePair("action","deslike"));
           
 
            return POST(urls[0],postParameters);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	
        	
        	
        	if (!result.equals("ERROR")) 
        		Toast.makeText(getBaseContext(), "Nice!!", Toast.LENGTH_LONG).show();
        	
        	
        	
            
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
	
	
	
	
	
	@Override
	public void onInfoWindowClick(Marker marker) {
		
		popup1.setVisibility(View.VISIBLE);
		clicked_marker_id = marker.getId();		//grab the marker id (m0,m1,m2....)
		
	}


}
