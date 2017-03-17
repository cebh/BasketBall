package com.example.accelerometer;

import android.app.Activity;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private static final String TAG = "com.example.accelerometer.MainActivity";
    private PowerManager.WakeLock mWakeLock;
    SimulationView sim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sim = new SimulationView(this);
        setContentView(sim);
        PowerManager mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Acquire WakeLock
        mWakeLock.acquire();
        sim.startSimulation();
        //implement startSimulation
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Release WakeLock
        mWakeLock.release();
        sim.stopSimulation();
        //implement stopSimulation
    }
}
