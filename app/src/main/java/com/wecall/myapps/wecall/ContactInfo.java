package com.wecall.myapps.wecall;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.wecall.myapps.wecall.MainActivity.cropToSquare;

public class ContactInfo extends AppCompatActivity {
    private String contactNumber;
    private String contactName;
    private String contactLastDate;
    private String contactPhotoID;
    private String contactAddress;


    private double lng;
    private double lat;

    private int timezoneOffset;

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
        contactAddress = dogNumber.getStringExtra("Address");


        //  Set the text view
        TextView textView = (TextView) findViewById(R.id.contact_info_text1);
        //textView.setText("The dog number you have clicked on is: " + contactNumber);
        textView.setText(contactName);


        TextView textView1 = (TextView) findViewById(R.id.contact_info_text2);
        textView1.setText(contactNumber);


        TextView textView2 = (TextView) findViewById(R.id.date_string);
        textView2.setText(contactLastDate);

        TextView textView3 = (TextView) findViewById(R.id.address_string);
        textView3.setText(contactAddress);

        TextView textView4 = (TextView) findViewById(R.id.localTime_string);
        textView4.setText("");


        FloatingActionButton buttonMap = (FloatingActionButton) findViewById(R.id.button_map);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Create new intent for starting the map activity
                Intent intent = new Intent(ContactInfo.this, ContactMapActivity.class);
                intent.putExtra("Name", contactName);
                intent.putExtra("Address", contactAddress);
                intent.putExtra("Lat", lat);
                intent.putExtra("Lng", lng);
                //  Start activity
                startActivity(intent);
            }
        });


        //  Set the imageview
        ImageView imageView = (ImageView) findViewById(R.id.big_contact_info_pic);


//        if (contactPhotoID != null) {
//            imageView.setImageURI(Uri.parse(contactPhotoID));
//        } else {
//            //  Not the same dog as we clicked on, fix this later
//            imageView.setImageResource(mThumbIds[0]);
//        }

        if (contactPhotoID != null) {
            Uri photoUri = Uri.parse(contactPhotoID);
            try {
                Bitmap photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                BitmapDrawable photoDrawable = new BitmapDrawable(getResources(), cropToSquare(photoBitmap));
                imageView.setBackgroundDrawable(photoDrawable);
            } catch (IOException e) {
                e.printStackTrace();
                imageView.setBackgroundResource(R.drawable.person);

            }

        } else {
            imageView.setBackgroundResource(R.drawable.person);


        }


        if (Geocoder.isPresent()) {
            Geocoder geoCoder = new Geocoder(this);
            List<Address> addressList = null;
            try {
                addressList = geoCoder.getFromLocationName(contactAddress, 1);
                if (addressList != null && addressList.size() > 0) {
                    double lat = addressList.get(0).getLatitude();
                    double lng = addressList.get(0).getLongitude();
                    getTimezoneOffset();
                } else {
                    Log.d("location", "no result for  " + contactAddress);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            String uri = "http://maps.google.com/maps/api/geocode/json?address=" + contactAddress + "&sensor=false";
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(uri)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String respstring = response.body().string();
                    Log.d("http", respstring);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject = new JSONObject(respstring);

                        lng = (double) ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lng");

                        lat = (double) ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lat");

                        getTimezoneOffset();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void getTimezoneOffset(){
        String uri =  "https://maps.googleapis.com/maps/api/timezone/json?location="+lat+","+lng+"&timestamp="+Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis()/1000;


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(uri)
                .build();
        client.newCall(request).enqueue(new Callback() {
            Handler mainHandler = new Handler(getApplicationContext().getMainLooper());
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);
                String respstring = response.body().string();
                Log.d("http", respstring);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(respstring);

                    timezoneOffset = (int) ( jsonObject.get("rawOffset"));

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView4 = (TextView) findViewById(R.id.localTime_string);
                            DateFormat sdf = new SimpleDateFormat("EEEE HH:mm");
                            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                            Log.d("time",(System.currentTimeMillis()/1000+timezoneOffset)+"");
                            textView4.setText(sdf.format(new Date(Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis()+timezoneOffset*1000)));
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }





    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.person
    };


}