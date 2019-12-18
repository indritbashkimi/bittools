package com.ibashkimi.provider.implementor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.Surface;
import android.view.WindowManager;

import com.ibashkimi.provider.providerdata.OrientationData;

import org.jetbrains.annotations.NotNull;

public class OrientationSensorImplementor2 extends AndroidSensorImplementor {

    private final WindowManager mWindowManager;

    private int mLastAccuracy;

    public OrientationSensorImplementor2(@NotNull Context context, int samplingPeriodUs) {
        this(context, Sensor.TYPE_ROTATION_VECTOR, samplingPeriodUs);
    }

    OrientationSensorImplementor2(@NotNull Context context, int sensor, int samplingPeriodUs) {
        super(context, new int[]{sensor}, samplingPeriodUs);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public void onSensorChanged(@NotNull SensorEvent event) {
        if (mLastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }
        float[] rotationMatrix = new float[9];
        float[] rotationVector = event.values.clone();
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector);

        final int worldAxisForDeviceAxisX;
        final int worldAxisForDeviceAxisY;

        // Remap the axes as if the device screen was the instrument panel,
        // and adjust the rotation matrix for the device orientation.
        switch (mWindowManager.getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
            default:
                worldAxisForDeviceAxisX = SensorManager.AXIS_X;
                worldAxisForDeviceAxisY = SensorManager.AXIS_Y;
                break;
            case Surface.ROTATION_90:
                worldAxisForDeviceAxisX = SensorManager.AXIS_Y;
                worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_Y;
                break;
            case Surface.ROTATION_180:
                worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_X;
                worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_Y;
                break;
            case Surface.ROTATION_270:
                worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_Y;
                worldAxisForDeviceAxisY = SensorManager.AXIS_X;
                break;
        }

        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX,
                worldAxisForDeviceAxisY, adjustedRotationMatrix);

        // Transform rotation matrix into azimuth/pitch/roll
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        // Convert radians to degrees
        float azimuth = orientation[0] * 57.29577951f; // todo
        float pitch = orientation[1] * -57.29577951f;
        float roll = orientation[2] * 57.29577951f;

        if (pitch > 90) {
            pitch = 90 - pitch % 90;
        } else if (pitch < -90) {
            pitch = -90 - pitch % 90;
        }
        if (roll > 90)
            roll = 90 - roll % 90;
        else if (roll < -90)
            roll = -90 - roll % 90;

        listener.onDataChanged(new OrientationData(azimuth, -pitch, roll));
    }

    @Override
    public void onAccuracyChanged(@NotNull Sensor sensor, int accuracy) {
        super.onAccuracyChanged(sensor, accuracy);
        this.mLastAccuracy = accuracy;
    }
}
