package com.apps.poultryapp.Login.Login.Presenter;


import android.content.Context;

import com.apps.poultryapp.Login.Login.Interfaces.Iterator;
import com.apps.poultryapp.Login.Login.Interfaces.Presenter;
import com.apps.poultryapp.Login.Login.Interfaces.View;
import com.apps.poultryapp.Login.Login.Iterator.LoginIteratorImpl;

public class LoginPresenterImpl implements Presenter {

    private View view;
    private Iterator iterator;
    private Context context;

    public LoginPresenterImpl(View view,Context context) {
        this.view = view;
        iterator =  new LoginIteratorImpl(this,context);
    }

    @Override
    public void singIn(String username, String pasword) {
        view.desableImputs();
        view.showProgress();

    iterator.signIn(username,pasword);
    }

    @Override
    public void loginSuccess() {
        view.goContainer();
        view.hideProgress();


    }

    @Override
    public void loginError(String error) {
        if (view != null){
            view.enableImputs();
            view.hideProgress();
            view.loginError(error);
        }

    }
}
