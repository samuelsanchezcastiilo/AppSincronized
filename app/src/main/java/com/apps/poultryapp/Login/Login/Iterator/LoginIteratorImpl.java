package com.apps.poultryapp.Login.Login.Iterator;


import android.content.Context;

import com.apps.poultryapp.Login.Login.Data.LoginDataImpl;
import com.apps.poultryapp.Login.Login.Interfaces.Data;
import com.apps.poultryapp.Login.Login.Interfaces.Iterator;
import com.apps.poultryapp.Login.Login.Presenter.LoginPresenterImpl;

public class LoginIteratorImpl implements Iterator {
    private LoginPresenterImpl presenter;
    private Data data;
    private Context context;

    public LoginIteratorImpl(LoginPresenterImpl presenter,Context context) {
        this.presenter = presenter;
        this.data = new LoginDataImpl(presenter,context);
    }

    @Override
    public void signIn(String username, String pasword)
    {
      data.singIn(username,pasword);
    }

    @Override
    public void signInTwoFactoryLogin(String username, String pasword, String code) {

    }
}
