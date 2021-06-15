package com.example.authentication.Fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.authentication.Models.Cars;
import com.example.authentication.R;
import com.example.authentication.Retrofit.MyService;
import com.example.authentication.Retrofit.RetrofitClient;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LineChartLayout extends AppCompatActivity {

    ArrayList<Cars> CarList = new ArrayList<>();
    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private  int LastXValue = 0;
    int millis = 0;
    GraphView graphView;
    ColorStateList colorStateList;
    DataPointInterface dataPointInterface;

    private int lastX = 0;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate( savedInstance );
        setContentView( R.layout.linegraph_iteam );
        setUIReference();


        // Timer, reset after 2s
        Timer timer = new Timer();
        timer.schedule(new WaitTimer(), 0, 2000);

        setGraphView();
        gridLabelRender();
        addEntryLayout();
        viewPortSetting();

    }

        //Timer
    private class WaitTimer extends TimerTask {
        @Override
        public void run() {
            //every 5 seconds
            if(millis % 2 == 0) {
                //get cars
                getVolumes();
                // post random cars
                postVolumes();


            }
            millis+=1;
        }
    }

    // automatically post cars, will delete
    private void postVolumes(){
        int random = RANDOM.nextInt(10);
        String stringRandom = String.valueOf(random);
        MyService iMyService;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService = retrofitClient.create(MyService.class);

        iMyService.postInformation(stringRandom)
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new io.reactivex.Observer<Response<String>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Response<String> stringResponse) {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Toast.makeText(LineChartLayout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    // get the number of vehicles
    boolean check = false;
    String response = "";
    private void getVolumes(){


        MyService iMyService;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService = retrofitClient.create(MyService.class);

        iMyService.getInformation()
                .subscribeOn(Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull String s) {
                        check = true;
                        response = s;
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Toast.makeText(LineChartLayout.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {
                            if(check){
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() - 1);
                                    Cars cars = new Cars();
                                    cars.setCars( jsonObject.getString( "volumes" ) );
                                    CarList.add( cars );

                                    int lastY =  Integer.parseInt( jsonObject.getString( "volumes" ));
                                    series.appendData(new DataPoint(LastXValue++, lastY), true, 10);

                                    check = true;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }  else {
                                Toast.makeText(LineChartLayout.this, "Chưa có dữ liệu", Toast.LENGTH_SHORT).show();

                            }

                    }
                });
    }

    // GraphView settings
    private void setGraphView(){
        // get graphView
        graphView.setBackgroundColor( Color.TRANSPARENT );

        // set data points
        series = new LineGraphSeries<DataPoint>();
        graphView.addSeries(series);
        graphView.setTitle("Traffics count");
        graphView.setBackgroundColor( Color.rgb(40, 40, 43) );
        graphView.onDataChanged( false,false );
    }

    // gridLabel settings
    private void gridLabelRender(){
        //set GridLabel
        GridLabelRenderer layout = graphView.getGridLabelRenderer();
        layout.setVerticalAxisTitle( " Cars" );
        layout.setHorizontalAxisTitle( " 10 seconds" );
        layout.setGridStyle( GridLabelRenderer.GridStyle.BOTH );
        layout.setGridColor( Color.WHITE );
        layout.setHorizontalLabelsColor( Color.WHITE );
        layout.setVerticalLabelsColor( Color.WHITE );
        layout.setHorizontalAxisTitleColor( Color.WHITE );
        layout.setVerticalAxisTitleColor( Color.WHITE );
    }

    // here, we choose to display points on the viewport and we scroll to end
    private void addEntryLayout() {
        series.setDrawDataPoints(true);
        series.setDrawDataPoints( true );
        series.setBackgroundColor( Color.argb(50,207,195,122) );
        series.setColor( Color.	rgb(161,199,146));
        series.setDrawAsPath( true );
        series.setDrawBackground( true );
        series.setThickness(5);
    }

    // Viewport configuration
    private void viewPortSetting(){
        //setting viewPort
        Viewport viewport = graphView.getViewport();

        viewport.setYAxisBoundsManual(true);
        viewport.setYAxisBoundsStatus(viewport.getYAxisBoundsStatus());
        viewport.setYAxisBoundsManual(true);
        viewport.setYAxisBoundsStatus(viewport.getYAxisBoundsStatus());

        viewport.setBackgroundColor( Color.BLACK);

        viewport.setYAxisBoundsStatus( Viewport.AxisBoundsStatus.AUTO_ADJUSTED);
        viewport.setMinX(0);
        viewport.setMinY(0);
        viewport.setMaxY(20);
        viewport.setScalable(true);
        viewport.setScrollable( true );

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

    private void addEntry() {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        series.appendData(new DataPoint(lastX++, RANDOM.nextDouble() * 10d), true, 10);
    }

    private void setUIReference(){
        graphView = (GraphView) findViewById(R.id.lineChartView);

    }
}
