package com.stenden.smartguide;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.google.zxing.Result;
import com.welcu.android.zxingfragmentlib.BarCodeScannerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuideFragment extends BarCodeScannerFragment implements HTTPCallback {
    public static GuideFragment newInstance() {
        return new GuideFragment();
    }
    static final int MSG_DISMISS_DIALOG = 0;
    static final int TIME_OUT = 5000;
    private AlertDialog mAlertDialog;
    private Pattern pattern;

    private HashMap<String, Long> lastTimes = new HashMap<>();

    public GuideFragment() {
        pattern = Pattern.compile("\\bGUIDE:(\\d+)\\b");
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        final HTTPCallback c = this;
        this.setmCallBack(new IResultCallback() {
            @Override
            public void result(Result lastResult) {
                Log.i("SmartGuide", lastResult.toString());
                if(lastTimes.containsKey(lastResult.toString())) {
                    Log.i("SmartGuide", "last scan was " + lastTimes.get(lastResult.toString()));
                    Log.i("SmartGuide", "now is " + System.currentTimeMillis());
                    if((lastTimes.get(lastResult.toString()) + 5000) > System.currentTimeMillis()) {
                        Log.i("SmartGuide", "too soon");
                        return;
                    }
                }

                lastTimes.put(lastResult.toString(), System.currentTimeMillis());

                Matcher m = pattern.matcher(lastResult.toString());
                if(m.matches())
                {
                    new HttpAsyncTask(c).execute("http://school.kuubstudios.com/SmartGuide-backend/api.php?ID=" + m.group(1));
                }
                else
                {
                    Toast.makeText(getActivity(), "Onbekende QR code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_DISMISS_DIALOG:
                    if (mAlertDialog != null && mAlertDialog.isShowing()) {
                        mAlertDialog.dismiss();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private void createDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        mAlertDialog = builder.create();
        mAlertDialog.show();
        // dismiss dialog in TIME_OUT ms
        mHandler.sendEmptyMessageDelayed(MSG_DISMISS_DIALOG, TIME_OUT);
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

                try {
                    JSONObject lokalen = json.getJSONObject("lokalen");
                    Iterator<String> i = lokalen.keys();
                    while (i.hasNext()) {
                        String key = i.next();
                        String value = lokalen.getString(key);
                        text += "\n" + key + ": " + value;
                    }
                } catch(JSONException e) {}

                try {
                    JSONObject leraren = json.getJSONObject("leraren");
                    Iterator<String> i2 = leraren.keys();
                    while (i2.hasNext()) {
                        String key = i2.next();
                        String value = leraren.getString(key);
                        text += "\n" + key + ": " + value;
                    }
                } catch(JSONException e) {}

                //Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                createDialog(text);
            } else {
                String message = json.getString("error");
                Toast.makeText(getActivity(), message != null ? message : "onbekende fout", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e("SmartGuide", e.toString());
        }

    }
}