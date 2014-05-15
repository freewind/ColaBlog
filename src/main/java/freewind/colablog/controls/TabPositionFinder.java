package freewind.colablog.controls;

import com.google.common.collect.Lists;

import java.util.List;

public class TabPositionFinder {
    private final String fullText;
    private int selectionStart;
    private int selectionEnd;

    TabPositionFinder(String fullText, int selectionStart, int selectionEnd) {
        this.fullText = fullText;
        this.selectionStart = selectionStart;
        this.selectionEnd = selectionEnd;
    }

    public List<Integer> find() {
        removeEndingLineSeparator();
        findingPreviousPosition();
        List<Integer> positions = Lists.newArrayList();
        findPositionsInSelection(positions);
        addTheStartPositionIfNeed(positions);
        return positions;
    }

    private void addTheStartPositionIfNeed(List<Integer> positions) {
        if (selectionStart == 0) {
            positions.add(0);
        }
    }

    private void findPositionsInSelection(List<Integer> positions) {
        for (int i = selectionEnd - 1; i >= selectionStart; i--) {
            if (isLineSeparator(charAt(i))) {
                positions.add(i + 1);
            }
        }
    }

    private void findingPreviousPosition() {
        if (selectionStart == fullText.length()) {
            selectionStart--;
        }
        for (int i = selectionStart; i >= 0; i--) {
            if (isLineSeparator(charAt(i))) {
                selectionStart = i;
                return;
            }
        }
        selectionStart = 0;
    }

    private void removeEndingLineSeparator() {
        if (selectionEnd > selectionStart) {
            if (isLineSeparator(charAt(selectionEnd - 1))) {
                selectionEnd--;
            }
        }
    }

    private char charAt(int end) {
        return fullText.charAt(end);
    }

    private boolean isLineSeparator(char c) {
        return c == '\n';
    }
}
