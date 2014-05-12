package freewind.colablog.controls;

import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;

public class Editor extends TextArea {

    private ClipboardPastingHandler clipboardPastingHandler;

    public void setClipboardPastingHandler(ClipboardPastingHandler clipboardPastingHandler) {
        this.clipboardPastingHandler = clipboardPastingHandler;
    }

    @Override
    public void paste() {
        System.out.println("### paste ###");
        if (clipboardPastingHandler.handle(Clipboard.getSystemClipboard())) {
            return;
        }
        super.paste();
    }

    public ScrollBar getVerticalScrollBar() {
        return (ScrollBar) this.lookup(".scroll-bar:vertical");
    }

}
