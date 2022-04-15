package org.nanoavionics.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.nanoavionics.gui.GuiController;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InputServiceTest {

    @Mock
    private GuiController guiController;

    @Mock
    private KeyboardListenerService keyboardListenerService;

    private LinkedList<KeyEvent> typedCharacters = new LinkedList<>();

    @InjectMocks
    private InputService inputService = new InputService(Mockito.mock(GuiController.class));

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        typedCharacters.add(new KeyEvent(new Button("click"), 0, 0, 0, 0, '1'));
        when(keyboardListenerService.getTypedCharacters()).thenReturn(typedCharacters);
    }

    @Test
    public void readingTypingInterval_shouldClearTypingArea() throws InterruptedException {
        inputService.readTypingInterval();

        verify(guiController).clearTypingArea();
    }

    @Test
    public void readingTypingInterval_shouldChangeLabel() throws InterruptedException {
        inputService.readTypingInterval();

        verify(guiController).changeLabel("Type typing interval in seconds and press enter");
    }

    @Test
    public void readingSessionDuration_shouldChangeLabel() throws InterruptedException {
        inputService.readSessionDuration();

        verify(guiController).changeLabel("Type typing session duration in seconds and press enter");
    }

    @Test
    public void readingTypingInterval_shouldStartGatheringInfo() throws InterruptedException {
        inputService.readTypingInterval();

        verify(keyboardListenerService).startGatheringInfo();
    }

    @Test
    public void readingTypingInterval_shouldCheckGatheringInfo() throws InterruptedException {
        inputService.readTypingInterval();

        verify(keyboardListenerService).isGatheringInfo();
    }

    @Test
    public void readingTypingInterval_shouldReturnParsedInteger() throws InterruptedException {
        typedCharacters.add(new KeyEvent(new Button("click"), 0, 0, 0, 0, '2'));
        typedCharacters.add(new KeyEvent(new Button("click"), 0, 0, 0, 0, '3'));

        Assertions.assertThat(inputService.readTypingInterval()).isEqualTo(123);
    }

    @Test
    public void readingTypingInterval_shouldChangeLabelToErrorMessage_ifInputNotParsableToNumber() throws InterruptedException {
        when(keyboardListenerService.getTypedCharacters()).thenReturn(new LinkedList<>()).thenReturn(typedCharacters);
        inputService.readTypingInterval();

        verify(guiController).changeLabel("Error while parsing number, please repeat");
    }
}
