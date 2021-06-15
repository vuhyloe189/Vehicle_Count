package com.example.authentication.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import java.time.LocalDateTime;  // Import the LocalDateTime class
import java.time.format.DateTimeFormatter;  // Import the DateTimeFormatter class

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.authentication.ChangePasswordActivity;
import com.example.authentication.LoginActivity;
import com.example.authentication.R;
import com.example.authentication.Retrofit.MyService;
import com.example.authentication.Retrofit.RetrofitClient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Profile extends AppCompatActivity {

    View account, phoneChange, avatarChange, passwordChange, more;
    View turnBack, savedProfile;
    TextView userName, userEmail;
    SharedPreferences sharedPreferences;

    final String URL = "http://52.237.113.8:9000/authenticate/profile/";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.profile_and_homescreen );

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setUIReferences();
        loadProfile();


        account.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, NoneFragment.class);
                startActivity( intent );
            }
        } );

        phoneChange.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, NoneFragment.class);
                startActivity( intent );
            }
        } );

        avatarChange.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, NoneFragment.class);
                startActivity( intent );
            }
        } );

        passwordChange.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, ChangePasswordActivity.class);
                startActivity( intent );
            }
        } );

        turnBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Dashboard.class);
                startActivity( intent );
            }
        } );

        savedProfile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Dashboard.class);
                startActivity( intent );
            }
        } );
    }


    String result ="";
    int code = 0;
    boolean flag = false;
    private void loadProfile(){
        MyService iMyService;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService = retrofitClient.create(MyService.class);

//        Log.d( "Url + Token", URL + sharedPreferences.getString( "email",null ) + sharedPreferences.getString( "token",null));
        iMyService.getProfile( URL + sharedPreferences.getString( "email",null ),"Bearer " + sharedPreferences.getString( "token",null))
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
                        Toasty.error( Profile.this, Objects.requireNonNull( e.getMessage() ), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {
                        if(code == 200){
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", jsonObject.getString( "name" ));
                                editor.putString("mail", jsonObject.getString( "email" ));
                                editor.apply();

                                setString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }  else {
                            Intent intent = new Intent(Profile.this, LoginActivity.class);
                            Toast.makeText( Profile.this, "Please login again as your account has expired !", Toast.LENGTH_LONG).show();
                            startActivity( intent );
                        }
                    }
                } );
    }

    private void setString(){
        userEmail.setText( sharedPreferences.getString( "mail" ,null) );
        userName.setText( sharedPreferences.getString( "username",null ) );
    }

    public void setUIReferences(){
        userName = findViewById( R.id.profileUserName );
        userEmail =findViewById( R.id.profileEmail );

        account = findViewById( R.id.EmailChange );
        phoneChange = findViewById( R.id.phoneChange );
        avatarChange = findViewById( R.id.avatarChange );
        passwordChange = findViewById( R.id.passwordChange );

        turnBack = findViewById( R.id.TurnBack );
        savedProfile = findViewById( R.id.save );
    }
}
