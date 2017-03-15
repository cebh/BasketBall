package com.example.accelerometer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Christian on 3/14/2017.
 */

public class SimulationView extends View implements SensorEventListener {

    private Sensor mSensor;
    private SensorManager mSensorManager;
    private Display mDisplay;

    private Particle mBall;

    private Bitmap mField;
    private Bitmap mBasket;
    private Bitmap mBitmap;
    private static final int BALL_SIZE = 64;
    private static final int BASKET_SIZE = 80;

    private float mXOrigin;
    private float mYOrigin;
    private float mHorizontalBound;
    private float mVerticalBound;

    private float mSensorX;
    private float mSensorY;
    private float mSensorZ;
    private long mSensorTimeStamp;

    public SimulationView(Context context) {
        super(context);
        mBall = new Particle();
        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        mBitmap = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true);
        Bitmap basket = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
        mBasket = Bitmap.createScaledBitmap(basket, BASKET_SIZE, BASKET_SIZE, true);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inDither = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        mField = BitmapFactory.decodeResource(getResources(), R.drawable.field, opts);

        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mXOrigin = 100;
        mYOrigin = 100;
        mHorizontalBound = mDisplay.getWidth();
        mVerticalBound = mDisplay.getHeight();
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (mDisplay.getRotation() == Surface.ROTATION_0) {
            mSensorX = sensorEvent.values[0];
            mSensorY = sensorEvent.values[1];
        } else if (mDisplay.getRotation() == Surface.ROTATION_90) {
            mSensorX = -sensorEvent.values[1];
            mSensorY = sensorEvent.values[0];
        }
        mSensorZ = sensorEvent.values[2];
        mSensorTimeStamp = sensorEvent.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void startSimulation()
    {
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopSimulation()
    {
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mField, 0, 0, null);
        canvas.drawBitmap(mBasket, mXOrigin - BASKET_SIZE/2, mYOrigin - BALL_SIZE/2, null);

        mBall.updatePosition(mSensorX, mSensorY, mSensorZ, mSensorTimeStamp);
        mBall.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound);

        canvas.drawBitmap(mBitmap, (mXOrigin - BALL_SIZE/2) + mBall.mPosX, (mYOrigin - BALL_SIZE/2) - mBall.mPosY, null);
        invalidate();
    }
}
