package com.wecall.myapps.wecall;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridView mGridView;                          // Grid view we populate
    private ArrayList<saveContact> contacts;             //  We save all the contacts here

    private String heroPhoneNumber;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 200;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //  Find and set hero picture
        ImageView imageView = (ImageView) findViewById(R.id.heropicture);
        imageView.setBackgroundResource(R.drawable.person);

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

        recommand();

        FloatingActionButton fabRecommand = (FloatingActionButton) findViewById(R.id.fabRecommand);
        fabRecommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recommand();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    //refresh hero
    private void recommand() {
        ImageView imageView = (ImageView) findViewById(R.id.heropicture);
        final saveContact heroContact = contacts.get((int) (Math.random() * (contacts.size() - 1)));
        if (heroContact.getPhotoID() != null) {
            Uri photoUri = Uri.parse(heroContact.getPhotoID());
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
        TextView heroName = (TextView) findViewById(R.id.hero_name);
        heroName.setText(heroContact.getContact_name());
     //   TextView heroLastTime = (TextView) findViewById(R.id.hero_last_time_contact);
     //   heroLastTime.setText(heroContact.getCorrectlyFormatedDate());

        heroPhoneNumber = heroContact.getString_phone_number();
        FloatingActionButton fabCall = (FloatingActionButton) findViewById(R.id.fabCall);
        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check the SDK version and whether the permission is already granted or not.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL_PHONE);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                } else {
                    // Android version is lesser than 6.0 or the permission is already granted.
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + heroContact.getString_phone_number()));
                    startActivity(intent);
                }

            }
        });
    }

    // crop image to square
    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
        return cropImg;
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
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + heroPhoneNumber));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot call", Toast.LENGTH_SHORT).show();
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

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {


            //  Gets infirmation we want
            String contactName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String contactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String lastTime = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED));
            String photoId = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));


            //  Converts epoch time  in string_last_time_contacted to readable date
            String epochString = lastTime;
            long epoch = Long.parseLong(epochString);
            String dateString;
            if (epoch == 0) {
                dateString = "Long time ago";
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                dateString = sdf.format(epoch);
            }


            //  Constructs saveContacts object s1 and put it in the arraylist of contacts
            saveContact s1 = new saveContact(contactName, lastTime, contactNumber, dateString, photoId);
            contacts.add(s1);

        }
        phones.close();
        Collections.sort(contacts, new Comparator<saveContact>() {
            public int compare(saveContact o1, saveContact o2) {
                return Long.compare(Long.parseLong(o2.getString_last_time_contacted()), Long.parseLong(o1.getString_last_time_contacted()));
            }
        });


        return contacts;        //  Returns arraylist of contacts
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
