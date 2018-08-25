package com.apps.poultryapp.Login.Login.Interfaces;

public interface Presenter {

    void singIn(String username ,String pasword); //iterator
    void loginSuccess();
    void loginError(String error);
}
