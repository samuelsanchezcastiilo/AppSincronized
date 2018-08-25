package com.apps.poultryapp.Login.Login.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.apps.poultryapp.Login.Home;
import com.apps.poultryapp.Login.Login.Presenter.LoginPresenterImpl;
import com.apps.poultryapp.R;

public class Login extends AppCompatActivity implements com.apps.poultryapp.Login.Login.Interfaces.View{
    private Button signin;
    private EditText userName;
    private EditText password;
    private ProgressBar progressBarLogin;

    private LoginPresenterImpl loginPresenter;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signin = (Button) findViewById(R.id.action_sign_in);

        userName = (EditText) findViewById(R.id.name_user);
        password = (EditText)findViewById(R.id.password_user);
        progressBarLogin = (ProgressBar) findViewById(R.id.progesLogin);

        loginPresenter = new LoginPresenterImpl(this,getApplicationContext());

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loginPresenter.singIn(userName.getText().toString(),password.getText().toString());
            }
        });

        //loginPresenter.checkForAuthenticatedUser();
    }


    @Override
    public void goContainer() {
        Intent intent = new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void enableImputs() {
        setInputs(true);
    }

    @Override
    public void desableImputs() {
    setInputs(false);
    }

    @Override
    public void showProgress() {
    progressBarLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
   progressBarLogin.setVisibility(View.INVISIBLE);
    }



    @Override
    public void loginError(String error) {
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }

    private void setInputs(boolean enable){
        userName.setEnabled(enable);
        password.setEnabled(enable);
        signin.setEnabled(enable);
    }
}
