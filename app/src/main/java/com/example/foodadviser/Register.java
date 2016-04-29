package com.example.foodadviser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.support.v7.app.ActionBarActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class Register extends ActionBarActivity {
EditText name,email,password;
Button signup;
RadioButton user,restaurant;
String S_email,S_password,S_type="user",S_name,checkemail;
JSONObject j_obj = null ;
RadioGroup usr;
String status = "";
String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
HttpClient httpclient = new DefaultHttpClient();
Controller aController;
public static String Gcmname;
public static String Gcmemail;
AsyncTask<Void, Void, Void> mGCmTask;
ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		name=(EditText)findViewById(R.id.et_reg_name);
		email=(EditText)findViewById(R.id.et_reg_email);
		password=(EditText)findViewById(R.id.et_reg_password);
		user=(RadioButton)findViewById(R.id.rb_user);
		restaurant=(RadioButton)findViewById(R.id.rb_restaurant);
		signup=(Button)findViewById(R.id.btn_signup);
		usr=(RadioGroup)findViewById(R.id.radiouser);
		aController = (Controller) getApplicationContext();
		if (!aController.isConnectingToInternet()) {
			
			// Internet Connection is not present
			aController.showAlertDialog(Register.this,"Internet Connection Error","Please connect to Internet connection", false);
			// stop executing code by return
			return;
		}
		
		
		
				
				
		usr.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId==R.id.rb_user){
					S_type="user";
				}else{
					S_type="restaurant";
				}
			}
		});
		signup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkemail=email.getText().toString().trim();
				 if (emailValidator(checkemail)==true)
			        { 
			          //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
			            // or
			            S_email=checkemail;
			            
						new JsonParser().execute();
			        }
			        else
			        {
			            email.setError("Invalid Email");
			           
			        }
				 if(S_type=="user"){
					 gcmreg();
				 }
				
			}
		});
	}
	public boolean emailValidator(String email) 
	{
	    Pattern pattern;
	    Matcher matcher;
	    final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	    pattern = Pattern.compile(EMAIL_PATTERN);
	    matcher = pattern.matcher(email);
	    return matcher.matches();
	}
	class JsonParser extends AsyncTask<String, Void, String>
	{
		InputStream streamreader;
		String result = "";
		String data = "";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub`

			 try{
	        
				 nameValuePairs.add(new BasicNameValuePair("email",S_email));
	             nameValuePairs.add(new BasicNameValuePair("name",S_name));
	             nameValuePairs.add(new BasicNameValuePair("password",S_password));
	             nameValuePairs.add(new BasicNameValuePair("type",S_type));
	             Gcmname=S_name;
	             Gcmemail=S_email;
	             Log.v(S_email, "got");
	             Log.v(S_name, "got");
	     		 Log.v(S_password,"got" );
	     		 Log.v(S_type,"got" );
	             
	             HttpPost httppost = new HttpPost("http://192.168.0.123/foodadviser/user_reg.php");
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
		S_name=name.getText().toString();
		
		S_password=password.getText().toString();
		
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
		   super.onPostExecute(result);
		 //  Log.v("result", result);
		  Log.v(result, "result_onpost");
		 
		  Toast.makeText(getApplicationContext(), "You are Signed up"+result, Toast.LENGTH_SHORT).show(); 
		Intent i=new Intent(getApplicationContext(),MainActivity.class);
		startActivity(i);
	
		 
		  
		  
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
		public void input(){
			
			try {
					if(status=="valid"){
						
			           
				 }
				
				
						
				} catch(Exception e){
					
				}
		}
		
	}
	public void gcmreg(){
		
		
		
		// Check if GCM configuration is set
				if (Config.YOUR_SERVER_URL == null || Config.GOOGLE_SENDER_ID == null || Config.YOUR_SERVER_URL.length() == 0
						|| Config.GOOGLE_SENDER_ID.length() == 0) {
					
					// GCM sernder id / server url is missing
					aController.showAlertDialog(Register.this, "Configuration Error!",
							"Please set your Server URL and GCM Sender ID", false);
					
					// stop executing code by return
					 return;
				}
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest permissions was properly set 
		GCMRegistrar.checkManifest(this);
		
		// Get GCM registration id
				final String regId = GCMRegistrar.getRegistrationId(this);

				// Check if regid already presents
				if (regId.equals("")) {
					
					// Register with GCM			
					GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);
					
				} else {
					
					// Device is already registered on GCM Server
					if (GCMRegistrar.isRegisteredOnServer(this)) {
						
						// Skips registration.				
						Toast.makeText(getApplicationContext(), "Already registered with GCM Server", Toast.LENGTH_LONG).show();
					
					} else {
						
						// Try to register again, but not in the UI thread.
						// It's also necessary to cancel the thread onDestroy(),
						// hence the use of AsyncTask instead of a raw thread.
						
						final Context context = this;
						mGCmTask = new AsyncTask<Void, Void, Void>() {

							@Override
							protected Void doInBackground(Void... params) {
								
								// Register on our server
								// On server creates a new user
								aController.register(context, S_name, S_email, regId);
								
								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								mGCmTask = null;
							}

						};
						
						// execute AsyncTask
						mGCmTask.execute(null, null, null);
					}
				}
	}
	// Create a broadcast receiver to get message and show on screen 
		private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				
				String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
				
				// Waking up mobile if it is sleeping
				aController.acquireWakeLock(getApplicationContext());
				
				// Display message on the screen
				//lblMessage.append(newMessage + "\n");			
				
				Toast.makeText(getApplicationContext(), "Got Message: " + newMessage, Toast.LENGTH_LONG).show();
				
				// Releasing wake lock
				aController.releaseWakeLock();
			}
		};
	@Override
	protected void onDestroy() {
		// Cancel AsyncTask
		if (mGCmTask != null) {
			mGCmTask.cancel(true);
		}
		try {
			// Unregister Broadcast Receiver
			unregisterReceiver(mHandleMessageReceiver);
			
			//Clear internal resources.
			GCMRegistrar.onDestroy(this);
			
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
