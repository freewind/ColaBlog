package freewind.colablog;

import com.google.common.collect.Lists;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.LinkedHashMap;
import java.util.List;

import static freewind.colablog.KeyShort.*;
import static javafx.scene.input.KeyCode.*;

public class Keymap {

    private final FastKeyPressFixer<KeyShort> fastKeyPressFixer = new FastKeyPressFixer<>();
    private final LinkedHashMap<List<KeyCode>, KeyShort> mapping = new LinkedHashMap<>();

    {
        mapping.put(codes(META, EQUALS), IncreaseFontSize);
        mapping.put(codes(META, MINUS), DecreaseFontSize);
        mapping.put(codes(META, DIGIT0), NormalFontSize);
        mapping.put(codes(META, F), SearchInEditor);
        mapping.put(codes(META, V), Paste);
    }

    private List<KeyCode> codes(KeyCode... codes) {
        return Lists.newArrayList(codes);
    }

    public KeyShort findKeyShort(KeyEvent event) {
        return fastKeyPressFixer.fix((x) -> {
            for (List<KeyCode> codes : mapping.keySet()) {
                if (keyShortMatches(codes, event)) {
                    return mapping.get(codes);
                }
            }
            return null;
        });
    }

    private boolean keyShortMatches(List<KeyCode> codes, KeyEvent event) {
        for (KeyCode code : codes) {
            if (!codeMatches(event, code)) {
                return false;
            }
        }
        return true;
    }

    private boolean codeMatches(KeyEvent event, KeyCode code) {
        return code == META && event.isMetaDown()
                || code == SHIFT && event.isShiftDown()
                || code == ALT && event.isAltDown()
                || code == event.getCode();
    }
}
