package org.nanoavionics.service;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class KeyboardListenerService implements KeyListener {

    private final static KeyboardListenerService INSTANCE = new KeyboardListenerService();

    private boolean sessionStarted;
    private boolean wordFinished;

    public void listenForNewWord() {
         wordFinished = false;
         typedCharacters = new LinkedList<>();
    }
    public boolean isWordFinished() {
        return wordFinished;
    }

    public LinkedList<Character> getTypedCharacters() {
        return typedCharacters;
    }

    private LinkedList<Character> typedCharacters = new LinkedList<>();

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
        if (!sessionStarted && !wordFinished) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                wordFinished = true;
            } else {
                typedCharacters.add(e.getKeyChar());
            }
            System.out.println(typedCharacters);
        }

        System.out.println(e);
    }
}
