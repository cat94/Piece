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

import nju.com.piece.database.DBFacade;
import nju.com.piece.logic.net.CallService;
import nju.com.piece.logic.update.GetServerUrl;

/**
 * Created by Hyman on 2015/6/11.
 */

public class ModifyPsw {

    private static final String urlString = GetServerUrl.getUrl() + "index.php?r=period/modifyPsw";
    private static final String TAG = "ModifyPsw";
    private Context context;
    private String newPsw;
    private ProgressBar progressBar;

    public ModifyPsw(Context context,ProgressBar progressBar) {
        this.context = context;
        this.progressBar=progressBar;

    }

    public void modify(String newPsw) {
        this.newPsw=newPsw;
        Log.i(TAG, "call modify");
        new ModifyTask().execute();
    }

    class ModifyTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            JSONObject tosendsObject = new JSONObject();
            Log.i(TAG, "start put json!");
            DBFacade dbFacade=new DBFacade(context);
            String userName=dbFacade.getAccount().getName();
            try {
                tosendsObject.put("username", userName);
                tosendsObject.put("newPsw", newPsw);
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
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);    //show the progressBar
        }

        @Override
        protected void onPostExecute(String  result) {
            progressBar.setVisibility(View.GONE);    //hide the progressBar
            if(result==null || result.equals("")){
                CallService.showNetErr(context);
                return;
            }
            if(result.equals("true")) {
                Toast.makeText(context,"修改成功！", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context,"修改失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
