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
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	
    private static final String PREF_NAME = "SpPref";
	private static final String UserName="username";
	private static final String Password="password";	
	private static final String Islogin ="Islogin";
	private static final String Place="place";	
	private static final String Image ="image";
	private static final String Email ="email";
	private static final String UserID ="userid";
	private static final String RestID ="restid";
	String logincheck="2",templogin;
	
	Controller aController;

Button btnlogin;
TextView signup;
EditText etusername,etpassword;
String username,password,spUser="",spPass="",spPlace="",spImage="",tempuser,temppass,tempplace,tempimage,tempemail,tempid,Rid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar =  getSupportActionBar();
        actionBar.hide();
//    	
//		// Check if Internet present
//		if (!aController.isConnectingToInternet()) {
//			
//			// Internet Connection is not present
//			aController.showAlertDialog(MainActivity.this,
//					"Internet Connection Error",
//					"Please connect to Internet connection", false);
//			// stop executing code by return
//			return;
//		}
      final  SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
     templogin= sp.getString(Islogin, "");
    tempemail=sp.getString(Email, "");
        tempimage=sp.getString(Image, "");
        temppass=sp.getString(Password, "");
        tempplace=sp.getString(Place, "");
        tempuser=sp.getString(UserName, "");
        tempid=sp.getString(UserID, "");
        Rid=sp.getString(RestID, "");
        if(templogin.equals("0")){
        	Intent i=new Intent(MainActivity.this,User.class);
        	i.putExtra("name", tempuser);
        	i.putExtra("id",tempid);
        	//Toast.makeText(getApplicationContext(), tempid, Toast.LENGTH_SHORT).show();
        	startActivity(i);
        }
        else if(templogin.equals("1")){
        	Intent i=new Intent(MainActivity.this,RestaurantAdmin.class);
        	
        	i.putExtra("email", tempemail);
			i.putExtra("name", tempuser);
			i.putExtra("place", tempplace);
			i.putExtra("image", tempimage);
			i.putExtra("id", Rid);
        	startActivity(i);
        }
       
        btnlogin=(Button)findViewById(R.id.btn_login);
        signup=(TextView)findViewById(R.id.tv_signup);
        etusername=(EditText)findViewById(R.id.et_login_username);
        etpassword=(EditText)findViewById(R.id.et_login_password);
        signup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent i=new Intent(getApplicationContext(),Register.class);
				startActivity(i);
			}
	
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new JsonParser().execute();
			}
		});
        
        
    }
    class JsonParser extends AsyncTask<String,Void,String>{
    	
    	InputStream stream;
    	String result = "";
    	String data = "";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//ProgressDialog loading = ProgressDialog.show(MainActivity.this, "Loading Content", "Please wait...",true,true);

			try {
				HttpClient httpclient = new DefaultHttpClient();
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	             nameValuePairs.add(new BasicNameValuePair("username",username));
	             nameValuePairs.add(new BasicNameValuePair("password",password));
	             Log.v(username, "got");
	     		Log.v(password,"got" );
	     		 HttpPost httppost = new HttpPost("http://192.168.0.123/foodadviser/login.php");
	             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	             
	             HttpResponse response = httpclient.execute(httppost);
	             HttpEntity entity = response.getEntity();
	             stream = entity.getContent();
				         
			} catch (Exception e) {
				// TODO: handle exception
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
			// TODO Auto-generated method stub
			username=etusername.getText().toString();
			password=etpassword.getText().toString();
			//Dialog dialog =new ProgressDialog(getApplicationContext());
			
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
		   super.onPostExecute(result);
		   Log.v("result", result);
		  Log.v(result, "result");
		  JSONObject j_obj = null ;
			try {
				j_obj=new JSONObject(result);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String status = null,place=null;
			String email=null,type=null,name=null,image=null,id=null;
			try {

				type=j_obj.getString("type");
				name=j_obj.getString("name");
				email=j_obj.getString("email");
				place=j_obj.getString("location");
				status = j_obj.getString("status");
				image = j_obj.getString("image");
				id=j_obj.getString("id");
				Log.v(status, "status");
				Log.v("status", status);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(status.trim().equals("success")&&type.trim().equals("user"))
			{
				
				Toast.makeText(MainActivity.this, "login sucess", Toast.LENGTH_LONG).show();
				Intent i =new Intent(getApplication(),User.class);
				i.putExtra("name", name);
				i.putExtra("id", id);
				spUser=username;
				spPass=password;
				logincheck="0";
				SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
			     SharedPreferences.Editor editor = sp.edit();
			
				editor.putString("Islogin",logincheck);
				editor.putString(UserName, name);
				editor.putString(Password, spPass);
				editor.putString(Place, place);
				editor.putString(Image, image);
				editor.putString(Email, email);
				editor.putString(UserID, id);
				editor.commit();
				//Toast.makeText(MainActivity.this, id, Toast.LENGTH_LONG).show();

				startActivity(i);
				 finish();
			}
			else if(status.trim().equals("success")&&type.trim().equals("restaurant"))
			{
				Toast.makeText(getApplicationContext(), "login sucess", Toast.LENGTH_LONG).show();
				Intent i =new Intent(getApplication(),RestaurantAdmin.class);
				i.putExtra("email", email);
				i.putExtra("name", name);
				i.putExtra("place", place);
				i.putExtra("image", image);
				i.putExtra("id", Rid);
				spUser=username;
				spPass=password;
				logincheck="1";
				SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
				 SharedPreferences.Editor editor = sp.edit();
				
				editor.putString("Islogin",logincheck);
				editor.putString(UserName, name);
				editor.putString(Password, spPass);
				editor.putString(Place, place);
				editor.putString(Image, image);
				editor.putString(Email, email);
				editor.putString(RestID, id);
				editor.commit();
				
				startActivity(i);
				 finish();
			}
			
			 else
			 {
				 Toast.makeText(getApplicationContext(), "login failed"
						 , Toast.LENGTH_LONG).show(); 
			 }
			
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			//ProgressDialog loading = ProgressDialog.show(MainActivity.this, "Logging in", "Please wait...",true,true);

			super.onProgressUpdate(values);
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
        	this.finish();
        	System.exit(0);
            return;
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
}
