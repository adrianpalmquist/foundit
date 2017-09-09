package com.esh.group7.foundit;

/**
 * Created by Adrian on 2017-09-09.
 */

import android.app.DialogFragment;
import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.nio.charset.Charset;

public class NFCWriteFragment extends DialogFragment {

    public static final String TAG = NFCWriteFragment.class.getSimpleName();

    private JSONObject jsonForPost = new JSONObject();
    JSONHandler j = new JSONHandler();

    public static NFCWriteFragment newInstance() {

        return new NFCWriteFragment();
    }

    private TextView mTvMessage;
    private ProgressBar mProgress;
    private Listener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write,container,false);
        initViews(view);

        try {
            jsonForPost.put("name", "Test Testsson");
            jsonForPost.put("phonenumber", "070123456");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new postRequest().execute(jsonForPost);

        return view;
    }

    private void initViews(View view) {
        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (MainActivity) context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }

    public void onNfcDetected(Ndef ndef, String messageToWrite){

        mProgress.setVisibility(View.VISIBLE);
        writeToNfc(ndef,messageToWrite);
    }

    private void writeToNfc(Ndef ndef, String message){

        mTvMessage.setText(getString(R.string.message_write_progress));
        if (ndef != null) {

            try {
                ndef.connect();
                NdefRecord mimeRecord = NdefRecord.createMime("text/plain", message.getBytes(Charset.forName("US-ASCII")));
                ndef.writeNdefMessage(new NdefMessage(mimeRecord));
                ndef.close();
                //Write Successful
                mTvMessage.setText(getString(R.string.message_write_success));

            } catch (IOException | FormatException e) {
                e.printStackTrace();
                mTvMessage.setText(getString(R.string.message_write_error));

            } finally {
                mProgress.setVisibility(View.GONE);
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
                    URL url = new URL(""); //TODO: Set url
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
            //TODO: Handle result
        }
    }
}