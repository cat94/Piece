package nju.com.piece.logic.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nju.com.piece.database.helpers.PeriodDBHelper;
import nju.com.piece.database.pos.PeriodPO;
import nju.com.piece.logic.net.CallService;
import nju.com.piece.logic.update.GetServerUrl;

/**
 * Created by Hyman on 2015/6/9.
 * <p/>
 * Interact with the background to sync the records
 */

public class SyncRecords {

    private static final String urlString = GetServerUrl.getUrl() + "index.php?r=period/sync";
    private static final String TAG = "SyncRecords";

    private ProgressBar progressBar;
    private Context context;

    public SyncRecords(Context context) {
        this.context = context;
    }

    public void sync() {
        Log.i(TAG, "call sync");
        new SyncRecordsTask().execute();
    }

    class SyncRecordsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<PeriodPO> localPeriods;    //records on this phone
            //first, get all records on this machine
            PeriodDBHelper periodDBHelper = PeriodDBHelper.instance(context);
            localPeriods = periodDBHelper.getAllPeriods();
            //package them to json
            JSONObject tosendsObject = new JSONObject();
            Log.i(TAG, "start put json!");
            JSONArray jsonArray = new JSONArray();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                for (PeriodPO peroid : localPeriods) {
                    JSONObject periodObject = new JSONObject();
                    periodObject.put("date", sdf.format(peroid.getDate()));
                    periodObject.put("tag", peroid.getTag());
                    periodObject.put("length", peroid.getLength());
                    jsonArray.put(periodObject);
                }
                tosendsObject.put("periods", jsonArray);
                //add account info
                tosendsObject.put("username", "test");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //change json to String
            String content = String.valueOf(tosendsObject);
            Log.i(TAG, "send :" + content);
            String responseData = CallService.call(urlString, content);
            Log.i(TAG, "res:" + responseData);
            JSONObject resultObject = null;
            try {
                resultObject = new JSONObject(responseData);
                String toAdd = resultObject.getString("toadd");
                //get the records to add to this phone
                JSONArray addperiods = new JSONArray(toAdd);
                Log.i(TAG, "to add:" + addperiods.length());
                if (addperiods.length() != 0) {
                    for (int i = 0; i < addperiods.length(); i++) {
                        JSONObject object = addperiods.getJSONObject(i);
                        String tag = object.getString("tag");
                        Date date = sdf.parse(object.getString("date"));
                        int length = Integer.valueOf(object.getString("length"));
                        PeriodDBHelper.instance(context).addPeriod(new PeriodPO(tag, length, date));
                    }
                }
                int added = Integer.valueOf(resultObject.getString("added"));     //the number that added on the cloud
                Log.i(TAG, "added:" + added);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            // progressBar.setVisibility(View.VISIBLE);    //show the progressBar
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            //   progressBar.setVisibility(View.GONE);    //hide the progressBar
        }
    }

}
