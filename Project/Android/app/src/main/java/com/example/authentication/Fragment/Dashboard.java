package com.example.authentication.Fragment;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.example.authentication.LoginActivity;
import com.example.authentication.R;
import com.example.authentication.Retrofit.MyService;
import com.example.authentication.Retrofit.RetrofitClient;
import com.google.android.material.tabs.TabLayout;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Objects;
import java.util.Random;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Dashboard extends AppCompatActivity {

    View layoutYesterday,layoutLastWeek;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    TabLayout tabLayout;
    View profile;
    TextView number1h,number3h,status1h,status3h,color1h,color3h,highestNum,averageNum,when,level;
    TextView logout;
    ProgressBar progress1h,progress3h;
    GraphView graph;
    SharedPreferences sharedPreferences;

    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate( savedInstanceState );
        super.setContentView( R.layout.dashboard_fragment );
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UiReference();

        //Card
        CardAnimations();

        drawerLayout = findViewById( R.id.dashboardMainActivity );
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,  R.string.Open,  R.string.Close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeButtonEnabled( true );
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable( Color.argb(255,21,102,224)));
        getSupportActionBar().setTitle( "In and outbound road flow" );


        //graphView
        setGraph();

        graph.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, LineChartLayout.class );
                startActivity( intent);
            }
        } );

        tabLayout.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:{


                        Animation animation1 =
                                AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.fade);
                        layoutYesterday.startAnimation(animation1);
                        layoutLastWeek.startAnimation(animation1);


                        highestNum.setText( "75" );
                        highestNum.setTextColor( Color.	rgb(0,0,255) );
                        averageNum.setText( "61" );
                        averageNum.setTextColor( Color.rgb(30,144,255));
                        when.setText( "12:15:30" + " AM");
                        level.setText( "heavy traffic" );
                        level.setTextColor( Color.rgb(30,144,255) );
                        break;
                    }

                    case 1:{

                        Animation animation1 =
                                AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.fade);
                        layoutYesterday.startAnimation(animation1);
                        layoutLastWeek.startAnimation(animation1);

                        highestNum.setText( "80" );
                        highestNum.setTextColor( Color.	rgb(0,0,255) );
                        averageNum.setText( "73" );
                        averageNum.setTextColor( Color.rgb(0,0,255));
                        when.setText( "15/4/2021");
                        level.setText( "quite a traffic jam" );
                        level.setTextColor( Color.rgb(0,0,255));
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:{
                        highestNum.setText( "75" );
                        highestNum.setTextColor( Color.	rgb(0,0,255) );
                        averageNum.setText( "61" );
                        averageNum.setTextColor( Color.rgb(30,144,255));
                        when.setText( "12:15:30" + " AM");
                        level.setText( "heavy traffic" );
                        level.setTextColor( Color.rgb(30,144,255) );
                        break;
                    }

                    case 1:{
                        highestNum.setText( "80" );
                        highestNum.setTextColor( Color.	rgb(0,0,255) );
                        averageNum.setText( "73" );
                        averageNum.setTextColor( Color.rgb(0,0,255));
                        when.setText( "15/4/2021");
                        level.setText( "quite a traffic jam" );
                        level.setTextColor( Color.rgb(0,0,255));
                        break;
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        } );

        logout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLogout();
            }
        } );

        profile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Profile.class);
                startActivity( intent );
            }
        } );

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void UiReference(){
        number1h = findViewById(R.id.dashboardCars1hours);
        number3h = findViewById(R.id.dashboardCars3hours);
        status1h = findViewById(R.id.text_view_progress_1h);
        status3h = findViewById(R.id.text_view_progress_3h);
        progress1h = findViewById(R.id.progress_bar_1h);
        progress3h = findViewById(R.id.progress_bar_3h);
        color1h = findViewById(R.id.color1h);
        color3h = findViewById(R.id.color3h);

        //grapview
        graph = (GraphView) findViewById(R.id.dashboardGraphView);

        //logout
        logout =findViewById( R.id.dashboardLogout);

        //function
        profile = findViewById( R.id.dashboardProfileOnTouch );

        //dashboard
        tabLayout = findViewById( R.id.dashboardTab );
        highestNum = findViewById(R.id.dashboardHighestNumber);
        averageNum = findViewById(R.id.dashboardAverageNumber);
        when = findViewById(R.id.dashboardWhen);
        level = findViewById(R.id.dashboardLevel);
        layoutYesterday = findViewById( R.id.layoutYesterday );
        layoutLastWeek = findViewById( R.id.layoutLastWeek );
    }

    public void CardAnimations(){
        progress1h.setProgress( 80 );
        progress3h.setProgress( 30 );
        status1h.setText( "High" );
        status3h.setText( "Low" );
        number1h.setText( "80" );
        number1h.setTextColor( Color.rgb(0,0,255) );
        number3h.setText( "30" );
        number3h.setTextColor( Color.rgb(135,206,250));
        color1h.setTextColor( Color.rgb(0,0,255) );
        color3h.setTextColor( Color.rgb(135,206,250) );
    }

    //grapView

    public void setGraph(){
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);

        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(false);
        viewport.setXAxisBoundsManual(false);
        viewport.setMinY(0);
        viewport.setMaxY(10);
        viewport.setMinX(0);
        viewport.setMaxX(5);
        viewport.setScrollable(false);


        GridLabelRenderer layout = graph.getGridLabelRenderer();
        layout.setHorizontalLabelsVisible( false );
        layout.setVerticalLabelsVisible( false );


//        viewport.setScalableY( false );
        viewport.setScalable( true);
        series.setDrawBackground(true);
        series.setThickness( 5 );
        series.setDrawDataPoints( true );
        series.setBackgroundColor( Color.argb(150,135,206,250) );
        series.setColor( Color.rgb(30,144,255));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // we're going to simulate real time with thread that append data to the graph
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                for (int i = 0; i < 1000; i++) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addEntry();
                        }
                    });

                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        }).start();
    }

    // add random data to graph
    private void addEntry() {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        series.appendData(new DataPoint(lastX++, RANDOM.nextDouble() * 10d), true, 10);
    }

    //logout
    int logoutCode = 0;
    private void setLogout(){
        MyService iMyService;
        AlertDialog alertDialog;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService = retrofitClient.create(MyService.class);
        alertDialog= new SpotsDialog.Builder().setContext(this).build();
        alertDialog.show();

        iMyService.basicLogout()
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
                        logoutCode = stringResponse.code();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        alertDialog.dismiss();
                                    }
                                }, 500);

                        Toasty.error( Dashboard.this, Objects.requireNonNull( e.getMessage() ), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        alertDialog.dismiss();

                                    }
                                }, 500);
                        if(logoutCode == 200){
                            Intent intent = new Intent( Dashboard.this, LoginActivity.class);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", null);
                            editor.putString("email", null);
                            editor.apply();
                            startActivity(intent);
                            Toasty.success( Dashboard.this, "Logout Successful !", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText( Dashboard.this, "Logout Unsuccessful !", Toast.LENGTH_LONG).show();

                    }
                } );
    }


}
