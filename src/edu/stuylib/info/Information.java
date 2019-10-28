package edu.stuylib.info;

import edu.stuylib.info.Version;

/**
 * Holds information about stuylib
 */
public interface Information {
    // Current version of stuylib
    final Version STUYLIB_VER = new Version(1,2,2);

    // Name of library + Version
    final String LIB_NAME = "StuyLib " + STUYLIB_VER;
}