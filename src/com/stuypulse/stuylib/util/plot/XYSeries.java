/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.stuypulse.stuylib.util.plot;

import com.stuypulse.stuylib.math.Vector2D;
import com.stuypulse.stuylib.streams.vectors.VStream;

import java.util.LinkedList;
import java.util.List;

/**
 * An XYSeries is used to plot a stream of points (VStream) that changes over time.
 *
 * @author Myles Pasetsky (myles.pasetsky@gmail.com)
 */
public class XYSeries extends Series {

    /** Contains the (x, y) data points */
    private List<Double> xValues;

    private List<Double> yValues;

    /** Outputs values to be plotted */
    private final VStream stream;

    /**
     * Creates a XYSeries and specifies that it is polling.
     *
     * @param config series config
     * @param stream determines the points to be plotted
     */
    public XYSeries(Config config, VStream stream) {
        super(config, true);

        xValues = new LinkedList<>();
        yValues = new LinkedList<>();

        this.stream = stream;
    }

    /** @return copied list of x values */
    @Override
    protected List<Double> getSafeXValues() {
        return new LinkedList<>(xValues);
    }

    /** @return copied list of y values */
    @Override
    protected List<Double> getSafeYValues() {
        return new LinkedList<>(yValues);
    }

    /** Adds next point from the stream to x and y values */
    @Override
    protected void poll() {
        Vector2D next = stream.get();
        xValues.add(next.x);
        yValues.add(next.y);
    }

    /** Removes oldest point from x and y values */
    @Override
    protected void pop() {
        xValues.remove(0);
        yValues.remove(0);
    }

    /** @return number of stored points */
    @Override
    public int size() {
        return yValues.size();
    }
}
