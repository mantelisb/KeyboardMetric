package org.nanoavionics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.nanoavionics.gui.GuiController;
import org.nanoavionics.service.InputService;
import org.nanoavionics.service.SessionService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KeyboardMetricTest {

    @Mock
    private InputService inputService;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private KeyboardMetric keyboardMetric = new KeyboardMetric(Mockito.mock(GuiController.class));

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void start_shouldCallInputService_forTypingInterval() throws InterruptedException {
        keyboardMetric.start();

        verify(inputService).readTypingInterval();
    }

    @Test
    public void start_shouldCallInputService_forSessionDuration() throws InterruptedException {
        keyboardMetric.start();

        verify(inputService).readSessionDuration();
    }

    @Test
    public void start_shouldCallSesionService_withCorrectArguments() throws InterruptedException {
        var sessionDuration = 5;
        int typingInterval = 2;
        when(inputService.readSessionDuration()).thenReturn(sessionDuration);
        when(inputService.readTypingInterval()).thenReturn(typingInterval);

        keyboardMetric.start();

        verify(sessionService).ready(typingInterval, sessionDuration);
    }
}