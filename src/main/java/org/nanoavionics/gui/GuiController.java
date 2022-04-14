package org.nanoavionics.gui;

import org.nanoavionics.service.KeyboardListenerService;

import javax.swing.*;
import java.awt.*;

public class GuiController {

    private JFrame frame;
    private JLabel label;
    private JTextField typingArea;

    public GuiController() {
        this.frame = new JFrame("KeyboardMetric");
        this.label = new JLabel("Starting...");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addTypingArea();
        frame.add(label);
        frame.pack();
        frame.setVisible(true);
    }

    public void changeLabel(String text) {
        label.setText(text);
    }

    public void clearTypingArea() {
        typingArea.setText("");
    }

    private void addTypingArea() {
        typingArea = new JTextField(40);
        typingArea.addKeyListener(KeyboardListenerService.getInstance());

        frame.add(typingArea, BorderLayout.PAGE_START);
    }
}
