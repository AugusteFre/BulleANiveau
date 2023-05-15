package com.example.bulleaniveau;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor gyroscope;
    private TextView gyroscopeValues;
    private ImageView imageBubble;

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
        imageBubble = findViewById(R.id.imageBubble);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
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
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float x = event.values[0];
            float y = event.values[1];

            // Mettez à jour votre TextView avec les valeurs du gyroscope
            gyroscopeValues.setText("X: " + x + "\nY: " + y);
            // Appelez une méthode pour mettre à jour la position du TextView
            updateBubbleosition(x, y);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ne faites rien pour l'instant
    }

    private void updateBubbleosition(float x, float y) {

        // Calculez les nouvelles coordonnées en ajoutant les valeurs du gyroscope
        float newX = (x * (screenWidth/2f/10f) + screenWidth/2f - imageBubble.getWidth()/2f);
        float newY = (-y * (screenHeight/2f/10f) + screenHeight/2f - imageBubble.getHeight()/2f);

        // Limitez les coordonnées à l'intérieur de l'écran
        newX = Math.max(0, Math.min(newX, screenWidth - imageBubble.getWidth()));
        newY = Math.max(0, Math.min(newY, screenHeight - imageBubble.getHeight()));

        // Définissez les nouvelles coordonnées pour le TextView
        imageBubble.setX(newX);
        imageBubble.setY(newY);
    }
}