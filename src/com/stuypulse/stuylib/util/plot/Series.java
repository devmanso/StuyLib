/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.stuypulse.stuylib.util.plot;

import java.awt.BasicStroke;
import java.util.List;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * A series handles (x, y) data that will be graphed. It supports getting safe copies of this data,
 * as well optionally supporting pushing and popping this data.
 *
 * <p>Using these operations, a series can update its data points and add itself to an XYChart.
 *
 * <p>A series must be created with a config, which has the label of the series (which appears in
 * the legend) and its maximum number of entries.
 *
 * <p>A series must also specifiy whether or not it is polling. If a series is "polling", it means
 * that its values will change when the poll operation is performed. This is useful as the plot
 * doesn't have to redraw if its series aren't polling because they will never change.
 *
 * @author Myles Pasetsky (myles.pasetsky@gmail.com)
 */
public abstract class Series {

    /** Config describes the baseline settings for a series that need to be configured. */
    public static class Config {

        /** label of a series that appears on the legend */
        private String label;

        /** max number of entries before old ones get removed */
        private int capacity;

        /**
         * Creates a config
         *
         * @param label series label, which appears on legend
         * @param capacity capacity, which sets max number of entries for a series
         */
        public Config(String label, int capacity) {
            this.label = label;
            this.capacity = capacity;
        }

        /** @return label */
        public String getLabel() {
            return label;
        }

        /** @return capacity */
        public int getCapacity() {
            return capacity;
        }
    }

    /** the config for this series */
    private final Config config;

    /** whether or not this series polls */
    private final boolean polling;

    /**
     * Creates a series given a config and if it's polling
     *
     * @param config series config
     * @param polling whether or not this series polls
     */
    public Series(Config config, boolean polling) {
        this.config = config;
        this.polling = polling;
    }

    /** @return series config */
    public Config getConfig() {
        return config;
    }

    /** @return the number of data points the series has */
    public abstract int size();

    /** @return a "safe" copy of the x-data that can be read by XYChart's threads */
    protected abstract List<Double> getSafeXValues();

    /** @return a "safe" copy of the y-data that can be read by XYChart's threads */
    protected abstract List<Double> getSafeYValues();

    /** removes oldest data point (can be a no-op depending on implementation) */
    protected abstract void pop();

    /** pushes new data point (can be a no-op depending on implementation) */
    protected abstract void poll();

    /** @return if the series is polling */
    protected final boolean isPolling() {
        return polling;
    }

    /**
     * Pushes new data point and removes data points if over capacity in the config
     *
     * <p>Depending on the implementation of a series, this may be a no-op
     */
    private final void update() {
        final int capacity = getConfig().getCapacity();
        poll();
        while (size() > capacity) {
            pop();
        }
    }

    /**
     * Puts the 'safe' data into the chart instance under the label specified in the config.
     *
     * @param chart chart to put x, y data into
     */
    protected final void update(XYChart chart) {
        update();

        String label = getConfig().getLabel();
        var x = getSafeXValues();
        var y = getSafeYValues();

        if (chart.getSeriesMap().containsKey(label)) {
            chart.updateXYSeries(label, x, y, null);
        } else {
            chart.addSeries(label, x, y);
            chart.getSeriesMap()
                    .get(label)
                    .setXYSeriesRenderStyle(XYSeriesRenderStyle.Line)
                    .setMarker(SeriesMarkers.NONE)
                    .setLineStyle(new BasicStroke(2.5f));
        }
    }
}
