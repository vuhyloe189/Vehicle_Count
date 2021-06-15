package com.example.authentication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.authentication.Fragment.Profile;
import com.example.authentication.Retrofit.MyService;
import com.example.authentication.Retrofit.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldPassword, newPassword, confirmPassword;
    String oldPasswordText = "", newPasswordText = "", confirmPasswordText = "";
    View turnBack, savePassword;
    SharedPreferences sharedPreferences;

    final String URL = "http://52.237.113.8:9000/authenticate/change_password/";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.change_password_fragment );
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        UIReference();
        getString();

        turnBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, Profile.class );
                startActivity( intent );
            }
        } );

        savePassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidPassword()){
                    ChangePassword();
                }
            }
        } );
    }

    String result ="";
    int code = 0;
    boolean flag = false;
    private void ChangePassword(){

        getString();

        MyService iMyService;
        AlertDialog alertDialog;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService = retrofitClient.create(MyService.class);
        alertDialog= new SpotsDialog.Builder().setContext(this).build();
        alertDialog.show();

        iMyService.changePassword( URL + sharedPreferences.getString( "email",null ),"Bearer " + sharedPreferences.getString( "token",null),oldPasswordText,newPasswordText)
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Observer<Response<String>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull Response<String> stringResponse) {
                        if(stringResponse.isSuccessful()){
                            assert stringResponse.body() != null;
                        }
                        result = stringResponse.body();
                        code = stringResponse.code();
                        flag =  true;
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Toasty.error( ChangePasswordActivity.this, Objects.requireNonNull( e.getMessage() ), Toast.LENGTH_SHORT).show();

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
                            Intent intent = new Intent(ChangePasswordActivity.this, Profile.class);
                            Toasty.success( ChangePasswordActivity.this, "The password was successfully changed !", Toast.LENGTH_SHORT).show();
                            startActivity( intent );
                        } else if( code == 402){
                            Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                            Toast.makeText( ChangePasswordActivity.this, "Please login again as your account has expired !", Toast.LENGTH_SHORT).show();
                            startActivity( intent );
                        } else {
                            Toast.makeText( ChangePasswordActivity.this, "Your current password is not correct !", Toast.LENGTH_SHORT).show();
                        }
                    }
                } );

    }

    public void UIReference(){
        oldPassword = findViewById(R.id.oldPasswordChange);
        newPassword = findViewById(R.id.newPasswordChange);
        confirmPassword = findViewById(R.id.confirmPasswordChange);

        turnBack = findViewById(R.id.turnBack_PasswordChange);
        savePassword = findViewById( R.id.save_PasswordChange );

    }

    private void getString(){
        oldPasswordText = oldPassword.getText().toString();
        newPasswordText = newPassword.getText().toString();
        confirmPasswordText = confirmPassword.getText().toString();

    }

    private boolean checkValidPassword(){
        getString();
        if(oldPasswordText == null|| newPasswordText == null || confirmPasswordText == null ){
            Toast.makeText( ChangePasswordActivity.this, "Please be as detailed as possible when filling out the form !", Toast.LENGTH_SHORT).show();
            return false;
        } else if (newPasswordText.equals( oldPasswordText )){
            Toast.makeText( ChangePasswordActivity.this, "Please create a new password that is distinct from the old one !", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!newPasswordText.equals( confirmPasswordText )){
            Toast.makeText( ChangePasswordActivity.this, "The passwords do not match !", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}
