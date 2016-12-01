package com.wecall.myapps.wecall;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ContactInfo extends AppCompatActivity {
    private String contactNumber;
    private String contactName;
    private String contactLastDate;
    private String contactPhotoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);


        //  Get the sent intent
        Intent dogNumber = getIntent();



        //  Test is the variable name 0 is the value if we dont get any sent value with us
        //contactNumber = dogNumber.getIntExtra("Test", 0);

        contactName = dogNumber.getStringExtra("Name");

        contactNumber = dogNumber.getStringExtra("Number");

        contactLastDate = dogNumber.getStringExtra("LastContact");

        contactPhotoID = dogNumber.getStringExtra("PhotoID");

        //  Set the text view
        TextView textView = (TextView) findViewById(R.id.contact_info_text1);
        //textView.setText("The dog number you have clicked on is: " + contactNumber);
        textView.setText(contactName);


        TextView textView1 = (TextView) findViewById(R.id.contact_info_text2);
        textView1.setText(contactNumber);


        TextView textView2 = (TextView) findViewById(R.id.date_string);
        textView2.setText(contactLastDate);






        //  Set the imageview
        ImageView imageView = (ImageView) findViewById(R.id.big_contact_info_pic);



        if (contactPhotoID != null) {
            imageView.setImageURI(Uri.parse(contactPhotoID));
        } else {
            //  Not the same dog as we clicked on, fix this later
            imageView.setImageResource(mThumbIds[0]);
        }


    }


    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.person
    };


}