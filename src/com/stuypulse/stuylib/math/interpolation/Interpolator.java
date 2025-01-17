/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.stuypulse.stuylib.math.interpolation;

import com.stuypulse.stuylib.math.Vector2D;
import com.stuypulse.stuylib.streams.filters.IFilter;

import java.util.Arrays;

/**
 * This class serves as a baseline for all other classes. The Interpolator is a filter that will
 * find the values of any point given a few reference points
 *
 * @author Eric (ericlin071906@gmail.com)
 * @author Ivan Wei (ivanw8288@gmail.com)
 */
public interface Interpolator extends IFilter {

    /**
     * A behavior that takes in a double and returns a double
     *
     * @param x point to be intepolated
     * @return interpolated value
     */
    double interpolate(double x);

    // doesn't NEED to be overrided (hence the default). All filters need a get() method
    // when get() is called, interpolated() will be passed through
    default double get(double x) {
        return interpolate(x);
    }

    /**
     * Returns the index of the point with the greatest x-coordinate less than or equal to x, or -1
     * if it is lower than every element. Assumes that the array is sorted.
     *
     * @param x the x-value to compare to
     * @param points the points to find the lower bound among
     * @return the index of the lower bound, or -1
     */
    public static int indexLowerBound(double x, Vector2D... points) {
        for (int i = 0; i < points.length; ++i) {
            if (points[i].x > x) {
                return i - 1;
            }
        }
        return points.length - 1;
    }

    /**
     * Performs an in-place sort by the value of x from smallest to greatest
     *
     * @param points the array for which to sort reference points
     * @return the parameter array, for chaining
     */
    public static Vector2D[] sortPoints(Vector2D[] points) {
        Arrays.sort(points, (lhs, rhs) -> (int) (Math.signum(lhs.x - rhs.x)));
        return points;
    }

    /**
     * Returns a sorted array of reference points by the value of x from smallest to greatest
     *
     * @param points reference points
     * @return an array of sorted reference points
     */
    public static Vector2D[] getSortedPoints(Vector2D... points) {
        Vector2D[] output = points.clone();
        sortPoints(points);
        return output;
    }
}
