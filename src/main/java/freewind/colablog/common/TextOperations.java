package freewind.colablog.common;

import freewind.colablog.utils.IO;

import java.util.List;

public class TextOperations {
    private final String text;
    private final List<String> lines;

    public TextOperations(String text) {
        this.text = text;
        this.lines = IO.readLines(text);
    }

    int whichLine(int caret) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            caret -= line.length() + 1;
            if (caret < 0) {
                return i;
            }
        }
        return 0;
    }

    public MoveUpDownHandler createUpDownHandler(int caret) {
        return new MoveUpDownHandler(caret);
    }

    public class MoveUpDownHandler {
        private final int maxWidth;
        private int newCaretPosition;

        public MoveUpDownHandler(int initCaret) {
            this.newCaretPosition = initCaret;
            this.maxWidth = getIndexInLine(initCaret);
        }

        public int moveDown() {
            if (isInLastLine(newCaretPosition)) {
                return text.length();
            }
            return move(1);
        }

        public int moveUp() {
            if (isInFirstLine(newCaretPosition)) {
                return 0;
            }
            return move(-1);
        }

        private int move(int lineSteps) {
            int lineIndex = whichLine(newCaretPosition);
            int newLineIndex = lineIndex + lineSteps;
            if (newLineIndex >= 0 && newLineIndex < totalLineCount()) {
                int newIndexInNextLine = Math.min(maxWidth, lines.get(newLineIndex).length());
                newCaretPosition = totalOfLines(newLineIndex - 1) + newIndexInNextLine;
            }
            return newCaretPosition;
        }
    }

    private boolean isInFirstLine(int caret) {
        return whichLine(caret) == 0;
    }

    private boolean isInLastLine(int newCaretPosition1) {
        return whichLine(newCaretPosition1) == lines.size() - 1;
    }

    private int totalOfLines(int line) {
        int total = 0;
        for (int i = 0; i <= line; i++) {
            total += lines.get(i).length() + 1;
        }
        return total;
    }

    private int totalLineCount() {
        return lines.size();
    }

    public int getLineWidth(int lineIndex) {
        return lines.get(lineIndex).length();
    }

    public int getIndexInLine(int caret) {
        for (String line : lines) {
            if (caret <= line.length()) {
                return caret;
            } else {
                caret -= line.length() + 1;
            }
        }
        return caret;
    }
}
