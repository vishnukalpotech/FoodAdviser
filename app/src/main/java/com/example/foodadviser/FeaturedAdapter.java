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
public class FeaturedAdapter extends BaseAdapter {
	String [] result,imagesource,images,email,Rid,Rating;
	String User,Userid;
    Context context;
    Bitmap[] imgbmp; 
    int[] bmp;
    private static LayoutInflater inflater=null;
    public FeaturedAdapter(User useractivity, String[] restname, String[] restimage,String[] restemail,String  user,String userid,String[] rid,String[] rating) {
        // TODO Auto-generated constructor stub
        result=restname;
        context=useractivity;
        images=restimage;
        email=restemail;
        User=user;
        Rid=rid;
        Userid=userid;
        Rating=rating;
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
	        TextView tvName,tvRate;
	        ImageView img;
	    }
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=new Holder();
        View rowView = null;

             rowView = inflater.inflate(R.layout.featuredrestaurantlist, null);
             holder.tvName=(TextView) rowView.findViewById(R.id.tv_frestname);
             holder.tvRate=(TextView) rowView.findViewById(R.id.tv_frestRate);
             holder.img=(ImageView) rowView.findViewById(R.id.iv_frestimage);
            holder. img.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
         holder.tvName.setText(result[position]);
         holder.tvRate.setText("Rate "+Rating[position]);
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
