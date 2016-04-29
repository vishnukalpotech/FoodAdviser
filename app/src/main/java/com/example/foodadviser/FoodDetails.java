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
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class FoodDetails extends ActionBarActivity {
TextView foodname,foodtype,fooddetails;
ImageView foodimage;
RatingBar rb;
String image,foodid,User,rating,Userid,restid;
String[] Foodusername,Foodid,FoodComment;
Button back,comment;
ListView listcomment;
EditText etcomment;
private int mInterval = 10000; // 10 seconds by default, can be changed later
Float ratingvalue=null,temprate;
double rate;
String ratings,tempr;

private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_details);
		rb=(RatingBar)findViewById(R.id.rBar_fooditem);
		foodname=(TextView)findViewById(R.id.tv_fooddetails_name);
		foodtype=(TextView)findViewById(R.id.tv_fooddetails_type);
		fooddetails=(TextView)findViewById(R.id.tv_fooddetails_details);
		foodimage=(ImageView)findViewById(R.id.iv_fooddetails_image);
		back=(Button)findViewById(R.id.btn_fooddetails_back);
		comment=(Button)findViewById(R.id.btn_fooddetails_comment);
		
		etcomment=(EditText)findViewById(R.id.et_food_comment);
		restid=getIntent().getStringExtra("rid");
		foodname.setText(getIntent().getStringExtra("fname"));
		foodtype.setText(getIntent().getStringExtra("ftype"));
		fooddetails.setText(getIntent().getStringExtra("fdetails"));
		image=getIntent().getStringExtra("fimage");
		User=getIntent().getStringExtra("user");
		foodid=getIntent().getStringExtra("fid");
		Userid=getIntent().getStringExtra("userid");
		new JsonGetRating().execute();
		
		listcomment=(ListView)findViewById(R.id.lv_food_comment);
		this.listcomment.setEmptyView(findViewById(R.id.lv_food_comment));
		
		byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
		foodimage.setImageBitmap(
	              BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
		new JsonListAdapter().execute();
		mHandler = new Handler();
		mStatusChecker.run();
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 finish();
			}
		});
		comment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ratingvalue==null || ratingvalue==0.0){
					Toast.makeText(getApplicationContext(), "Please Rate this food..!!!", Toast.LENGTH_SHORT).show();
				}
				else{
				new JsonAddComment().execute();
				new JsonAddRating().execute();
				rb.setFocusable(false);
				rb.setIsIndicator(true);
				rb.setEnabled(false);
				
				}
			}
		});
		if(ratingvalue!=null){
			rb.setFocusable(false);
			rb.setIsIndicator(true);
			rb.setEnabled(false);
		}
		rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				ratingvalue=rb.getRating();
				
			}
		});
		
	}
	@Override
	public void onBackPressed() {

		return;
	           }
	
	class JsonListAdapter extends AsyncTask<String, Void, String>
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
	          
	             nameValuePairs.add(new BasicNameValuePair("foodid",foodid));
	             
	             Log.v(foodid,"got");
	             HttpPost httppost = new HttpPost("http://192.168.0.123/foodadviser/getComment.php");
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
	        	 String foodid[]=new String[jArray.length()];
	        	 String fusername[]=new String[jArray.length()];
	        	 String fcomment[]=new String[jArray.length()];
	        	
	              String arr[]=new String[jArray.length()];
	                for(int i=0;i<jArray.length();i++){
	                        JSONObject json_data = jArray.getJSONObject(i);
	                       
	                      // List all
	                       
	                        String fdid=json_data.getString("foodid");
	                          String funame=json_data.getString("username");
	                        String fComment=json_data.getString("comment");
	                       String rid=json_data.getString("rid");

	                       
	                          	foodid[i]=fdid;
	                          	fusername[i]=funame;
	                          	fcomment[i]=fComment;
	                          	
	                          	 Log.v(fdid, "got");
	                          	 Log.v(funame, "got");
	                          	 Log.v(fComment, "got");
	                          	 
	                          	Foodid=foodid;
	                          	Foodusername=fusername;
	                          	FoodComment=fcomment;
	                          	
	                           
	                          	
	                			}
	                
	                setListAdapter();
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
	public void setListAdapter(){
		
		listcomment.setAdapter(new ListAdapter(this,Foodid,Foodusername,FoodComment));
	}
	
	class JsonAddComment extends AsyncTask<String, Void, String>
	{
		InputStream streamreader;
		String result = "";
		String data = "";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub`


			 try{
				  HttpClient httpclient = new DefaultHttpClient();
		            
		             ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		             nameValuePairs.add(new BasicNameValuePair("fid",foodid));
		             nameValuePairs.add(new BasicNameValuePair("fuser",User));
		             nameValuePairs.add(new BasicNameValuePair("fcomment",etcomment.getText().toString()));
		             nameValuePairs.add(new BasicNameValuePair("rid",restid));
		             
		             
	             HttpPost httppost = new HttpPost("http://192.168.0.123/foodadviser/addComment.php");
	             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	             
	             HttpResponse response = httpclient.execute(httppost);
	             HttpEntity entity = response.getEntity();
	             streamreader = entity.getContent();
	     }catch(Exception e){
	             Log.e("log_tag", "Error in http connection "+e.toString());
	     }
			 try{
	             BufferedReader reader = new BufferedReader(new InputStreamReader(streamreader,"iso-8859-1"),8);
	             StringBuilder sb = new StringBuilder();
	             String line = null;
	             while ((line = reader.readLine()) != null) {
	                     sb.append(line + "\n");
	             }
	             streamreader.close();
	           
	             result=sb.toString();
	             
	     }catch(Exception e){
	             Log.e("log_tag", "Error converting result "+e.toString());
	     }
	    	 
					return result;
					
		}
	
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			
			
			
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
		   super.onPostExecute(result);
		 //  Log.v("result", result);
		  Log.v(result, "got");
		  
		  Toast.makeText(getApplicationContext(), "You commented..."+result, Toast.LENGTH_LONG).show(); 
		  etcomment.setText(null);
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
		//	ProgressDialog loading = ProgressDialog.show(Register.this, "Registering Student", "Please wait...",true,true);

			super.onProgressUpdate(values);
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
		
	}
	Runnable mStatusChecker = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				new JsonListAdapter().execute();
				
	       } finally {
	           
	            mHandler.postDelayed(mStatusChecker, mInterval);
	       }
			
						  }
		};


		class JsonAddRating extends AsyncTask<String, Void, String>
		{
			InputStream streamreader;
			String result = "";
			String data = "";
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub`


				 try{
					  HttpClient httpclient = new DefaultHttpClient();
			            
			             ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			             nameValuePairs.add(new BasicNameValuePair("fid",foodid));
			             nameValuePairs.add(new BasicNameValuePair("fuser",Userid));
			             nameValuePairs.add(new BasicNameValuePair("frating",ratingvalue.toString()));
			             nameValuePairs.add(new BasicNameValuePair("rid",restid));
			            
			             
			             
		             HttpPost httppost = new HttpPost("http://192.168.0.123/foodadviser/addRating.php");
		             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		             
		             HttpResponse response = httpclient.execute(httppost);
		             HttpEntity entity = response.getEntity();
		             streamreader = entity.getContent();
		     }catch(Exception e){
		             Log.e("log_tag", "Error in http connection "+e.toString());
		     }
				 try{
		             BufferedReader reader = new BufferedReader(new InputStreamReader(streamreader,"iso-8859-1"),8);
		             StringBuilder sb = new StringBuilder();
		             String line = null;
		             while ((line = reader.readLine()) != null) {
		                     sb.append(line + "\n");
		             }
		             streamreader.close();
		           
		             result=sb.toString();
		             
		     }catch(Exception e){
		             Log.e("log_tag", "Error converting result "+e.toString());
		     }
		    	 
						return result;
						
			}
		
			
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				
				
				
				
				super.onPreExecute();
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
			   super.onPostExecute(result);
			 //  Log.v("result", result);
			  Log.v(result, "got");
			  
			  Toast.makeText(getApplicationContext(), "You Rated..."+ratingvalue.toString(), Toast.LENGTH_LONG).show(); 
			 
			}
			@Override
			protected void onProgressUpdate(Void... values) {
				// TODO Auto-generated method stub
			//	ProgressDialog loading = ProgressDialog.show(Register.this, "Registering Student", "Please wait...",true,true);

				super.onProgressUpdate(values);
			}
			@Override
			protected void onCancelled() {
				// TODO Auto-generated method stub
				super.onCancelled();
			}
			
		}
		
		
		class JsonGetRating extends AsyncTask<String, Void, String>
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
		          
		             nameValuePairs.add(new BasicNameValuePair("foodid",foodid));
		             nameValuePairs.add(new BasicNameValuePair("userid",Userid));
		             Log.v(foodid,"got");
		             Log.v(Userid,"got");
		             HttpPost httppost = new HttpPost("http://192.168.0.123/foodadviser/getRating.php");
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
				 pDialog = new ProgressDialog(FoodDetails.this);
			        pDialog.setMessage("Please Wait...");
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
		        	 String foodid[]=new String[jArray.length()];
		        	 String userid[]=new String[jArray.length()];
		        	 String frating[]=new String[jArray.length()];
		        	
		              String arr[]=new String[jArray.length()];
		                for(int i=0;i<jArray.length();i++){
		                        JSONObject json_data = jArray.getJSONObject(i);
		                       
		                      // List all
		                       
		                        String fdid=json_data.getString("foodid");
		                          String funame=json_data.getString("userid");
		                        String fRate=json_data.getString("rating");
		                        String rid=json_data.getString("rid");
		                       
		                          	foodid[i]=fdid;
		                          	userid[i]=funame;
		                          	frating[i]=fRate;
		                          	
		                          	 Log.v(fdid, "got");
		                          	 Log.v(funame, "got");
		                          	 Log.v(String.valueOf(fRate), "got");
		                          	 
		                        ratings=frating[i];
		                           
		                        rate=Double.parseDouble(ratings)+rate;
		                        rate=rate/jArray.length();
		                			}
		              
		             // Toast.makeText(getApplicationContext(),String.valueOf(rate) , Toast.LENGTH_SHORT).show();
		         
		              tempr=String.valueOf(rate);
		      		temprate=Float.parseFloat(tempr);
		      		Toast.makeText(getApplicationContext(), String.valueOf(temprate), Toast.LENGTH_SHORT).show();
		      	rb.setRating(temprate);//      food_list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spinne_food_item,R.id.sp_food_item_name,arr));

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

}

