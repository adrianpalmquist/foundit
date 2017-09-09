package com.esh.group7.foundit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class JSONHandler{

    /**
     * Parses an inputstream to JSONArray
     * @param in inputstream
     * @return JSONArray from inputstream
     * @throws JSONException
     * @throws IOException
     */
    public JSONArray ParseToJSONArray(InputStream in) throws JSONException, IOException {

        BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder sb = new StringBuilder();

        String inputStr;

        while ((inputStr = streamReader.readLine()) != null)
            sb.append(inputStr);

        return new JSONArray(sb.toString());
    }

    /**
     * Parses an inputstream to JSONObject
     * @param in inputstream
     * @return JSONObject from inputstream
     * @throws JSONException
     * @throws IOException
     */
    public JSONObject ParseToJSONObject(InputStream in) throws JSONException, IOException {

        BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder sb = new StringBuilder();

        String inputStr;

        while ((inputStr = streamReader.readLine()) != null)
            sb.append(inputStr);

        return new JSONObject(sb.toString());
    }
}
