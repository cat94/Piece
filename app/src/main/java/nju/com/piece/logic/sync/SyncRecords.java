package nju.com.piece.logic.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import nju.com.piece.database.DBFacade;
import nju.com.piece.database.TagType;
import nju.com.piece.database.helpers.PeriodDBHelper;
import nju.com.piece.database.pos.PeriodPO;
import nju.com.piece.database.pos.TagPO;
import nju.com.piece.logic.net.CallService;
import nju.com.piece.logic.update.GetServerUrl;

/**
 * Created by Hyman on 2015/6/9.
 *
 * Interact with the background to sync the records
 */

public class SyncRecords {

    private static final String urlString = GetServerUrl.getUrl() + "index.php?r=period/sync";
    private static final String TAG = "SyncRecords";

    private ProgressBar progressBar;
    private Context context;

    public SyncRecords(Context context, ProgressBar progressBar) {
        this.progressBar = progressBar;
        this.context = context;
    }

    public void sync() {
        Log.i(TAG, "call sync");
        new SyncRecordsTask().execute();
    }

    class SyncRecordsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            ArrayList<PeriodPO> localPeriods;    //records on this phone
            //first, get all records on this machine
            PeriodDBHelper periodDBHelper = PeriodDBHelper.instance(context);
            localPeriods = periodDBHelper.getAllPeriods();
            DBFacade dbFacade=new DBFacade(context);
            ArrayList<TagPO> tagList= (ArrayList<TagPO>) dbFacade.getAllTags();
            //package them to json
            JSONObject tosendsObject = new JSONObject();
            Log.i(TAG, "start put json!");
            JSONArray tagArray = new JSONArray();
            JSONArray periodArray = new JSONArray();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                //add tags
                for (TagPO tag : tagList) {
                    JSONObject tagObject = new JSONObject();
                    tagObject.put("tagName", tag.getTagName());
                    tagObject.put("tagType",tag.getType());
                    tagObject.put("targetMinute",tag.getTargetMinute());
                    tagObject.put("resource",tag.getResource());
                    tagObject.put("startDate",sdf.format(tag.getStartDate()));
                    tagObject.put("endDate",sdf.format(tag.getEndDate()));
                    tagArray.put(tagObject);
                }
                tosendsObject.put("tags", tagArray);
                //add periods
                for (PeriodPO peroid : localPeriods) {
                    JSONObject periodObject = new JSONObject();
                    periodObject.put("date", sdf.format(peroid.getDate()));
                    periodObject.put("tag", peroid.getTag());
                    periodObject.put("length", peroid.getLength());
                    periodArray.put(periodObject);
                }
                tosendsObject.put("periods", periodArray);
                //add account info
                tosendsObject.put("username", dbFacade.getAccount().getName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //change json to String
            String content = String.valueOf(tosendsObject);
            Log.i(TAG, "send :" + content);
            String responseData = CallService.call(urlString, content, context);
            if (responseData == null || responseData.equals("")) {
                return false;
            } else {
                Log.i(TAG, "res:" + responseData);
                JSONObject resultObject = null;
                try {
                    resultObject = new JSONObject(responseData);
                    String toAddTags = resultObject.getString("toAddTag");
                    //get the tags to add to this phone
                    JSONArray addTags = new JSONArray(toAddTags);
                    Log.i(TAG, "to add tags:" + addTags.length());
                    if (addTags.length() != 0) {
                        for (int i = 0; i < addTags.length(); i++) {
                            JSONObject object = addTags.getJSONObject(i);
                            String tagName=object.getString("tagName");
                            String type=object.getString("tagType");
                            int resource=Integer.valueOf(object.getString("resource"));
                            int targetMinute=Integer.valueOf(object.getString("targetMinute"));
                            Date startDate= sdf.parse(object.getString("startDate"));
                            Date endDate = sdf.parse(object.getString("endDate"));
                            TagPO tagPO=new TagPO(tagName, TagType.valueOf(type),resource,targetMinute,endDate);
                            tagPO.setStartDate(startDate);
                            dbFacade.addTag(tagPO);
                        }
                    }
                    String toAddPeriods = resultObject.getString("toAddPeriod");
                    //get the records to add to this phone
                    JSONArray addperiods = new JSONArray(toAddPeriods);
                    Log.i(TAG, "to add periods:" + addperiods.length());
                    if (addperiods.length() != 0) {
                        for (int i = 0; i < addperiods.length(); i++) {
                            JSONObject object = addperiods.getJSONObject(i);
                            String tag = object.getString("tag");
                            Date date = sdf.parse(object.getString("date"));
                            int length = Integer.valueOf(object.getString("length"));
                            dbFacade.addPeriod(new PeriodPO(tag, length, date));
                        }
                    }

                    int addedTagNum = Integer.valueOf(resultObject.getString("addedTagNum"));     //the number of tags that added on the cloud
                    Log.i(TAG, "addedTagNum:" + addedTagNum);
                    int addedPeriodNum = Integer.valueOf(resultObject.getString("addedPeriodNum"));     //the number of periodsthat added on the cloud
                    Log.i(TAG, "addedPeriodNum:" + addedPeriodNum);
                    Log.i(TAG, "本机添加标签个数:" + addTags.length());
                    Log.i(TAG, "本机添加记录个数:" + addperiods.length());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            if(progressBar!=null) {
                progressBar.setVisibility(View.VISIBLE);    //show the progressBar
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(progressBar!=null) {
                progressBar.setVisibility(View.GONE);    //hide the progressBar
            }
            if (!result) {
                CallService.showNetErr(context);
                return;
            }
            super.onPostExecute(result);
        }
    }
}