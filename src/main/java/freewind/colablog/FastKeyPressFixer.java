package freewind.colablog;

import java.util.function.Function;

// try to resolve a javafx bug
// http://stackoverflow.com/questions/23543282/why-press-command-on-mac-os-will-trigger-4-key-events
public class FastKeyPressFixer<T> {

    private long lastPressTime = 0;

    public T fix(Function<Void, T> action) {
        long now = System.currentTimeMillis();
        if (now - lastPressTime >= 20) {
            lastPressTime = now;
            return action.apply(null);
        }
        return null;
    }
}
