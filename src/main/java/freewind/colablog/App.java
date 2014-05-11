package freewind.colablog;

import freewind.colablog.start.StartController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;

public class App extends Application {

    public static File currentBlogDir;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showStartScene();
        primaryStage.show();
    }

    public void showStartScene() throws java.io.IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/freewind/colablog/start/start.fxml"));
        Parent root = loader.load();
        StartController controller = loader.getController();
        controller.setApp(this);
        primaryStage.setScene(new Scene(root, 400, 250));
    }

    public void showMainScene() throws java.io.IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/freewind/colablog/app.fxml"));
        Parent root = loader.load();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setScene(new Scene(root));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
