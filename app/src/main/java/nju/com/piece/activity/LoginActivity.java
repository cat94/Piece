package nju.com.piece.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import nju.com.piece.R;
import nju.com.piece.logic.login_reg.Login;

public class LoginActivity extends Activity implements View.OnClickListener{
    EditText userName;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName=(EditText)findViewById(R.id.username_edit);
        password=(EditText)findViewById(R.id.password_edit);
        Button loginButton=(Button)findViewById(R.id.signin_button);
        TextView registerText=(TextView)findViewById(R.id.register_text);
        loginButton.setOnClickListener(this);
        registerText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_button:
                String us=userName.getText().toString();
                String psw=password.getText().toString();
                ProgressBar progressBar=(ProgressBar)findViewById(R.id.login_progressBar);
                Login login=new Login(this,progressBar);
                login.login(us,psw);
                break;
            case R.id.register_text:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
