package com.example.foodadviser;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class NewFoodAdapter extends BaseAdapter {
	String [] result,imagesource,images,email,type,details,id;
	String User,Userid,Rid;
    Context context;
    
 
    private static LayoutInflater inflater=null;
    public NewFoodAdapter(NewFood useractivity, String[] prgmNameList, String[] prgmImages,String[] restemail,String[] ftype,String[] fdetails,String[] foodid) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        //User=user;
        context=useractivity;
        images=prgmImages;
        email=restemail;
        type=ftype;
        details=fdetails;
        id=foodid;
      //  Rid=rid;
       // Userid=userid;
         inflater = ( LayoutInflater )context.
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int len=result.length;
		return len;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	 public class Holder
	    {
	        TextView tv;
	        ImageView img;
	    }
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=new Holder();
        View rowView = null;
        
             rowView = inflater.inflate(R.layout.foodlist, null);
             holder.tv=(TextView) rowView.findViewById(R.id.tv_foodlistt_name);
             holder.img=(ImageView) rowView.findViewById(R.id.iv_foodlist_image);
         holder. img.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
         holder.tv.setText(result[position]);
         byte[] imageAsBytes = Base64.decode(images[position].getBytes(), Base64.DEFAULT);
         
      holder.img.setImageBitmap(
              BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

         rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub/
           Intent i=new Intent(context, FoodDetails.class);
           i.putExtra("fname", result[position]);
           i.putExtra("ftype", type[position]);
           i.putExtra("fdetails", details[position]);
           i.putExtra("fimage", images[position]);
           i.putExtra("fid", id[position]);
           i.putExtra("user", User);
           i.putExtra("userid", Userid);
           i.putExtra("rid", Rid);
          v.getContext().startActivity(i);
            //  Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });

        return rowView;
	}
}
