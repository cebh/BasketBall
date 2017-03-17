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

    private Sensor mSensor; //sensor the acceleration
    private SensorManager mSensorManager; //manage the sensor
    private Display mDisplay; //screen display

    private Particle mBall; //ball particle

    private Bitmap mField; //field image
    private Bitmap mBasket; //basket image
    private Bitmap mBitmap; //ball image
    private static final int BALL_SIZE = 64; //ball size
    private static final int BASKET_SIZE = 80; //basket size

    //variables for screen size and ball/basket origin
    private float mXOrigin;
    private float mYOrigin;
    private float mHorizontalBound;
    private float mVerticalBound;

    //variables for acceleration sensor
    private float mSensorX;
    private float mSensorY;
    private float mSensorZ;
    private long mSensorTimeStamp;

    public SimulationView(Context context) {
        super(context);
        mBall = new Particle(); //create ball

        //create ball and basket images
        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        mBitmap = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true);
        Bitmap basket = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
        mBasket = Bitmap.createScaledBitmap(basket, BASKET_SIZE, BASKET_SIZE, true);
        BitmapFactory.Options opts = new BitmapFactory.Options();

        //bitmap configuration
        opts.inDither = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;

        //create field image
        mField = BitmapFactory.decodeResource(getResources(), R.drawable.field, opts);

        //window manager
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        //create sensor
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (mDisplay.getRotation() == Surface.ROTATION_0) { //if in portrait
            mSensorX = sensorEvent.values[0];
            mSensorY = sensorEvent.values[1];
        } else if (mDisplay.getRotation() == Surface.ROTATION_90) { //if in landscape
            mSensorX = -sensorEvent.values[1];
            mSensorY = sensorEvent.values[0];
        }
        mSensorZ = sensorEvent.values[2]; //z sensor
        mSensorTimeStamp = sensorEvent.timestamp; //timestamp
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //unused
    }

    @Override
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight)
    {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        //allign 0,0 to center, set bounds
        mXOrigin = width * 0.5f;
        mYOrigin = height * 0.5f;
        mHorizontalBound = (width - BALL_SIZE) * 0.5f;
        mVerticalBound = (height - BALL_SIZE) * 0.5f;
    }

    public void startSimulation()
    {
        //register the listener
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopSimulation()
    {
        //unregister the listener
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mField, 0, 0, null); //draw field
        canvas.drawBitmap(mBasket, mXOrigin - BASKET_SIZE/2, mYOrigin - BALL_SIZE/2, null); //draw basket

        mBall.updatePosition(mSensorX, mSensorY, mSensorZ, mSensorTimeStamp); //update ball position
        mBall.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound); //resolve collosions with the bounds
        canvas.drawBitmap(mBitmap, (mXOrigin - BALL_SIZE/2) + mBall.mPosX, (mYOrigin - BALL_SIZE/2) - mBall.mPosY, null); //draw the ball
        invalidate();
    }
}
