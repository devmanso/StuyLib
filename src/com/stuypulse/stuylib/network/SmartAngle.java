/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.stuypulse.stuylib.network;

import com.stuypulse.stuylib.math.Angle;
import com.stuypulse.stuylib.util.Conversion;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.EntryNotification;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.function.Supplier;

/**
 * SmartAngle works as a wrapper for numbers on the network. This class handles converting a double
 * on the network to an Angle
 *
 * @author Myles Pasetsky (myles.pasetsky@gmail.com)
 */
public class SmartAngle implements Supplier<Angle> {

    // Flags for when the network table entry triggers an update
    private static final int LISTENER_FLAGS = EntryListenerFlags.kUpdate | EntryListenerFlags.kNew;

    // Built-in conversions from network doubles to Angles
    public static final Conversion<Double, Angle> kDegrees =
            Conversion.make(Angle::fromDegrees, a -> a.toDegrees());
    public static final Conversion<Double, Angle> kRadians =
            Conversion.make(Angle::fromRadians, a -> a.toRadians());

    // network table entry and its default value
    private NetworkTableEntry mEntry;
    private Angle mDefaultValue;

    // the double<->angle conversion being used by this smart angle
    private Conversion<Double, Angle> mConversion;

    // the angle from the network value, updated only when network value changes
    private Angle mAngle;

    /**
     * Create a SmartAngle with a NetworkTableEntry and a default angle value
     *
     * @param entry entry to wrap
     * @param value default angle value
     */
    public SmartAngle(NetworkTableEntry entry, Angle value) {
        mEntry = entry;
        mDefaultValue = value;
        mEntry.setDefaultDouble(mConversion.from(mDefaultValue));

        mEntry.addListener(this::update, LISTENER_FLAGS);
        degrees();
    }

    /**
     * Create a SmartAngle with a network entry key and a default angle value
     *
     * @param id network entry key
     * @param value default value
     */
    public SmartAngle(String id, Angle value) {
        this(SmartDashboard.getEntry(id), value);
    }

    /**
     * Forcibly updates the stored angle given an entry update object
     *
     * @param notif entry update notification
     */
    private void update(EntryNotification notif) {
        double value = notif.value.getDouble();
        mAngle = mConversion.to(value);
    }

    /**
     * Sets the conversion from the double stored on the network to an Angle class (e.g. sets what
     * unit the double on the network is in)
     *
     * @param conversion conversion between Double and Angle
     * @return reference to this
     */
    public SmartAngle units(Conversion<Double, Angle> conversion) {
        mConversion = conversion;
        return this;
    }

    /**
     * Sets the unit of the network double to be radians
     *
     * @return reference to this
     */
    public SmartAngle radians() {
        return units(kRadians);
    }

    /**
     * Sets the unit of the network double to be degrees
     *
     * @return reference to this
     */
    public SmartAngle degrees() {
        return units(kDegrees);
    }

    /** @return the angle stored by the SmartAngle */
    public Angle getAngle() {
        return mAngle;
    }

    /** @return the angle stored by the SmartAngle as a Rotation2d */
    public Rotation2d getRotation2d() {
        return getAngle().getRotation2d();
    }

    /** @return the angle stored by the SmartAngle */
    @Override
    public Angle get() {
        return getAngle();
    }

    /** @param angle angle to set SmartAngle to */
    public void set(Angle angle) {
        mEntry.forceSetDouble(mConversion.from(angle));
    }

    /** resets the SmartAngle to its default value */
    public void reset() {
        set(mDefaultValue);
    }

    /** @return the default value of the SmartAngle */
    public Angle getDefault() {
        return mDefaultValue;
    }
}