package com.example.accelerometer;

import android.util.Log;

/**
 * Created by Christian on 3/14/2017.
 */

public class Particle {
    /* coefficient of restituation */
    private static final float COR = 0.7f;

    public float mPosX;
    public float mPosY;
    private float mVelX;
    private float mVelY;

    //use the acceleration values to calculate displacement of the particle along the XZ and Y axis.
    public void updatePosition(float sx, float sy, float sz, long timeStamp) {
        float dt = (System.nanoTime() - timeStamp) / 100000000.0f;
        mVelX += -sx * dt;
        mVelY += -sy * dt;

        mPosX += mVelX * dt;
        mPosY += mVelY * dt;

        Log.d(mPosX + "", mPosY + "");
        //Log.d("pos", sx + "," + sy + "," + sz + "," + dt);

    }

    //add logic to create a bounce effect when it collides with the boundary.
    public void resolveCollisionWithBounds(float mHorizontalBound, float mVerticalBound) {
        if(mPosX > mHorizontalBound) {
            mPosX = mHorizontalBound;
            mVelX = -mVelX * COR;
        } else if (mPosX < 0) {
            mPosX = 0;
            mVelX = -mVelX * COR;
        }
        if(mPosY > 0) {
            mPosY = 0;
            mVelY = -mVelY * COR;
        } else if (mPosY < -mVerticalBound) {
            mPosY = -mVerticalBound;
            mVelY = -mVelY * COR;
        }
    }
}
