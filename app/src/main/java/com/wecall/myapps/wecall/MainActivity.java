package com.wecall.myapps.wecall;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridView mGridView;                          // Grid view we populate
    private ArrayList<saveContact> contacts;             //  We save all the contacts here

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //  Find and set hero picture
        ImageView imageView = (ImageView) findViewById(R.id.heropicture);
        imageView.setImageResource(R.drawable.person);

        //  Find and set the gridview
        this.mGridView = (GridView) findViewById((R.id.gridview));
        showContacts();  // This method saves all contacts in the ArrayList of saveContact contacts
        mGridView.setAdapter(new ImageAdapter(this, contacts));     //  Brings everything into adapter and populate the gridview with adapter


        //  When item is clicked on gridView
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                //  Communicate what new activity to start by sending the intent
                //  Send extra variable with put extra
                //  Start new activity
                Intent intent = new Intent(MainActivity.this, ContactInfo.class);
                intent.putExtra("Name", contacts.get(position).getContact_name());
                intent.putExtra("Number", contacts.get(position).getString_phone_number());
                intent.putExtra("LastContact", contacts.get(position).getCorrectlyFormatedDate());
                intent.putExtra("PhotoID", contacts.get(position).getPhotoID());
                startActivity(intent);

            }
        });


    }


    /**
     * Show the contacts in the ListView.
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            contacts = getContactNames();           //  Saves names into our ArrayList of contacts
        }
    }


    //  Request permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();             //  Call show contacts again to populate list
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Read the name of all the contacts.
     *
     * @return a list of names.
     */
    private ArrayList<saveContact> getContactNames() {
        ArrayList<saveContact> contacts = new ArrayList<>();                //  ArrayList contacts

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {


            //  Gets infirmation we want
            String contactName=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String contactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String lastTime= phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED));
            String photoId = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));


            //  Converts epoch time  in string_last_time_contacted to readable date
            String epochString = lastTime;
            long epoch = Long.parseLong(epochString);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = sdf.format(epoch);


            //  Constructs saveContacts object s1 and put it in the arraylist of contacts
            saveContact s1 = new saveContact(contactName, lastTime,contactNumber, dateString, photoId);
            contacts.add(s1);

        }
        phones.close();



        return contacts;        //  Returns arraylist of contacts
    }



}
