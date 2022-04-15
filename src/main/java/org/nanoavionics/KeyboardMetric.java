package org.nanoavionics;

import org.nanoavionics.gui.GuiController;
import org.nanoavionics.service.InputService;
import org.nanoavionics.service.SessionService;

public class KeyboardMetric {

    private InputService inputService;
    private SessionService sessionService;

    public KeyboardMetric(GuiController guiController) {
        inputService = new InputService(guiController);
        sessionService = new SessionService(guiController);
    }

    public static void main(String[] args) throws InterruptedException {
        new KeyboardMetric(new GuiController()).start();
    }

    private void start() throws InterruptedException {
        var typingInterval = inputService.readTypingInterval();
        var sessionDuration = inputService.readSessionDuration();

        sessionService.ready(typingInterval, sessionDuration);
    }

}