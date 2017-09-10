package com.esh.group7.foundit;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Adrian on 2017-09-09.
 */

public class Register extends AppCompatActivity implements Listener {

    public static final String TAG = MainActivity.class.getSimpleName();


    public static LocationManager locationManager;

    NfcAdapter mNfcAdapter;
    private NFCWriteFragment mNfcWriteFragment;

    EditText nameEditText;
    EditText addressEditText;
    EditText phoneEditText;
    EditText emailEditText;

    Button confirm;

    String id;

    private JSONObject jsonForPost = new JSONObject();
    JSONHandler j = new JSONHandler();


    private boolean isDialogDisplayed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        initNFC();

        id = "id-222";

        nameEditText = (EditText) findViewById(R.id.editTextName);
        addressEditText = (EditText) findViewById(R.id.editText2Address);
        phoneEditText = (EditText) findViewById(R.id.editText3Phone);
        emailEditText = (EditText) findViewById(R.id.editText4Mail);

        if (Welcome.dh.getName().length()>1) {
            nameEditText.setText(Welcome.dh.getName());
        }
        if (Welcome.dh.getAddress().length()>1) {
            addressEditText.setText(Welcome.dh.getAddress());
        }
        if (Welcome.dh.getPhone().length()>1) {
            phoneEditText.setText(Welcome.dh.getPhone());
        }
        if (Welcome.dh.getMail().length()>1) {
            emailEditText.setText(Welcome.dh.getMail());
        }

        nameEditText.setBackgroundColor(Color.WHITE);
        addressEditText.setBackgroundColor(Color.WHITE);
        phoneEditText.setBackgroundColor(Color.WHITE);
        emailEditText.setBackgroundColor(Color.WHITE);

        confirm = (Button) findViewById(R.id.buttonRegisterPerson);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jsonForPost.put("id", id);
                    jsonForPost.put("name", nameEditText.getText().toString());
                    jsonForPost.put("address", addressEditText.getText().toString());
                    jsonForPost.put("phone", phoneEditText.getText().toString());
                    jsonForPost.put("email", emailEditText.getText().toString());
                    jsonForPost.put("lat", "");
                    jsonForPost.put("lon", "");
                    Welcome.dh.setName(nameEditText.getText().toString());
                    Welcome.dh.setAddress(addressEditText.getText().toString());
                    Welcome.dh.setPhone(phoneEditText.getText().toString());
                    Welcome.dh.setMail(emailEditText.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new postRequest().execute(jsonForPost);

                showWriteFragment();


            }
        });


    }

    private void initNFC() {

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    private void showWriteFragment() {


        mNfcWriteFragment = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);

        if (mNfcWriteFragment == null) {

            mNfcWriteFragment = NFCWriteFragment.newInstance();
        }
        mNfcWriteFragment.show(getFragmentManager(), NFCWriteFragment.TAG);

    }

    @Override
    public void onDialogDisplayed() {
        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {
        isDialogDisplayed = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected, tagDetected, ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d(TAG, "onNewIntent: " + intent.getAction());

        if (tag != null) {
            Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);

            if (isDialogDisplayed) {

                String messageToWrite = id;
                mNfcWriteFragment = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);
                if (ndef == null) {
                    Log.e(TAG, "ndef is null when writing");
                        return;
                }
                mNfcWriteFragment.onNfcDetected(ndef, messageToWrite);

            }
        }
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
                    Log.d("Register", "Setting up connection");
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
        }
    }

}
