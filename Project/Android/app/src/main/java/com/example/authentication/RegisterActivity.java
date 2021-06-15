package com.example.authentication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.authentication.Retrofit.MyService;
import com.example.authentication.Retrofit.*;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    EditText userName, email, password, rePassword;
    View btnSign;
    TextView loginChanged;
    String getUserName, getUserEmail, getUSerPassword, getMatchPassword;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        setUIReference();

        loginChanged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValid()) {
                    Register();
                }
            }
        });

    }

    private void getText(){
        // getText
        getUserName = userName.getText().toString();
        getUserEmail = email.getText().toString();
        getUSerPassword = password.getText().toString();
        getMatchPassword = rePassword.getText().toString();
    }

    private void setUIReference(){
        userName = findViewById(R.id.userNameTextSignIn);
        email = findViewById(R.id.emailTextSignIn);
        password = findViewById(R.id.passwordTextSignIn);
        rePassword = findViewById(R.id.rePasswordTextSignIn);
        btnSign = findViewById(R.id.btnSignIn);
        loginChanged = findViewById(R.id.loginTextViewSignIn);
    }

    int result = 0;
    boolean flag =  false;
    private void Register(){

        getText();

        MyService iMyService;
        AlertDialog alertDialog;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService = retrofitClient.create(MyService.class);
        alertDialog= new SpotsDialog.Builder().setContext(this).build();
        alertDialog.show();

        iMyService.registerUser(getUserName, getUserEmail, getUSerPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Response<String> stringResponse) {
                        if(stringResponse.isSuccessful()){
                            assert stringResponse.body() != null;
                            flag = true;
                        }
                        else {
                            flag = false;
                        }
                        result = stringResponse.code();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        alertDialog.dismiss();
                                    }
                                }, 500);

                        Toasty.error(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        alertDialog.dismiss();

                                    }
                                }, 500);
                            if (result == 201) {
                                Toasty.success(RegisterActivity.this, "User Registered Successfully !", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else if (result == 409) {
                                Toast.makeText(RegisterActivity.this, "User Already Registered !", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, String.valueOf(result), Toast.LENGTH_SHORT).show();
                            }
                        }
                });
    }

    private boolean checkValid(){
        if(userName.getText().toString().isEmpty()){
            userName.setError("Invalid your user name");
            return  false;
        }
        if(email.getText().toString().isEmpty()){
            email.setError("Invalid your user email");
            return  false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Invalid your user password");
            return  false;
        }
        if(rePassword.getText().toString().isEmpty()){
            rePassword.setError("Invalid your user match password");
            return  false;
        }
        if(!password.getText().toString().equals(rePassword.getText().toString())){
            rePassword.setError("Password do not match");
            return false;
        }
        if(password.getText().toString().length() < 8){
            password.setError("Password should be at least 8 characters");
            return false;
        }
        return true;
    }

}
