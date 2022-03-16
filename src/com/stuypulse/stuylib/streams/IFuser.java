/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.stuypulse.stuylib.streams;

import com.stuypulse.stuylib.streams.filters.HighPassFilter;
import com.stuypulse.stuylib.streams.filters.IFilter;
import com.stuypulse.stuylib.streams.filters.LowPassFilter;

/**
 * A class that combines two IStreams, usually in order to combine some slow data source with a
 * faster one. The Setpoint IStream will return the error of some system, and the Measurement
 * IStream will return the value of the faster data source. The Measurement IStream should generally
 * move in the opposite direction of the Setpoint IStream.
 *
 * <p>Example Usage: Setpoint = Limelight, Measurement = Encoders
 *
 * @author Myles Pasetsky
 */
public class IFuser implements IStream {

    private final Number mFilterRC;

    private final IStream mSetpoint;
    private final IStream mMeasurement;

    private IFilter mSetpointFilter;
    private IFilter mMeasurementFilter;

    private double mInitialTarget;

    /**
     * Create an IFuser with an RC, Setpoint Filter, and Measurement Filter
     *
     * @param rc RC value for the lowpass / highpass filters
     * @param setpoint a filter that returns the error in a control loop
     * @param measurement a filter that returns an encoder / any measurement (should be negative of
     *     setpoint)
     */
    public IFuser(Number rc, IStream setpoint, IStream measurement) {
        mSetpoint = setpoint;
        mMeasurement = measurement;

        mFilterRC = rc;

        initialize();
    }

    /** Resets the IFuser so that it can ignore any previous data / reset its initial read */
    public void initialize() {
        mSetpointFilter = new LowPassFilter(mFilterRC);
        mMeasurementFilter = new HighPassFilter(mFilterRC);

        mInitialTarget = mSetpoint.get() + mMeasurement.get();
    }

    private double getSetpoint() {
        return mSetpointFilter.get(mSetpoint.get());
    }

    private double getMeasurement() {
        return mMeasurementFilter.get(mInitialTarget - mMeasurement.get());
    }

    /** Get the result of merging the setpoint and measurement streams */
    public double get() {
        return getSetpoint() + getMeasurement();
    }
}
