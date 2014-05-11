package freewind.colablog;

import javafx.scene.Node;
import javafx.scene.Parent;

public class NodeDumper {

    public void dump(Node n) {
        dump(n, 0);
    }

    private void dump(Node n, int depth) {
        for (int i = 0; i < depth; i++) System.out.print("  ");
        System.out.println(n);
        if (n instanceof Parent)
            for (Node c : ((Parent) n).getChildrenUnmodifiable())
                dump(c, depth + 1);
    }
}
