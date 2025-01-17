/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.stuypulse.stuylib.math.interpolation;

import com.stuypulse.stuylib.math.Vector2D;

/**
 * This class uses two reference points to interpolate all points on a line between them
 *
 * @author Eric (ericlin071906@gmail.com)
 */
public class IntervalInterpolator implements Interpolator {
    private final Vector2D point1;
    private final Vector2D point2;

    /**
     * Store the two reference points
     *
     * @param point1 first point to interpolate from
     * @param point2 second point to interpolate from
     */
    public IntervalInterpolator(Vector2D point1, Vector2D point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    @Override
    public double interpolate(double x) {
        double range = point2.x - point1.x;
        double slope = (point2.y - point1.y) / range;
        double yIntercept = point1.y - (slope * point1.x); // y = mx + b

        double interpolatedValue = slope * x + yIntercept;

        return interpolatedValue;
    }
}
