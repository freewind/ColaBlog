package freewind.colablog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/freewind/colablog/app.fxml"));
        primaryStage.setTitle("Hello, ColaBlog");
        Screen screen = Screen.getPrimary();
        primaryStage.setScene(new Scene(root, screen.getBounds().getWidth(), screen.getBounds().getHeight()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
