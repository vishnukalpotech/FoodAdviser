package com.example.foodadviser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

public class BestRatedFood extends ActionBarActivity {
	GridView gridnewfood;
	Button back;
	JSONArray  jfoodid=new JSONArray();
	JSONObject  jsonfood=new JSONObject();
	String[] Foodname,Foodtype,Fooddetails,Restname,Restemail,Foodimage,Foodid,Fdids,Rid,Avg,Rids;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_best_rated_food);
		back=(Button)findViewById(R.id.btn_best_back);
		gridnewfood=(GridView)findViewById(R.id.grid_best_food);
		new BestRatedItemsGet().execute();
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	
	
	}
	class BestRatedItems extends AsyncTask<String, Void, String>
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
	             Log.v("array",jsonfood.toString());
	             nameValuePairs.add(new BasicNameValuePair("fid",jsonfood.toString()));
	             
	             Log.v("array",jsonfood.toString());
	             HttpPost httppost = new HttpPost("http://192.168.0.123/foodadviser/getBestRatingfood.php");
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
			pDialog = new ProgressDialog(BestRatedFood.this);
		    pDialog.setMessage("Getting Best Rated Food Items...");
		    pDialog.show();
				super.onPreExecute();
			
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
	        	 String foodid[]=new String[jArray.length()];
	        	 String frname[]=new String[jArray.length()];
	        	 String fimage[]=new String[jArray.length()];
	        	 String fname[]=new String[jArray.length()];
	        	 String ftype[]=new String[jArray.length()];
	        	 String fdetails[]=new String[jArray.length()];
	        	 String fremail[]=new String[jArray.length()];
	        	 String rids[]=new String[jArray.length()];
	              String arr[]=new String[jArray.length()];
	                for(int i=0;i<jArray.length();i++){
	                        JSONObject json_data = jArray.getJSONObject(i);
	                       
	                      // List all
	                       
	                        String fdid=json_data.getString("foodid");
	                          String rname=json_data.getString("rname");
	                        String foodname=json_data.getString("foodname");
	                        String fooddetails=json_data.getString("fooddetails");
	                        String foodtype=json_data.getString("foodtype");
	                        String fdimage=json_data.getString("image");
	                        String rid=json_data.getString("rid");

	                       /* restaurant_email[i]*/String email=json_data.getString("email");
	                    
	                   arr[i]=foodname;

	                       Log.v(foodname, "got");
	                          	frname[i]=rname;
	                          	fname[i]=foodname;
	                          	ftype[i]=foodtype;
	                          	fdetails[i]=fooddetails;
	                          	fremail[i]=email;
	                          	fimage[i]=fdimage;
	                          	foodid[i]=fdid;
	                          	rids[i]=rid;
	                          	 Log.v(rname, "got");
	                          	 Log.v(foodtype, "got");
	                          	 Log.v(fooddetails, "got");
	                          	 Log.v(email, "got");
	                          	Foodname=fname;
	                          	Foodtype=ftype;
	                          	Fooddetails=fdetails;
	                          	Restemail=fremail;
	                          	Restname=frname;
	                           Foodimage=fimage;
	                           Foodid=foodid;
	                           Rids=rids;
	                          	
	                			}
	                
	                setGridAdapter();
	          //      food_list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spinne_food_item,R.id.sp_food_item_name,arr));

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
	
	
	class BestRatedItemsGet extends AsyncTask<String, Void, String>
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
	             
	             HttpPost httppost = new HttpPost("http://192.168.0.123/foodadviser/bestRatingfood.php");
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
	        	 String foodid[]=new String[jArray.length()];
	        	 String avg[]=new String[jArray.length()];
	        	 String rid[]=new String[jArray.length()];
	              String arr[]=new String[jArray.length()];
	                for(int i=0;i<jArray.length();i++){
	                        JSONObject json_data = jArray.getJSONObject(i);
	                       
	                      // List all
	                       
	                        String fdid=json_data.getString("foodid");
	                        
	                        String avgs=json_data.getString("avg");
	                        
	                        String rids=json_data.getString("rid");

	                     

	                       Log.v(fdid, "got");
	                          	avg[i]=avgs;
	                          	rid[i]=rids;
	                          	foodid[i]=fdid;
	                          	 Log.v(rids, "got");
	                          	 Log.v(avgs, "got");
	                          	 
	                          	Rid=rid;
	                          	Avg=avg;
	                           jfoodid.put(foodid[i]);
	                           
	                           Log.v(jfoodid.toString(), "json array");
	                			}
	                
	                jsonfood.put("json", jfoodid);
	                Log.v(jsonfood.toString(), "Get json array");
	                
	          //      food_list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spinne_food_item,R.id.sp_food_item_name,arr));
	            	new BestRatedItems().execute();
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
	
	
	
	public void setGridAdapter(){
		
		gridnewfood.setAdapter(new RatedFoodAdapter(this, Foodname,Foodimage,Restemail,Foodtype,Fooddetails,Foodid,Rids));
	}
	@Override
	public void onBackPressed() {

	               return;
	           }

	
}
