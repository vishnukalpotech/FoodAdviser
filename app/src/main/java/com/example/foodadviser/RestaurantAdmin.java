package com.example.foodadviser;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantAdmin extends ActionBarActivity  implements Runnable{
String name,email,fdtpe,Rplace="",url,getplace,imagestring="",getimage,rid;

TextView tvemail;
EditText etname;
Button add,update,logout;
Spinner spfditem,sp_rest_type;
String[] search_text;
AutoCompleteTextView atplace;
ArrayList<String> place;
ArrayAdapter<String> adp;
Bitmap bmap,getbitmap;
ImageView imgrest;
BitmapDrawable drawable;
	JSONObject json;
	JSONArray contacts = null;
String browserKey="AIzaSyBkN2AtKv1UePN2sncriq6rt1-pyCv1TXU";
private static int RESULT_LOAD_IMAGE = 1;
private static final String TAG = "MAIN_ACTIVITY_ASYNC";
private static final String TAG_RESULT = "predictions";
private int mInterval = 10000; // 10 seconds by default, can be changed later
private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_admin);
		
		new JsonGet().execute();
		mHandler = new Handler();
		mStatusChecker.run();
		sp_rest_type=(Spinner)findViewById(R.id.sp_foodtype);
		etname=(EditText)findViewById(R.id.et_rest_admin_email);
		tvemail=(TextView)findViewById(R.id.tv_email);
		name=getIntent().getStringExtra("name");
		email=getIntent().getStringExtra("email");
		getplace=getIntent().getStringExtra("place");
		getimage=getIntent().getStringExtra("image");
		rid=getIntent().getStringExtra("id");
		spfditem=(Spinner)findViewById(R.id.sp_food_list);
		etname.setText(name);
		tvemail.setText(email);
		imgrest=(ImageView)findViewById(R.id.iv_rest_image);
		
		logout=(Button)findViewById(R.id.btn_rest_logout);
		add=(Button)findViewById(R.id.btn_rest_add);
		atplace=(AutoCompleteTextView)findViewById(R.id.auto_place);
		atplace.setText(getplace);
		Rplace=getplace;
		atplace.setThreshold(0);
		getbitmap=Base64ToBitmap(getimage);
		imgrest.setImageBitmap(getbitmap);
		imgrest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent i = new Intent(
	                        Intent.ACTION_PICK,
	                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

	                startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),MainActivity.class);
				finish();
				startActivity(i);
			}
		});
		atplace.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				search_text= atplace.getText().toString().split(",");
				   url="https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+search_text[0]+"&location=37.76999,-122.44696&radius=500&sensor=true&key="+browserKey;
				   if(search_text.length<=1){
					   place=new ArrayList<String>();
					   Log.d("URL",url);
						paserdata parse=new paserdata();
						parse.execute();
						Rplace=atplace.getText().toString();
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
				Rplace=atplace.getText().toString();
			}
		});
		update=(Button)findViewById(R.id.btn_rest_update);
		
	
		
		add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent(getApplicationContext(),Add_Food_Item.class);
				i.putExtra("email", email);
				i.putExtra("rname", etname.getText().toString());
				i.putExtra("Rid", rid);
			startActivity(i);
			}
		});
		update.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new JsonParser().execute();
			}
		});
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
	             HttpClient httpclient = new DefaultHttpClient();
	            
	             ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	             nameValuePairs.add(new BasicNameValuePair("name",etname.getText().toString()));
	             nameValuePairs.add(new BasicNameValuePair("email",email));
	             nameValuePairs.add(new BasicNameValuePair("type",fdtpe));
	             nameValuePairs.add(new BasicNameValuePair("location",Rplace));
	             nameValuePairs.add(new BasicNameValuePair("image",imagestring));
	             Log.v(etname.getText().toString(), "got");
	             Log.v(email, "got");
	             Log.v(fdtpe, "got");
	             Log.v(Rplace, "got");
	             Log.v(imagestring, "got");
	     		//Log.v(uploadImage,"got" );
	            // Toast.makeText(getApplicationContext(), usr1, Toast.LENGTH_LONG).show();
	             
	             
	             HttpPost httppost = new HttpPost("http://192.168.0.123/foodadviser/updateUser.php");
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
			
			
			fdtpe=sp_rest_type.getSelectedItem().toString();
			
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
		   super.onPostExecute(result);
		 //  Log.v("result", result);
		  Log.v(result, "got");
		  
		  Toast.makeText(getApplicationContext(), "Profile Updated..."+result, Toast.LENGTH_LONG).show(); 
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
	class JsonGet extends AsyncTask<String, Void, String>
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
	          
	             nameValuePairs.add(new BasicNameValuePair("email",email));
	             
	             
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
	              //JSONArray jArray = new JSONArray(result);
	       
	              String arr[]=new String[jArray.length()];
	                for(int i=0;i<jArray.length();i++){
	                        JSONObject json_data = jArray.getJSONObject(i);
	                       
	                      // List all
	                       
	                    // int id=json_data.getInt("id");
	                          String fname=json_data.getString("foodname");
	                        String name=json_data.getString("rname");
	                        String email=json_data.getString("email");
	                       String type=json_data.getString("foodtype");
	                       String details=json_data.getString("fooddetails");
	                   arr[i]=fname;

	                       Log.v(fname, "got");
	                          	
		}
                    spfditem.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spinne_food_item,R.id.sp_food_item_name,arr));

	       // lst.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arr));        
			}catch(Exception e)
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
	Runnable mStatusChecker = new Runnable() {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			new JsonGet().execute();
       } finally {
           
            mHandler.postDelayed(mStatusChecker, mInterval);
       }
		
					  }
	};
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
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
					place.add(description);
				
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
				    android.R.layout.simple_list_item_1, place) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
				    View view = super.getView(position, convertView, parent);
				    TextView text = (TextView) view.findViewById(android.R.id.text1);
				      text.setTextColor(Color.BLACK);
				    return view;
				  }
				};
			
			
			atplace.setAdapter(adp);	
			
		
		}
		
		}
@Override
public void onBackPressed() {

               return;
           }
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        ImageView imageView = (ImageView) findViewById(R.id.iv_rest_image);
        imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        
        imgrest.buildDrawingCache();
		bmap = imgrest.getDrawingCache();
		
          imagestring=getStringImage(bmap);
         Log.v(imagestring, "Image got");
        
//Toast.makeText(getApplicationContext(), image, Toast.LENGTH_SHORT).show();
    }


}
public String getStringImage(Bitmap bmp){
	 ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bmp.compress(CompressFormat.JPEG, 70, stream);
	    byte[] byteFormat = stream.toByteArray();
	    // get the base 64 string
	    String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

	    return imgString;
}
Bitmap Base64ToBitmap(String myImageData)
{
	
    byte[] imageAsBytes = Base64.decode(myImageData.getBytes(),Base64.DEFAULT);
    return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
}

}
