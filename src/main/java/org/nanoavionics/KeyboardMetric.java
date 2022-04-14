package org.nanoavionics;

import org.nanoavionics.gui.GuiController;
import org.nanoavionics.service.KeyboardListenerService;

public class KeyboardMetric {

    private GuiController guiController;
    private KeyboardListenerService keyboardListenerService;

    public KeyboardMetric() {
        guiController = new GuiController();
        keyboardListenerService = KeyboardListenerService.getInstance();
    }

    public static void main(String[] args) throws InterruptedException {
        new KeyboardMetric().run();
    }

    private void run() throws InterruptedException {
        var typingInterval = readNumber("Type typing interval in seconds and press enter");
        var sessionDuration = readNumber("Type typing session duration in seconds and press enter");

    }

    private Integer readNumber(String newLabelText) throws InterruptedException {
        guiController.clearTypingArea();
        guiController.changeLabel(newLabelText);
        keyboardListenerService.listenForNewWord();

        while (!keyboardListenerService.isWordFinished()) {
            Thread.sleep(250);
        }

        try {
            return Integer.parseInt(keyboardListenerService.getTypedCharacters()
                    .stream()
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString());
        } catch (NumberFormatException e) {
            return readNumber("Error while parsing number, please repeat");
        }
    }
}