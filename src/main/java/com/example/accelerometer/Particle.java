package com.example.accelerometer;

import android.util.Log;

/**
 * Created by Christian on 3/14/2017.
 */

public class Particle {
    /* coefficient of restituation */
    private static final float COR = 0.7f;

    //ball positions and velocity
    public float mPosX;
    public float mPosY;
    private float mVelX;
    private float mVelY;

    //use the acceleration values to calculate displacement of the particle along the XZ and Y axis.
    public void updatePosition(float sx, float sy, float sz, long timeStamp) {
        float dt = (System.nanoTime() - timeStamp) / 1000000000.0f/ 100000.0f; //how quickly time moves
        //use sensor values to adjust velocity and position
        mVelX += -sx * dt;
        mVelY += -sy * dt;
        mPosX += mVelX * dt;
        mPosY += mVelY * dt;
    }

    //add logic to create a bounce effect when it collides with the boundary.
    public void resolveCollisionWithBounds(float mHorizontalBound, float mVerticalBound) {
        if(mPosX > mHorizontalBound) {
            mPosX = mHorizontalBound;
            mVelX = -mVelX * COR;
        } else if (mPosX < -mHorizontalBound) {
            mPosX = -mHorizontalBound;
            mVelX = -mVelX * COR;
        }
        if(mPosY > mVerticalBound) {
            mPosY = mVerticalBound;
            mVelY = -mVelY * COR;
        } else if (mPosY < -mVerticalBound) {
            mPosY = -mVerticalBound;
            mVelY = -mVelY * COR;
        }
    }
}
