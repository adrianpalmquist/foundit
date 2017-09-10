package com.esh.group7.foundit;

/**
 * Created by Adrian on 2017-09-09.
 */

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static com.esh.group7.foundit.MainActivity.locationManager;

public class NFCReadFragment extends DialogFragment {

    //private XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
    //private XmlPullParser myparser = xmlFactoryObject.newPullParser();

    public static final String TAG = NFCReadFragment.class.getSimpleName();


    private JSONObject jsonForPost = new JSONObject();
    JSONHandler j = new JSONHandler();

    private String requestURL = "";

    public static NFCReadFragment newInstance() {

        return new NFCReadFragment();
    }

    private TextView mTvMessage;
    private Listener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_read, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ReceiveInfo) context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }

    public void onNfcDetected(Ndef ndef) {

        readFromNFC(ndef);
    }

    private void readFromNFC(Ndef ndef) {

        String message = "";

        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            message = new String(ndefMessage.getRecords()[0].getPayload());
            Log.d(TAG, "readFromNFC: " + message);
            jsonForPost.put("id", message);
            mTvMessage.setText("Success, information about the owner has been collected");
            //new postRequest().execute(jsonForPost);
            ReceiveInfo.setInformation();
            ndef.close();

        } catch (IOException | FormatException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Location location = MainActivity.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
       // String locationString = String.valueOf(location.getLongitude() + ", " + location.getLatitude());

        //Log.d("Location", locationString);

    }

    private class postRequest extends AsyncTask<JSONObject, String, String> {

        @Override
        protected String doInBackground(JSONObject... params) {
            {
                JSONObject obj = params[0];
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                DataOutputStream printout;


                try {
                    URL url = new URL("http://10.0.0.43:8000");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.connect();

                    printout = new DataOutputStream(connection.getOutputStream());
                    String str = obj.toString();
                    byte[] data = str.getBytes("UTF-8");
                    printout.write(data);
                    printout.flush();
                    printout.close();

                    InputStream is = connection.getInputStream();
                    JSONArray resultJSON = j.ParseToJSONArray(is);
                    Log.e("ResultJSON", resultJSON.toString());

                    return null;

                } catch (MalformedURLException e) {
                    Log.e("Fel1", e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("Fel2", e.toString());
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.e("Fel3", e.toString());
                    e.printStackTrace();
                } finally {
                    try {
                        if (connection != null)
                            connection.disconnect();
                        if (reader != null)
                            reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Log.d("blabla", result);
            //ReceiveInfo.setInformation();
        }
    }


    private class getRequestForLocation extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            {

                HttpURLConnection connection = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream is = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is));
                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (connection != null)
                            connection.disconnect();
                        if (reader != null)
                            reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("Location: ", result);
        }
    }
}
