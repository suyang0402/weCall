package com.wecall.myapps.wecall;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Jacob on 11/29/16.
 *
 *  Adapter used to populate the grid view
 *
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<saveContact> contacts = new ArrayList<saveContact>();

    //  Constructor 1
    public ImageAdapter(Context c) {
        mContext = c;
    }


    //  Constructor 2 with array list
    public ImageAdapter(Context c, ArrayList<saveContact> list){
        mContext = c;
        contacts = list;            //  Save the list of contacts
    }



    //  this controls how many grids we get
    public int getCount() {
        return contacts.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        //                  ImageView imageView;

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {



            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_name);
            TextView textView1 = (TextView) grid.findViewById(R.id.grid_last_time_contact);
            TextView textView2 = (TextView) grid.findViewById(R.id.grid_phone_number);
            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_img);

            textView.setText(contacts.get(position).getContact_name());
            textView1.setText(contacts.get(position).getString_last_time_contacted());
            textView2.setText(contacts.get(position).getString_phone_number());

            imageView.setImageResource(mThumbIds[3]);









        } else {
            //              imageView = (ImageView) convertView;
            grid = (View) convertView;
        }

        //              imageView.setImageResource(mThumbIds[position]);
        //              return imageView;
        return grid;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };
}