/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.stuypulse.stuylib.input.keyboard.computer;

import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * This is a simple class that opens a Java AWT window, which has a KeyListener that uploads
 * keyboard information to a network table
 *
 * @author Sam (sam.belliveau@gmail.com)
 */
public class NetKeyWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    /** NetKeyListener for window */
    private NetKeyListener mListener;

    /** Opens Network Keyboard Input Window */
    public NetKeyWindow() {
        // Set Title and Open Network Table
        super("Network Keyboard Input");

        // Get team number from user
        int team;
        do {
            try {
                String teamNum = JOptionPane.showInputDialog("Enter Team Number:");
                team = Integer.parseInt(teamNum);
            } catch (Exception e) {
                team = -1;
            }
        } while (team < 0);

        // Get keyboard port from user
        int port = 0;
        try {
            String keyboardPort =
                    JOptionPane.showInputDialog("Enter Virtual Keyboard Port (Default=0):");
            port = Integer.parseInt(keyboardPort);
        } catch (Exception e) {
            port = 0;
        }

        // Connect NetKeyListener
        mListener = new NetKeyListener(team, port);
        addKeyListener(mListener);

        // Set Title
        setTitle("Network Keyboard Input [Team: " + team + ", Port: " + port + "]");

        // Message
        JLabel message =
                new JLabel("Sending Keyboard Input to [Team: " + team + ", Port: " + port + "]");
        message.setBorder(new LineBorder(Color.BLACK, 4));

        // Message Panel
        final JPanel messagePanel = new JPanel(new GridBagLayout());
        messagePanel.add(message);
        getContentPane().add(messagePanel);

        // Pack and Set Size
        pack();

        // Set Visible
        setVisible(true);
    }
}
