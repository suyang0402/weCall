package com.wecall.myapps.wecall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactInfo extends AppCompatActivity {
    private int contactNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);


        //  Get the sent intent
        Intent dogNumber = getIntent();



        //  Test is the variable name 0 is the value if we dont get any sent value with us
        contactNumber = dogNumber.getIntExtra("Test", 0);


        //  Set the text view
        TextView textView = (TextView) findViewById(R.id.contact_info_text1);
        textView.setText("The dog number you have clicked on is: " + contactNumber);



        //  Set the imageview
        ImageView imageView = (ImageView) findViewById(R.id.big_contact_info_pic);

        //  Not the same dog as we clicked on, fix this later
        imageView.setImageResource(mThumbIds[contactNumber]);

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