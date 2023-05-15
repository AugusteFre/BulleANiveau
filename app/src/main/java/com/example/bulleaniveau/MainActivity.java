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

    // déclaration des modules
    private SensorManager sensorManager;
    private Sensor gravity;
    private TextView gravityValues;
    private ImageView imageBubble;

    // déclaration des variables
    private int screenHeight;
    private int screenWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // mesure la largeur et hauteur de l'écran
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        // déclare les vues
        gravityValues = findViewById(R.id.gravityValues);
        imageBubble = findViewById(R.id.imageBubble);
        
        // définit le type de capteur
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL);
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
            float z = event.values[2];

            // Met à jour la TextView avec les valeurs du capteur de gravité, raccourci à un chiffre après la virgule
            gravityValues.setText("X: " + ((int)(x*10))/10f + "\nY: " + ((int)(y*10))/10f + "\nZ: " + ((int)(z*10))/10f);

            // Met à jour la TextView avec les valeurs du capteur de gravité, affichés en % (il y a des pourcents négatifs, donc jsp)
            // gravityValues.setText("X: " + ((int)(x*10)) + "%\nY: " + ((int)(y*10)) + "%\nZ: " + ((int)(z*10)) + "%");

            // Méthode qui met à jour la position de la bulle
            updateBubbleosition(z, y);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ne fais rien pour l'instant
    }

    /**
     * Met à jour la position de la bulle en fonction de la rotation du téléphone
     * @param z la position z retournée par le "capteur de gravité"
     * @param y la position y retournée par le "capteur de gravité"
     */
    private void updateBubbleosition(float z, float y) {

        // Calculez les nouvelles coordonnées en ajoutant les valeurs du gravity
        float newZ = (-z * (screenWidth/2f/10f) + screenWidth/2f - imageBubble.getWidth()/2f);
        float newY = (-y * (screenHeight/2f/10f) + screenHeight/2f - imageBubble.getHeight()/2f);

        // Limitez les coordonnées à l'intérieur de l'écran
        newZ = Math.max(0, Math.min(newZ, screenWidth - imageBubble.getWidth()));
        newY = Math.max(0, Math.min(newY, screenHeight - imageBubble.getHeight()));

        // Définissez les nouvelles coordonnées pour le TextView
        imageBubble.setX(newZ);
        imageBubble.setY(newY);
    }
}