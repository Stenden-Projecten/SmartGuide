package com.stenden.smartguide;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpAsyncTask extends AsyncTask<String, Void, String> {
    public static String Get(String url) {
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            InputStream inputStream = httpResponse.getEntity().getContent();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            while((line = bufferedReader.readLine()) != null) result += line;

            inputStream.close();
        } catch(Exception e) {
            Log.e("SmartGuide HTTP", e.toString());
        }

        return result;
    }

    private HTTPCallback callback;

    public HttpAsyncTask(HTTPCallback c) {
        callback = c;
    }

    @Override
    protected String doInBackground(String... urls) {
        return Get(urls[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        if(callback != null) {
            callback.Done(result);
        }
    }
}