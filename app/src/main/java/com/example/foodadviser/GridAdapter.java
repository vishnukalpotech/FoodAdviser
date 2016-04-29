package com.example.foodadviser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.sax.StartElementListener;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GridAdapter  extends BaseAdapter {
	String [] result,imagesource,images,email,Rid;
	String User,Userid;
    Context context;
    Bitmap[] imgbmp; 
    int[] bmp;
    private static LayoutInflater inflater=null;
    public GridAdapter(User useractivity, String[] prgmNameList, String[] prgmImages,String[] restemail,String  user,String userid,String[] rid) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=useractivity;
        images=prgmImages;
        email=restemail;
        User=user;
        Rid=rid;
        Userid=userid;
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

             rowView = inflater.inflate(R.layout.restaurantlist, null);
             holder.tv=(TextView) rowView.findViewById(R.id.tv_user_rest_name);
             holder.img=(ImageView) rowView.findViewById(R.id.iv_user_rest_image);
            holder. img.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
         holder.tv.setText(result[position]);
         byte[] imageAsBytes = Base64.decode(images[position].getBytes(), Base64.DEFAULT);
         
      holder.img.setImageBitmap(
              BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

         rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
           Intent i=new Intent(context, RestaurantList.class);
           i.putExtra("Rname", result[position]);
           i.putExtra("Remail", email[position]);
           i.putExtra("user", User);
           i.putExtra("userid", Userid);
           i.putExtra("rid", Rid[position]);
          v.getContext().startActivity(i);
            //  Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });

        return rowView;
	}
	
	
}
