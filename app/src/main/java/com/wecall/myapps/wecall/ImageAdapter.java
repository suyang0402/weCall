package com.wecall.myapps.wecall;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);


            //  Had problems with gridview because i put all my code here
            /*
             I think the behavior you see its normal because all you do in your adapter is populating the first visible elements
              and then the adapter will be reusing the exact same elements when you scroll up and down because of the recycling.
              http://stackoverflow.com/questions/10672273/why-items-change-order-upon-scrolling-in-android-gridview
             */







        } else {

            grid = (View) convertView;
        }
        TextView textView = (TextView) grid.findViewById(R.id.grid_name);
        TextView textView1 = (TextView) grid.findViewById(R.id.grid_last_time_contact);
       // TextView textView2 = (TextView) grid.findViewById(R.id.grid_phone_number);
        ImageView imageView = (ImageView) grid.findViewById(R.id.grid_img);




        textView.setText(contacts.get(position).getContact_name());
        textView1.setText(contacts.get(position).getCorrectlyFormatedDate());
        //textView2.setText(contacts.get(position).getString_phone_number());


        if (contacts.get(position).getPhotoID() != null) {
            Uri photoUri =Uri.parse(contacts.get(position).getPhotoID());
            try {
                Bitmap photoBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(),photoUri);
                imageView.setImageBitmap(MainActivity.cropToSquare(photoBitmap));
            } catch (IOException e) {
                e.printStackTrace();
                imageView.setImageResource(mThumbIds[0]);
            }

        } else {
            imageView.setImageResource(mThumbIds[0]);
        }

        return grid;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.person
    };
}