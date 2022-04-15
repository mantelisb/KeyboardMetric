package org.nanoavionics.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.nanoavionics.gui.GuiController;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.*;

public class SessionServiceTest {

    public static final int TYPING_INTERVAL_IN_SECONDS = 7;
    public static final int SESSION_DURATION_IN_SECONDS = 20;
    @Mock
    private GuiController guiController;

    @Mock
    private KeyboardListenerService keyboardListenerService;

    @InjectMocks
    @Spy
    private SessionService sessionService = new SessionService(Mockito.mock(GuiController.class));

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        doReturn(true).when(sessionService).lastTypedWordMatches(any());
    }

    @Test
    public void read_shouldChangeLabel() throws InterruptedException {
        sessionService.ready(TYPING_INTERVAL_IN_SECONDS, SESSION_DURATION_IN_SECONDS);

        verify(guiController).changeLabel("Session is started after typing `start`, to end session type `stop`");
    }

    @Test
    public void read_shouldClearTypingAreay() throws InterruptedException {
        sessionService.ready(TYPING_INTERVAL_IN_SECONDS, SESSION_DURATION_IN_SECONDS);

        verify(guiController).clearTypingArea();
    }

    @Test
    public void read_shouldResetKeyboardStatus() throws InterruptedException {
        sessionService.ready(TYPING_INTERVAL_IN_SECONDS, SESSION_DURATION_IN_SECONDS);

        verify(keyboardListenerService).resetKeyboardStatus();
    }

    @Test
    public void read_shouldClearTypedCharacters() throws InterruptedException {
        sessionService.ready(TYPING_INTERVAL_IN_SECONDS, SESSION_DURATION_IN_SECONDS);

        verify(keyboardListenerService).clearTypedCharacters();
    }

    @Test
    public void read_shouldDisplayTypingSpeedMetrics() throws InterruptedException {
        sessionService.ready(TYPING_INTERVAL_IN_SECONDS, SESSION_DURATION_IN_SECONDS);

        verify(guiController).changeLabel(contains("Typing speed"));
    }

    @Test
    public void read_shouldDisplayTopTypedButtonsMetrics() throws InterruptedException {
        sessionService.ready(TYPING_INTERVAL_IN_SECONDS, SESSION_DURATION_IN_SECONDS);

        verify(guiController).changeLabel(contains("Top typed buttons"));
    }

    @Test
    public void read_shouldDisplayTypedCharactersCountMetrics() throws InterruptedException {
        sessionService.ready(TYPING_INTERVAL_IN_SECONDS, SESSION_DURATION_IN_SECONDS);

        verify(guiController).changeLabel(contains("Typed character count"));
    }

    @Test
    public void findTop3Characters_shouldReturnMappedAndSortedCharacters() {
        var top3Characters = sessionService.findTop3Characters(List.of(
                new KeyEvent(new Button("click"), 0, 0, 0, 0, '0'),
                new KeyEvent(new Button("click"), 0, 0, 0, 65, 'A'),
                new KeyEvent(new Button("click"), 0, 0, 0, 65, 'a'),
                new KeyEvent(new Button("click"), 0, 0, 0, 65, 'a'),
                new KeyEvent(new Button("click"), 0, 0, 0, 65, 'A'),
                new KeyEvent(new Button("click"), 0, 0, 0, 65, 'A'),
                new KeyEvent(new Button("click"), 0, 0, 0, 10, ' '),
                new KeyEvent(new Button("click"), 0, 0, 0, 10, ' '),
                new KeyEvent(new Button("click"), 0, 0, 0, 10, ' '),
                new KeyEvent(new Button("click"), 0, 0, 0, 10, ' ')));

        Assertions.assertThat(top3Characters).isEqualTo("Enter( ),A(A),A(a)");
    }

    @Test
    public void findTop3Characters_shouldReturnMappedAndSortedCharacters_evenIfThereIsLessThan3() {
        var top3Characters = sessionService.findTop3Characters(List.of(
                new KeyEvent(new Button("click"), 0, 0, 0, 65, 'A'),
                new KeyEvent(new Button("click"), 0, 0, 0, 65, 'a'),
                new KeyEvent(new Button("click"), 0, 0, 0, 65, 'a'),
                new KeyEvent(new Button("click"), 0, 0, 0, 65, 'a'),
                new KeyEvent(new Button("click"), 0, 0, 0, 65, 'A')));

        Assertions.assertThat(top3Characters).isEqualTo("A(a),A(A)");
    }

    @Test
    public void lastTypedWordMatches_shouldReturnTrue_when1LetterWordMatches() {
        when(keyboardListenerService.getTypedCharacters()).thenReturn(new LinkedList<>(List.of(
                new KeyEvent(new Button("click"), 0, 0, 0, 0, '2'),
                new KeyEvent(new Button("click"), 0, 0, 0, 0, 'a'))));
        sessionService.lastTypedWordMatches("a");
    }

    @Test
    public void lastTypedWordMatches_shouldReturnTrue_when2LetterWordMatches() {
        when(keyboardListenerService.getTypedCharacters()).thenReturn(new LinkedList<>(List.of(
                new KeyEvent(new Button("click"), 0, 0, 0, 0, '2'),
                new KeyEvent(new Button("click"), 0, 0, 0, 0, 'b'),
                new KeyEvent(new Button("click"), 0, 0, 0, 0, 'a'))));
        sessionService.lastTypedWordMatches("ab");
    }

    @Test
    public void lastTypedWordMatches_shouldReturnFalse_when2outOf3LetterWordMatches() {
        when(keyboardListenerService.getTypedCharacters()).thenReturn(new LinkedList<>(List.of(
                new KeyEvent(new Button("click"), 0, 0, 0, 0, 'c'),
                new KeyEvent(new Button("click"), 0, 0, 0, 0, 'b'),
                new KeyEvent(new Button("click"), 0, 0, 0, 0, 'a'))));
        sessionService.lastTypedWordMatches("abd");
    }
}
