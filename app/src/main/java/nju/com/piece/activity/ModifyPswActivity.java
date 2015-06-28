package nju.com.piece.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import nju.com.piece.R;
import nju.com.piece.logic.login_reg.Login;
import nju.com.piece.logic.login_reg.ModifyPsw;

public class ModifyPswActivity extends BaseActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psw);
        final EditText psw1Text = (EditText) findViewById(R.id.psw1);
        final EditText psw2Text = (EditText) findViewById(R.id.psw2);
        Button button = (Button) findViewById(R.id.modifypsw_subBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psw1 = psw1Text.getText().toString();
                String psw2 = psw2Text.getText().toString();
                if (psw1.equals("") || psw2.equals("")) {
                    Toast.makeText(ModifyPswActivity.this, "输入不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!psw1.equals(psw2)) {
                    Toast.makeText(ModifyPswActivity.this, "两次输入不相等！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.login_progressBar);
                    ModifyPsw modifyPsw = new ModifyPsw(ModifyPswActivity.this, progressBar);
                    modifyPsw.modify(psw1);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, SetActivity.class);
        startActivity(intent);
        this.finish();
        return true;
    }

}
