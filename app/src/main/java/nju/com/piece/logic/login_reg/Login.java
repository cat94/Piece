package nju.com.piece.logic.login_reg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;


import nju.com.piece.R;
import nju.com.piece.activity.MainActivity;
import nju.com.piece.database.DBFacade;
import nju.com.piece.database.helpers.DatabaseHelper;
import nju.com.piece.database.pos.AccountPO;
import nju.com.piece.logic.net.CallService;
import nju.com.piece.logic.sync.SyncRecords;
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
                //add account info
                tosendsObject.put("username", userName);
                tosendsObject.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //change json to String
            String content = String.valueOf(tosendsObject);
            Log.i(TAG, "send :" + content);
            String responseData = CallService.call(urlString, content,context);
            if(responseData==null || responseData.equals("")){
                return null;
            }
            Log.i(TAG, "res:" + responseData);
            JSONObject resultObject = null;
            String result=null;
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
            if(result==null){
                CallService.showNetErr(context);
                return;
            }
            //Toast.makeText(context,"result:"+result,Toast.LENGTH_SHORT).show();
            //here save the account info on this phone
            if(result.equals("true")) {
                //同步
                DBFacade dbFacade = new DBFacade(context);
                dbFacade.clearAccount();
                DatabaseHelper.setCurrentUser(userName);
                dbFacade.setAccount(new AccountPO(userName, password));
                SyncRecords syncRecords=new SyncRecords(context,null);
                syncRecords.sync();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                Activity activity = (Activity) context;
                activity.finish();
            }
            else{


                CharSequence text = "密码错误";
                ((TextView)((Activity)context).findViewById(R.id.login_toast_text)).setText(text);


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        CharSequence text = "";
                        ((TextView)((Activity)context).findViewById(R.id.login_toast_text)).setText(text);
                    }
                }, 2000);

            }
            //jump to timelineacticity
        }
    }
}
