package nju.com.piece.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import nju.com.piece.R;
import nju.com.piece.logic.login_reg.CheckName;
import nju.com.piece.logic.login_reg.Login;
import nju.com.piece.logic.login_reg.Register;

public class RegisterActivity extends Activity implements View.OnFocusChangeListener,View.OnClickListener{
    EditText userName;
    EditText psw1;
    EditText psw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName=(EditText)findViewById(R.id.reg_accountEt);
        psw1=(EditText)findViewById(R.id.reg_pwdEt);
        psw2=(EditText)findViewById(R.id.reg_pwdEt2);
        userName.setOnFocusChangeListener(this);
        psw2.setOnFocusChangeListener(this);
        TextView backText=(TextView)findViewById(R.id.backBtn);
        TextView regText=(TextView)findViewById(R.id.reg_regBtn);
        backText.setOnClickListener(this);
        regText.setOnClickListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //when edittext lose the focus
        if(hasFocus==false) {
            switch (v.getId()) {
                case R.id.reg_accountEt:
                    CheckName checkName=new CheckName(this,userName);
                    checkName.check(userName.getText().toString());
                    break;
                case R.id.reg_pwdEt2:

                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.reg_regBtn:
                //get userName and psw
                if(psw1.getText().toString().equals("")||psw2.getText().toString().equals("")||userName.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this,"输入不能为空！",Toast.LENGTH_LONG).show();
                }
                else if(psw1.getText().toString().equals(psw2.getText().toString())) {
                    Register register = new Register(this, (ProgressBar) findViewById(R.id.reg_progressbar));
                    register.reg(userName.getText().toString(),psw1.getText().toString() );
                }
                else{
                    Toast.makeText(RegisterActivity.this,"两次密码不相同！",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.backBtn:
                Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
