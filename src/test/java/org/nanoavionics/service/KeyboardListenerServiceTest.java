package org.nanoavionics.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.assertj.core.api.Assertions.assertThat;

public class KeyboardListenerServiceTest {

    private KeyboardListenerService keyboardListenerService = KeyboardListenerService.getInstance();

    private KeyEvent keyEvent = new KeyEvent(new Button("click"), 0, 0, 0, 0, '1');

    @BeforeEach
    public void setUp() {
        keyboardListenerService.resetKeyboardStatus();
    }

    @Test
    public void keyReleased_shouldAddKeyEvent_whenStatusReady() {
        keyboardListenerService.keyReleased(keyEvent);

        assertThat(keyboardListenerService.getTypedCharacters()).hasSize(1).containsExactly(keyEvent);
    }

    @Test
    public void keyTyped_shouldNotAddKeyEvent() {
        keyboardListenerService.keyTyped(keyEvent);

        assertThat(keyboardListenerService.getTypedCharacters()).hasSize(0);
    }

    @Test
    public void keyPressed_shouldNotAddKeyEvent() {
        keyboardListenerService.keyPressed(keyEvent);

        assertThat(keyboardListenerService.getTypedCharacters()).hasSize(0);
    }

    @Test
    public void clearTypedCharacters_shouldClearTypedCharacters() {
        addTypedCharacter();

        keyboardListenerService.clearTypedCharacters();

        assertThat(keyboardListenerService.getTypedCharacters()).hasSize(0);
    }

    @Test
    public void resetKeyboard_shouldClearTypedCharacters() {
        addTypedCharacter();

        keyboardListenerService.resetKeyboardStatus();

        assertThat(keyboardListenerService.getTypedCharacters()).hasSize(0);
    }

    @Test
    public void resetKeyboard_shouldResetStatusFromGatheringInfo() {
        keyboardListenerService.startGatheringInfo();

        keyboardListenerService.resetKeyboardStatus();

        assertThat(keyboardListenerService.isGatheringInfo()).isFalse();
    }

    @Test
    public void startGatheringInfo_shouldClearTypedCharacters() {
        addTypedCharacter();

        keyboardListenerService.startGatheringInfo();

        assertThat(keyboardListenerService.getTypedCharacters()).hasSize(0);
    }

    @Test
    public void startGatheringInfo_shouldResetStatusFromGatheringInfo() {
        keyboardListenerService.startGatheringInfo();

        assertThat(keyboardListenerService.isGatheringInfo()).isTrue();
    }

    @Test
    public void keyReleased_shouldAddKeyEvent_whenGatheringInfo() {
        keyboardListenerService.startGatheringInfo();
        keyboardListenerService.keyReleased(keyEvent);

        assertThat(keyboardListenerService.getTypedCharacters()).hasSize(1).containsExactly(keyEvent);
    }

    @Test
    public void keyReleased_shouldNotAddKeyEvent_whenGatheringInfo_butKeyIsEnter() {
        keyboardListenerService.startGatheringInfo();
        keyboardListenerService.keyReleased(new KeyEvent(new Button("click"), 0, 0, 0, KeyEvent.VK_ENTER, '0'));

        assertThat(keyboardListenerService.getTypedCharacters()).hasSize(0);
    }

    @Test
    public void keyReleased_shouldNotAddKeyEvent_whenInfoGathered() {
        keyboardListenerService.startGatheringInfo();
        keyboardListenerService.keyReleased(keyEvent);
        assertThat(keyboardListenerService.getTypedCharacters()).hasSize(1).containsExactly(keyEvent);

        keyboardListenerService.keyReleased(new KeyEvent(new Button("click"), 0, 0, 0, KeyEvent.VK_ENTER, '0'));
        keyboardListenerService.keyReleased(new KeyEvent(new Button("click"), 0, 0, 0, 0, '2'));
        assertThat(keyboardListenerService.getTypedCharacters()).hasSize(1).containsExactly(keyEvent);
    }

    @Test
    public void keyReleased_shouldChangeStatusFromGatheringInfo_whenKeyIsEnter() {
        keyboardListenerService.startGatheringInfo();
        keyboardListenerService.keyReleased(new KeyEvent(new Button("click"), 0, 0, 0, KeyEvent.VK_ENTER, '0'));

        assertThat(keyboardListenerService.isGatheringInfo()).isFalse();
    }


    private void addTypedCharacter() {
        keyboardListenerService.keyReleased(keyEvent);
        assertThat(keyboardListenerService.getTypedCharacters()).hasSize(1);
    }
}
