package freewind.colablog.controls;

import javafx.scene.input.Clipboard;

public interface ClipboardPastingHandler {
    boolean handle(Clipboard clipboard);
}
