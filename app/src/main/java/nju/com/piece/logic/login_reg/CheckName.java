package nju.com.piece.logic.login_reg;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import nju.com.piece.logic.net.CallService;
import nju.com.piece.logic.update.GetServerUrl;

/**
 * Created by Hyman on 2015/6/11.
 */

public class CheckName {

    private static final String urlString = GetServerUrl.getUrl() + "index.php?r=period/checkName";
    private static final String TAG = "CheckName";
    private Context context;
    private EditText editText;
    private String userName;

    public CheckName( Context context,EditText editText) {
        this.context = context;
        this.editText=editText;
    }

    public void check(String userName) {
        Log.i(TAG, "call check");
        this.userName=userName;
        new CheckTask().execute();
    }

    class CheckTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            JSONObject tosendsObject = new JSONObject();
            Log.i(TAG, "start put json!");
            try {
                tosendsObject.put("username", userName);
                //add account info
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //change json to String
            String content = String.valueOf(tosendsObject);
            Log.i(TAG, "send :" + content);
            String responseData = CallService.call(urlString, content);
            Log.i(TAG, "res:" + responseData);
            JSONObject resultObject = null;
            String result="";
            try {
                resultObject = new JSONObject(responseData);
                result = resultObject.getString("result");
                Log.i(TAG, "result:" + result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String  result) {
            if(result.equals("true")) {
               // Toast.makeText(context, " can be registered!", Toast.LENGTH_SHORT).show();
            }
            else{
                editText.setText(userName+"has been registered!");
            }
            //here save the account info on this phone

            //jump to timelineacticity
        }
    }
}
