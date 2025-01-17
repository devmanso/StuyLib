/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.stuypulse.stuylib.util.plot;

import com.stuypulse.stuylib.streams.IStream;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A TimeSeries is used to plot a stream of values (IStream) that changes over time.
 *
 * <p>A TimeSeries is created with a TimeSpan, which the stream is always assumed to be in. This
 * allows the x-axis to be precomputed with evenly spaced points in the time span.
 *
 * @author Myles Pasetsky (myles.pasetsky@gmail.com)
 */
public class TimeSeries extends Series {

    /** The span of time that the series is within */
    public static class TimeSpan {

        /** bounds of time span */
        public final double min, max;

        /**
         * Create a TimeSpan
         *
         * @param min minimum time
         * @param max maximum time
         */
        public TimeSpan(double min, double max) {
            this.min = min;
            this.max = max;
        }
    }

    /** precomputed x-values */
    private List<Double> xValues;

    /** y-values from stream */
    private List<Double> yValues;

    /** stream of values, source of data for this series */
    private final IStream stream;

    /**
     * Creates a time series.
     *
     * @param config series config
     * @param span time span
     * @param stream input stream for getting y-data
     */
    public TimeSeries(Config config, TimeSpan span, IStream stream) {
        super(config, true);

        xValues = new ArrayList<>();
        yValues = new LinkedList<>();

        final double delta = (span.max - span.min) / config.getCapacity();
        for (int i = 0; i < config.getCapacity(); ++i) {
            xValues.add(span.min + i * delta);
            yValues.add(0.0);
        }

        this.stream = stream;
    }

    /**
     * Returns reference to x values, which is safe because they are precompted and non-changing
     *
     * @return reference to x values
     */
    @Override
    protected List<Double> getSafeXValues() {
        return xValues;
    }

    /** @return copy of y-values because the y-values changes from the stream */
    @Override
    protected List<Double> getSafeYValues() {
        return new LinkedList<>(yValues);
    }

    /** adds a value from the stream to the y-values */
    @Override
    protected void poll() {
        yValues.add(stream.get());
    }

    /** removes the oldest y-value */
    @Override
    protected void pop() {
        yValues.remove(0);
    }

    /** returns the number of y-values (because x-values never change) */
    @Override
    public int size() {
        return yValues.size();
    }
}
