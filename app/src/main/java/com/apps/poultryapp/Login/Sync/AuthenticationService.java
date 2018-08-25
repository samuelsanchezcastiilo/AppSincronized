package com.apps.poultryapp.Login.Sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class AuthenticationService extends Service {

    private ExpenseAuthenticator autenticador;
    @Override
    public void onCreate() {
        // Nueva instancia del autenticador
        autenticador = new ExpenseAuthenticator(this);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return autenticador.getIBinder();
    }
}
