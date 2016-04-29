package com.example.foodadviser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.*;
import com.example.foodadviser.Register.JsonParser;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class Add_Food_Item extends ActionBarActivity  {
	
	Intent i;
	Button additem,browse_img;
	String rname,email,fdname,fdtype,fddetails,image,rid;
	EditText etfdname,etfddetails;
	Spinner spfdtype;
	Bitmap bmap;
	BitmapDrawable drawable;
	ImageView fdimage;
	  private static int RESULT_LOAD_IMAGE = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add__food__item);
		rname=getIntent().getStringExtra("rname");
		email=getIntent().getStringExtra("email");
		rid=getIntent().getStringExtra("Rid");
		etfdname=(EditText)findViewById(R.id.et_food_name);
		etfddetails=(EditText)findViewById(R.id.et_add_food_details);
		spfdtype=(Spinner)findViewById(R.id.sp_add_food_item);
		additem=(Button)findViewById(R.id.btn_ad_itms);
		fdimage=(ImageView)findViewById(R.id.img_food_item);
		browse_img=(Button)findViewById(R.id.btn_browse_img);
		 additem.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new JsonParser().execute();
			
				onBackPressed();
			}
		});

		browse_img.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent i = new Intent(
	                        Intent.ACTION_PICK,
	                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

	                startActivityForResult(i, RESULT_LOAD_IMAGE);
	                
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
	             nameValuePairs.add(new BasicNameValuePair("rname",rname));
	             nameValuePairs.add(new BasicNameValuePair("email",email));
	             nameValuePairs.add(new BasicNameValuePair("foodname",fdname));
	             nameValuePairs.add(new BasicNameValuePair("foodtype",fdtype));
	             nameValuePairs.add(new BasicNameValuePair("details",fddetails));
	             nameValuePairs.add(new BasicNameValuePair("image",image));
	             nameValuePairs.add(new BasicNameValuePair("rid",rid));
	             Log.v(rname, "got");
	             Log.v(email, "got");
	     		 Log.v(fdname,"got" );
	     		 Log.v(fdtype, "got");
	     		 Log.v(fddetails, "got");
	     		 Log.v(image, "image got");
	     		
	     		//Log.v(uploadImage,"got" );
	            // Toast.makeText(getApplicationContext(), usr1, Toast.LENGTH_LONG).show();
	             
	             
	             HttpPost httppost = new HttpPost("http://192.168.0.123/foodadviser/addFoodItem.php");
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
			
			
			fdname=etfdname.getText().toString();
			fddetails=etfddetails.getText().toString();
			fdtype=spfdtype.getSelectedItem().toString();
			
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
		   super.onPostExecute(result);
		 //  Log.v("result", result);
		  Log.v(result, "got");
		  
		  Toast.makeText(getApplicationContext(), "Item Added"+result, Toast.LENGTH_LONG).show(); 
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
	@Override
    public void onBackPressed() {      
        
            super.onBackPressed();      
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

	            ImageView imageView = (ImageView) findViewById(R.id.img_food_item);
	            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
	            
	            fdimage.buildDrawingCache();
				bmap = fdimage.getDrawingCache();
				
	              image=getStringImage(bmap);
	             // Log.v(image, "Image got");
                
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
	
}
	

