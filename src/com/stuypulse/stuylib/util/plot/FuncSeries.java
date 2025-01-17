/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.stuypulse.stuylib.util.plot;

import com.stuypulse.stuylib.streams.filters.IFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * A FuncSeries plots a function (IFilter) over a given domain.
 *
 * <p>A FuncSeries data is precomputed, so its x and y values are non-changing. This means that the
 * series does not get polled and does not implement pop() or poll().
 *
 * @author Ben Goldfisher
 */
public class FuncSeries extends Series {

    /** Domain describes the x-values that the series will be graphed over */
    public static class Domain {

        /** min and max x-values that will be graphed */
        public final double min, max;

        /**
         * Creates a Domain
         *
         * @param min smallest x-value that will be graphed
         * @param max largest x-value that will be graphed
         */
        public Domain(double min, double max) {
            this.min = min;
            this.max = max;
        }
    }

    /** Contains the precomputed (x, y) data values */
    private List<Double> xValues;

    private List<Double> yValues;

    /**
     * Creates a FuncSeries and specifies that it is not polling.
     *
     * @param config series config
     * @param domain range of x-values inputted to the function
     * @param func a function with one input and output to be graphed
     */
    public FuncSeries(Config config, Domain domain, IFilter func) {
        super(config, false);

        xValues = new ArrayList<Double>();
        yValues = new ArrayList<Double>();

        // Fill the series capacity with evenly spaced points in the given domain
        for (int i = 0; i < config.getCapacity(); i++) {
            double x = (i * (domain.max - domain.min)) / config.getCapacity() + domain.min;

            xValues.add(x);
            yValues.add(func.get(x));
        }
    }

    /** @return max number of stored (x, y) values */
    @Override
    public int size() {
        return getConfig().getCapacity();
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

    /**
     * Returns reference to y values, which is safe because they are precompted and non-changing
     *
     * @return reference to y values
     */
    @Override
    protected List<Double> getSafeYValues() {
        return yValues;
    }

    /** No-op because series is non-polling */
    @Override
    protected void pop() {}

    /** No-op because series is non-polling */
    @Override
    protected void poll() {}
}
