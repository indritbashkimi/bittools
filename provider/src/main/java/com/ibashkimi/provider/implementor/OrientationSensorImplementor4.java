package com.ibashkimi.provider.implementor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.ibashkimi.provider.filter.AngleLowPassFilter;
import com.ibashkimi.provider.filter.Filter;
import com.ibashkimi.provider.providerdata.OrientationData;

import org.jetbrains.annotations.NotNull;

/**
 * Use only for inclination. No compass. Invalid azimuth.
 */
public class OrientationSensorImplementor4 extends AndroidSensorImplementor {

    private static final int MIN_VALUES = 20;
    /**
     * Rotation Matrix
     */
    private final float[] MAG = new float[]{1f, 1f, 1f};
    private final float[] I = new float[16];
    private final float[] R = new float[16];
    private final float[] outR = new float[16];
    private final float[] LOC = new float[3];
    private Display display;
    private Filter azimuthFilter = new AngleLowPassFilter(7);
    private Filter pitchFilter = new AngleLowPassFilter(7);
    private Filter rollFilter = new AngleLowPassFilter(7);
    /**
     * Orientation
     */
    private float pitch;
    private float roll;
    private float balance;
    private float tmp;
    private float oldPitch;
    private float oldRoll;
    private float oldBalance;
    private float minStep = 360;
    private float refValues = 0;
    private Orientation orientation;
    private boolean locked;
    private int displayOrientation;

    public OrientationSensorImplementor4(@NotNull Context context, int samplingPeriodUs) {
        super(context, new int[]{Sensor.TYPE_ACCELEROMETER}, samplingPeriodUs);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.display = windowManager.getDefaultDisplay();
    }

    public void onAccuracyChanged(@NonNull Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(@NonNull SensorEvent event) {

        oldPitch = pitch;
        oldRoll = roll;
        oldBalance = balance;

        SensorManager.getRotationMatrix(R, I, event.values, MAG);
        // compute pitch, roll & balance
        switch (display.getRotation()) {
            case Surface.ROTATION_270:
                SensorManager.remapCoordinateSystem(
                        R,
                        SensorManager.AXIS_MINUS_Y,
                        SensorManager.AXIS_X,
                        outR);
                break;
            case Surface.ROTATION_180:
                SensorManager.remapCoordinateSystem(
                        R,
                        SensorManager.AXIS_MINUS_X,
                        SensorManager.AXIS_MINUS_Y,
                        outR);
                break;
            case Surface.ROTATION_90:
                SensorManager.remapCoordinateSystem(
                        R,
                        SensorManager.AXIS_Y,
                        SensorManager.AXIS_MINUS_X,
                        outR);
                break;
            case Surface.ROTATION_0:
            default:
                SensorManager.remapCoordinateSystem(
                        R,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Y,
                        outR);
                break;
        }

        SensorManager.getOrientation(outR, LOC);

        // normalize z on ux, uy
        tmp = (float) Math.sqrt(outR[8] * outR[8] + outR[9] * outR[9]);
        tmp = (tmp == 0 ? 0 : outR[8] / tmp);

        // LOC[0] compass
        pitch = (float) Math.toDegrees(pitchFilter.doJob(LOC[1]));
        roll = -(float) Math.toDegrees(rollFilter.doJob(LOC[2]));
        balance = (float) Math.toDegrees(Math.asin(azimuthFilter.doJob(tmp)));

        // calculating minimal sensor step
        if (oldRoll != roll || oldPitch != pitch || oldBalance != balance) {
            if (oldPitch != pitch) {
                minStep = Math.min(minStep, Math.abs(pitch - oldPitch));
            }
            if (oldRoll != roll) {
                minStep = Math.min(minStep, Math.abs(roll - oldRoll));
            }
            if (oldBalance != balance) {
                minStep = Math.min(minStep, Math.abs(balance - oldBalance));
            }
            if (refValues < MIN_VALUES) {
                refValues++;
            }
        }

        if (!locked || orientation == null) {
            if (pitch < -45 && pitch > -135) {
                // top side up
                orientation = Orientation.TOP;
            } else if (pitch > 45 && pitch < 135) {
                // bottom side up
                orientation = Orientation.BOTTOM;
            } else if (roll > 45) {
                // right side up
                orientation = Orientation.RIGHT;
            } else if (roll < -45) {
                // left side up
                orientation = Orientation.LEFT;
            } else {
                // landing
                orientation = Orientation.LANDING;
            }
        }

        // azimuth is garbage. use only for orientation.
        listener.onDataChanged(new OrientationData(balance, pitch, -roll));
    }

    /**
     * Return the minimal sensor step
     *
     * @return the minimal sensor step
     * 0 if not yet known
     */
    public float getSensibility() {
        if (refValues >= MIN_VALUES) {
            return minStep;
        } else {
            return 0;
        }
    }

    public enum Orientation {

        LANDING(1, 0),
        TOP(1, 0),
        RIGHT(1, 90),
        BOTTOM(-1, 180),
        LEFT(-1, -90);

        private int reverse;
        private int rotation;

        private Orientation(int reverse, int rotation) {
            this.reverse = reverse;
            this.rotation = rotation;
        }

        public int getReverse() {
            return reverse;
        }

        public int getRotation() {
            return rotation;
        }

        public boolean isLevel(float pitch, float roll, float balance, float sensibility) {
            switch (this) {
                case BOTTOM:
                case TOP:
                    return balance <= sensibility
                            && balance >= -sensibility;
                case LANDING:
                    return roll <= sensibility
                            && roll >= -sensibility
                            && (Math.abs(pitch) <= sensibility
                            || Math.abs(pitch) >= 180 - sensibility);
                case LEFT:
                case RIGHT:
                    return Math.abs(pitch) <= sensibility
                            || Math.abs(pitch) >= 180 - sensibility;
            }
            return false;
        }

    }

}
