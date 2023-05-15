package com.example.bulleaniveau;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor gyroscope;
    private TextView gyroscopeValues;

    private int screenHeight;
    private int screenWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        gyroscopeValues = findViewById(R.id.gyroscopeValues);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Mettez à jour votre TextView avec les valeurs du gyroscope
            gyroscopeValues.setText("X: " + x + "\nY: " + y + "\nZ: " + z);
            // Appelez une méthode pour mettre à jour la position du TextView
            updateTextViewPosition(x, y);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ne faites rien pour l'instant
    }

    private void updateTextViewPosition(float x, float y) {

        // Calculez les nouvelles coordonnées en ajoutant les valeurs du gyroscope
        float newX = (x * (screenWidth/2f/10f) + screenWidth/2f);
        float newY = (y * (screenHeight/2f/10f) + screenHeight/2f);

        // Définissez les nouvelles coordonnées pour le TextView
        gyroscopeValues.setX(newX);
        gyroscopeValues.setY(newY);
    }
}