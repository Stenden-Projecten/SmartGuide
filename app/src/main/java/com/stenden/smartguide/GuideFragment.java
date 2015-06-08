package com.stenden.smartguide;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;
import com.welcu.android.zxingfragmentlib.BarCodeScannerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuideFragment extends BarCodeScannerFragment implements HttpCallback {
    public static GuideFragment newInstance() {
        return new GuideFragment();
    }

    private Pattern pattern;

    public GuideFragment() {
        pattern = Pattern.compile("\\bGUIDE:(\\d+)\\b");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final HttpCallback c = this;
        this.setmCallBack(new IResultCallback() {
            @Override
            public void result(Result lastResult) {
                Matcher m = pattern.matcher(lastResult.toString());
                if(m.matches()) {
                    new HttpAsyncTask(c).execute("http://school.kuubstudios.com/SmartGuide-backend/api.php?ID=" + m.group(1));
                } else {
                    Toast.makeText(getActivity(), "Onbekende QR code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getRequestedCameraId() {
        return -1;
    }

    @Override
    public void Done(String data) {
        try {
            JSONObject json = new JSONObject(data);

            boolean success = json.getBoolean("success");

            if(success) {
                String text = json.getString("tekst");
                //JSONArray lokalen = json.getJSONArray("lokalen");
                //JSONArray leraren = json.getJSONArray("leraren");

                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            } else {
                String message = json.getString("error");
                Toast.makeText(getActivity(), message != null ? message : "onbekende fout", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e("SmartGuide", e.toString());
        }

    }
}
