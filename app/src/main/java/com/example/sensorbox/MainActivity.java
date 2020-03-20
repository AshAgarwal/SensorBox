package com.example.sensorbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor accelometer, gravity;
    private LinearLayout accLayout, gravityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accLayout = (LinearLayout) findViewById(R.id.accLayout);
        gravityLayout = (LinearLayout) findViewById(R.id.gravityLayout);

        Log.e(TAG, "onCreate: Initializing the Sensor Manager and Sensor Object");
        accelometer = null;
        gravity = null;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null){
            if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
                accelometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                TextView textView = (TextView) accLayout.getChildAt(0);
                String title = textView.getText() + " (" + accelometer.getPower() + "mA" + ")";
                textView.setText(title);
            } else {
                accLayout.setVisibility(View.GONE);
            }

            if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null){
                gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
                TextView textView = (TextView) gravityLayout.getChildAt(0);
                String title = textView.getText() + " (" + gravity.getPower() + "mA" + ")";
                textView.setText(title);
            } else {
                gravityLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: Registring the Sensor Manager");
        sensorManager.registerListener(this,accelometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: Unregistring the Sensor Manager");
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        DecimalFormat dFormat = new DecimalFormat("##.###");

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            String[] values = {
                    "x: " + dFormat.format(event.values[0]) + " " + getResources().getString(R.string.siUnit),
                    "y: " + dFormat.format(event.values[1]) + " " + getResources().getString(R.string.siUnit),
                    "z: " + dFormat.format(event.values[2]) + " " + getResources().getString(R.string.siUnit)
            };

            for (int i = 1; i < accLayout.getChildCount(); i++){
                TextView text = (TextView) accLayout.getChildAt(i);
                text.setText(values[i - 1]);
            }
        } else if (event.sensor.getType() == Sensor.TYPE_GRAVITY){
            String[] values = {
                    "x: " + dFormat.format(event.values[0]) + " " + getResources().getString(R.string.siUnit),
                    "y: " + dFormat.format(event.values[1]) + " " + getResources().getString(R.string.siUnit),
                    "z: " + dFormat.format(event.values[2]) + " " + getResources().getString(R.string.siUnit)
            };

            for (int i = 1; i < gravityLayout.getChildCount(); i++){
                TextView text = (TextView) gravityLayout.getChildAt(i);
                text.setText(values[i - 1]);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
