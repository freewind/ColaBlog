package freewind.colablog.controls;

import freewind.colablog.keymap.KeyShort;
import javafx.scene.control.IndexRange;
import javafx.scene.input.KeyEvent;

import java.util.List;

public interface KeyshortHandler {
    boolean handle(KeyShort keyShort, KeyEvent keyEvent);
}

class FontSizeKeyshortHandler implements KeyshortHandler {

    private static final String TAB_SPACES = "    ";
    private Editor editor;

    public FontSizeKeyshortHandler(Editor editor) {
        this.editor = editor;
    }

    @Override
    public boolean handle(KeyShort keyShort, KeyEvent keyEvent) {
        switch (keyShort) {
            case Tab:
                keyEvent.consume();
                if (editor.getSelection().getLength() == 0) {
                    editor.insertText(editor.getAnchor(), TAB_SPACES);
                } else {
                    IndexRange selection = editor.getSelection();
                    List<Integer> lineSeparatorPositions = new TabPositionFinder(editor.getText(), selection.getStart(), selection.getEnd()).find();
                    for (int position : lineSeparatorPositions) {
                        editor.insertText(position, TAB_SPACES);
                    }
                    editor.selectRange(selection.getStart() + TAB_SPACES.length(), selection.getEnd() + TAB_SPACES.length() * lineSeparatorPositions.size());
                }
                return true;
            case ShiftTab:
                keyEvent.consume();
                IndexRange selection = editor.getSelection();
                List<Integer> lineSeparatorPositions = new TabPositionFinder(editor.getText(), selection.getStart(), selection.getEnd()).find();
                int deletedBeforeSelection = 0;
                int deletedInsideSelection = 0;
                for (int position : lineSeparatorPositions) {
                    int end = findDeletionEnd(position);
                    if (end > position) {
                        editor.deleteText(position, end);
                        int deleted = end - position;
                        if (position < selection.getStart()) {
                            deletedBeforeSelection += deleted;
                        } else {
                            deletedInsideSelection += deleted;
                        }
                    }
                }
                int newStart = selection.getStart() - deletedBeforeSelection;
                editor.selectRange(newStart, Math.max(newStart, selection.getEnd() - deletedBeforeSelection - deletedInsideSelection));
                return true;
        }
        return false;
    }

    private int findDeletionEnd(int position) {
        int end = position;
        for (int i = position; i < Math.min(editor.getText().length(), position + TAB_SPACES.length()); i++) {
            if (editor.getText().charAt(i) == ' ') {
                end = i + 1;
            } else {
                break;
            }
        }
        return end;
    }

}

class TabKeyshortHandler implements KeyshortHandler {
    private Editor editor;
    private Double initFontSize;

    public TabKeyshortHandler(Editor editor) {
        this.editor = editor;
    }

    @Override
    public boolean handle(KeyShort keyShort, KeyEvent keyEvent) {
        double fontSize = editor.fontProperty().getValue().getSize();
        switch (keyShort) {
            case IncreaseFontSize:
                if (initFontSize == null) {
                    initFontSize = fontSize;
                }
                editor.setStyle("-fx-font-size: " + (fontSize + 2) + "px");
                return true;
            case DecreaseFontSize:
                if (initFontSize == null) {
                    initFontSize = fontSize;
                }
                editor.setStyle("-fx-font-size: " + (fontSize - 2) + "px");
                return true;
            case NormalFontSize:
                if (initFontSize != null) {
                    editor.setStyle("-fx-font-size: " + initFontSize + "px");
                }
                return true;
        }
        return false;
    }
}