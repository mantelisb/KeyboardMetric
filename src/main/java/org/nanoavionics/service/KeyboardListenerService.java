package org.nanoavionics.service;

import org.nanoavionics.domain.KeyboardStatus;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class KeyboardListenerService implements KeyListener {

    private final static KeyboardListenerService INSTANCE = new KeyboardListenerService();

    private KeyboardStatus keyboardStatus = KeyboardStatus.READY;

    private LinkedList<KeyEvent> typedCharacters = new LinkedList<>();

    private KeyboardListenerService() {
    }

    public static KeyboardListenerService getInstance() {
        return INSTANCE;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (keyboardStatus) {
            case READY -> typedCharacters.add(e);
            case GATHERING_INFO -> {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    keyboardStatus = KeyboardStatus.INFO_GATHERED;
                } else {
                    typedCharacters.add(e);
                }
            }
        }
    }

    public boolean isGatheringInfo() {
        return KeyboardStatus.GATHERING_INFO.equals(keyboardStatus);
    }

    public void startGatheringInfo() {
        keyboardStatus = KeyboardStatus.GATHERING_INFO;
        typedCharacters.clear();
    }

    public void resetKeyboardStatus() {
        keyboardStatus = KeyboardStatus.READY;
        typedCharacters.clear();
    }

    public LinkedList<KeyEvent> getTypedCharacters() {
        return typedCharacters;
    }

    public void clearTypedCharacters() {
        typedCharacters.clear();
    }
}
