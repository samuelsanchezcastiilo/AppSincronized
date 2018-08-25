package com.apps.poultryapp.Login.Login.Data;
import android.content.Context;
import com.apps.poultryapp.Login.Login.Interfaces.ClienteRetrofit;
import com.apps.poultryapp.Login.Login.Interfaces.Data;
import com.apps.poultryapp.Login.Login.Presenter.LoginPresenterImpl;
import com.apps.poultryapp.Login.Model.User;
import com.apps.poultryapp.Login.Utils.Constantes;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginDataImpl implements Data  {
    private LoginPresenterImpl  presenter;
    Context context;
    public LoginDataImpl(LoginPresenterImpl presenter,Context context) {
        this.presenter = presenter;
        this.context = context;
    }
    @Override
    public void singIn(final String username, final String password) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit.Builder reBuilder = new Retrofit.Builder()
                .baseUrl(Constantes.IP).addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient);
        Retrofit retrofit = reBuilder.build();
        ClienteRetrofit clienteRetrofit = retrofit.create(ClienteRetrofit.class);
        clienteRetrofit.Login(username, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                if (response.code() == 200) {
                    SessionPref.get(context).saveUser(response.body());
                    presenter.loginSuccess();
                } else {
                    presenter.loginError("usuario o contraseña invalidad");
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                presenter.loginError("error de conexion verifique du estado de red");
            }
        });


    }


}
