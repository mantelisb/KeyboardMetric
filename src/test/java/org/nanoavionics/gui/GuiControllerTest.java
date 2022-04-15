package org.nanoavionics.gui;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.*;

import static org.mockito.Mockito.verify;

public class GuiControllerTest {
    @Mock
    private JLabel label;

    @Mock
    private JTextField typingArea;

    @InjectMocks
    private GuiController guiController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void changeLabel_shouldSetText() {
        var updatedLabel = "new text";
        guiController.changeLabel(updatedLabel);

        verify(label).setText(updatedLabel);
    }

    @Test
    public void clearingTypingArea_shouldSetTextToEmptyString() {
        guiController.clearTypingArea();

        verify(typingArea).setText(StringUtils.EMPTY);
    }
}
