package com.apps.poultryapp.Login.Login.Interfaces;

public interface Iterator {

    void signIn(String username, String pasword);
    void signInTwoFactoryLogin(String username ,String pasword, String code);
}
