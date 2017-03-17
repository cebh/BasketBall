package com.example.accelerometer;

import android.app.Activity;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private static final String TAG = "com.example.accelerometer.MainActivity"; //java file path in package
    private PowerManager.WakeLock mWakeLock; //wake lock for brightness
    SimulationView sim; //simulation view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sim = new SimulationView(this); //create the cimulation view
        setContentView(sim); //set the content view to the simulation
        PowerManager mPowerManager = (PowerManager) getSystemService(POWER_SERVICE); //create power manager
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG); //set lock to keep screen awake
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Acquire WakeLock
        mWakeLock.acquire(); //acquire the lock
        sim.startSimulation(); //start simulation
        //implement startSimulation
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Release WakeLock
        mWakeLock.release(); //release the lock
        sim.stopSimulation(); //stop the simulation
        //implement stopSimulation
    }
}
