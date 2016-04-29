package com.example.foodadviser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class User extends ActionBarActivity {
AutoCompleteTextView place;
double latitude,longitude;
LocationManager loc;

JSONArray  restaurantid=new JSONArray();
JSONObject  jsonrest=new JSONObject();
Button logout,get,btn,location;
String username,selected_place="",SPuser,url,setplace,Get_rest_email,Sfname="",Sftype="",Sfdetails="",Ssremail="",Srname="",userid,restid,currentAddress="null";
TextView tv_username,featuredRest;
String [] restaurantimage,restaurantname,restaurant_email,temprestaurantimage,temprestaurantname,temprestaurant_email;
Spinner rest_list,food_list;
String[] search_text,sfname,sftype,sfdetails,sremail,srname,Restid,tempRid,tempAvg,tempRestid;

Bitmap [] restimge;
ArrayList<String> list_place;
ArrayAdapter<String> adp;
JSONObject json;
JSONArray contacts = null;
GridView gv;

Controller aController;
Context mcontext;  
String browserKey="AIzaSyBkN2AtKv1UePN2sncriq6rt1-pyCv1TXU";
private static final String TAG = "MAIN_ACTIVITY_ASYNC";
private static final String TAG_RESULT = "predictions";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
aController = (Controller) getApplicationContext();
		
		
		// Check if Internet present
		if (!aController.isConnectingToInternet()) {
			
			// Internet Connection is not present
			aController.showAlertDialog(User.this,
					"Internet Connection Error",
					"Please connect to Internet connection and try again", false);
			// stop executing code by return
			return;
		}
		loc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		loc.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,1000, new MyLocationListener());

	get=(Button)findViewById(R.id.btn_get);
	featuredRest=(TextView)findViewById(R.id.tv_Featured_Restaurants);
	location=(Button)findViewById(R.id.btn_user_location);
	tv_username=(TextView)findViewById(R.id.tv_user_name);
	place=(AutoCompleteTextView)findViewById(R.id.atv_user_place);
	place.setThreshold(1);
	gv=(GridView)findViewById(R.id.gridrest);
	username=getIntent().getStringExtra("name");
	userid=getIntent().getStringExtra("id");
    place.clearFocus();
	
	tv_username.setText("Welcomes "+username);
	
	new JsonGetRating().execute();
	
	get.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new JsonGet().execute();
			
		}
	});
	
	place.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			search_text= place.getText().toString().split(",");
			   url="https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+search_text[0]+"&location=37.76999,-122.44696&radius=500&sensor=true&key="+browserKey;
			   if(search_text.length<=1){
				   list_place=new ArrayList<String>();
				   Log.d("URL",url);
					paserdata parse=new paserdata();
					parse.execute();
					selected_place=place.getText().toString();
			   }
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			selected_place=place.getText().toString();
			
		}
	});
	location.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			selected_place=currentAddress;
			new JsonGet().execute();
		}
	});
	new JsonGetFeatured().execute();
	
	}
public class paserdata extends AsyncTask<Void, Integer, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			
			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			 json = jParser.getJSONFromUrl(url.toString());
			if(json !=null)
			{
			try {
				// Getting Array of Contacts
				contacts = json.getJSONArray(TAG_RESULT);
				
				for(int i = 0; i < contacts.length(); i++){
					JSONObject c = contacts.getJSONObject(i);
					String description = c.getString("description");
					Log.d("description", description);
					list_place.add(description);
				
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			}
			
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void result) {
			adp = new ArrayAdapter<String>(getApplicationContext(), 
				    android.R.layout.simple_list_item_1, list_place) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
				    View view = super.getView(position, convertView, parent);
				    TextView text = (TextView) view.findViewById(android.R.id.text1);
				      text.setTextColor(Color.BLACK);
				    return view;
				  }
				};
		place.setAdapter(adp);	
		}
		}
class JsonGet extends AsyncTask<String, Void, String>
{ 
	ProgressDialog pDialog;
	InputStream stream;
	String result = "";
	String data = "";
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub`
		 try{
             HttpClient httpclient = new DefaultHttpClient();
             ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
          
             nameValuePairs.add(new BasicNameValuePair("location",setplace));
             
             
             HttpPost httppost = new HttpPost("http://192.168.0.123/foodadviser/selectRestaurant.php");
             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
             
             HttpResponse response = httpclient.execute(httppost);
             HttpEntity entity = response.getEntity();
             stream = entity.getContent();
     }catch(Exception e){
             Log.e("log_tag", "Error in http connection "+e.toString());
     }
		 try{
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"iso-8859-1"),8);
             StringBuilder sb = new StringBuilder();
             String line = null;
             while ((line = reader.readLine()) != null) {
                     sb.append(line + "\n");
             }
             stream.close();
           
             result=sb.toString();
             
     }catch(Exception e){
             Log.e("log_tag", "Error converting result "+e.toString());
     }
				return result;
				
	}
	@Override
	protected void onPreExecute() {
	setplace=selected_place;
	pDialog = new ProgressDialog(User.this);
    pDialog.setMessage("Getting Restaurants...");
    pDialog.show();
		super.onPreExecute();
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		pDialog.dismiss();
		
	   Log.v("result", result);
	  Log.v(result, "result");
	 // Toast.makeText(getApplicationContext(),result.toString(), Toast.LENGTH_SHORT).show();
	   
		try {
			JSONObject jObj = new JSONObject(result);
        	JSONArray jArray =jObj.getJSONArray("result");
			if(jArray.isNull(1)){
				Toast.makeText(getApplicationContext(), "Sorry... No Restaurants Found in this location !!!", Toast.LENGTH_SHORT).show();
			}
			else{

              String arr[]=new String[jArray.length()];
              String rest_Email[]=new String[jArray.length()];
              String rest_name[]=new String[jArray.length()];
              String rest_image[]=new String[jArray.length()];
              String rest_id[]=new String[jArray.length()];
                for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                       
                      // List all
                       
                     int id=json_data.getInt("id");
                          String place=json_data.getString("location");
                        String name=json_data.getString("name");
                        String password=json_data.getString("password");
                        String type=json_data.getString("type");
                        String rtype=json_data.getString("rtype");
                        String image=json_data.getString("image");
                        
                      /*  restaurant_email[i]*/String email=json_data.getString("email");
                    
                   arr[i]=name;

                       Log.v(name, "got");
                       rest_name[i]=name;
                       restaurantname=rest_name;
                        rest_Email[i]=email;
                        restaurant_email=rest_Email;
                        rest_image[i]=image;
                        restaurantimage=rest_image;
                        Log.v(image, "got");
                        rest_id[i]=String.valueOf(id);
                        Restid=rest_id;
                     
                      
                			}
                //rest_list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spinne_food_item,R.id.sp_food_item_name,arr));
                set();
                featuredRest.setVisibility(View.INVISIBLE);
       // lst.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arr));        
		}}catch(Exception e)
		{
         
		}
		
		
	}
	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
//ProgressDialog loading = ProgressDialog.show(Home.this, "Loading Content", "Please wait...",true,true);

		super.onProgressUpdate(values);
	}
	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}
}
class JsonItems extends AsyncTask<String, Void, String>
{ 
	ProgressDialog pDialog;
	InputStream stream;
	String result = "";
	String data = "";
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub`
		 try{
             HttpClient httpclient = new DefaultHttpClient();
             ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
          
             nameValuePairs.add(new BasicNameValuePair("email",Get_rest_email));
             
             Log.v(Get_rest_email,"got");
             HttpPost httppost = new HttpPost("http://192.168.0.123/foodadviser/getFoodItem.php");
             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
             
             HttpResponse response = httpclient.execute(httppost);
             HttpEntity entity = response.getEntity();
             stream = entity.getContent();
     }catch(Exception e){
             Log.e("log_tag", "Error in http connection "+e.toString());
     }
		 try{
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"iso-8859-1"),8);
             StringBuilder sb = new StringBuilder();
             String line = null;
             while ((line = reader.readLine()) != null) {
                     sb.append(line + "\n");
             }
             stream.close();
           
             result=sb.toString();
             
     }catch(Exception e){
             Log.e("log_tag", "Error converting result "+e.toString());
     }
				return result;
				
	}
	@Override
	protected void onPreExecute() {
	    pDialog = new ProgressDialog(User.this);
        pDialog.setMessage("Getting Food Menu...");
        pDialog.show();
		
		super.onPreExecute();
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		 pDialog.dismiss();
	   Log.v("result", result);
	  Log.v(result, "result");
	  
	   
		try {
		
        	
			JSONObject jObj = new JSONObject(result);
        	JSONArray jArray =jObj.getJSONArray("result");
        	 String frname[]=new String[jArray.length()];
        	 String Rid[]=new String[jArray.length()];
        	 String fname[]=new String[jArray.length()];
        	 String ftype[]=new String[jArray.length()];
        	 String fdetails[]=new String[jArray.length()];
        	 String fremail[]=new String[jArray.length()];
              String arr[]=new String[jArray.length()];
                for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                       
                      // List all
                       
                     
                          String rname=json_data.getString("rname");
                        String foodname=json_data.getString("foodname");
                        String fooddetails=json_data.getString("fooddetails");
                        String foodtype=json_data.getString("foodtype");
                        
                        String rid=json_data.getString("rid");
                        
                       /* restaurant_email[i]*/String email=json_data.getString("email");
                    
                   arr[i]=foodname;

                       Log.v(foodname, "got");
                          	frname[i]=rname;
                          	fname[i]=foodname;
                          	ftype[i]=foodtype;
                          	fdetails[i]=fooddetails;
                          	fremail[i]=email;
                          	Rid[i]=rid;
                          	 Log.v(rname, "got");
                          	 Log.v(foodtype, "got");
                          	 Log.v(fooddetails, "got");
                          	 Log.v(email, "got");
                          	sfname=fname;
                          	sftype=ftype;
                          	sfdetails=fdetails;
                          	sremail=fremail;
                          	srname=frname;
                          	
                          
                          	
                			}
             //   food_list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spinne_food_item,R.id.sp_food_item_name,arr));

       // lst.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arr));        
		}catch(Exception e)
		{
         

		}
		
	}
	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
//ProgressDialog loading = ProgressDialog.show(User.this, "Loading Content", "Please wait...",true,true);

		super.onProgressUpdate(values);
	}
	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}
}
Bitmap[] Base64ToBitmap(String[] myImageData)
{
	Bitmap[] img = null;
	try {
		for(int i=0;i<myImageData.length;i++){
			
		    byte[] imageAsBytes = Base64.decode(myImageData[i].getBytes(),Base64.DEFAULT);
		     img[i]=BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
			}
	} catch (Exception e) {
		// TODO: handle exception
	}
	
	return img;
}

	public void set(){
		gv.setAdapter(new GridAdapter(this, restaurantname,restaurantimage,restaurant_email,username,userid,Restid));
	}
	 boolean doubleBackToExitPressedOnce = false;
	@Override
	public void onBackPressed() {

		if (doubleBackToExitPressedOnce) {
			Intent main = new Intent(Intent.ACTION_MAIN);
	        main.addCategory(Intent.CATEGORY_HOME);
	        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(main);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;                       
            }
        }, 2000);
	               }
	
	
	class JsonGetRating extends AsyncTask<String, Void, String>
	{ 
		
		
		InputStream stream;
		String result = "";
		String data = "";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub`
			 try{
				 
			
	             HttpClient httpclient = new DefaultHttpClient();
	             ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	          
	             HttpPost httppost = new HttpPost("http://192.168.0.123/foodadviser/getAvgRating.php");
	             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	             
	             HttpResponse response = httpclient.execute(httppost);
	             HttpEntity entity = response.getEntity();
	             stream = entity.getContent();
	     }catch(Exception e){
	             Log.e("log_tag", "Error in http connection "+e.toString());
	     }
			 try{
	             BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"iso-8859-1"),8);
	             StringBuilder sb = new StringBuilder();
	             String line = null;
	             while ((line = reader.readLine()) != null) {
	                     sb.append(line + "\n");
	             }
	             stream.close();
	           
	             result=sb.toString();
	             
	     }catch(Exception e){
	             Log.e("log_tag", "Error converting result "+e.toString());
	     }
					return result;
					
		}
		@Override
		protected void onPreExecute() {
		
			
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
		   Log.v("result", result);
		  Log.v(result, "result");
		  
		   
			try {
			
	        	
				JSONObject jObj = new JSONObject(result);
	        	JSONArray jArray =jObj.getJSONArray("result");
	        	 String restid[]=new String[jArray.length()];
	        	 String avgs[]=new String[jArray.length()];
	        	 
	        	
	              String arr[]=new String[jArray.length()];
	                for(int i=0;i<jArray.length();i++){
	                        JSONObject json_data = jArray.getJSONObject(i);
	                       
	                      // List all
	                      
	                        String rid   =json_data.getString("rid");
	                        String avg   =json_data.getString("avg");
	                       
	                        restid[i]=rid;
	                        avgs[i]=avg;
	                          	
	                          	 Log.v(String.valueOf(avg), "got");
	                          	 tempAvg=avgs;
	                          	 tempRid=restid;
	                             restaurantid.put(restid[i]);
	                             Log.v(restaurantid.toString(), "json array");	                			
	                             }
	                
	                jsonrest.put("json", restaurantid);
			}catch(Exception e)
			{
	         

			}
			
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
	//ProgressDialog loading = ProgressDialog.show(User.this, "Loading Content", "Please wait...",true,true);

			super.onProgressUpdate(values);
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}
	
	class JsonGetFeatured extends AsyncTask<String, Void, String>
	{ 
		ProgressDialog pDialog;
		InputStream stream;
		String result = "";
		String data = "";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub`
			 try{
	             HttpClient httpclient = new DefaultHttpClient();
	             ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	          
	              Log.v("array",jsonrest.toString());
	             nameValuePairs.add(new BasicNameValuePair("rid",jsonrest.toString()));
	             
	             Log.v(restaurantid.toString(), "got");
	             HttpPost httppost = new HttpPost("http://192.168.0.123/foodadviser/getFeaturedRest.php");
	             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	             
	             HttpResponse response = httpclient.execute(httppost);
	             HttpEntity entity = response.getEntity();
	             stream = entity.getContent();
	     }catch(Exception e){
	             Log.e("log_tag", "Error in http connection "+e.toString());
	     }
			 try{
	             BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"iso-8859-1"),8);
	             StringBuilder sb = new StringBuilder();
	             String line = null;
	             while ((line = reader.readLine()) != null) {
	                     sb.append(line + "\n");
	             }
	             stream.close();
	           
	             result=sb.toString();
	             
	     }catch(Exception e){
	             Log.e("log_tag", "Error converting result "+e.toString());
	     }
					return result;
					
		}
		@Override
		protected void onPreExecute() {
		setplace=selected_place;
	    pDialog = new ProgressDialog(User.this);
        pDialog.setMessage("Getting Featured Restaurants...");
        pDialog.show();
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			 pDialog.dismiss();
			
		   Log.v("result", result);
		  Log.v(result, "result");
		 // Toast.makeText(getApplicationContext(),result.toString(), Toast.LENGTH_SHORT).show();
		   
			try {
				JSONObject jObj = new JSONObject(result);
	        	JSONArray jArray =jObj.getJSONArray("result");
	              String arr[]=new String[jArray.length()];
	              String rest_Email[]=new String[jArray.length()];
	              String rest_name[]=new String[jArray.length()];
	              String rest_image[]=new String[jArray.length()];
	              String rest_id[]=new String[jArray.length()];
	                for(int i=0;i<jArray.length();i++){
	                        JSONObject json_data = jArray.getJSONObject(i);
	                       
	                      // List all
	                       
	                     int id=json_data.getInt("id");
	                          String place=json_data.getString("location");
	                        String name=json_data.getString("name");
	                        String password=json_data.getString("password");
	                        String type=json_data.getString("type");
	                        String rtype=json_data.getString("rtype");
	                        String image=json_data.getString("image");
	                        
	                      /*  restaurant_email[i]*/String email=json_data.getString("email");
	                    
	                   arr[i]=name;

	                       Log.v(name, "got");
	                       rest_name[i]=name;
	                       temprestaurantname=rest_name;
	                        rest_Email[i]=email;
	                        temprestaurant_email=rest_Email;
	                        rest_image[i]=image;
	                        temprestaurantimage=rest_image;
	                        Log.v(image, "got");
	                        rest_id[i]=String.valueOf(id);
	                        tempRestid=rest_id;
	                      
	                			}
	                setFeatured();
	                }
	                //rest_list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spinne_food_item,R.id.sp_food_item_name,arr));
	                
	       // lst.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arr));        
			catch(Exception e)
			{
	         
			}
			
			
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
	//ProgressDialog loading = ProgressDialog.show(Home.this, "Loading Content", "Please wait...",true,true);

			super.onProgressUpdate(values);
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}
	public void setFeatured(){
		gv.setAdapter(new FeaturedAdapter(this, temprestaurantname,temprestaurantimage,temprestaurant_email,username,userid,tempRestid,tempAvg));
	}
	 public void show(View v) throws IOException
	    {
	    	  Location location = loc.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    		 
	    		        if (location != null) {
	    	            String message = String.format(
	    		                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
	    		                    location.getLongitude(), location.getLatitude());
	    	            //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	 		        try{
	    	            StringBuilder result = null;
	    	            Geocoder gcd = new Geocoder(this, Locale.getDefault());
	    	            List<Address> addresses = 
	    	                gcd.getFromLocation(location.getLatitude(),location.getLongitude(),100);
	    	            if (addresses.size() > 0) {
	    	                 result = new StringBuilder();
	    	                for(int i = 0; i < addresses.size(); i++){
	    	                    Address address =  addresses.get(i);
	    	                    int maxIndex = address.getMaxAddressLineIndex();
	    	                    for (int x = 0; x <= maxIndex; x++ ){
	    	                        result.append(address.getAddressLine(x));
	    	                        result.append(",");
	    	                    }               
	    	                    result.append(address.getLocality());
	    	                    result.append(",");
	    	                    result.append(address.getPostalCode());
	    	                    result.append("\n\n");
	    	                }
	    	                
	    	            }
	    	            Toast.makeText(getApplicationContext(), "Location "+ result.toString(),Toast.LENGTH_LONG).show();
	    	         
	 		        }catch(Exception ep)
	 		        {
	 		        Toast.makeText(getApplicationContext(), "error "+ep.getMessage(),Toast.LENGTH_LONG).show();	
	 		        }
	 		        
	    	        }    

	    	    }
	    		        class MyLocationListener implements LocationListener
	    		        {

							@Override
							public void onLocationChanged(Location location) {
								// TODO Auto-generated method stub
								String message = String.format(
		                        "New Location \n Longitude: %1$s \n Latitude: %2$s",
											                    location.getLongitude(), location.getLatitude()
											            );
									           // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
									            latitude=location.getLatitude();
									            longitude=location.getLongitude();
									            currentAddress=getCompleteAddressString(latitude,longitude);
									     Toast.makeText(getApplicationContext(), currentAddress, Toast.LENGTH_LONG).show();
									        	
							}

							@Override
							public void onProviderDisabled(String provider) {
								// TODO Auto-generated method stub
								Toast.makeText(getApplicationContext(), "GPS turned off",
										                 Toast.LENGTH_LONG).show();
								
							}

							@Override
							public void onProviderEnabled(String provider) {
								// TODO Auto-generated method stub
								Toast.makeText(getApplicationContext(), "GPS turned on",
						                 Toast.LENGTH_LONG).show();
							}

							@Override
							public void onStatusChanged(String provider,
									int status, Bundle extras) {
								// TODO Auto-generated method stub
								//Toast.makeText(getApplicationContext(), " Provider status changed ",Toast.LENGTH_LONG).show();
							}
	    		        	
	    		        }
	    		        private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
	    		            String strAdd = "";
	    		            String adrs="";
	    		            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
	    		            try {
	    		                List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
	    		                if (addresses != null) {
	    		                    Address returnedAddress = addresses.get(0);
	    		                    StringBuilder strReturnedAddress = new StringBuilder("");
	    		                    String city=addresses.get(0).getLocality();
	    		                    String state=addresses.get(0).getAdminArea();
	    		                    String country=addresses.get(0).getCountryName();
	    		                    for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
	    		                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
	    		                    }
	    		                   adrs=city+", "+state+", "+country;
	    		                    strAdd = strReturnedAddress.toString();
	    		                    Log.w("My Current loction address", "" + strReturnedAddress.toString());
	    		                } else {
	    		                    Log.w("My Current loction address", "No Address returned!");
	    		                }
	    		            } catch (Exception e) {
	    		                e.printStackTrace();
	    		                Log.w("My Current loction address", "Canont get Address!");
	    		            }
	    		            return adrs;
	    		        }
	    		        @Override
	    		        public boolean onCreateOptionsMenu(Menu menu) {
	    		            // Inflate the menu; this adds items to the action bar if it is present.
	    		            getMenuInflater().inflate(R.menu.user, menu);
	    		            return true;
	    		        }

	    		        @Override
	    		        public boolean onOptionsItemSelected(MenuItem item) {
	    		            // Handle action bar item clicks here. The action bar will
	    		            // automatically handle clicks on the Home/Up button, so long
	    		            // as you specify a parent activity in AndroidManifest.xml.
	    		            int id = item.getItemId();
	    		            if (id == R.id.Newfoodmenuitem) {
	    		            	Intent i=new Intent(getApplicationContext(),NewFood.class);
	    		            	startActivity(i);
	    		                return true;
	    		            }
	    		            if (id == R.id.Ratedfoodmenuitem) {
	    		            	Intent i=new Intent(getApplicationContext(),BestRatedFood.class);
	    		            	startActivity(i);
	    		                return true;
	    		            }
	    		            return super.onOptionsItemSelected(item);
	    		        }
}
