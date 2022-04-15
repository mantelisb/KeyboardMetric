package org.nanoavionics.service;

import org.apache.commons.lang3.time.StopWatch;
import org.nanoavionics.domain.Key;
import org.nanoavionics.gui.GuiController;

import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SessionService {

    private GuiController guiController;
    private KeyboardListenerService keyboardListenerService = KeyboardListenerService.getInstance();

    public SessionService(GuiController guiController) {
        this.guiController = guiController;
    }

    public void ready(Integer typingIntervalInSeconds, Integer sessionDurationInSeconds) throws InterruptedException {
        prepare();

        while (!lastTypedWordMatches("start")) {
            Thread.sleep(5);
        }
        keyboardListenerService.clearTypedCharacters();

        startSession(typingIntervalInSeconds, sessionDurationInSeconds);
    }

    private void startSession(Integer typingIntervalInSeconds, Integer sessionDurationInSeconds) throws InterruptedException {
        var nextIntervalEndInSeconds = typingIntervalInSeconds;
        var previousIntervalMetric = "";
        var lastIntervalEnd = 0;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        while (!sessionFinished(sessionDurationInSeconds, stopWatch) && !lastTypedWordMatches("stop")) {
            Thread.sleep(50);
            var currentSessionDurationInSeconds = (double) stopWatch.getTime() / 1000;
            var typedCharactersCount = keyboardListenerService.getTypedCharacters().size();

            if (intervalFinished(nextIntervalEndInSeconds, currentSessionDurationInSeconds)) {
                var lastIntervalTypedCharacters = keyboardListenerService.getTypedCharacters().subList(lastIntervalEnd, typedCharactersCount);
                lastIntervalEnd = typedCharactersCount;
                nextIntervalEndInSeconds += typingIntervalInSeconds;

                previousIntervalMetric = getMetric("Previous interval", lastIntervalTypedCharacters);
            }
            displayMetrics(typedCharactersCount / currentSessionDurationInSeconds, previousIntervalMetric);
        }
        stopWatch.stop();

        displayMetrics(
                keyboardListenerService.getTypedCharacters().size() / ((double) stopWatch.getTime() / 1000),
                getMetric("Session", keyboardListenerService.getTypedCharacters()));
    }

    private boolean intervalFinished(Integer nextIntervalEndInSeconds, double currentSessionDurationInSeconds) {
        return currentSessionDurationInSeconds >= nextIntervalEndInSeconds;
    }

    private boolean sessionFinished(Integer sessionDuration, StopWatch stopWatch) {
        return stopWatch.getTime() >= sessionDuration * 1000;
    }

    private void prepare() {
        guiController.changeLabel("Session is started after typing `start`, to end session type `stop`");
        guiController.clearTypingArea();
        keyboardListenerService.resetKeyboardStatus();
    }

    private void displayMetrics(double typingSpeed, String metric) {
        guiController.changeLabel("Typing speed/s: %.2f, %s".formatted(typingSpeed, metric));
    }

    private String getMetric(String metricScope, List<KeyEvent> lastIntervalTypedCharacters) {
        return "%s metrics, Typed character count: %s, Top typed buttons: %s".formatted(
                metricScope,
                lastIntervalTypedCharacters.size(),
                findTop3Characters(lastIntervalTypedCharacters));
    }

    String findTop3Characters(List<KeyEvent> lastIntervalTypedCharacters) {
        return lastIntervalTypedCharacters
                .stream()
                .map(keyEvent -> new Key(keyEvent.getKeyChar(), keyEvent.getKeyCode()))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .map(key -> "%s(%s)".formatted(KeyEvent.getKeyText(key.code()), key.character()))
                .collect(Collectors.joining(","));
    }


    boolean lastTypedWordMatches(String lastWord) {
        var descendingIterator = keyboardListenerService.getTypedCharacters().descendingIterator();

        for (int i = lastWord.length() - 1; i >= 0 && descendingIterator.hasNext(); i--) {
            if (descendingIterator.next().getKeyChar() != lastWord.charAt(i)) {
                break;
            } else if (i == 0) {
                return true;
            }
        }

        return false;
    }

}
