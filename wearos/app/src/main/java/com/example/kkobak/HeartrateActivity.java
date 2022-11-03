package com.example.kkobak;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HeartrateActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor hr;

    private TextView textView;

    private static final String TAG = "____Main___";

    private List<Integer> heartRate = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartrate);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        hr = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        textView = findViewById(R.id.txt_heartrate);
        checkPermission();
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, hr, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        System.out.println("센서 이벤트 발생");
        if (sensorEvent.sensor.getType()== Sensor.TYPE_HEART_RATE){
            int heartValue = (int)sensorEvent.values[0];
//            System.out.println(LocalDateTime.now());
            textView.setText(heartValue+"");
//            System.out.println(sensorEvent.values[0]);
//            heartRate.add(heartValue);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void checkPermission() { // step 3 started (according to content detail)

        // Runtime permission ------------
        if (checkSelfPermission(Manifest.permission.BODY_SENSORS) // check runtime permission for BODY_SENSORS
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.BODY_SENSORS}, 1); // If BODY_SENSORS permission has not been taken before then ask for the permission with popup
        } else {
            Log.d(TAG, "ALREADY GRANTED"); //if BODY_SENSORS is allowed for this app then print this line in log.
        }
    }

    @Override
    protected void onDestroy() {
        System.out.println("========================================심박수 데이터 입니다!!");
        System.out.println(heartRate.size());
        for (Integer integer : heartRate) {
            System.out.println(integer);
        }

        sensorManager.unregisterListener(this);
        super.onDestroy();
    }
}