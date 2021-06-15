package com.example.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authentication.Fragment.Dashboard;
import com.example.authentication.Retrofit.MyService;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.example.authentication.Retrofit.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();
    String emailInput = "", passwordInput = "", base = "", authorization ="" ;
    SharedPreferences sharedPreferences ;
    TextView signIn;
    AppCompatTextView btnLogin;
    EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout );

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        signIn  = findViewById(R.id.signInTextViewLogin);
        btnLogin = findViewById(R.id.btnLogin);
        email = (EditText) findViewById(R.id.emailTextLogin);
        password = (EditText) findViewById(R.id.passwordTextLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValid()) {
                    Login_real();
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }



    String result ="";
    int code = 0;
    boolean flag = false;

    private void Login_real(){
        MyService iMyService;
        AlertDialog alertDialog;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService = retrofitClient.create(MyService.class);
        alertDialog= new SpotsDialog.Builder().setContext(this).build();
        alertDialog.show();

        iMyService.basicLogin( email.getText().toString() , password.getText().toString() )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Observer<Response<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Response<String> stringResponse) {
                        if(stringResponse.isSuccessful()){
                            assert stringResponse.body() != null;
                        }
                        result = stringResponse.body();
                        code = stringResponse.code();
                        flag =  true;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        alertDialog.dismiss();
                                    }
                                }, 500);

                        Toasty.error( LoginActivity.this, Objects.requireNonNull( e.getMessage() ), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        alertDialog.dismiss();
                                    }
                                }, 500);
                        if(code == 200){
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Intent intent = new Intent( LoginActivity.this, Dashboard.class);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token", jsonObject.getString( "token" ));
                                editor.putString("email", jsonObject.getString( "message" ));
                                editor.apply();
                                startActivity(intent);
                                Toasty.success( LoginActivity.this, "Successful !!!", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }  else {
                            Toast.makeText( LoginActivity.this, "The email address or password is incorrect !", Toast.LENGTH_SHORT).show();
                        }
                    }
                } );


    }

//    private void Login(){
////        SetUIReference();
////        setString();
//        MyService iMyService;
//        AlertDialog alertDialog;
//        alertDialog= new SpotsDialog.Builder().setContext(this).build();
//        alertDialog.show();
//        Retrofit retrofitClient = RetrofitClient.getInstance();
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        base = email.getText().toString() + ":" + password.getText().toString();
//        authorization = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
//
//        UserLoginClient.LoginService loginService = UserLoginClient.createService(UserLoginClient.LoginService.class,authorization);
//
//        //Debug, input right
//        Log.v( TAG, "The information is " + authorization);
//
//        Call<UserComponent> call = loginService.basicLogin();
//        call.enqueue(new Callback<UserComponent >() {
//                         @Override
//                         public void onResponse(@NotNull Call<UserComponent> call, @NotNull Response<UserComponent> response) {
//                             if (response.isSuccessful()) {
//                                 new android.os.Handler().postDelayed(
//                                         new Runnable() {
//                                             public void run() {
//                                                 alertDialog.dismiss();
//                                             }
//                                         }, 500);
//                                 Toasty.success(MainActivity.this,"Login successfully! ", Toasty.LENGTH_SHORT).show();
//
//                                 assert response.body() != null;
//                                 if(true) {
//                                     Intent intent = new Intent(MainActivity.this, HomeScreenFragment.class);
//                                     SharedPreferences.Editor editor = sharedPreferences.edit();
//                                     editor.putString("token", response.body().token);
//                                     editor.putString("email", response.body().message);
//                                     editor.apply();
//                                     startActivity(intent);
//                                 }
//                             } else {
//                                 new android.os.Handler().postDelayed(
//                                         new Runnable() {
//                                             public void run() {
//                                                 alertDialog.dismiss();
//                                             }
//                                         }, 500);
//                                 Toast.makeText(MainActivity.this,"Login failed !, Checking your email and your password", Toast.LENGTH_SHORT).show();
//                             }
//                         }
//
//                         @Override
//                         public void onFailure(@NotNull Call<UserComponent> call, @NotNull Throwable t) {
//                             // something went completely south (like no internet connection)
//                             new android.os.Handler().postDelayed(
//                                     new Runnable() {
//                                         public void run() {
//                                             alertDialog.dismiss();
//                                         }
//                                     }, 500);
//                             Log.d("Error", Objects.requireNonNull(t.getMessage()));
//                             Toasty.error(MainActivity.this,t.getMessage(), Toasty.LENGTH_SHORT).show();
//                         }
//                     });
//
//    }

    private void setString(){
        emailInput = email.getText().toString();
        passwordInput = password.getText().toString();
    }

    private boolean checkValid(){
        setString();
        if(emailInput.isEmpty()){
            Toast.makeText( LoginActivity.this, "Please input the email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(passwordInput.isEmpty()){
            Toast.makeText( LoginActivity.this, "Please input the password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}