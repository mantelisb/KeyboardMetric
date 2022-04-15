package org.nanoavionics.service;

import org.nanoavionics.gui.GuiController;

import java.awt.event.KeyEvent;

public class InputService {

    private GuiController guiController;
    private KeyboardListenerService keyboardListenerService = KeyboardListenerService.getInstance();

    public InputService(GuiController guiController) {
        this.guiController = guiController;
    }

    public Integer readTypingInterval() throws InterruptedException {
        return readNumber("Type typing interval in seconds and press enter");
    }

    public Integer readSessionDuration() throws InterruptedException {
        return readNumber("Type typing session duration in seconds and press enter");
    }

    private Integer readNumber(String newLabelText) throws InterruptedException {
        guiController.clearTypingArea();
        guiController.changeLabel(newLabelText);
        keyboardListenerService.startGatheringInfo();

        while (keyboardListenerService.isGatheringInfo()) {
            Thread.sleep(250);
        }

        try {
            return parseNumber();
        } catch (NumberFormatException e) {
            return readNumber("Error while parsing number, please repeat");
        }
    }

    private int parseNumber() {
        return Integer.parseInt(keyboardListenerService.getTypedCharacters()
                .stream()
                .map(KeyEvent::getKeyChar)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString());
    }
}
