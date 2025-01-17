/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.stuypulse.stuylib.math.interpolation;

import com.stuypulse.stuylib.math.Vector2D;

import java.util.ArrayList;

/**
 * This class uses Lagrange polynomial interpolation. It derives an equation for a set of points in
 * the form
 *
 * <p>P(x) = a(x) + b(x) + c(x) ...
 *
 * <p>where a(x), b(x), c(x) .., is a "sub" - polynomial / "partial" - polynomial.
 *
 * <p>These partial polynomials are derived from the reference data provided. The partial polynomial
 * is derived by a(x-r1)(x-r2)(x-r3)... in where
 *
 * <p>r1, r2, r3,... are the x coordinates of the reference points NOT including the current
 * reference point a is the y coordinate current reference point divided by (x-r1)(x-r2)(x-r3)...,
 *
 * <p>where x is replaced by the current x coordinate of the reference point, and NOT including the
 * current reference point.
 *
 * <p>We then sum up all the partial polynomials to get the final polynomial. This final polynomial
 * is a polynomial that all reference points will pass through. We plug the x value of the y we want
 * to find into this polynomial and it will return the y value.
 *
 * @author Eric Lin (ericlin071906@gmail.com)
 * @author Myles Pasetsky (selym3 on github)
 */
public class PolyInterpolator implements Interpolator {

    /**
     * This class contains a constructor that creates a polynomial. It has several functions that
     * can be called
     */
    private static class Polynomial {
        private double a;
        private ArrayList<Double> factors;

        /**
         * A constructor that creates a new (partial) polynomial and stores all factors in a new
         * arrayList for easy access
         */
        public Polynomial() {
            a = 0.0;
            factors = new ArrayList<>();
        }

        /**
         * Set the coefficent of a
         *
         * @param a
         */
        public void setCoefficient(double a) {
            this.a = a;
        }

        public void addZero(double zero) {
            // Add factor to the list of factors
            factors.add(zero);
        }

        /**
         * Evaluates polynomial in the form a * (x - factors[0]) * (x - factors[1]) * ...
         *
         * @param x target point (point we want to interpolate the y for)
         * @return output (y)
         */
        public double get(double x) {
            double output = a;
            for (double factor : factors) {
                output *= (x - factor);
            }
            return output;
        }
    }

    /**
     * This class a partial polynomial that is used to derive the polynomial
     *
     * @param targetReference the reference point for that partial polynomial
     * @param points the reference points (for that partial polynomial)
     * @return a partial polynomial
     */
    private static Polynomial getPartialPolynomial(Vector2D targetReference, Vector2D[] points) {

        Polynomial polynomial = new Polynomial();
        double a = targetReference.y;

        for (Vector2D point : points) {
            if (point != targetReference) {
                a /= targetReference.x - point.x;
                polynomial.addZero(point.x);
            }
        }
        polynomial.setCoefficient(a);
        return polynomial;
    }

    private final Polynomial[] partialPolynomials; // storing the partial polynomials

    /**
     * Constructor for the class that takes in a set of points and creates a polynomial
     *
     * @param points are reference points
     */
    public PolyInterpolator(Vector2D... points) {
        if (points.length <= 1) {
            throw new IllegalArgumentException("PolyInterpolator needs at least 2 or more points");
        }

        partialPolynomials = new Polynomial[points.length];

        // creates a list of partial polynomials
        for (int i = 0; i < points.length; i++) {
            partialPolynomials[i] = getPartialPolynomial(points[i], points);
        }
    }

    @Override
    /**
     * Adds the list of partial polynomials from above, while plugging x into the polynomial
     *
     * @param x target point
     */
    public double interpolate(double x) {
        double fullPolynomial = 0;
        for (int i = 0; i < partialPolynomials.length; i++) {
            fullPolynomial += partialPolynomials[i].get(x);
        }
        return fullPolynomial;
    }
}
