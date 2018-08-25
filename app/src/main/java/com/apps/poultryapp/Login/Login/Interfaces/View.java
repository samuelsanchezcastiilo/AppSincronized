package com.apps.poultryapp.Login.Login.Interfaces;

public interface View {

    void goContainer ();

    void enableImputs();
    void desableImputs();

    void showProgress();
    void hideProgress();

    void loginError(String error);
}
