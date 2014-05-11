package freewind.colablog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/freewind/colablog/app.fxml"));
        primaryStage.setTitle("Hello, ColaBlog");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        dump(root);
    }

    public static void dump(Node n) {
        dump(n, 0);
    }

    private static void dump(Node n, int depth) {
        for (int i = 0; i < depth; i++) System.out.print("  ");
        System.out.println(n);
        if (n instanceof Parent)
            for (Node c : ((Parent) n).getChildrenUnmodifiable())
                dump(c, depth + 1);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
