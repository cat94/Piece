package nju.com.piece.logic.login_reg;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import nju.com.piece.logic.net.CallService;
import nju.com.piece.logic.update.GetServerUrl;

/**
 * Created by Hyman on 2015/6/11.
 */

public class Login {

    private static final String urlString = GetServerUrl.getUrl() + "index.php?r=period/login";
    private static final String TAG = "Login";

    private ProgressBar progressBar;
    private Context context;

    private String userName;
    private String password;

    public Login( Context context,ProgressBar progressBar) {
        this.progressBar=progressBar;
        this.context = context;
    }

    public void login(String userName,String password) {
        Log.i(TAG, "call login");
        this.userName=userName;
        this.password=password;
        new LoginTask().execute();
    }

    class LoginTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            JSONObject tosendsObject = new JSONObject();
            Log.i(TAG, "start put json!");
            try {
                tosendsObject.put("username", userName);
                //add account info
                tosendsObject.put("password", password);
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
            progressBar.setVisibility(View.VISIBLE);    //show the progressBar
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String  result) {
             progressBar.setVisibility(View.GONE);    //hide the progressBar
            Toast.makeText(context,"result:"+result,Toast.LENGTH_SHORT).show();
            //here save the account info on this phone

            //jump to timelineacticity
        }
    }
}
