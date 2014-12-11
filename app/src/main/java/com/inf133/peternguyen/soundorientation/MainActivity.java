package com.inf133.peternguyen.soundorientation;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.SoundPool;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mOrientation;

    private TextView sensorAzimuthTextView;
    private TextView sensorPitchAngleTextView;
    private TextView sensorRollAngleTextView;
    private int lastOrientationEntered;
    private static SoundPool soundPool;
    private static HashMap<Integer,Integer> soundPoolMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        sensorAzimuthTextView  = (TextView) this.findViewById(R.id.azimuth_angle);
        sensorAzimuthTextView.setText("Azimuth Angle: " + 0);

        sensorPitchAngleTextView = (TextView) this.findViewById(R.id.pitch_angle);
        sensorPitchAngleTextView.setText("Pitch Angle: " + 0);

        sensorRollAngleTextView = (TextView) this.findViewById(R.id.roll_angle);
        sensorRollAngleTextView.setText("Roll Angle: " + 0);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);


    }

    private void pauseSensor(){
        mSensorManager.unregisterListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float azimuth_angle = event.values[0];
        float pitch_angle = event.values[1];
        float roll_angle = event.values[2];

        // usb side up
        if(roll_angle >= -30 && roll_angle <= 30 && pitch_angle <=90 && pitch_angle > 15 && lastOrientationEntered != 1){
            playSound(this, R.raw.flava_1);
            lastOrientationEntered = 1;
        }
        //usb side down
        else if (roll_angle >= -30 && roll_angle <= 30 && pitch_angle >=-90 && pitch_angle < -15 && lastOrientationEntered != 2){
            playSound(this, R.raw.gucci_14);
            lastOrientationEntered = 2;
        }
        //rightside down
        else if(roll_angle <= - 15 && roll_angle >= -90 && pitch_angle <= 30 && pitch_angle >= -30 && lastOrientationEntered != 3){
            playSound(this, R.raw.mikejones_2);
            lastOrientationEntered = 3;
        }
        //left side down
        else if(roll_angle >= 15 && roll_angle <= 90 && pitch_angle <= 30 && pitch_angle >= -30 && lastOrientationEntered != 4){
            playSound(this, R.raw.snoop_5);
            lastOrientationEntered = 4;
        }
        else if(roll_angle >= -30 && roll_angle <= 30 && pitch_angle < -130 || pitch_angle >= 130 && lastOrientationEntered != 5){
            playSound(this, R.raw.willsmith_1);
            lastOrientationEntered = 5;
        }
        sensorAzimuthTextView.setText("Azimuth Angle: " + azimuth_angle);

        sensorPitchAngleTextView.setText("Pitch Angle: " + pitch_angle);

        sensorRollAngleTextView.setText("Roll Angle: " + roll_angle);
    }


    public static void initSounds(Context context) {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap(3);
        soundPoolMap.put( R.raw.flava_1, soundPool.load(context, R.raw.flava_1, 1) );
        soundPoolMap.put( R.raw.gucci_14, soundPool.load(context, R.raw.gucci_14, 2) );
        soundPoolMap.put( R.raw.mikejones_2, soundPool.load(context, R.raw.mikejones_2, 3) );
        soundPoolMap.put( R.raw.snoop_5, soundPool.load(context, R.raw.snoop_5, 4));
        soundPoolMap.put( R.raw.willsmith_1, soundPool.load(context, R.raw.willsmith_1, 5));
    }

    /** Play a given sound in the soundPool */
    public static void playSound(Context context, int soundID) {
        if(soundPool == null || soundPoolMap == null){
            initSounds(context);
        }
        float volume = 1f;// whatever in the range = 0.0 to 1.0

        // play sound with same right and left volume, with a priority of 1,
        // zero repeats (i.e play once), and a playback rate of 1f
        soundPool.play(soundPoolMap.get(soundID), volume, volume, 1, 0, 1f);
    }
}
