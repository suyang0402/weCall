package com.wecall.myapps.wecall;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapContacts extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private ArrayList<ContactsLocation> locations = new ArrayList<ContactsLocation>();



    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_contacts);
        showContacts();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("location", Geocoder.isPresent()+"");
        if (Geocoder.isPresent()) {
            Geocoder geoCoder = new Geocoder(this);
            for (int index = 0; index < locations.size(); index++) {
                if (locations.get(index).getName() != null && locations.get(index).getCity() != null) {
                    try {
                        List<Address> addressList = geoCoder.getFromLocationName(locations.get(index).getCity(), 1);
                        if (addressList != null && addressList.size() > 0) {
                            double lat = addressList.get(0).getLatitude();
                            double lng = addressList.get(0).getLongitude();
                            LatLng berlin = new LatLng(lat, lng);
                            mMap.addMarker(new MarkerOptions().position(berlin).title(locations.get(index).getName() + " lives here."));

                        } else {
                            Log.d("location", "no result for  " + locations.get(index).getCity());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } // end catch
                } // end if

            }
        }else{
            for (int index = 0; index < locations.size(); index++) {
                if (locations.get(index).getName() != null && locations.get(index).getCity() != null) {
                    try {
                            LatLng berlin = getLatLongFromGivenAddress(locations.get(index).getCity());
                            mMap.addMarker(new MarkerOptions().position(berlin).title(locations.get(index).getName() + " lives here."));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } // end catch
                } // end if

            }
        }

        //  Set first camera position to stockholm, not ideal fix later
        LatLng stockholm = new LatLng(59, 18);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(stockholm));
    }



    /**
     * Show the contacts in the ListView.
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            getContactNames();           //  Populate map
        }
    }



    /**
     * Read the name of all the contacts.
     *
     * @return a list of names.
     */
    private void getContactNames() {

//        Cursor phones = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        Cursor address_cursror = getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null, null, null, null);

//        int i=0;
//        while(phones.moveToNext()) {
//            i++;
//            Log.d("location","i: "+i);

            while (address_cursror.moveToNext()) {
                String full = address_cursror.getString(address_cursror.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
                Log.d("location","full: "+full);
                String name = address_cursror.getString(address_cursror.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME));
                String street = address_cursror.getString(address_cursror.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                String state = address_cursror.getString(address_cursror.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                String zip = address_cursror.getString(address_cursror.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                String city = address_cursror.getString(address_cursror.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                ContactsLocation cl = new ContactsLocation(name, full);
                locations.add(cl);
                if (name != null && city != null) {
                    Log.d("Location", city);
                    Log.d("Location", name);
                }

            }

 //       }

        address_cursror.close();
 //       phones.close();

    }


    public static LatLng  getLatLongFromGivenAddress(String youraddress) throws IOException {
        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                youraddress + "&sensor=false";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println(response.body().string());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(response.body().string());

            double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            Log.d("latitude", lat + "");
            Log.d("longitude", lng + "");

            return new LatLng(lat, lng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
