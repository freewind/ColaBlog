package freewind.colablog.controls;

import com.google.common.collect.Lists;
import javafx.scene.control.IndexRange;

import java.util.List;

class TabPositionFinder {
    private final IndexRange selection;
    private final String fullText;

    TabPositionFinder(IndexRange selection, String fullText) {
        this.selection = selection;
        this.fullText = fullText;
    }

    public List<Integer> find() {
        List<Integer> positions = Lists.newArrayList();
        findInSelection(positions);
        removeLastIfEndingWithLineSeparator(positions);
        addPreviousOneIfNeed(positions);
        return positions;
    }

    private void addPreviousOneIfNeed(List<Integer> positions) {
        if (!isLineSeparator(charAt(selection.getStart()))) {
            for (int i = selection.getStart() - 1; i >= 0; i--) {
                if (isLineSeparator(charAt(i))) {
                    positions.add(i);
                    return;
                }
            }
        }
    }

    private void removeLastIfEndingWithLineSeparator(List<Integer> positions) {
        if (isLineSeparator(charAt(selection.getEnd()))) {
            positions.remove(0);
        }
    }

    private void findInSelection(List<Integer> positions) {
        for (int position = selection.getEnd(); position >= selection.getStart(); position--) {
            if (isLineSeparator(charAt(position))) {
                positions.add(position);
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
