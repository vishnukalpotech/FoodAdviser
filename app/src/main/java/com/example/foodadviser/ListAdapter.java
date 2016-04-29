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
import android.widget.ListView;
import android.widget.TextView;


public class ListAdapter extends BaseAdapter {
	String [] Fid,Fuser,Fcomment;
    Context context;
    
 
    private static LayoutInflater inflater=null;
    public ListAdapter(FoodDetails useractivity,String[] fid,String[] fuser,String[] fcomment) {
        // TODO Auto-generated constructor stub
        Fid=fid;
        context=useractivity;
        Fcomment=fcomment;
        Fuser=fuser;
        
         inflater = ( LayoutInflater )context.
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int len=Fid.length;
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
	        TextView tvuser,tvcomment;
	        ListView lv;
	        ImageView img;
	    }
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=new Holder();
        View rowView = null;
        
             rowView = inflater.inflate(R.layout.customlistview_comments, null);
             
             holder.tvuser=(TextView) rowView.findViewById(R.id.tv_comment_user);
             holder.tvcomment=(TextView) rowView.findViewById(R.id.tv_comment_comment);
             holder.lv=(ListView)rowView.findViewById(R.id.lv_food_comment);
         holder.tvuser.setText(Fuser[position]);
         holder.tvcomment.setText(Fcomment[position]);

         rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub/
         
            //  Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });

        return rowView;
	}
	
	

}
